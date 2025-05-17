package com.projet.first

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.Post
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    lateinit var listPosts: RecyclerView
    var postsArray = arrayListOf<Post>()
    lateinit var adapter: PostsRecyclerAdapter
    lateinit var db: FirstUpDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = FirstUpDB(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Accueil"

        // recuperer l'email de l'utilisateur
        val email = intent.getStringExtra("email")

        listPosts = findViewById(R.id.listPosts)
        listPosts.layoutManager = LinearLayoutManager(this)
        adapter = PostsRecyclerAdapter(this, postsArray)
        listPosts.adapter = adapter
    } // fin onCreate

    override fun onResume() {
        super.onResume()
        postsArray.clear()
        postsArray.addAll(db.findPosts())
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAdd -> {
                Toast.makeText(this, "Bouton Ajouter cliqué", Toast.LENGTH_SHORT).show()
                Intent(this, AddPostActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }
            R.id.itemConfig -> {
                Toast.makeText(this, "Configuration", Toast.LENGTH_SHORT).show()
            }
            R.id.itemLogout -> {
                showLogoutConfirmationDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        builder.setTitle("Déconnexion")
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
        builder.setPositiveButton("Oui") { dialogInterface, id ->
            this.getSharedPreferences("app_state", MODE_PRIVATE).edit {
                putBoolean("isAuthenticated", false)
            }
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Non") { dialogInterface, id ->
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Annuler") { dialogInterface, id ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}



