package com.arash.altafi.sqlite.db.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.arash.altafi.sqlite.db.DBContract
import com.arash.altafi.sqlite.models.User
import java.lang.Exception

class UserDBHelper(context: Context) : SQLiteOpenHelper(context , DATABASE_NAME , null , DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_QUERY)
        onCreate(db)
    }

    fun insertUser(user: User) : Boolean {
        try {
            val db = writableDatabase
            val values = ContentValues()
            values.put(DBContract.UserEntity.COLUMN_USERNAME , user.userName)
            values.put(DBContract.UserEntity.COLUMN_FIRSTNAME , user.firstName)
            values.put(DBContract.UserEntity.COLUMN_LASTNAME , user.lastName)
            db.insert(DBContract.UserEntity.TABLE_NAME , null , values)
            return true
        }
        catch (e:Exception) {
            return false
        }
    }

    fun getAllUsers(): ArrayList<User> {
        val users = ArrayList<User>()
        val db = readableDatabase
        var cursor : Cursor ?= null
        try {
            cursor = db.rawQuery("SELECT * FROM ${DBContract.UserEntity.TABLE_NAME}", null)
        } catch (e: Exception) {
            db.execSQL(SQL_CREATE_QUERY)
            return ArrayList()
        }
        var username: String
        var firstname: String
        var lastname: String

        if (cursor.moveToFirst()) {
 //          isAfterLast  => یعنی جایی که دیگه ادامه نداره و به تهش رسیده است
            while (!cursor.isAfterLast) {
                username = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_USERNAME))
                firstname = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_FIRSTNAME))
                lastname = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_LASTNAME))
                users.add(User(username, firstname, lastname))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun getUserByUsername(username : String): User? {
        val db = readableDatabase
        var cursor : Cursor ?= null
        try {
            cursor = db.rawQuery("SELECT * FROM ${DBContract.UserEntity.TABLE_NAME}" +
                    " WHERE ${DBContract.UserEntity.COLUMN_USERNAME} = ?", arrayOf(username))
        } catch (e: Exception) {
            db.execSQL(SQL_CREATE_QUERY)
            return null
        }
        var username: String = ""
        var firstname: String = ""
        var lastname: String = ""

        if (cursor.moveToFirst()) {
            //          isAfterLast  => یعنی جایی که دیگه ادامه نداره و به تهش رسیده است
            while (!cursor.isAfterLast) {
                username = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_USERNAME))
                firstname = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_FIRSTNAME))
                lastname = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_LASTNAME))
                cursor.moveToNext()
            }
        }
        else {
            return null
        }
        return User(username , firstname , lastname)
    }

    fun deleteUserByUsername(username:String) : Boolean {
        try {
            val user = getUserByUsername(username)
            if (user == null) {
                return false
            }
            val db = writableDatabase
            val where = "${DBContract.UserEntity.COLUMN_USERNAME} = ?"
            val args = arrayOf(username)
            db.delete(DBContract.UserEntity.TABLE_NAME , where , args)
            return true
        }
        catch (e:Exception) {
            return false
        }
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UserManagement.db"

        private val SQL_CREATE_QUERY = "CREATE TABLE ${DBContract.UserEntity.TABLE_NAME} (" +
                "${DBContract.UserEntity.COLUMN_USERNAME} TEXT PRIMARY KEY," +
                "${DBContract.UserEntity.COLUMN_FIRSTNAME} TEXT ," +
                "${DBContract.UserEntity.COLUMN_LASTNAME} TEXT)"

        private val SQL_DELETE_QUERY = "DROP TABLE IF EXISTS ${DBContract.UserEntity.TABLE_NAME}"
    }

}