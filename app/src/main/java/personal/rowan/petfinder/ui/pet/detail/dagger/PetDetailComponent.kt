package personal.rowan.petfinder.ui.pet.detail.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.RealmModule
import personal.rowan.petfinder.ui.pet.detail.PetDetailFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */

@PetDetailScope
@Component(modules = arrayOf(RealmModule::class), dependencies = arrayOf(AppComponent::class))
interface PetDetailComponent {

    fun inject(petDetailFragment: PetDetailFragment)

    companion object {
        val injector: Action1<PetDetailFragment> = Action1 { petDetailFragment ->
            DaggerPetDetailComponent.builder()
                    .appComponent(App.applicationComponent(petDetailFragment.context!!))
                    .build()
                    .inject(petDetailFragment)
        }
    }

}