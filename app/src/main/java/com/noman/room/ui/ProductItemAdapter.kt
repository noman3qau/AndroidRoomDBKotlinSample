package com.noman.room.ui

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.noman.room.R
import com.noman.room.model.Product
import com.noman.room.model.User

/**
 * Product items adapter class that is used to show product on UI
 */
class ProductItemAdapter(var productList: List<Product>) : RecyclerView.Adapter<ProductItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.view_product_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (productList.get(position).productColor.length == 7) {
            holder.productColor.setBackgroundColor(Color.parseColor(productList.get(position).productColor))
        } else {
            holder.productColor.setBackgroundColor(Color.parseColor("#000000"))
        }
        holder.productName.text = productList.get(position).productName
        holder.productPrice.text = "Rs. " + productList.get(position).productPrice.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var productColor: View
        var productName: TextView
        var productPrice: TextView

        init {
            productColor = itemView?.findViewById(R.id.productColor)!!
            productName = itemView?.findViewById(R.id.productName)
            productPrice = itemView?.findViewById(R.id.productPrice)
        }
    }

}