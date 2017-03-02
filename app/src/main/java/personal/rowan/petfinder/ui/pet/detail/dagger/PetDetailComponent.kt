package personal.rowan.petfinder.ui.pet.detail.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.ApplicationComponent
import personal.rowan.petfinder.application.dagger.module.RealmModule
import personal.rowan.petfinder.ui.pet.detail.PetDetailFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */

@PetDetailScope
@Component(modules = arrayOf(RealmModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface PetDetailComponent {

    fun inject(petDetailFragment: PetDetailFragment)

    companion object {
        val injector: Action1<PetDetailFragment> = Action1 { petDetailFragment ->
            DaggerPetDetailComponent.builder()
                    .applicationComponent(App.applicationComponent(petDetailFragment.context))
                    .build()
                    .inject(petDetailFragment)
        }
    }

}