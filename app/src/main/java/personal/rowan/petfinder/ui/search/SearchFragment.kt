package personal.rowan.petfinder.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment

/**
 * Created by Rowan Hall
 */
class SearchFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

}