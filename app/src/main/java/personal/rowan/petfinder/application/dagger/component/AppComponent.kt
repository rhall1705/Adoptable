package personal.rowan.petfinder.application.dagger.component

import android.content.Context

import dagger.Component
import personal.rowan.petfinder.application.dagger.module.AppModule

/**
 * Created by Rowan Hall
 */

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun context(): Context

}
