package com.example.userstory

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.userstory.databinding.ItemUserStoryBinding
import com.squareup.picasso.Picasso
import androidx.core.util.Pair

class UserStoryAdapter: PagingDataAdapter<ListStoryItem, UserStoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemUserStoryBinding, mView: View) : RecyclerView.ViewHolder(binding.root) {
        private val cardView: CardView = binding.cardView
        private val imageView: ImageView = binding.ivUserStory
        private val nameTextView: TextView = binding.tvName
        private var view = mView

        fun bind(data: ListStoryItem) {
            Picasso.get().load(data.photoUrl).into(imageView)
            nameTextView.text = data.name

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    view.context as Activity,
                    Pair.create(binding.ivUserStory, "photo"),
                    Pair.create(binding.tvName, "name")
                )

            cardView.setOnClickListener {
                val moveWithDataIntent = Intent(view.context, StoryDetailActivity::class.java)
                moveWithDataIntent.putExtra(StoryDetailActivity.EXTRA_PHOTO_URL, data.photoUrl)
                moveWithDataIntent.putExtra(StoryDetailActivity.EXTRA_NAME, data.name)
                moveWithDataIntent.putExtra(
                    StoryDetailActivity.EXTRA_DESCRIPTION, data.description
                )
                view.context.startActivity(moveWithDataIntent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}
