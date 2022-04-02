package com.example.parstagram.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : FeedFragment() {


    override fun queryPost() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all post
        query.include(Post.KEY_USER)
        query.whereEqualTo(Post.KEY_USER,ParseUser.getCurrentUser())
        query.limit = 20
        query.addDescendingOrder("createdAt")
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e!=null){
                    e.printStackTrace()
                    Log.e(TAG, "Error fetching post")
                }
                else{
                    if (posts!=null){
                        displayPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })

    }

    companion object{
        val TAG = "FeedFragment"
    }


}