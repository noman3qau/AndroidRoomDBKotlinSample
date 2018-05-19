package com.noman.room.database

import android.arch.persistence.room.*
import com.noman.room.model.Product

/**
 * This interface class this used to queries in database table "product"
 */
@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAllProduct(): List<Product>

    @Query("SELECT * FROM product WHERE usernumber=:userNumber")
    fun getProductByUserNumber(userNumber: String): List<Product>

    @Query("DELETE FROM product WHERE productid=:userNumber")
    fun deleteAllProductsByUserNumber(userNumber: Int)

    @Query("DELETE FROM product WHERE productname=:productName")
    fun deleteProductByName(productName: String)

    @Query("DELETE FROM product")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(produst: Product)

}