package com.neshan.task1.di.module;

import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import kotlin.Suppress;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import static com.neshan.task1.BuildConfig.BASE_URL;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.gson.Gson;
import com.neshan.task1.utils.StringConverterFactory;
import com.neshan.task1.domain.remote.ApiErrorHandle;
import com.neshan.task1.domain.remote.ApiService;
import com.neshan.task1.domain.repository.MapsRepository;
import com.neshan.task1.domain.repository.MapsRepositoryImp;


@Module(includes = {ApplicationModule.class})
public class NetworkModule {

    @Provides
    @Singleton
    Retrofit providesRetrofit(
            GsonConverterFactory gsonConverterFactory,
            StringConverterFactory stringConverterFactory,
            ScalarsConverterFactory scalarsConverterFactory,
            RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
            OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addConverterFactory(stringConverterFactory)
                .addConverterFactory(scalarsConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Context context, Boolean isNetworkAvailable) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .cache(null) // mCache can be used here
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    builder.addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json");
                    request = builder.build();
                    return chain.proceed(request);
                });
        return client.build();
    }


    @Provides
    @Singleton
    Gson providesGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    StringConverterFactory providesStringConverterFactory() {
        return StringConverterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    ScalarsConverterFactory providesScalarsConverterFactory() {
        return ScalarsConverterFactory.create();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory providesRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    ApiErrorHandle providesApiErrorHandle() {
        return new ApiErrorHandle();
    }

    @Provides
    @Singleton
    Boolean provideIsNetworkAvailable(Context context) {
        return checkNetworkState(context);
    }

    @Suppress(names = "DEPRECATION")
    private Boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true;
            } else return actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo.isConnected();
        }
    }

    @Singleton
    @Provides
    ApiService provideService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Singleton
    @Provides
    MapsRepository provideDirectionRepository(ApiService retrofitService) {
        return new MapsRepositoryImp(retrofitService);
    }

}
