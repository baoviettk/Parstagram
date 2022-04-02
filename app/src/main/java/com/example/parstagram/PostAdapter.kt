package com.example.parstagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(private val context: Context, private val posts: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post= posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Post>) {
        posts.addAll(tweetList)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvProfileId: TextView
        val ivPostPhoto: ImageView
        val tvPostDescription: TextView

        init {
            tvProfileId= itemView.findViewById(R.id.tvProfileId)
            ivPostPhoto= itemView.findViewById(R.id.ivPostPhoto)
            tvPostDescription= itemView.findViewById(R.id.tvPostDescription)
        }

        fun bind(post:Post){
            tvProfileId.text= post.getUser()?.username
            tvPostDescription.text=post.getDescription()
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivPostPhoto)
        }
    }
}