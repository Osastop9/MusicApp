package com.example.musicapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapptest.model.MusicItem
import com.example.musicapptest.repository.NetworkState

class MusicAdapter(public val context: Context) : PagedListAdapter<MusicItem, RecyclerView.ViewHolder>(MusicDiffCallback()) {

    val MUSIC_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MUSIC_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.music_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_layout, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MUSIC_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MUSIC_VIEW_TYPE
        }
    }

    class MusicDiffCallback : DiffUtil.ItemCallback<MusicItem>() {
        override fun areItemsTheSame(oldItem: MusicItem, newItem: MusicItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MusicItem, newItem: MusicItem): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(music: MusicItem?, context: Context) {
            itemView.findViewById<TextView>(R.id.tv_artist_name).text = music?.artist_name
            itemView.findViewById<TextView>(R.id.tv_title).text = music?.name
            val coverImage = music?.artwork
            Glide.with(itemView.context)
                .load(coverImage)
                .into(itemView.findViewById(R.id.iv_albumImage))

            itemView.setOnClickListener {
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.findViewById<ProgressBar>(R.id.progress_bar_item).visibility = View.VISIBLE
            } else {
                itemView.findViewById<ProgressBar>(R.id.progress_bar_item).visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.findViewById<TextView>(R.id.error_msg_item).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.error_msg_item).text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.findViewById<TextView>(R.id.error_msg_item).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.error_msg_item).text = networkState.msg
            } else {
                itemView.findViewById<TextView>(R.id.error_msg_item).visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) { // hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount()) // remove the progressbar at the end
            } else { // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount()) // add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { // hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1) // add the network message at the end
        }
    }
}
