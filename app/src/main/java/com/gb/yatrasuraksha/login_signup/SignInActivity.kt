package com.gb.yatrasuraksha.login_signup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.gb.yatrasuraksha.MainActivity
import com.gb.yatrasuraksha.databinding.ActivitySignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.loginButton.setOnClickListener { loginButtonClick() }

        binding.register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginButtonClick() {
        val email = binding.signinEmail.text.toString()
        val password = binding.signinPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_LONG).show()
        } else {
            val progressBar = ProgressDialog(this).apply {
                setMessage("Logging in...")
                setCancelable(false)
                setProgressStyle(ProgressDialog.STYLE_SPINNER)
            }
            progressBar.show()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.dismiss()
                    if (task.isSuccessful) {
                        Firebase.firestore.collection("User")
                            .document(Firebase.auth.currentUser!!.uid)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Fill personal details", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, PersonalDetailsActivity::class.java))
                                    finish()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}