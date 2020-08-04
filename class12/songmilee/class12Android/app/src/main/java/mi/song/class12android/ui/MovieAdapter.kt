package mi.song.class12android.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mi.song.class12android.R
import mi.song.class12android.data.model.MovieInfo
import mi.song.class12android.databinding.ItemMovieBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieVh>() {
    private val movieList = ArrayList<MovieInfo>()

    fun addMovieInfo(movieList: List<MovieInfo>) {
        this.movieList.addAll(movieList)
        notifyDataSetChanged()
    }

    fun clearMovieList() {
        movieList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVh {
        val binding: ItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return MovieVh(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieVh, position: Int) {
        holder.bind(movieList[position])
    }

    inner class MovieVh(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieInfo: MovieInfo) {
            binding.movieInfo = movieInfo
            binding.executePendingBindings()
        }
    }
}