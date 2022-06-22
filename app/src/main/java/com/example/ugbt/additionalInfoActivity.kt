package com.example.ugbt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class additionalInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)
        var source1 = findViewById<TextView>(R.id.source1)
        source1.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.healthline.com/health/gallbladder-problems-symptoms"))
            startActivity(browserIntent)
        }
    }
}

//https://www.healthline.com/health/gallbladder-problems-symptoms