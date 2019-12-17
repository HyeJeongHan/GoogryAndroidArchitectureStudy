package com.jay.architecturestudy.ui.image

import android.os.Bundle
import android.util.Log
import com.jay.architecturestudy.R
import com.jay.architecturestudy.data.model.Image
import com.jay.architecturestudy.data.model.ResponseNaverQuery
import com.jay.architecturestudy.data.repository.NaverSearchRepositoryImpl
import com.jay.architecturestudy.network.Api
import com.jay.architecturestudy.ui.BaseFragment
import com.jay.architecturestudy.util.toPx
import com.jay.architecturestudy.widget.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragemnt_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageFragment : BaseFragment(R.layout.fragemnt_image) {

    private lateinit var imageAdapter: ImageAdapter

    private val naverSearchRepository by lazy {
        NaverSearchRepositoryImpl()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            imageAdapter = ImageAdapter()
            recycler_view.run {
                adapter = imageAdapter
                addItemDecoration(SpacesItemDecoration(12.toPx(), 6.toPx(), 11.toPx()))
            }
        }

        search_bar.onClickAction = { keyword ->
            search(keyword)
        }
    }

    override fun search(keyword: String) {
        naverSearchRepository.getImage(
            keyword = keyword,
            success = {responseImage ->
                imageAdapter.setData(responseImage.images)
            },
            fail = { e ->
                Log.e("Image", "error=${e.message}")
            }
        )
    }
}