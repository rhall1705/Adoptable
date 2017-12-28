package personal.rowan.petfinder.ui.location.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.UserLocationModule
import personal.rowan.petfinder.ui.location.LocationFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */

@LocationScope
@Component(modules = arrayOf(UserLocationModule::class), dependencies = arrayOf(AppComponent::class))
interface LocationComponent {

    fun inject(locationFragment: LocationFragment)

    companion object {
        val injector: Action1<LocationFragment> = Action1 { locationFragment ->
            DaggerLocationComponent.builder()
                    .appComponent(App.applicationComponent(locationFragment.context!!))
                    .build()
                    .inject(locationFragment)
        }
    }

}