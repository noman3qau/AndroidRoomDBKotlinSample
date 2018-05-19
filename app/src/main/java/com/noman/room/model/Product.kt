package com.noman.room.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

/**
 * This actualy Product class that is used to provide database table and object to provide Product data
 */
@Entity(tableName = "product", foreignKeys = arrayOf(
        ForeignKey(entity = User::class,
                parentColumns = arrayOf("number"),
                childColumns = arrayOf("usernumber"),
                onDelete = CASCADE)
))
data class Product(
        @ColumnInfo(name = "usernumber")
        var usernumber: String,

        @ColumnInfo(name = "productid")
        @PrimaryKey(autoGenerate = true)
        var productId: Long,

        @ColumnInfo(name = "productname")
        var productName: String,

        @ColumnInfo(name = "productcolor")
        var productColor: String,

        @ColumnInfo(name = "productprice")
        var productPrice: Int
)