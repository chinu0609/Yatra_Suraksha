package com.gb.yatrasuraksha.reports

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gb.yatrasuraksha.R
import com.gb.yatrasuraksha.databinding.ActivityShowPdfBinding

class ShowPdfActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change color of status bar
        window.statusBarColor = resources.getColor(R.color.bg, theme)

        binding.pdfView.fromAsset("analysis2.pdf").load()
    }
}