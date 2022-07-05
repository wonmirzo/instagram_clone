package com.wonmirzo.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.wonmirzo.R
import com.wonmirzo.adapter.ProfileAdapter
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.StorageManager
import com.wonmirzo.manager.handler.DBPostsHandler
import com.wonmirzo.manager.handler.DBUserHandler
import com.wonmirzo.manager.handler.DBUsersHandler
import com.wonmirzo.manager.handler.StorageHandler
import com.wonmirzo.model.Post
import com.wonmirzo.model.User
import java.lang.Exception

/*
* In ProfileFragment posts that user uploaded can be seen and user is able to change his/her profile photo
* */
class ProfileFragment : BaseFragment() {
    private val TAG = ProfileFragment::class.java.simpleName
    private lateinit var rvProfile: RecyclerView

    private var pickedPhoto: Uri? = null
    private var allPhotos = ArrayList<Uri>()

    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var ivProfile: ShapeableImageView
    private lateinit var tvPosts: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rvProfile = view.findViewById(R.id.rvProfile)
        rvProfile.layoutManager = GridLayoutManager(activity, 2)

        tvFullName = view.findViewById(R.id.tvFullName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPosts = view.findViewById(R.id.tvPosts)
        tvFollowers = view.findViewById(R.id.tvFollowers)
        tvFollowing = view.findViewById(R.id.tvFollowing)
        ivProfile = view.findViewById(R.id.ivProfile)

        val ivLogOut: ImageView = view.findViewById(R.id.ivLogOut)
        ivLogOut.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(requireActivity())
        }

        ivProfile.setOnClickListener {
            pickFishBunPhoto()
        }

        loadUserInfo()
        loadMyPosts()
        loadMyFollowing()
        loadMyFollowers()
    }

    private fun loadMyFollowing() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                tvFollowing.text = users.size.toString()
            }

            override fun onError(e: Exception) {
            }
        })
    }

    private fun loadMyFollowers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowers(uid, object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                tvFollowers.text = users.size.toString()
            }

            override fun onError(e: Exception) {
            }
        })
    }

    private fun loadMyPosts() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadPosts(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                tvPosts.text = posts.size.toString()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
            }
        })
    }

    private fun loadUserInfo() {
        DatabaseManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null) {
                    showUserInfo(user)
                }
            }

            override fun onError(e: Exception) {
            }
        })
    }

    private fun uploadUserPhoto() {
        if (pickedPhoto == null) return
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                DatabaseManager.updateUserImage(imgUrl)
                ivProfile.setImageURI(pickedPhoto)
            }

            override fun onError(exception: Exception?) {

            }
        })
    }

    private fun showUserInfo(user: User) {
        tvFullName.text = user.fullName
        tvEmail.text = user.email
        Glide.with(this).load(user.userImg)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(ivProfile)
    }

    /**
     * Pick photo using FishBun library
     */
    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                uploadUserPhoto()
            }
        }


    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = ProfileAdapter(this, items)
        rvProfile.adapter = adapter
    }

}