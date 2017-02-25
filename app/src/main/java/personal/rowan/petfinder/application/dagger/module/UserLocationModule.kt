package personal.rowan.petfinder.application.dagger.module

import dagger.Module
import dagger.Provides
import personal.rowan.petfinder.application.UserLocationManager

/**
 * Created by Rowan Hall
 */

@Module
class UserLocationModule {

    @Provides
    internal fun providesUserLocationManager(): UserLocationManager {
        return UserLocationManager.INSTANCE
    }

}