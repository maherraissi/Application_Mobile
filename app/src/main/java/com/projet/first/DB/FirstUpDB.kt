package com.projet.first.DB

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

        return false // TODO
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
