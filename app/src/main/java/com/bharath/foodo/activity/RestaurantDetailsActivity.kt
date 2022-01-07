package com.bharath.foodo.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bharath.foodo.R
import com.bharath.foodo.adapter.HomeRecyclerAdapter
import com.bharath.foodo.adapter.RestDetailsRecyclerAdapter
import com.bharath.foodo.database.RestaurantDatabase
import com.bharath.foodo.database.RestaurantEntity
import com.bharath.foodo.model.Restaurant
import com.bharath.foodo.model.RestaurantDetails
import com.bharath.foodo.util.ConnectionManager
import org.json.JSONException

class RestaurantDetailsActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    lateinit var progressLayout: RelativeLayout

    lateinit var recyclerRestaurantDetails: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: RestDetailsRecyclerAdapter

    lateinit var btnProceed: Button

    lateinit var toolbar: Toolbar

    var restaurantId: String? = "100"

    var restaurantName: String? = ""

    val restaurantDetailsList = arrayListOf<RestaurantDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        recyclerRestaurantDetails = findViewById(R.id.recyclerRestaurantDetails)
        toolbar = findViewById(R.id.RestaurantDetailsToolbar)

        btnProceed = findViewById(R.id.btnProceed)

        progressBar = findViewById(R.id.RestDetailsProgressBar)
        progressLayout = findViewById(R.id.RestDetailsProgressLayout)
        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(this)
        setUpToolbar()


        if (intent != null) {
            restaurantId = intent.getStringExtra("restaurant_id")
            restaurantName = intent.getStringExtra("restaurant_name")

        } else {
            finish()
            Toast.makeText(
                this@RestaurantDetailsActivity,
                "Some unexpected error occured!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (restaurantId == "100" || restaurantName == "") {
            finish()
            Toast.makeText(
                this@RestaurantDetailsActivity,
                "Some unexpected error occured!",
                Toast.LENGTH_SHORT
            ).show()
        }


        val queue = Volley.newRequestQueue(this@RestaurantDetailsActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE

                        val success = it.getJSONObject("data").getBoolean("success")
                        if (success) {
                            val data = it.getJSONObject("data").getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantDetailsJsonObject = data.getJSONObject(i)
                                val restaurantDetailsObject = RestaurantDetails(
                                    restaurantDetailsJsonObject.getString("id"),
                                    restaurantDetailsJsonObject.getString("name"),
                                    restaurantDetailsJsonObject.getString("cost_for_one"),
                                )
                                restaurantDetailsList.add(restaurantDetailsObject)

                                recyclerAdapter =
                                    RestDetailsRecyclerAdapter(this, restaurantDetailsList,btnProceed)
                                recyclerRestaurantDetails.adapter = recyclerAdapter
                                recyclerRestaurantDetails.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                this@RestaurantDetailsActivity,
                                "Some Error Ocurred!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@RestaurantDetailsActivity,
                            "Some unexpected error has occured!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantDetailsActivity,
                        "Volley error occured!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "8e09739e7cf3a9"
                        return headers
                    }

                }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this?.finish()
            }

            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = restaurantName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
