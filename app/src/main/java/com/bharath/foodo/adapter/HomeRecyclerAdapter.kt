package com.bharath.foodo.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bharath.foodo.R
import com.bharath.foodo.activity.RestaurantDetailsActivity
import com.bharath.foodo.database.RestaurantDatabase
import com.bharath.foodo.database.RestaurantEntity
import com.bharath.foodo.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context,val itemList:ArrayList<Restaurant>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtRestaurantName:TextView=view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPrice:TextView=view.findViewById(R.id.txtPrice)
        val txtRestaurantRating:TextView=view.findViewById(R.id.txtRating)
        val imgRestaurantImg:ImageView=view.findViewById(R.id.imgRestaurant)
        val imgFavourite:ImageView=view.findViewById(R.id.imgFavourite)
        val llcContext:LinearLayout=view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_home_single_row,parent,false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant=itemList[position]
        holder.txtRestaurantName.text=restaurant.restaurantName
        holder.txtRestaurantPrice.text=restaurant.restaurantCostForOne
        holder.txtRestaurantRating.text=restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.logo).into(holder.imgRestaurantImg)



        holder.llcContext.setOnClickListener {
            val intent =Intent(context,RestaurantDetailsActivity::class.java)
            intent.putExtra("restaurant_id",restaurant.restaurantId)
            intent.putExtra("restaurant_name",restaurant.restaurantName)
            context.startActivity(intent)
        }

        val restaurantEntity = RestaurantEntity(
            restaurantId = restaurant.restaurantId,
            restaurantName = restaurant.restaurantName,
            restaurantCostForOne = restaurant.restaurantCostForOne,
            restaurantRating = restaurant.restaurantRating,
            restaurantImage = restaurant.restaurantImage
        )
        val checkFav = DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFavourite = checkFav.get()
        if(isFavourite) {
            holder.imgFavourite.setImageResource(R.drawable.ic_heart)
        } else {
            holder.imgFavourite.setImageResource(R.drawable.ic_favourite)
        }

        holder.imgFavourite.setOnClickListener {
            if(!DBAsyncTask(context, restaurantEntity, 1).execute().get()){

                val async = DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} added to favourites",Toast.LENGTH_SHORT).show()
                    holder.imgFavourite.setImageResource(R.drawable.ic_heart)
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            } else {

                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} removed to favourites",Toast.LENGTH_SHORT).show()
                    holder.imgFavourite.setImageResource(R.drawable.ic_favourite)
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int):

        AsyncTask<Void, Void, Boolean>(){

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1 -> {
                    val restaurantEntity: RestaurantEntity? = db.restaurantDao().getRestaurantById(
                        restaurantEntity.restaurantId
                    )
                    db.close()
                    return restaurantEntity != null
                }

                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }


    override fun getItemCount(): Int {
        return itemList.size
    }
}