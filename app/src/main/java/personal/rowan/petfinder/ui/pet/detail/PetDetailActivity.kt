package personal.rowan.petfinder.ui.pet.detail

import android.content.Context
import android.content.Intent
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.base.ContainerActivity

/**
 * Created by Rowan Hall
 */
class PetDetailActivity : ContainerActivity() {

    companion object {
        private val ARG_PET_DETAIL_MODEL = "PetDetailActivity.Extra.Model"

        fun createIntent(context: Context, pet: Pet): Intent {
            val intent = Intent(context, PetDetailActivity::class.java)
            intent.putExtra(ARG_PET_DETAIL_MODEL, PetDetailViewModel(context, pet))
            return intent
        }
    }

    override fun getFragment(): BaseFragment? {
        return PetDetailFragment.getInstance(intent.getParcelableExtra(ARG_PET_DETAIL_MODEL))
    }

}