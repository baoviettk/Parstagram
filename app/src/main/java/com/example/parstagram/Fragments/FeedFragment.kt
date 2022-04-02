package com.example.parstagram.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class FeedFragment : Fragment() {
    lateinit var rvPost:RecyclerView
    lateinit var adapter: PostAdapter
    var displayPosts :ArrayList<Post> = arrayListOf()
    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPost= view.findViewById(R.id.rvPost)
        swipeContainer=view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            queryPost()
        }
        adapter= PostAdapter(requireContext(),displayPosts)
        rvPost.adapter=adapter
        rvPost.layoutManager=LinearLayoutManager(requireContext())
        queryPost()


    }

    open fun queryPost() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all post
        query.include(Post.KEY_USER)
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
                        adapter.clear()
                        adapter.addAll(posts)
                        displayPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
        swipeContainer.isRefreshing=false
    }

    companion object{
        val TAG = "FeedFragment"
    }


}