package com.bharath.foodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bharath.foodo.R
import com.bharath.foodo.model.Cart

class CartRecyclerAdapter (val context: Context, val Cart: ArrayList<Cart>) :
    RecyclerView.Adapter<CartRecyclerAdapter.ViewHolderCart>() {


    class ViewHolderCart(view: View) : RecyclerView.ViewHolder(view) {
        val txtCartDishName: TextView = view.findViewById(R.id.CartDishName)
        val txtCartDishPrice:TextView=view.findViewById(R.id.CartDishPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCart {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)
        return ViewHolderCart(view)
    }

    override fun getItemCount(): Int {
        return Cart.size
    }

    override fun onBindViewHolder(holder: ViewHolderCart, position: Int) {
        val cartItem = Cart[position]
        holder.txtCartDishName.text = cartItem.itemName
        holder.txtCartDishPrice.text = "Rs. ${cartItem.itemPrice}"
    }
}