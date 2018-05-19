package com.noman.room.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.content.Context
import com.noman.room.model.Product
import com.noman.room.model.User

/**
 * A database class @AppRoomDatabase that create a sqlite database and get reference to it.
 */
@Database(entities = arrayOf(User::class, Product::class), version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    companion object {
        private val DB_NAME = "roomDatabase.db"

        private var instance: AppRoomDatabase? = null

        /**
         * This method use to give Synchronized instance to database
         */
        @Synchronized
        fun getInstance(context: Context): AppRoomDatabase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        /**
         * This methid create and databse by given name
         */
        private fun create(context: Context): AppRoomDatabase {
            return Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    DB_NAME).build()
        }
    }

    /**
     * @UserDao class this used to provide DAO methods to operate on table
     */
    abstract fun getUserDao(): UserDao

    /**
     * @ProductDao class this used to ptovide DAO methods to operate on table
     */
    abstract fun getProductDao(): ProductDao

}