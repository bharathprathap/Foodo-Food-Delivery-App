package com.bharath.foodo.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bharath.foodo.R
import com.bharath.foodo.model.Restaurant
import com.bharath.foodo.model.RestaurantDetails
import org.w3c.dom.Text


class RestDetailsRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<RestaurantDetails>,
    val btnProceed: Button
) :
    RecyclerView.Adapter<RestDetailsRecyclerAdapter.RestDetailsViewHolder>() {
    var count = 0
    class RestDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtNumber: TextView = view.findViewById(R.id.txtNumber)
        val txtDishName: TextView = view.findViewById(R.id.txtDishName)
        val txtDishPrice: TextView = view.findViewById(R.id.txtDishPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestDetailsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_restaurant_single_row, parent, false)
        return RestDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestDetailsViewHolder, position: Int) {
        val restaurantDetails = itemList[position]
        holder.txtDishName.text = restaurantDetails.name
        holder.txtNumber.text = restaurantDetails.id
        holder.txtDishPrice.text = restaurantDetails.cost_for_one

        holder.btnAdd.setOnClickListener {
            if (holder.btnAdd.text == "Add") {
                btnProceed.visibility = View.VISIBLE
                holder.btnAdd.text = "Remove"
                holder.btnAdd.setBackgroundColor(Color.RED)
                count += 1
            } else if (holder.btnAdd.text == "Remove") {
                holder.btnAdd.text = "Add"
                holder.btnAdd.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )
                count -= 1
            }
            if (count == 0) {
                btnProceed.visibility = View.INVISIBLE
            }
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}