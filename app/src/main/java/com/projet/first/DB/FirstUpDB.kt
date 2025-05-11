package com.projet.first.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.projet.first.Data.Post
import com.projet.first.Data.User

class FirstUpDB(mContexte: Context) : SQLiteOpenHelper(
    mContexte,
    DB_NAME,
    null,
    DB_VERSION
    ){
    override fun onCreate(db: SQLiteDatabase?) {
        // la creation de la table
        val createTableUser = """
            CREATE TABLE $USERS_TABLE_NAME(
            $USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $NAME varchar(50) NOT NULL,
            $EMAIL varchar(50) NOT NULL,
            $PASSWORD varchar(50) NOT NULL
            )
        """.trimIndent()

        val createTablePost = """
            CREATE TABLE $POSTS_TABLE_NAME(
            $POST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $TITLE varchar(50) ,
            $DESCRIPTION text ,
            $IMAGE blob,
            $LIKES INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTableUser)
        db?.execSQL(createTablePost)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // la mise à jour de la table

        db?.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $POSTS_TABLE_NAME")
        onCreate(db)

    }


    fun addUser(user: User): Boolean{
        //inserer un nouvel utilisateur
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, user.name)
        values.put(EMAIL, user.email)
        values.put(PASSWORD, user.password)

        val result = db.insert(USERS_TABLE_NAME, null, values).toInt()

        db.close()


        return result != -1
    }

    fun findUser(email: String, password: String): User? {

        var user: User? = null
        val db = this.readableDatabase
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query (USERS_TABLE_NAME, null, "$EMAIL=? AND $PASSWORD=?", selectionArgs, null, null, null)
        if (cursor.moveToFirst()){
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val email = cursor.getString(2)
            val user = User(id, name, email, "")
            return user

        }

       db.close()
        return user

    }

    fun findPosts() : ArrayList<Post>{
        val posts = ArrayList<Post>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $POSTS_TABLE_NAME"

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null ){
            if (cursor.moveToFirst()){
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(POST_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE))
                    val likes = cursor.getInt(cursor.getColumnIndexOrThrow(LIKES))
                    val post = Post(id,title, description, image, likes)
                    posts.add(post)

                } while (cursor.moveToNext())
            }
        }

        db.close()

        return posts
    }

    fun addPost(post: Post) : Boolean{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TITLE, post.titre)
        values.put(DESCRIPTION, post.description)
        values.put(IMAGE, post.image)
        values.put(LIKES, 0)

        val result = db.insert(POSTS_TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun deletePost(id: Int) : Boolean{
        val db = writableDatabase

        val rowDeleted = db.delete(POSTS_TABLE_NAME, "$POST_ID=?", arrayOf(id.toString()))

        return rowDeleted>0
    }

    fun incrementLikes(post: Post) {
        val db = writableDatabase

        val newLikesCount = post.jaime + 1
        val values = ContentValues()
        values.put(LIKES, newLikesCount)

        db.update(POSTS_TABLE_NAME, values, "id=?", arrayOf("${post.id}"))
        db.close()

    }

    companion object{
        private val DB_NAME = "FirstUpDB"
        private val DB_VERSION = 3
        //users
        private var USERS_TABLE_NAME = "users"
        private var USER_ID = "id"
        private var NAME = "nom"
        private var EMAIL = "email"
        private var PASSWORD = "password"

        //posts
        private var POSTS_TABLE_NAME = "posts"
        private var POST_ID = "id"
        private var TITLE = "title"
        private var DESCRIPTION = "description"
        private var IMAGE = "image"
        private var LIKES = "jaime"

    }

}
