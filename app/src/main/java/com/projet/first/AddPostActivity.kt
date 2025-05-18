package com.projet.first

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

        Toast.makeText(this, "AddPostActivity lancée", Toast.LENGTH_SHORT).show()

        db = FirstUpDB(this)

        btnEnregistrer = findViewById(R.id.btnEnregistrer)
        editTitle = findViewById(R.id.editTitle)
        editDescription = findViewById(R.id.editDescription)
        imagePost = findViewById(R.id.imagePost)

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { data ->
                val inputStream = data?.let { contentResolver.openInputStream(it) }
                val selectedBitmap = BitmapFactory.decodeStream(inputStream)
                if (selectedBitmap != null) {
                    // Redimensionner l'image avant de l'utiliser
                    bitmap = resizeBitmap(selectedBitmap, 800)
                    imagePost.setImageBitmap(bitmap)
                }
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
                if (bitmap == null) {
                    Toast.makeText(this, "Veuillez sélectionner une image svp !", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("DEBUG", "Avant getBytes")
                val imagesBlob: ByteArray = getBytes(bitmap!!)
                Log.d("DEBUG", "Après getBytes")
                val post = Post(titre, description, imagesBlob)
                Log.d("DEBUG", "Avant addPost")
                db.addPost(post)
                Log.d("DEBUG", "Après addPost")
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

// Fonction utilitaire pour redimensionner le bitmap
fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val bitmapRatio: Float = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int
    if (bitmapRatio > 1) {
        newWidth = maxSize
        newHeight = (maxSize / bitmapRatio).toInt()
    } else {
        newHeight = maxSize
        newWidth = (maxSize * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}

