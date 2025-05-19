package com.projet.first

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.User

class ModifyProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    lateinit var db: FirstUpDB

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var tvError: TextView

    // Current user to modify
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify_profile)

        db = FirstUpDB(this)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)
        tvError = findViewById(R.id.tvError)

        tvError.visibility = View.INVISIBLE

        // Load user from intent or DB — replace with your own logic
        val userId = intent.getIntExtra("userId", -1)
        if (userId != -1) {
            val userFromDb = db.getUserById(userId)
            if (userFromDb != null) {
                currentUser = userFromDb
                etName.setText(currentUser.name)
                etEmail.setText(currentUser.email)
                etPassword.setText(currentUser.password)
            } else {
                Toast.makeText(this, "Utilisateur non trouvé", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Erreur lors du chargement du profil", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSave.setOnClickListener {
            tvError.visibility = View.INVISIBLE
            tvError.text = ""

            val updatedName = etName.text.toString().trim()
            val updatedEmail = etEmail.text.toString().trim()
            val updatedPassword = etPassword.text.toString()

            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPassword.isEmpty()) {
                tvError.text = getString(R.string.error_empty_fields)
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Update user object
            currentUser.name = updatedName
            currentUser.email = updatedEmail
            currentUser.password = updatedPassword

            // Update user in DB
            val isUpdated = db.updateUser(currentUser)
            if (isUpdated) {
                Toast.makeText(this, "Profil mis à jour avec succès !", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                tvError.text = "Erreur lors de la mise à jour"
                tvError.visibility = View.VISIBLE
            }
        }
    }
}
