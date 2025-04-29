package com.projet.first

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


            val connect = findViewById<Button>(R.id.connect)
            val email = findViewById<EditText>(R.id.email)
            val password = findViewById<EditText>(R.id.password)
            val error = findViewById<TextView>(R.id.error)


            connect.setOnClickListener {
                error.visibility = TextView.INVISIBLE
                val txtEmail = email.text.toString()
                val txtPassword = password.text.toString()
                if(txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()) {

                    error.text = "Vous devez remplir tout les champs!"
                    error.visibility = TextView.VISIBLE

                }
                else{
                    val correctEmail = "maher@gmail.com"
                    val correctPassword = "110322"
                    if(correctEmail == txtEmail && correctPassword == txtPassword){
                        email.setText("")
                        password.setText("")
                       // Intent Explicite
                        Intent(this, HomeActivity::class.java).also {
                            it.putExtra("email", txtEmail)
                            startActivity(it)
                        }
                    }
                    else{
                        error.text = "Email ou mot de passe incorrect"
                        error.visibility = TextView.VISIBLE

                    }
                    }

            }
        }
    }
