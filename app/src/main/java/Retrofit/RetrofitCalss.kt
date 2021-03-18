package Retrofit

import io.reactivex.android.BuildConfig
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object RetrofitCalss {
    private var retrofit: Retrofit? = null

    fun getClient(/*baseUrl: String*/): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(/*baseUrl*/"https://5e510330f2c0d300147c034c.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        }
        return retrofit!!
    }
}