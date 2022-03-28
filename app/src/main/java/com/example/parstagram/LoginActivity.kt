package com.example.parstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin= findViewById<Button>(R.id.btnLogin)
        val btnSignup= findViewById<Button>(R.id.btnSignup)
        if(ParseUser.getCurrentUser()!=null){
            gotoMainActivity()
        }
        btnLogin.setOnClickListener{
            val username=etUsername.text.toString()
            val password=etPassword.text.toString()
            loginUser(username,password)
        }

        btnSignup.setOnClickListener{
            val username=etUsername.text.toString()
            val password=etPassword.text.toString()
            signupUser(username,password)
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG,"Successfully login user")
                gotoMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this,"Not login", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun gotoMainActivity() {
        val intent= Intent(this@LoginActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signupUser(username: String, password: String){
        val user = ParseUser()
        user.username = username
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null) {
                Log.i(TAG,"Successfully signup user")
                Toast.makeText(this,"Sign up successfully", Toast.LENGTH_SHORT).show()
                gotoMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT)
            }
        }
    }

    companion object{
       val TAG= "LoginActivity"
    }
}