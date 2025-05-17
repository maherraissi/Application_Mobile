package com.projet.first

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.Post
import androidx.core.content.edit

class HomeActivity : AppCompatActivity() {

    lateinit var listPosts: ListView
    var postsArray = arrayListOf<Post>()
    lateinit var adapter: PostsAdapter
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

    } // fin onCreate


    override fun onResume() {
        super.onResume()
        postsArray = db.findPosts()
        adapter = PostsAdapter( this, R.layout.item_post, postsArray)
        listPosts.adapter = adapter

        listPosts.setOnItemClickListener { adapterView, view, position, id ->
            val ClickedPost = postsArray[position]
            Intent(this, PostDetailsActivity::class.java).also {
                it.putExtra("titre", ClickedPost.titre)
                startActivity(it)

            }

        }
        registerForContextMenu(listPosts)

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

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.list_popup_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)


    }

    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        val info: AdapterView.AdapterContextMenuInfo =
            item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        when (item.itemId) {
            R.id.itemShow -> {
                Intent(this, PostDetailsActivity::class.java).also {
                    it.putExtra("titre", postsArray[position].titre)
                    startActivity(it)
                }

            }

            R.id.itemDelete -> {
                postsArray.removeAt(position)
                adapter.notifyDataSetChanged()

            }
        }
        return super.onContextItemSelected(item)
    }


        fun showLogoutConfirmationDialog() {
            val builder = AlertDialog.Builder(this)
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



