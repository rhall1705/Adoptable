package personal.rowan.petfinder.application.dagger.module

import dagger.Module
import dagger.Provides
import personal.rowan.petfinder.application.LocationPermissionManager

/**
 * Created by Rowan Hall
 */

@Module
class LocationPermissionModule {

    @Provides
    internal fun providesLocationPermissionManager(): LocationPermissionManager {
        return LocationPermissionManager.instance
    }

}