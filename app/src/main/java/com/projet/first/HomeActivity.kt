package com.projet.first

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.projet.first.Data.Post

class HomeActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    lateinit var listPosts: ListView
    var postsArray = arrayListOf<Post>()
    lateinit var adapter: PostsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        supportActionBar?.title = "Accueil"


        val email = intent.getStringExtra("email")

        listPosts = findViewById<ListView>(R.id.listPosts)
        postsArray = arrayListOf(
            Post(
                "post1",
                "une description du post 1 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img1
            ),
            Post(
                "post2",
                "une description du post 2 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img2
            ),
            Post(
                "post3",
                "une description du post 3 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img3
            ),
            Post(
                "post4",
                "une description du post 4 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img4
            ),
            Post(
                "post5",
                "une description du post 5 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img5
            ),
            Post(
                "post6",
                "une description du post 6 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img6
            ),
            Post(
                "post7",
                "une description du post 7 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img7
            ),
            Post(
                "post8",
                "une description du post 8 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img8
            ),
            Post(
                "post9",
                "une description du post 9 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img9
            ),
            Post(
                "post10",
                "une description du post 10 qui va être afficher ici au lieu de ce texte qui ne veut rien dire...",
                R.drawable.img10
            ),

            )

        adapter = PostsAdapter(mContext = this, resource = R.layout.item_post, postsArray)
        listPosts.adapter = adapter

        listPosts.setOnItemClickListener { adapterView, view, position, id ->
            val ClickedPost = postsArray[position]
            Intent(this, PostDetailsActivity::class.java).also {
                it.putExtra("titre", ClickedPost.titre)
                startActivity(it)

            }


        }
        registerForContextMenu(listPosts)
    } // fin onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAdd -> {
                Toast.makeText(this, "Ajouter un post", Toast.LENGTH_SHORT).show()
            }

            R.id.itemConfig -> {
                Toast.makeText(this, "Configuration", Toast.LENGTH_SHORT).show()
            }

            R.id.itemLogout -> {
                // afficher un dialog pour confirmer la déconnexion
                showLogoutConfirmationDialog()
                //finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Déconnexion")
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
        builder.setPositiveButton("Oui") { dialogInterface, id ->
            val editor = getSharedPreferences("authentification", MODE_PRIVATE).edit()
            editor.remove("isAuthenticated")
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

