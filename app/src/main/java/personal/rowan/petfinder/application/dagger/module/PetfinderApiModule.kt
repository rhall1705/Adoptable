package personal.rowan.petfinder.application.dagger.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import personal.rowan.petfinder.model.pet.Breeds
import personal.rowan.petfinder.model.pet.Options
import personal.rowan.petfinder.model.pet.Pets
import personal.rowan.petfinder.network.BreedsTypeAdapter
import personal.rowan.petfinder.network.OptionsTypeAdapter
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.network.PetsTypeAdapter
import personal.rowan.petfinder.util.RetrofitServiceFactory

/**
 * Created by Rowan Hall
 */

@Module
class PetfinderApiModule {

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("key", PetfinderService.API_KEY)
                        .addQueryParameter("format", "json")
                        .build()

                val requestBuilder = original.newBuilder()
                        .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

    private val gson: Gson = GsonBuilder()
            //.registerTypeAdapter(Pets::class.java, PetsTypeAdapter())
            .registerTypeAdapter(Breeds::class.java, BreedsTypeAdapter())
            .registerTypeAdapter(Options::class.java, OptionsTypeAdapter())
            .create()

    @Provides
    fun providePetfinderService(): PetfinderService {
        return RetrofitServiceFactory.createRetrofitService(PetfinderService::class.java, PetfinderService.BASE_URL, httpClient, gson)
    }

}
