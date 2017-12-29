package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.model.shelter.ShelterResult

/**
 * Created by Rowan Hall
 */
class ShelterViewState(val shelterData: MutableList<ShelterListViewState>, val offset: String) {

    companion object {
        fun fromShelterResult(initialState: ShelterViewState?, shelterResult: ShelterResult?, clear: Boolean, context: Context): ShelterViewState {
            val shelterData: MutableList<ShelterListViewState> = if (!clear && initialState?.shelterData != null) initialState.shelterData else ArrayList()
            val shelters: List<Shelter>? = shelterResult?.petfinder?.shelters?.shelter
            if (shelters != null) {
                val listViewStates: MutableList<ShelterListViewState> = ArrayList()
                for (shelter in shelters) {
                    listViewStates.add(ShelterListViewState(context, shelter))
                }
                shelterData.addAll(listViewStates)
            }
            var offset = shelterResult?.petfinder?.lastOffset?.`$t`
            offset = if (offset == null) "0" else offset
            return ShelterViewState(shelterData, offset)
        }
    }

}