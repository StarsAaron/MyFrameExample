package com.myframe.net;

import android.content.Context;
import android.util.Log;

import com.myframe.model.DailyComment;
import com.myframe.model.DailyDetail;
import com.myframe.model.DailyExtraMessage;
import com.myframe.model.DailyListBean;
import com.myframe.model.DailyRecommend;
import com.myframe.model.DailyTypeBean;
import com.myframe.model.LuanchImageBean;
import com.myframe.model.ThemesDetails;
import com.myframe.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Retrofit管理类
 */
public class RetrofitHelper {

    private static final String ZHIHU_DAILY_URL = "http://news-at.zhihu.com/api/4/";
    private static final String ZHIHU_LAST_URL = "http://news-at.zhihu.com/api/3/";
    private static OkHttpClient mOkHttpClient;
    private final ZhiHuDailyAPI mZhiHuApi;
    private static final int CACHE_TIME_LONG = 60 * 60 * 24 * 7;
    private Context context;

    public RetrofitHelper(Context context) {
        this.context = context;
        initOkHttpClient();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(ZHIHU_DAILY_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mZhiHuApi = mRetrofit.create(ZhiHuDailyAPI.class);
    }


    public static ZhiHuDailyAPI getLastZhiHuApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZHIHU_LAST_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ZhiHuDailyAPI.class);
    }


    /**
     * 初始化OKHttpClient
     */
    private void initOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(context.getCacheDir(), "HttpCache"), 1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(new LoggingInterceptor()) //如果在拦截器中统一配置，则所有的请求都会缓存。
                            .addNetworkInterceptor(new LoggingInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    // 拦截器可以打印日志和设置缓存
    // 如果我们的服务器不支持缓存，也就是响应头没有对应字段，那么我们可以使用网络拦截器实现
    // 注意：addInterceptor和addNetworkInterceptor 需要同时设置。
    // 这两者的区别可以参考Interceptors 拦截器。我只说一下效果，如果你只是想实现在线缓存，
    // 那么可以只添加网络拦截器，如果只想实现离线缓存，可以使用只添加应用拦截器。
    // 如果在拦截器中统一配置，则所有的请求都会缓存。但是在实际开发中有些接口需要保证数据的实时性
    // ，那么我们就不能统一配置，这时可以这样：

    // @Headers("Cache-Control: public, max-age=时间秒数")
    // @GET("weilu/test") Observable<Test> getData();
    // 1.不需要缓存：Cache-Control: no-cache或Cache-Control: max-age=0
    // 2.如果想先显示数据，在请求。（类似于微博等）：Cache-Control: only-if-cached
    // 通过以上配置后通过拦截器中的request.cacheControl().toString() 就可以获取到我们配置
    // 的Cache-Control头文件，实现对应的缓存策略。
    public class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Log.d("LoggingInterceptor", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            if (!NetUtils.isConnected(context)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            long t1 = System.nanoTime();
            //##########################################
            Response response = chain.proceed(request);
            //##########################################

            long t2 = System.nanoTime();
            Log.d("LoggingInterceptor", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            if (NetUtils.isConnected(context)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置(注掉部分)
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        //.header("Cache-Control", "max-age=3600")
                        .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                return response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_TIME_LONG)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }


    /**
     * 知乎日报Api封装 方便直接调用
     **/

    public Observable<DailyListBean> getLatestNews() {

        return mZhiHuApi.getlatestNews();
    }

    public Observable<DailyListBean> getBeforeNews(String date) {

        return mZhiHuApi.getBeforeNews(date);
    }

    public Observable<DailyDetail> getNewsDetails(int id) {

        return mZhiHuApi.getNewsDetails(id);
    }

    public Observable<LuanchImageBean> getLuanchImage(String res) {

        return mZhiHuApi.getLuanchImage(res);
    }

    public Observable<DailyTypeBean> getDailyType() {

        return mZhiHuApi.getDailyType();
    }

    public Observable<ThemesDetails> getThemesDetailsById(int id) {

        return mZhiHuApi.getThemesDetailsById(id);
    }

    public Observable<DailyExtraMessage> getDailyExtraMessageById(int id) {

        return mZhiHuApi.getDailyExtraMessageById(id);
    }

    public Observable<DailyComment> getDailyLongCommentById(int id) {

        return mZhiHuApi.getDailyLongComment(id);
    }

    public Observable<DailyComment> getDailyShortCommentById(int id) {

        return mZhiHuApi.getDailyShortComment(id);
    }

    public Observable<DailyRecommend> getDailyRecommendEditors(int id) {

        return mZhiHuApi.getDailyRecommendEditors(id);
    }
}
