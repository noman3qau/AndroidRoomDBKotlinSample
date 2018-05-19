package com.noman.room.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * This actual class that is used to present user table and provide to give data of user
 */
@Entity(tableName = "user")
data class User(
        @ColumnInfo(name = "number")
        @PrimaryKey
        val number: String,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "email")
        val email: String
)