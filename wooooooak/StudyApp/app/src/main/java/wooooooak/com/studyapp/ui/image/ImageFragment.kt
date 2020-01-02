package wooooooak.com.studyapp.ui.image

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wooooooak.com.studyapp.R
import wooooooak.com.studyapp.common.ext.startWebView
import wooooooak.com.studyapp.data.model.database.AppDataBase
import wooooooak.com.studyapp.data.model.datasource.local.NaverLocalDataSourceImpl
import wooooooak.com.studyapp.data.model.repository.NaverApiRepositoryImpl
import wooooooak.com.studyapp.data.model.response.image.Image
import wooooooak.com.studyapp.data.model.sharedpreference.SharedPreferenceManager
import wooooooak.com.studyapp.ui.base.BaseSearchListAdapter
import wooooooak.com.studyapp.ui.base.ItemSearchFragment

class ImageFragment : ItemSearchFragment<Image>(R.layout.fragment_image) {

    override val adapter = ImageListAdapter(object : BaseSearchListAdapter.ItemListener<Image> {
        override fun loadMoreItems(list: List<Image>, index: Int) {
            lifecycleScope.launch {
                presenter.fetchMoreItems(list, index)
            }
        }

        override fun renderWebView(url: String) {
            requireContext().startWebView(url)
        }
    })

    private val presenter by lazy {
        ImagePresenter(
            this, NaverApiRepositoryImpl(
                NaverLocalDataSourceImpl(
                    SharedPreferenceManager(requireContext()),
                    AppDataBase.getDatabase(requireContext())
                )
            )
        )
    }

    override fun initItemsByTitle(title: String, cached: Boolean) {
        lifecycleScope.launch {
            presenter.fetchItemsWithNewTitle(title, cached)
        }
    }

}