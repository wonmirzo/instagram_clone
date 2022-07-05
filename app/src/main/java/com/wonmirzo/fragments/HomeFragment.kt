package com.wonmirzo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wonmirzo.R
import com.wonmirzo.adapter.HomeAdapter
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.handler.DBPostHandler
import com.wonmirzo.manager.handler.DBPostsHandler
import com.wonmirzo.manager.handler.DBUserHandler
import com.wonmirzo.model.Post
import com.wonmirzo.model.User
import com.wonmirzo.utils.DialogListener
import com.wonmirzo.utils.Utils
import java.lang.Exception
import java.lang.RuntimeException

class HomeFragment : BaseFragment() {
    private val TAG = HomeFragment::class.java.simpleName
    private var listener: HomeListener? = null
    private lateinit var recyclerView: RecyclerView
    var feeds = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && feeds.size > 0) {
            loadMyFeeds()
        }
    }

    /*
    * onAttach is for communication of Fragments
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is HomeListener) {
            context
        } else {
            throw RuntimeException("$context must implement HomeListener")
        }
    }

    /*
    * onDetach is for communication of Fragments
    * */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        val ivCamera: ImageView = view.findViewById(R.id.ivCamera)
        ivCamera.setOnClickListener {
            listener!!.scrollToUpload()
        }

        loadMyFeeds()
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {
        val adapter = HomeAdapter(this, posts)
        recyclerView.adapter = adapter
    }

    private fun loadMyFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                feeds.clear()
                feeds.addAll(posts)
                refreshAdapter(feeds)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                Utils.sendNotification(requireContext(), me!!, post.deviceToken)
            }

            override fun onError(e: Exception) {
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
                loadMyFeeds()
            }

            override fun onError(e: Exception) {

            }
        })
    }

    /*
    * This interface is created for communication with UploadFragment
    * */
    interface HomeListener {
        fun scrollToUpload()
    }
}