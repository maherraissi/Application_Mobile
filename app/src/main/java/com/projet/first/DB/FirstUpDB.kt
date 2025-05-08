package com.projet.first.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
            CREATE TABLE users(
            $USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $NAME varchar(50) NOT NULL,
            $EMAIL varchar(50) NOT NULL,
            $PASSWORD varchar(50) NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableUser)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // la mise à jour de la table

        db?.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        onCreate(db)

    }


    fun addUser(user: User): Boolean{
        //inserer un nouvel utilisateur
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, user.name)
        values.put(EMAIL, user.email)
        values.put(PASSWORD, user.password)

        val result = db.insert(USERS_TABLE_NAME, null, values)

        db.close()


        return result != -1L
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

    companion object{
        private val DB_NAME = "FirstUpDB"
        private val DB_VERSION = 1
        private var USERS_TABLE_NAME = "users"
        private var USER_ID = "id"
        private var NAME = "nom"
        private var EMAIL = "email"
        private var PASSWORD = "password"

    }

}
