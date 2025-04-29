package com.projet.first

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_details)

        val tvTitre = findViewById<TextView>(R.id.tvTitre)
        val titre = intent.getStringExtra("titre")
        tvTitre.text = titre

        supportActionBar!!.title = titre
    }
}
