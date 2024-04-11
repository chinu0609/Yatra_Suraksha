package com.gb.yatrasuraksha.login_signup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gb.yatrasuraksha.MainActivity
import com.gb.yatrasuraksha.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var email: Editable? = null
    private var password: Editable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent: Intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        } else {
            registerAndLogin()
        }
    }

    private fun registerAndLogin() {
        binding.login.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signupButton.setOnClickListener {
            if (isFilled()) {
                val progressBar = ProgressDialog(this).apply {
                    setMessage("Registering...")
                    setCancelable(false)
                    setProgressStyle(ProgressDialog.STYLE_SPINNER)
                }
                progressBar.show()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.signupEmail.text.toString(),
                    binding.signupPassword.text.toString()
                ).addOnCompleteListener { task ->
                    progressBar.dismiss()
                    if (task.isSuccessful) {
                        val intent = Intent(this, PersonalDetailsActivity::class.java)
                        intent.putExtra("email", binding.signupEmail.text.toString())
                        intent.putExtra("password", binding.signupPassword.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    progressBar.dismiss()
                    Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please fill above fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isFilled(): Boolean {
        email = binding.signupEmail.text
        password = binding.signupPassword.text

        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }
}