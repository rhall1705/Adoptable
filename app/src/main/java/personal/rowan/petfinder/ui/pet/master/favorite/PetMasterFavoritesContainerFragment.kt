package personal.rowan.petfinder.ui.pet.master.favorite

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterFavoritesContainerFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_master_favorites_container_toolbar)
    private val container: FrameLayout by bindView(R.id.pet_master_favorites_container)

    companion object {
        fun getInstance(): PetMasterFavoritesContainerFragment {
            return PetMasterFavoritesContainerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_master_favorites_container, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(toolbar, getString(R.string.pet_master_favorites_title))
        if(savedInstanceState == null) {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(container.id,
                    PetMasterFragment.getInstance())
            fragmentTransaction.commit()
        }
    }

}