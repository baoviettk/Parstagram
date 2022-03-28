package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var etDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Post button
        findViewById<Button>(R.id.btnPost).setOnClickListener{
            val description= etDescription.text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile!=null) {
                summitPost(description, user, photoFile!!)
                Toast.makeText(this,"Post successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e(TAG,"Error while taking photo")
                Toast.makeText(this,"Error while taking photo", Toast.LENGTH_SHORT).show()
            }

        }
        etDescription=findViewById<EditText>(R.id.etDescription)

        //Take photo button
        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener{
            onLaunchCamera()
        }

        queryPost()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.ivPhoto)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun summitPost(description:String, user: ParseUser, photo:File) {
        var post= Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(photo))
        post.saveInBackground{exception->
            if (exception!=null){
                exception.printStackTrace()
                Log.e(TAG, "Error while saving post")
                Toast.makeText(this, "Error while saving post", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.i(TAG, "Successfully post")
                etDescription.text.clear()
            }
        }
    }

    private fun queryPost() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all post
        query.include(Post.KEY_USER)
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e!=null){
                    e.printStackTrace()
                    Log.e(TAG, "Error fetching post")
                }
                else{
                    if (posts!=null){
                        for (post in posts){
                            Log.i(TAG, "Post: "+ post.getDescription() + " User: " + (post.getUser()?.username))
                        }
                    }
                }
            }

        })

    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    fun getPhotoFileUri(fileName: String): File? {
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    companion object {
        val TAG="MainActivity"
    }

}