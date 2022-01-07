package com.bharath.foodo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="restaurants")
data class RestaurantEntity(
    @ColumnInfo(name="restaurant_id") @PrimaryKey val restaurantId:String,
    @ColumnInfo(name="restaurant_name") val restaurantName:String,
    @ColumnInfo(name="restaurant_rating")val restaurantRating:String,
    @ColumnInfo(name="restaurant_costForOne")val restaurantCostForOne: String,
    @ColumnInfo(name="restaurant_image")val restaurantImage:String

)