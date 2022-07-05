package com.wonmirzo.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wonmirzo.R
import com.wonmirzo.adapter.SearchAdapter
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.handler.DBFollowHandler
import com.wonmirzo.manager.handler.DBUserHandler
import com.wonmirzo.manager.handler.DBUsersHandler
import com.wonmirzo.model.User
import com.wonmirzo.utils.Utils
import java.lang.Exception

/*
* In SearchFragment all registered users can be found by searching keyword and followed
* */
class SearchFragment : BaseFragment() {
    private val TAG = SearchFragment::class.java.simpleName
    private lateinit var rvSearch: RecyclerView
    private var items = ArrayList<User>()
    private var users = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rvSearch = view.findViewById(R.id.rvSearch)
        rvSearch.layoutManager = GridLayoutManager(activity, 1)

        val etSearch: EditText = view.findViewById(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                usersByKeyword(keyword)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        loadUsers()
    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val adapter = SearchAdapter(this, items)
        rvSearch.adapter = adapter
    }

    fun usersByKeyword(keyword: String) {
        if (keyword.isEmpty())
            refreshAdapter(items)

        users.clear()
        for (user in items)
            if (user.fullName.lowercase().startsWith(keyword.lowercase()))
                users.add(user)

        refreshAdapter(users)
    }

    private fun loadUsers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
                    override fun onSuccess(following: ArrayList<User>) {
                        items.clear()
                        items.addAll(mergedUsers(uid, users, following))
                        refreshAdapter(items)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun mergedUsers(
        uid: String,
        users: ArrayList<User>,
        following: ArrayList<User>
    ): ArrayList<User> {
        val items = ArrayList<User>()
        for (u in users) {
            for (f in following) {
                if (u.uid == f.uid) {
                    u.isFollowed = true
                    break
                }
            }
            if (uid != u.uid) {
                items.add(u)
            }
        }
        return items
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun followUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = true
                        DatabaseManager.storePostsToMyFeed(uid, to)
                        Utils.sendNotification(requireContext(), me, to.deviceToken)
                    }

                    override fun onError(e: Exception) {
                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun unFollowUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = false
                        DatabaseManager.removePostsFromMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }
}