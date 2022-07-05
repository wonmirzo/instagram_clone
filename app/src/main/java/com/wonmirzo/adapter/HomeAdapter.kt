package com.wonmirzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.wonmirzo.R
import com.wonmirzo.fragments.HomeFragment
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.model.Post

class HomeAdapter(private var fragment: HomeFragment, var items: ArrayList<Post>) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_home, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]

        if (holder is PostViewHolder) {
            val ivPost = holder.ivPost
            val tvFullName = holder.tvFullName
            val ivProfile = holder.ivProfile
            val tvCaption = holder.tvCaption
            val tvTime = holder.tvTime
            val ivMore = holder.ivMore
            val ivLike = holder.ivLike

            tvFullName.text = post.fullName
            tvCaption.text = post.caption
            tvTime.text = post.currentDate

            Glide.with(fragment)
                .load(post.userImg)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(ivProfile)

            Glide.with(fragment)
                .load(post.postImg)
                .into(ivPost)

            ivLike.setOnClickListener {
                if (post.isLiked) {
                    post.isLiked = false
                    ivLike.setImageResource(R.mipmap.ic_favorite)
                } else {
                    post.isLiked = true
                    ivLike.setImageResource(R.mipmap.ic_favorite_filled)
                }
                fragment.likeOrUnlikePost(post)
            }

            if (post.isLiked) {
                ivLike.setImageResource(R.mipmap.ic_favorite_filled)
            } else {
                ivLike.setImageResource(R.mipmap.ic_favorite)
            }

            val uid = AuthManager.currentUser()!!.uid
            if (uid == post.uid) {
                ivMore.visibility = View.VISIBLE
            } else {
                ivMore.visibility = View.GONE
            }
            ivMore.setOnClickListener {
                fragment.showDeleteDialog(post)
            }
        }
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ShapeableImageView = view.findViewById(R.id.ivProfile)
        val ivPost: ShapeableImageView = view.findViewById(R.id.ivPost)
        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvCaption: TextView = view.findViewById(R.id.tvCaption)
        val ivMore: ImageView = view.findViewById(R.id.ivMore)
        val ivLike: ImageView = view.findViewById(R.id.ivLike)
        val ivShare: ImageView = view.findViewById(R.id.ivShare)
    }
}