package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post: ParseObject() {
    fun getDescription():String?{
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(newDescription:String){
        put(KEY_DESCRIPTION, newDescription)
    }

    fun getImage():ParseFile?{
        return getParseFile(KEY_IMAGE)
    }
    fun setImage(newImage:ParseFile){
        put(KEY_IMAGE, newImage)
    }

    fun getUser():ParseUser?{
        return getParseUser(KEY_USER)
    }
    fun setUser(newUser: ParseUser){
        put(KEY_USER, newUser)
    }

    companion object{
        const val KEY_DESCRIPTION= "description"
        const val KEY_IMAGE= "image"
        const val KEY_USER= "user"
    }
}