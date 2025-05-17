package com.projet.first

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.projet.first.DB.FirstUpDB


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirstUpDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences("app_state", Context.MODE_PRIVATE)
        db = FirstUpDB(this)

        val isAuthenticated = sharedPreferences.getBoolean("isAuthenticated", false)
        val emailSharedPreferences = sharedPreferences.getString("email", "")
        if (isAuthenticated) {
            Intent(this, HomeActivity::class.java).also {
                it.putExtra("email", emailSharedPreferences)
                startActivity(it)
            }
        }


        val connect = findViewById<Button>(R.id.connect)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val error = findViewById<TextView>(R.id.error)
        val tvregister = findViewById<TextView>(R.id.tvregister)




        connect.setOnClickListener {
            error.visibility = View.GONE
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            if (txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()) {

                error.text = "Vous devez remplir tout les champs!"
                error.visibility = TextView.VISIBLE

            } else {
                val user = db.findUser(txtEmail, txtPassword)

                if (user != null) {
                    email.setText("")
                    password.setText("")

                    // enregistrer dans sharedPreferences les boolean isAuthenticated
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isAuthenticated", true)
                    editor.putString("email", txtEmail)
                    editor.apply()


                    // Intent Explicite
                    Intent(this, HomeActivity::class.java).also {
                        it.putExtra("email", txtEmail)
                        startActivity(it)
                    }
                } else {
                    error.text = "Email ou mot de passe incorrect"
                    error.visibility = TextView.VISIBLE

                }
            }

        }

        tvregister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}
