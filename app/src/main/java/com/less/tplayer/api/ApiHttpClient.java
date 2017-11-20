package com.less.tplayer.api;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.less.tplayer.TpApplication;
import com.less.tplayer.api.cookie.CookieJarImpl;
import com.less.tplayer.api.cookie.store.MemoryCookieStore;
import com.less.tplayer.util.SharedPreferenceUtils;
import com.less.tplayer.util.Singleton;
import com.less.tplayer.util.TDevice;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by deeper on 2017/11/20.
 */

public class ApiHttpClient {
    private static final String TAG = ApiHttpClient.class.getSimpleName();
    private static final int TIMEOUT = 20;
    private static final String KEY_APP_UNIQUE_ID = "appUniqueID";
    public static final String HOST = "http://www.jianshu.com";
    private OkHttpClient okHttpClient;
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
}
