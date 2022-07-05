package com.wonmirzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.wonmirzo.R
import com.wonmirzo.fragments.SearchFragment
import com.wonmirzo.model.User

class SearchAdapter(private var fragment: SearchFragment, private var items: ArrayList<User>) :
    BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user: User = items[position]

        if (holder is UserViewHolder) {
            holder.tvFullName.text = user.fullName
            holder.tvEmail.text = user.email

            Glide.with(fragment).load(user.userImg)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(holder.ivProfile)

            val tvFollow = holder.tvFollow
            tvFollow.setOnClickListener {
                if (!user.isFollowed) {
                    tvFollow.text = fragment.getString(R.string.str_following)
                } else {
                    tvFollow.text = fragment.getString(R.string.str_follow)
                }
                fragment.followOrUnfollow(user)
            }

            if (!user.isFollowed) {
                tvFollow.text = fragment.getString(R.string.str_follow)
            } else {
                tvFollow.text = fragment.getString(R.string.str_following)
            }
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ShapeableImageView = view.findViewById(R.id.ivProfile)
        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvFollow: TextView = view.findViewById(R.id.tvFollow)
    }
}