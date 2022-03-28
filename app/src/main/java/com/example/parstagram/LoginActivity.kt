package com.example.parstagram

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser


class LoginActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnSignup: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin= findViewById(R.id.btnLogin)
        btnSignup= findViewById(R.id.btnSignup)
        if(ParseUser.getCurrentUser()!=null){
            gotoMainActivity()
        }
        btnLogin.setOnClickListener{
            val username=etUsername.text.toString()
            val password=etPassword.text.toString()
            loginUser(username,password)
        }
        btnLogin.isEnabled=false
        btnSignup.isEnabled=false

        btnSignup.setOnClickListener{
            val username=etUsername.text.toString()
            val password=etPassword.text.toString()
            signupUser(username,password)
        }

        etUsername.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0==null){
                    Log.e(TAG, "Error at when adding user name")
                }
                else if (p0.isEmpty()){
                    btnLogin.isEnabled=false
                    btnSignup.isEnabled=false
                    Log.i(TAG, "Buttons are disabled")
                }
                else if(etPassword.text.length>0){
                    btnLogin.isEnabled=true
                    btnSignup.isEnabled=true

                }
            }



        })

        etPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0==null){
                    Log.e(TAG, "Error at when adding password")
                }
                else if (p0.isEmpty()){
                    btnLogin.isEnabled=false
                    btnSignup.isEnabled=false
                }
                else if(etUsername.text.length>0){
                    btnLogin.isEnabled=true
                    btnSignup.isEnabled=true

                }
            }

        })
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