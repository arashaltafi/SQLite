package com.arash.altafi.sqlite.db

import android.provider.BaseColumns

// قرار داد

object DBContract {

    class UserEntity : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_USERNAME = "username"
            val COLUMN_FIRSTNAME = "firstname"
            val COLUMN_LASTNAME = "lastname"

        }
    }

}