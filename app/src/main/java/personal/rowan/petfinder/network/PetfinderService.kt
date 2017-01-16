package personal.rowan.petfinder.network

import personal.rowan.petfinder.BuildConfig
import personal.rowan.petfinder.model.pet.PetResult
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Rowan Hall
 */

interface PetfinderService {

    @GET("pet.find")
    fun getNearbyPets(@Query("location") location: String, @Query("offset") offset: String?): Observable<PetResult>

    companion object {
        val BASE_URL = "http://api.petfinder.com/"
        val API_KEY: String = BuildConfig.PET_FINDER_API_KEY
    }

}
