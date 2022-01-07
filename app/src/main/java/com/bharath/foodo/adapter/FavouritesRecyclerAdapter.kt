package com.bharath.foodo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bharath.foodo.R
import com.bharath.foodo.activity.RestaurantDetailsActivity
import com.bharath.foodo.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context, var itemList : List<RestaurantEntity>): RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>(){
    class FavouritesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val restaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val restaurantCost_for_one : TextView = view.findViewById(R.id.txtCostForOne)
        val restaurantRating : TextView = view.findViewById(R.id.txtRating)
        val restaurantImage : ImageView = view.findViewById(R.id.imgRestaurant)
        val restaurantHeart : ImageView = view.findViewById(R.id.imgFavourite)
        val favouritesLayout : LinearLayout = view.findViewById(R.id.favouritesLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourites_single_row, parent,false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantCost_for_one.text = restaurant.restaurantCostForOne
        holder.restaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.ic_launcher_foreground).into(holder.restaurantImage)

        holder.favouritesLayout.setOnClickListener {
            Toast.makeText(context , "Clicked on ${holder.restaurantName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra("id", restaurant.restaurantId)
                .putExtra("name", restaurant.restaurantName)
                .putExtra("price", restaurant.restaurantCostForOne)
                .putExtra("rating", restaurant.restaurantRating)
                .putExtra("image", restaurant.restaurantImage)
            context.startActivity(intent)
        }

        val restaurantEntity = RestaurantEntity(
            restaurantId = restaurant.restaurantId,
            restaurantName = restaurant.restaurantName,
            restaurantCostForOne = restaurant.restaurantCostForOne,
            restaurantRating = restaurant.restaurantRating,
            restaurantImage = restaurant.restaurantImage
        )
        val checkFav = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav) {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart)
        } else {
            holder.restaurantHeart.setImageResource(R.drawable.ic_favourite)
        }

        holder.restaurantHeart.setOnClickListener {
            if(!HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute().get()){

                val async = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} added to favourites",Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_heart)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            } else {

                val async = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} removed to favourites",Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_favourite)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}