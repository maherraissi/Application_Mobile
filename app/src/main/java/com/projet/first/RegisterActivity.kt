package com.projet.first

import android.annotation.SuppressLint
import android.content.Intent
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

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var db: FirstUpDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        db = FirstUpDB(this)

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnSave.setOnClickListener {
            tvError.visibility = TextView.INVISIBLE
            tvError.text = ""

            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val confirmPassword = editConfirmPassword.text.toString()

            // Check if null

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                tvError.text = getString(R.string.error_empty_fields)
                tvError.visibility = View.VISIBLE
            } else {
                // check if password and confirm password are the same
                if (password != confirmPassword) {
                    tvError.text = getString(R.string.password_different)
                    tvError.visibility = View.VISIBLE
                } else {
                    val user = User( name, email, password)
                    val isInserted = db.addUser(user)
                    if (isInserted) {
                        Toast.makeText(this, getString(R.string.success_registration), Toast.LENGTH_SHORT
                        ).show()
                        Intent(this, HomeActivity::class.java).also {
                            it.putExtra("email", email)
                            startActivity(it)
                        }
                        finish()
                    }
                }
            }
        }
    }
}
