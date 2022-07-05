package com.wonmirzo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wonmirzo.R
import com.wonmirzo.adapter.FavoriteAdapter
import com.wonmirzo.adapter.HomeAdapter
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.handler.DBPostHandler
import com.wonmirzo.manager.handler.DBPostsHandler
import com.wonmirzo.model.Post
import com.wonmirzo.utils.DialogListener
import com.wonmirzo.utils.Utils
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : BaseFragment() {
    val TAG = FavoriteFragment::class.java.simpleName
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        loadLikedFeeds()
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = FavoriteAdapter(this, items)
        recyclerView.adapter = adapter
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)

        loadLikedFeeds()
    }

    private fun loadLikedFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadLikedFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    fun showDeleteDialog(post: Post) {
        Utils.dialogDouble(
            requireContext(),
            getString(R.string.str_delete_post),
            object : DialogListener {
                override fun onCallback(isChosen: Boolean) {
                    if (isChosen) {
                        deletePost(post)
                    }
                }
            })
    }

    fun deletePost(post: Post) {
        DatabaseManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadLikedFeeds()
            }

            override fun onError(e: Exception) {

            }
        })
    }
}