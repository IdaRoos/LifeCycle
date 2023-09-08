package com.example.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var drivingLicenseTextView: TextView
    private lateinit var genderTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        nameTextView = findViewById(R.id.nameTextView)
        ageTextView = findViewById(R.id.ageTextView)
        emailTextView = findViewById(R.id.emailTextView)
        drivingLicenseTextView = findViewById(R.id.drivingLicenseTextView)
        genderTextView = findViewById(R.id.genderTextView)


        val db = Firebase.firestore

// Hämta alla dokument i collection "usersinformation"
        db.collection("usersinformation")
            .orderBy("timestamp", Query.Direction.DESCENDING) // order by senast tillagda datan
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val latestUserDocument = querySnapshot.documents[0]
                    val userData = latestUserDocument.data

                    if (userData != null) {
                        nameTextView.text = "Name: ${userData["name"]}"
                        ageTextView.text = "Age: ${userData["age"]}"
                        emailTextView.text = "Email: ${userData["email"]}"
                        drivingLicenseTextView.text = "Has Driving License: ${userData["hasDrivingLicense"]}"
                        genderTextView.text = "Gender: ${userData["gender"]}"
                    } else {
                        Toast.makeText(this, "User data is null", Toast.LENGTH_LONG).show()
                    }
                } else {

                    Toast.makeText(this, "Couldn't find user information", Toast.LENGTH_LONG).show()
                }
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
}