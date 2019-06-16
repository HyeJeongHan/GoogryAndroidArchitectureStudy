package ado.sabgil.studyproject.adapter

import ado.sabgil.studyproject.R
import ado.sabgil.studyproject.data.model.Ticker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


class TickerAdapter : ListAdapter<Ticker, TickerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TickerViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_ticker, parent, false)
    )

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.executePendingBindings()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Ticker>() {
            override fun areItemsTheSame(oldItem: Ticker, newItem: Ticker): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Ticker, newItem: Ticker): Boolean {
                return oldItem == newItem
            }
        }
    }
}