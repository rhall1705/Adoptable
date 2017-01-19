package personal.rowan.petfinder.ui.shelter.master.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.ApplicationComponent
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.ui.shelter.master.ShelterMasterFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */
@ShelterMasterScope
@Component(modules = arrayOf(PetfinderApiModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface ShelterMasterComponent {

    fun inject(shelterMasterFragment: ShelterMasterFragment)

    companion object {
        val injector: Action1<ShelterMasterFragment> = Action1 { sheltermasterFragment ->
            DaggerShelterMasterComponent.builder()
                    .applicationComponent(App.applicationComponent(sheltermasterFragment.context))
                    .build()
                    .inject(sheltermasterFragment)
        }
    }

}