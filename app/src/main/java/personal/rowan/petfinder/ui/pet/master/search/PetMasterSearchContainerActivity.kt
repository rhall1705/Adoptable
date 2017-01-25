package personal.rowan.petfinder.ui.pet.master.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterSearchContainerActivity : BaseActivity() {

    private val toolbar: Toolbar by bindView(R.id.pet_master_search_container_toolbar)
    private val container: FrameLayout by bindView(R.id.pet_master_search_container)

    companion object {

        private val ARG_LOCATION = "PetMasterSearchContainerActivity.Location"
        private val ARG_ANIMAL = "PetMasterSearchContainerActivity.Animal"
        private val ARG_SIZE = "PetMasterSearchContainerActivity.Size"
        private val ARG_AGE = "PetMasterSearchContainerActivity.Age"
        private val ARG_BREED = "PetMasterSearchContainerActivity.Breed"

        @JvmOverloads fun getIntent(context: Context, location: String, animal: String? = null, size: String? = null, age: String? = null, breed: String? = null): Intent {
            val intent: Intent = Intent(context, PetMasterSearchContainerActivity::class.java)
            intent.putExtra(ARG_LOCATION, location)
            intent.putExtra(ARG_ANIMAL, animal)
            intent.putExtra(ARG_SIZE, size)
            intent.putExtra(ARG_AGE, age)
            intent.putExtra(ARG_BREED, breed)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_master_search_container)

        setToolbar(toolbar, getString(R.string.pet_master_search_title), true)
        if(savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val intent = intent
            fragmentTransaction.replace(container.id, PetMasterFragment.getInstance(intent.getStringExtra(ARG_LOCATION), intent.getStringExtra(ARG_ANIMAL), intent.getStringExtra(ARG_SIZE), intent.getStringExtra(ARG_AGE), intent.getStringExtra(ARG_BREED)))
            fragmentTransaction.commit()
        }
    }

}
