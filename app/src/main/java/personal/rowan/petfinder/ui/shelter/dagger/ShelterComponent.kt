package personal.rowan.petfinder.ui.shelter.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.ApplicationComponent
import personal.rowan.petfinder.application.dagger.module.LocationPermissionModule
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.ui.shelter.ShelterFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */
@ShelterScope
@Component(modules = arrayOf(PetfinderApiModule::class, LocationPermissionModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface ShelterComponent {

    fun inject(shelterFragment: ShelterFragment)

    companion object {
        val injector: Action1<ShelterFragment> = Action1 { shelterFragment ->
            DaggerShelterComponent.builder()
                    .applicationComponent(App.Companion.applicationComponent(shelterFragment.context))
                    .build()
                    .inject(shelterFragment)
        }
    }

}