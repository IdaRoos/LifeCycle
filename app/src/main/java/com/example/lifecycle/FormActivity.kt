package com.example.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val db = Firebase.firestore

        var nameEditText: EditText = findViewById(R.id.nameEditText)
        var ageEditText: EditText = findViewById(R.id.ageEditTextNumber)
        var emailEditText: EditText = findViewById(R.id.emailEditText)
        var drivingLicenseCheckBox: CheckBox = findViewById(R.id.drivingLicenseCheckBox)
        var genderRadioGroup: RadioGroup = findViewById(R.id.genderRadioGroup)
        var saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {

            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val email = emailEditText.text.toString()
            val hasDrivingLicense = drivingLicenseCheckBox.isChecked
            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            var gender = ""


            if (selectedGenderId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedGenderId)
                gender = selectedRadioButton.text.toString()
            }

            if (name.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty()) {

                val user = hashMapOf(
                    "name" to name,
                    "age" to age,
                    "email" to email,
                    "hasDrivingLicense" to hasDrivingLicense,
                    "gender" to gender,
                    "timestamp" to FieldValue.serverTimestamp()

                )


                // Lägg till användarobjektet i Firestore
                db.collection("usersinformation").add(user).addOnSuccessListener {

                        Toast.makeText(this, "Information added", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_LONG).show()

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