package com.wonmirzo.fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.wonmirzo.R
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.StorageManager
import com.wonmirzo.manager.handler.DBPostHandler
import com.wonmirzo.manager.handler.DBUserHandler
import com.wonmirzo.manager.handler.StorageHandler
import com.wonmirzo.model.Post
import com.wonmirzo.model.User
import com.wonmirzo.utils.Logger
import com.wonmirzo.utils.Utils
import java.lang.Exception
import java.lang.RuntimeException


/*
* In UploadFragment, user can upload
* a post with photo and caption
* */
class UploadFragment : BaseFragment() {
    private val TAG = UploadFragment::class.java.simpleName
    private var listener: UploadListener? = null

    private lateinit var flPhoto: FrameLayout
    private lateinit var ivPhoto: ImageView
    private lateinit var etCaption: EditText

    private var pickedPhoto: Uri? = null
    private var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        initViews(view)
        return view
    }

    /*
    * onAttach is for communication of Fragments
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is UploadListener) {
            context
        } else {
            throw RuntimeException("$context must implement UploadListener")
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
        val flView: FrameLayout = view.findViewById(R.id.flView)
        setViewHeight(flView)
        etCaption = view.findViewById(R.id.etCaption)
        flPhoto = view.findViewById(R.id.flPhoto)
        ivPhoto = view.findViewById(R.id.ivPhoto)

        val ivClose: ImageView = view.findViewById(R.id.ivClose)
        val ivPick: ImageView = view.findViewById(R.id.ivPick)
        val ivUpload: ImageView = view.findViewById(R.id.ivUpload)

        ivPick.setOnClickListener {
            pickFishBunPhoto()
        }
        ivClose.setOnClickListener {
            hidePickedPhoto()
        }
        ivUpload.setOnClickListener {
            uploadNewPost()
        }
    }

    /*
    * Set view height as screen width
    * */
    private fun setViewHeight(view: FrameLayout) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(requireActivity().application).width
        view.layoutParams
    }

    /*
    * Pick photo using FishBun library
    * */
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
                showPickedPhoto()
            }
        }

    private fun showPickedPhoto() {
        flPhoto.visibility = View.VISIBLE
        ivPhoto.setImageURI(pickedPhoto)
    }

    private fun hidePickedPhoto() {
        pickedPhoto = null
        flPhoto.visibility = View.GONE
    }

    private fun uploadNewPost() {
        val caption = etCaption.text.toString().trim()
        if (caption.isNotEmpty() && pickedPhoto != null) {
            uploadPostPhoto(caption, pickedPhoto!!)
        }
    }

    private fun uploadPostPhoto(caption: String, uri: Uri) {
        showLoading(requireActivity())
        StorageManager.uploadPostPhoto(uri, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                val post = Post(caption, imgUrl)
                post.setCurrentTime()
                val uid = AuthManager.currentUser()!!.uid

                DatabaseManager.loadUser(uid, object : DBUserHandler {
                    override fun onSuccess(user: User?) {
                        post.uid = uid
                        post.fullName = user!!.fullName
                        post.userImg = user.userImg
                        storePostToDB(post)
                    }

                    override fun onError(e: Exception) {
                    }
                })
            }

            override fun onError(exception: Exception?) {

            }
        })
    }

    private fun storePostToDB(post: Post) {
        DatabaseManager.storePosts(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                storeFeedToDB(post)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun storeFeedToDB(post: Post) {
        DatabaseManager.storeFeeds(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                dismissLoading()
                resetAll()
                listener!!.scrollToHome()
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }


    private fun resetAll() {
        allPhotos.clear()
        etCaption.text.clear()
        pickedPhoto = null
        flPhoto.visibility = View.GONE
    }

    /*
    * This interface is created for communication with HomeFragment
    * */
    interface UploadListener {
        fun scrollToHome()
    }
}