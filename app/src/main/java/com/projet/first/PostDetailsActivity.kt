package com.projet.first

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import com.projet.first.DB.FirstUpDB

class
PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        // Récupération des données
        val titre = intent.getStringExtra("titre")
        val description = intent.getStringExtra("description")
        val image = intent.getByteArrayExtra("image")
        val id = intent.getIntExtra("id", -1)

        val etTitre = findViewById<EditText>(R.id.etTitre)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val imagePost = findViewById<ImageView>(R.id.imagePost)
        val btnModifier = findViewById<Button>(R.id.btnModifier)
        val btnEnregistrer = findViewById<Button>(R.id.btnEnregistrer)

        // Désactiver les champs au départ
        etTitre.isEnabled = false
        etDescription.isEnabled = false
        imagePost.isEnabled = false
        var imageBytes = image // pour garder l'image modifiée

        etTitre.setText(titre)
        etDescription.setText(description)
        if (image != null) {
            val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            imagePost.setImageBitmap(bitmap)
        }

        // Gestion du choix d'une nouvelle image
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { data ->
            val inputStream = data?.let { contentResolver.openInputStream(it) }
            val selectedBitmap = BitmapFactory.decodeStream(inputStream)
            if (selectedBitmap != null) {
                val resizedBitmap = resizeBitmap(selectedBitmap, 800)
                imagePost.setImageBitmap(resizedBitmap)
                // Convertir en ByteArray pour sauvegarde
                val stream = java.io.ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                imageBytes = stream.toByteArray()
            }
        }

        btnModifier.setOnClickListener {
            etTitre.isEnabled = true
            etDescription.isEnabled = true
            imagePost.isEnabled = true
            imagePost.setOnClickListener {
                galleryLauncher.launch("image/*")
            }
        }

        btnEnregistrer.setOnClickListener {
            // Mettre à jour le post dans la base de données
            val nouveauTitre = etTitre.text.toString()
            val nouvelleDescription = etDescription.text.toString()
            val nouvelleImage = imageBytes
            if (id != -1 && nouveauTitre.isNotEmpty() && nouvelleDescription.isNotEmpty() && nouvelleImage != null) {
                val db = FirstUpDB(this)
                val post = com.projet.first.Data.Post(id, nouveauTitre, nouvelleDescription, nouvelleImage, 0)
                db.updatePost(post)
            }
            // Redirection vers HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Toolbar personnalisée
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = titre ?: "Détails"

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
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
}
