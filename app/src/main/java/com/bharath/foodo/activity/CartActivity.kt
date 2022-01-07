package com.bharath.foodo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bharath.foodo.R
import com.bharath.foodo.model.Cart
import com.bharath.foodo.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
/*
    var totalAmount = 0
    var cartListItems = arrayListOf<Cart>()
    lateinit var btnPlaceOrder:Button
    lateinit var linearLayout:LinearLayout
    lateinit var txtRestaurantName:TextView
    lateinit var recyclerView:RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar:Toolbar
    lateinit var restaurantId:String
    lateinit var selectedItemsId:String
    lateinit var layoutManager:RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        txtRestaurantName = findViewById(R.id.txtRestaurantName)
        linearLayout = findViewById(R.id.CartLinearlayout)
        toolbar = findViewById(R.id.CartToolbar)
        progressLayout = findViewById(R.id.CartProgressLayout)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerCart)


        restaurantId = intent.getStringExtra("restaurantId")
        txtRestaurantName = intent.getStringExtra("restaurantName")
        selectedItemsId = intent.getStringArrayListExtra("selectedItemsId")
        txtOrderingFrom.text = restaurantName

        setToolBar()

        btnPlaceOrder.setOnClickListener {

            val sharedPreferences = this.getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )

            if (ConnectionManager().checkConnectivity(this)) {

                cartProgressLayout.visibility = View.VISIBLE
                try {
                    val foodArray = JSONArray()

                    for (foodItem in selectedItemsId) {
                        val singleItemObject = JSONObject()
                        singleItemObject.put("food_item_id", foodItem)
                        foodArray.put(singleItemObject)
                    }

                    val sendOrder = JSONObject()
                    sendOrder.put("user_id", sharedPreferences.getString("user_id", "0"))
                    sendOrder.put("restaurant_id", restaurantId)
                    sendOrder.put("total_cost", totalAmount)
                    sendOrder.put("food", foodArray)

                    val queue = Volley.newRequestQueue(this)
                    val url ="http://13.235.250.119/v2/place_order/fetch_result"

                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.POST,
                        url,
                        sendOrder,
                        Response.Listener {

                            val response = it.getJSONObject("data")
                            val success = response.getBoolean("success")
                            if (success) {

                                Toast.makeText(
                                    this,
                                    "Order Placed",
                                    Toast.LENGTH_SHORT
                                ).show()

                                createNotification()

                                val intent = Intent(this, OrderPlacedActivity::class.java)
                                startActivity(intent)
                                finishAffinity()

                            } else {
                                val responseMessageServer =
                                    response.getString("errorMessage")
                                Toast.makeText(
                                    this,
                                    responseMessageServer.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            cartProgressLayout.visibility = View.INVISIBLE
                        },
                        Response.ErrorListener {

                            Toast.makeText(
                                this,
                                "Some Error occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "26c5144c5b9c13"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)

                } catch (e: JSONException) {
                    Toast.makeText(
                        this,
                        "Some unexpected error occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Check Internet Connection!")
                alterDialog.setPositiveButton("Open Settings") { _, _ ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                    startActivity(settingsIntent)
                }
                alterDialog.setNegativeButton("Exit") { _, _ ->
                    finishAffinity()
                }
                alterDialog.setCancelable(false)
                alterDialog.create()
                alterDialog.show()
            }
        }


    }
    */
}