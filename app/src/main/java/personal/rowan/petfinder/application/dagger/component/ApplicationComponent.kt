package personal.rowan.petfinder.application.dagger.component

import android.content.Context

import dagger.Component
import personal.rowan.petfinder.application.dagger.module.ApplicationModule

/**
 * Created by Rowan Hall
 */

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun context(): Context

}
