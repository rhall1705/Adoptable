package personal.rowan.petfinder.util

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rowan Hall
 */

object RetrofitServiceFactory {

    fun <T> createRetrofitService(clazz: Class<T>, endpoint: String, client: OkHttpClient, gson: Gson): T {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(endpoint)
                .client(client)
                .build()

        return retrofit.create(clazz)
    }

}
