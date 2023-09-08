package com.example.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        usernameEditText = findViewById(R.id.registerUsernameEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginButton: Button = findViewById(R.id.backToLoginButton)

        registerButton.setOnClickListener {
            val enteredUsername = usernameEditText.text.toString().trim()
            val enteredPassword = passwordEditText.text.toString().trim()

            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
                // Kontrollera om användarnamnet är tillgängligt
                isUsernameAvailable(enteredUsername) { isAvailable ->
                    if (isAvailable) {

                        val user = hashMapOf(
                            "username" to enteredUsername, "password" to enteredPassword
                        )

                        db.collection("users").add(user).addOnSuccessListener {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_form -> {
                val intent = Intent(this, FormActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun isUsernameAvailable(username: String, callback: (Boolean) -> Unit) {
        db.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { querySnapshot ->
                // Om användarnamnet inte finns i databasen är det tillgängligt
                callback(querySnapshot.isEmpty)
            }.addOnFailureListener { exception ->
                // Hantera eventuella fel här
                callback(false)
            }
    }
}
