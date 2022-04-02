package com.example.parstagram.Fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComposeFragment : Fragment() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPreview: ImageView
    lateinit var etDescription: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnPost).setOnClickListener{
            val description= etDescription.text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile!=null) {
                summitPost(description, user, photoFile!!)
                Toast.makeText(requireContext(),"Post successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e(MainActivity.TAG,"Error while taking photo")
                Toast.makeText(requireContext(),"Error while taking photo", Toast.LENGTH_SHORT).show()
            }

        }
        ivPreview= view.findViewById(R.id.ivPhoto)
        etDescription=view.findViewById<EditText>(R.id.etDescription)

        //Take photo button
        view.findViewById<Button>(R.id.btnTakePhoto).setOnClickListener{
            onLaunchCamera()
        }

//        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
//            ParseUser.logOut();
//            gotoLoginScreen()
//        }

        fun summitPost(description:String, user: ParseUser, photo: File) {
            var post= Post()
            post.setDescription(description)
            post.setUser(user)
            post.setImage(ParseFile(photo))
            post.saveInBackground{exception->
                if (exception!=null){
                    exception.printStackTrace()
                    Log.e(MainActivity.TAG, "Error while saving post")
                    Toast.makeText(requireContext(), "Error while saving post", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.i(MainActivity.TAG, "Successfully post")
                    etDescription.text.clear()
                }
            }
        }


    }

    private fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    private fun getPhotoFileUri(fileName: String): File? {
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    fun summitPost(description:String, user: ParseUser, photo:File) {
        var post= Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(photo))
        post.saveInBackground{exception->
            if (exception!=null){
                exception.printStackTrace()
                Log.e(MainActivity.TAG, "Error while saving post")
                Toast.makeText(requireContext(), "Error while saving post", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.i(MainActivity.TAG, "Successfully post")
                etDescription.text.clear()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}