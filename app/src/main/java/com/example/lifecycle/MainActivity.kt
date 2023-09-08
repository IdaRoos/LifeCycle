package com.example.lifecycle

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.createAccountButton)

        val db = Firebase.firestore

        val sharedPrefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPrefs.getString("username", "")
        val savedPassword = sharedPrefs.getString("password", "")

        // Om sparade uppgifter finns, fyll i användarnamn och lösenord
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameEditText.setText(savedUsername)
            passwordEditText.setText(savedPassword)
        }


        loginButton.setOnClickListener {
            val enteredUsername = usernameEditText.text.toString().trim()
            val enteredPassword = passwordEditText.text.toString().trim()

            val editor = sharedPrefs.edit()
            editor.putString("username", enteredUsername)
            editor.putString("password", enteredPassword)
            editor.apply()

            // Hämta användaren från Firestore och kontrollera inloggningsuppgifterna
            db.collection("users")
                .whereEqualTo("username", enteredUsername)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {

                        val userDocument = result.documents[0]
                        val storedPassword = userDocument.getString("password")

                        if (enteredPassword == storedPassword) {
                            val intent = Intent(this, FormActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Felaktigt lösenord", Toast.LENGTH_SHORT).show()
                        }
                    } else {

                        Toast.makeText(applicationContext, "Användaren hittades inte", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val enteredUsername = usernameEditText.text.toString()
        val enteredPassword = passwordEditText.text.toString()
        outState.putString("username", enteredUsername)
        outState.putString("password", enteredPassword)
    }
}