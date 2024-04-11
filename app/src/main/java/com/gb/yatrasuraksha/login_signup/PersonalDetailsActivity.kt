package com.gb.yatrasuraksha.login_signup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.widget.Toast
import com.gb.yatrasuraksha.MainActivity
import com.gb.yatrasuraksha.databinding.ActivityPersonalDetailsBinding
import com.gb.yatrasuraksha.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPersonalDetailsBinding
    private var name : Editable? = null
    private var phoneNumber : Editable? = null
    private var emergencyContact : Editable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //remove status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //get data from intent
        val email : String = intent.getStringExtra("email")!!
        val password : String = intent.getStringExtra("password")!!

        //signing in with details
        binding.addButton.setOnClickListener {
            if (isFilled()) {
                val progressBar = ProgressDialog(this).apply {
                    setMessage("Adding user details...")
                    setCancelable(false)
                    setProgressStyle(ProgressDialog.STYLE_SPINNER)
                }
                progressBar.show() // Show the progress bar

                val user = User(email, password, binding.name.text.toString(), binding.phoneNumber.text.toString(), binding.emergencyContact.text.toString())

                Firebase.firestore.collection("User")
                    .document(Firebase.auth.currentUser!!.uid)
                    .set(user)
                    .addOnSuccessListener {
                        progressBar.dismiss() // Dismiss the progress bar upon success
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        progressBar.dismiss() // Dismiss the progress bar on failure
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill above fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isFilled(): Boolean {
        name = binding.name.text
        phoneNumber = binding.phoneNumber.text
        emergencyContact = binding.emergencyContact.text

        return !(name.isNullOrEmpty() || phoneNumber.isNullOrEmpty() || emergencyContact.isNullOrEmpty())
    }

}