package com.less.tplayer.api;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.less.tplayer.TpApplication;
import com.less.tplayer.api.cookie.CookieJarImpl;
import com.less.tplayer.api.cookie.store.MemoryCookieStore;
import com.less.tplayer.util.OkLogger;
import com.less.tplayer.util.SharedPreferenceUtils;
import com.less.tplayer.util.Singleton;
import com.less.tplayer.util.TDevice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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

    /**
     * ================================================
     * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
     * 版    本：1.0
     * 创建日期：16/9/11
     * 描    述：Https相关的工具类
     * 修订历史：
     * ================================================
     */
    public static class HttpsUtils {

        public static class SSLParams {
            public SSLSocketFactory sSLSocketFactory;
            public X509TrustManager trustManager;
        }

        public static SSLParams getSslSocketFactory() {
            return getSslSocketFactoryBase(null, null, null);
        }

        /**
         * https单向认证
         * 可以额外配置信任服务端的证书策略，否则默认是按CA证书去验证的，若不是CA可信任的证书，则无法通过验证
         */
        public static SSLParams getSslSocketFactory(X509TrustManager trustManager) {
            return getSslSocketFactoryBase(trustManager, null, null);
        }

        /**
         * https单向认证
         * 用含有服务端公钥的证书校验服务端证书
         */
        public static SSLParams getSslSocketFactory(InputStream... certificates) {
            return getSslSocketFactoryBase(null, null, null, certificates);
        }

        /**
         * https双向认证
         * bksFile 和 password -> 客户端使用bks证书校验服务端证书
         * certificates -> 用含有服务端公钥的证书校验服务端证书
         */
        public static SSLParams getSslSocketFactory(InputStream bksFile, String password, InputStream... certificates) {
            return getSslSocketFactoryBase(null, bksFile, password, certificates);
        }

        /**
         * https双向认证
         * bksFile 和 password -> 客户端使用bks证书校验服务端证书
         * X509TrustManager -> 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
         */
        public static SSLParams getSslSocketFactory(InputStream bksFile, String password, X509TrustManager trustManager) {
            return getSslSocketFactoryBase(trustManager, bksFile, password);
        }

        private static SSLParams getSslSocketFactoryBase(X509TrustManager trustManager, InputStream bksFile, String password, InputStream... certificates) {
            SSLParams sslParams = new SSLParams();
            try {
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                X509TrustManager manager;
                if (trustManager != null) {
                    //优先使用用户自定义的TrustManager
                    manager = trustManager;
                } else if (trustManagers != null) {
                    //然后使用默认的TrustManager
                    manager = chooseTrustManager(trustManagers);
                } else {
                    //否则使用不安全的TrustManager
                    manager = UnSafeTrustManager;
                }
                // 创建TLS类型的SSLContext对象， that uses our TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
                // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
                sslContext.init(keyManagers, new TrustManager[]{manager}, null);
                // 通过sslContext获取SSLSocketFactory对象
                sslParams.sSLSocketFactory = sslContext.getSocketFactory();
                sslParams.trustManager = manager;
                return sslParams;
            } catch (NoSuchAlgorithmException e) {
                throw new AssertionError(e);
            } catch (KeyManagementException e) {
                throw new AssertionError(e);
            }
        }

        private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
            try {
                if (bksFile == null || password == null) {
                    return null;
                }
                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(clientKeyStore, password.toCharArray());
                return kmf.getKeyManagers();
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
            }
            return null;
        }

        private static TrustManager[] prepareTrustManager(InputStream... certificates) {
            if (certificates == null || certificates.length <= 0) {
                return null;
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                // 创建一个默认类型的KeyStore，存储我们信任的证书
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certStream : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    // 证书工厂根据证书文件的流生成证书 cert
                    Certificate cert = certificateFactory.generateCertificate(certStream);
                    // 将 cert 作为可信证书放入到keyStore中
                    keyStore.setCertificateEntry(certificateAlias, cert);
                    try {
                        if (certStream != null) {
                            certStream.close();
                        }
                    } catch (IOException e) {
                        OkLogger.printStackTrace(e);
                    }
                }
                //我们创建一个默认类型的TrustManagerFactory
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书
                tmf.init(keyStore);
                //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书
                return tmf.getTrustManagers();
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
            }
            return null;
        }

        private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }

        /**
         * 为了解决客户端不信任服务器数字证书的问题，网络上大部分的解决方案都是让客户端不对证书做任何检查，
         * 这是一种有很大安全漏洞的办法
         */
        public static X509TrustManager UnSafeTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };

        /**
         * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
         * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
         * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
         */
        public static HostnameVerifier UnSafeHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
