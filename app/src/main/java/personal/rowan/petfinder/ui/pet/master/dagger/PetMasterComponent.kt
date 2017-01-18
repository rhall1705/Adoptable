package personal.rowan.petfinder.ui.pet.master.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.ApplicationComponent
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */

@PetMasterScope
@Component(modules = arrayOf(PetfinderApiModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface PetMasterComponent {

    fun inject(petMasterFragment: PetMasterFragment)

    companion object {
        val injector: Action1<PetMasterFragment> = Action1 { petmasterFragment ->
            DaggerPetMasterComponent.builder()
                    .applicationComponent(App.applicationComponent(petmasterFragment.context))
                    .build()
                    .inject(petmasterFragment)
        }
    }

}