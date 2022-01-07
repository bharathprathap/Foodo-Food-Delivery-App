package com.bharath.foodo.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bharath.foodo.R
import com.bharath.foodo.adapter.HomeRecyclerAdapter
import com.bharath.foodo.database.RestaurantDatabase
import com.bharath.foodo.database.RestaurantEntity
import com.bharath.foodo.model.Restaurant
import com.bharath.foodo.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class HomeFragment : Fragment() {

    lateinit var recyclerHome:RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    lateinit var restaurantId:String
    lateinit var txtRestaurantName:String
    lateinit var txtRestaurantRating:String
    lateinit var txtCostForOne:String

    var ratingComparator = Comparator<Restaurant> { res1, res2 ->
        if(res1.restaurantRating.compareTo(res2.restaurantRating, true)==0){
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantRating.compareTo(res2.restaurantRating, true)
        }
    }
    var priceComparator = Comparator<Restaurant> { res1, res2 ->
        if(res1.restaurantCostForOne.compareTo(res2.restaurantCostForOne, true)==0){
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantCostForOne.compareTo(res2.restaurantCostForOne, true)
        }
    }



    val restaurantInfoList=arrayListOf<Restaurant>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerHome=view.findViewById(R.id.recyclerHome)

        layoutManager=LinearLayoutManager(activity)

        progressLayout=view.findViewById(R.id.progressLayout)

        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE


        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest=object :JsonObjectRequest(Method.GET,url,null, Response.Listener{

                try {
                    progressLayout.visibility=View.GONE
                    val success=it.getJSONObject("data").getBoolean("success")
                    if(success){
                        val data=it.getJSONObject("data").getJSONArray("data")
                        for (i in 0 until data.length()){
                            val restaurantJsonObject=data.getJSONObject(i)
                            val restaurantObject=Restaurant(
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("cost_for_one"),
                                restaurantJsonObject.getString("image_url")
                            )


                            restaurantInfoList.add(restaurantObject)

                            recyclerAdapter= HomeRecyclerAdapter(activity as Context,restaurantInfoList)

                            recyclerHome.adapter= recyclerAdapter
                            recyclerHome.layoutManager=layoutManager


                        }
                    }
                    else{
                        Toast.makeText(activity as Context,"Some Error Ocurred!",Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:JSONException){
                    Toast.makeText(activity as Context,"Some unexpected error has occured!!",Toast.LENGTH_SHORT).show()
                }


            },Response.ErrorListener {
                Toast.makeText(activity as Context,"Volley error occured!!",Toast.LENGTH_SHORT).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="8e09739e7cf3a9"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view



    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.itemRatingL){
            Collections.sort(restaurantInfoList,ratingComparator)
        } else if(id == R.id.itemRatingH){
            Collections.sort(restaurantInfoList,ratingComparator)
            restaurantInfoList.reverse()
        } else if(id == R.id.itemPriceL){
            Collections.sort(restaurantInfoList, priceComparator)
        } else if(id == R.id.itemPriceH){
            Collections.sort(restaurantInfoList, priceComparator)
            restaurantInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode:Int): AsyncTask<Void, Void, Boolean>(){
        /*
        Mode1 ->Check if the restaurant is favourite or not
        MOde2->Save the restaurant into DB as favourite
        mode3-> remove restaurant from favourites
        * */
        val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"restaurant-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1->{
                    val restaurant: RestaurantEntity?=db.restaurantDao().getRestaurantById(restaurantEntity.restaurantId.toString())
                    db.close()
                    return restaurant !=null
                }

                2->{
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3->{
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }


}