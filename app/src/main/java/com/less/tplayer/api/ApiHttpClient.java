package com.less.tplayer.api;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.less.tplayer.TpApplication;
import com.less.tplayer.api.callback.HttpCallback;
import com.less.tplayer.api.cookie.CookieJarImpl;
import com.less.tplayer.api.cookie.store.MemoryCookieStore;
import com.less.tplayer.util.SharedPreferenceUtils;
import com.less.tplayer.util.Singleton;
import com.less.tplayer.util.TDevice;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by deeper on 2017/11/20.
 */

public class ApiHttpClient {
    private static final String TAG = ApiHttpClient.class.getSimpleName();
    private static final int TIMEOUT = 20;
    private static final String KEY_APP_UNIQUE_ID = "appUniqueID";
    public static final String HOST = "http://www.jianshu.com";
    private Handler mDelivery = new Handler(Looper.getMainLooper());
    private OkHttpClient okHttp;

    static public ApiHttpClient getDefault() {
        return gDefault.get();
    }

    private static final Singleton<ApiHttpClient> gDefault = new Singleton<ApiHttpClient>() {

        @Override
        protected ApiHttpClient create() {
            ApiHttpClient apiHttpClient = new ApiHttpClient();
            File httpCacheDirectory = new File(TpApplication.getContext().getCacheDir(), "app_cache");
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(TAG);
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            Map<String, String> headers = initHeaders();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cache(cache);
            builder.addNetworkInterceptor(loggingInterceptor);
            builder.addNetworkInterceptor(new BaseInterceptor(headers));
            builder.addInterceptor(new CaheInterceptor(TpApplication.getContext()));
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);

            // initSSL
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

            // init cookieJar
            CookieJar cookieJar = new CookieJarImpl(new MemoryCookieStore());
            builder.cookieJar(cookieJar);
            apiHttpClient.setOkHttp(builder.build());
            return apiHttpClient;
        }
    };

    private static Map<String, String> initHeaders() {
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept-Language", Locale.getDefault().toString());
        headers.put("Host", HOST);
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent",getUserAgent());
        return headers;
    }

    public void get(String url, Map<String,String> params, final HttpCallback httpCallback){
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        url = builder.build().toString();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = getOkHttp().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallback.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final Object t = httpCallback.convertResponse(response);

                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallback.onSuccess(t);
                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallback.onError(new IOException("convertResponse IO Error"));
                        }
                    });
                }
            }
        });

    }

    public void post(){
        if (files == null || files.isEmpty())
        {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            FormBody formBody = builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < files.size(); i++) {
                PostFormBuilder.FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    private static String getUserAgent() {
        StringBuilder ua = new StringBuilder("TPlayer@ZRX.NET");

        // App版本
        ua.append('/' + TDevice.getVersionName(TpApplication.getContext())+ '_' + TDevice.getVersionCode(TpApplication.getContext(),TpApplication.getContext().getPackageName()));
        // 手机系统平台
        ua.append("/Android");
        // 手机系统版本
        ua.append("/" + android.os.Build.VERSION.RELEASE);
        // 手机型号
        ua.append("/" + android.os.Build.MODEL);
        // 客户端唯一标识
        ua.append("/" + getAppId());
        return ua.toString();
    }

    private static String getAppId() {
        String uniqueID = SharedPreferenceUtils.getStringData(KEY_APP_UNIQUE_ID,null);
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            SharedPreferenceUtils.setStringData(KEY_APP_UNIQUE_ID,uniqueID);
        }
        return uniqueID;
    }

    public static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }

    public void setOkHttp(OkHttpClient okHttp) {
        this.okHttp = okHttp;
    }

    public OkHttpClient getOkHttp(){
        return okHttp;
    }
}
