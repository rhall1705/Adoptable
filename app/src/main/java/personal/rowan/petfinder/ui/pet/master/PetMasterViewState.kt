package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import io.realm.Realm
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewState
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager

/**
 * Created by Rowan Hall
 */
class PetMasterViewState(val petData: MutableList<PetDetailViewState>, val offset: String) {

    companion object {
        fun fromPetResult(initialState: PetMasterViewState?, petResult: PetResult?, clear: Boolean, context: Context): PetMasterViewState {
            val petData = if (!clear && initialState?.petData != null) initialState.petData else ArrayList()
            val pets: List<Pet>? = petResult?.petfinder?.pets?.pet
            if(pets != null) {
                petData.addAll(PetDetailViewState.fromPetList(context, pets, RealmFavoritesManager(Realm.getDefaultInstance()))) // Realm must be created on the thread it is being accessed from
            }
            var offset = petResult?.petfinder?.lastOffset?.`$t`
            offset = if (offset == null) "0" else offset
            return PetMasterViewState(petData, offset)
        }
    }

}