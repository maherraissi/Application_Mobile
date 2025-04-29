package com.projet.first

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HomeActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Accueil"


        val email = intent.getStringExtra("email")

        val listPosts = findViewById<ListView>(R.id.listPosts)
        val postsArray = listOf(
            "Post 1",
            "Post 2",
            "Post 3",
            "Post 4",
            "Post 5",
            "Post 6",
            "Post 7",
            "Post 8",
            "Post 9",
            "Post 10"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postsArray)
        listPosts.adapter = adapter



    }
}

