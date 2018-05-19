package com.noman.room.database

import android.arch.persistence.room.*
import com.noman.room.model.User

/**
 * This interface class is used to queries in user table
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE number=:number")
    fun getUserByNumber(number: Int): User

    @Query("DELETE FROM user WHERE name=:name")
    fun deleteUserByName(name: String)

    @Query("SELECT * FROM user WHERE name=:name")
    fun getUserByName(name: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun intert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun interMultiple(users: List<User>)


}