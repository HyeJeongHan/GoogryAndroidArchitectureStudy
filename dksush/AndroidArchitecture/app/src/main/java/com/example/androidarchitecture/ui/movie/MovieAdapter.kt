package com.example.androidarchitecture.ui.movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.androidarchitecture.data.response.MovieData
import com.example.androidarchitecture.databinding.ItemMovieBinding
import com.example.androidarchitecture.ui.WebviewActivity
import com.example.androidarchitecture.ui.base.BaseRecyclerAdapter
import com.example.androidarchitecture.ui.base.BaseViewHolder


class MovieAdapter : BaseRecyclerAdapter<MovieData, MovieAdapter.MovieHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieHolder(
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context
    )

    inner class MovieHolder(private val binding: ItemMovieBinding, private val context: Context) :
        BaseViewHolder<MovieData>(binding.root) {
        lateinit var item: MovieData

        init {
            binding.setOnClick {
                Intent(context, WebviewActivity::class.java).apply {
                    putExtra("link", item.link)
                }.run { context.startActivity(this) }
            }
        }

        override fun bind(item: MovieData) {
            this.item = item
            with(binding) {
                items = item
                executePendingBindings()

            }
        }

    }

}

private class DiffCallback : DiffUtil.ItemCallback<MovieData>() {
    // 두 객체가 같은 항목인지.
    override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
        return oldItem == newItem

    }

    // 두 항목의 데이터가 같은지.
    // areItemsTheSame 가 true 일떄만 호출.
    override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
        return oldItem == newItem


    }

}