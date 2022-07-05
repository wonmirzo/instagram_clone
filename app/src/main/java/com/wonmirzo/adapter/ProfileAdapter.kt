package com.wonmirzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.wonmirzo.R
import com.wonmirzo.fragments.ProfileFragment
import com.wonmirzo.model.Post
import com.wonmirzo.utils.Utils

class ProfileAdapter(private var fragment: ProfileFragment, private var items: ArrayList<Post>) :
    BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_profile, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]

        if (holder is PostViewHolder) {
            val ivPost = holder.ivPost
            setViewHeight(ivPost)
            Glide.with(fragment).load(post.postImg).into(ivPost)
        }
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPost: ShapeableImageView = view.findViewById(R.id.ivPost)
    }


    /**
     * Set ShapeableImageView height as screen width
     */
    private fun setViewHeight(view: View) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width / 2
        view.layoutParams = params

    }
}