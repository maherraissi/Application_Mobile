package com.projet.first

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.Post
import java.io.ByteArrayOutputStream

class AddPostActivity : AppCompatActivity() {

    lateinit var btnEnregistrer: Button
    lateinit var editTitle: EditText
    lateinit var editDescription: EditText
    lateinit var imagePost: ImageView

    var bitmap: Bitmap? = null
    lateinit var db: FirstUpDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        db = FirstUpDB(this)

        btnEnregistrer = findViewById(R.id.btnEnregistrer)
        editTitle = findViewById(R.id.editTitle)
        editDescription = findViewById(R.id.editDescription)
        imagePost = findViewById(R.id.imagePost)

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { data ->
                val inputStream = data?.let { contentResolver.openInputStream(it) }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imagePost.setImageBitmap(bitmap)
            }


        // recuperer l'image

        imagePost.setOnClickListener {
            // ouvrir la galerie
            galleryLauncher.launch("image/*")

        }


        btnEnregistrer.setOnClickListener {
            // recuperer les valeurs
            val titre = editTitle.text.toString()
            val description = editDescription.text.toString()
            if (titre.isEmpty() || description.isEmpty() || bitmap == null) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            } else {
                val imagesBlob: ByteArray = getBytes(bitmap!!)

                val post = Post(titre, description, imagesBlob)


                db.addPost(post)

                // supprimer les valeurs
                editTitle.setText("")
                editDescription.setText("")
                bitmap = null

                finish()
            }
        }
    }// fin onCreate

    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()



        }
    }

