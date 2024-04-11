package com.gb.yatrasuraksha

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.gb.yatrasuraksha.chatbot.ChatBotActivity
import com.gb.yatrasuraksha.databinding.ActivityMainBinding
import com.gb.yatrasuraksha.googlemaps.MapsActivity
import com.gb.yatrasuraksha.login_signup.SignInActivity
import com.gb.yatrasuraksha.probability.ProbabilityActivity
import com.gb.yatrasuraksha.reports.ShowPdfActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change color of status bar
        window.statusBarColor = resources.getColor(R.color.white, theme)

        //on clicking sign out button
        binding.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Custom animation speed or duration.
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            binding.animationView.progress = animation.animatedValue as Float
        }
        animator.start()

        //on clicking map button
        binding.map.setOnClickListener { startActivity(Intent(this, MapsActivity::class.java)) }

        //by clicking on chatbot
        binding.animationView.setOnClickListener { startActivity(Intent(this, ChatBotActivity::class.java)) }

        //by clicking on show report
        binding.showReport.setOnClickListener { startActivity(Intent(this, ShowPdfActivity::class.java)) }

        //by clicking on probability
        binding.probability.setOnClickListener { startActivity(Intent(this, ProbabilityActivity::class.java)) }
    }
}