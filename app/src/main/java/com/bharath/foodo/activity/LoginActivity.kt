package com.bharath.foodo.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bharath.foodo.R
import com.bharath.foodo.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var textSignUp: TextView
    lateinit var textForgotPassword: TextView
    lateinit var etMobileNumber: EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login)

        textSignUp=findViewById(R.id.txtSignUp)
        textForgotPassword=findViewById(R.id.txtForgotPassword)
        etMobileNumber=findViewById(R.id.etMobileNumber)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogIn)

        textForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        textSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        val queue= Volley.newRequestQueue(this@LoginActivity)
        val url="http://13.235.250.119/v2/login/fetch_result"

        btnLogin.setOnClickListener {

            if(etMobileNumber.text.toString().length == 10){
                if(etPassword.text.toString().length>4){

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", etMobileNumber.text.toString())
                    jsonParams.put("password",etPassword.text.toString())
                    if(ConnectionManager().checkConnectivity(this)){

                        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                            try {

                                val success = it.getJSONObject("data").getBoolean("success")
                                if(success){
                                    val data = it.getJSONObject("data").getJSONObject("data")
                                    savePreferences(data)
                                    startActivity(intent)

                                }
                                else {
                                    Toast.makeText(this, "Login Error! Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: JSONException){
                                Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                            }

                        }, Response.ErrorListener {
                            Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String,String> ()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "8e09739e7cf3a9"
                                return headers
                            }
                        }
                        queue.add(jsonObjectRequest)

                    } else {
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle("ERROR")
                        dialog.setMessage("Internet Connection Not Found")
                        dialog.setPositiveButton("Open Setting"){text, listener ->
                            val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingIntent)
                            finish()
                        }
                        dialog.setNegativeButton("Exit"){text, listener ->
                            ActivityCompat.finishAffinity(this)
                        }
                        dialog.create().show()
                    }

                }
                else{
                    Toast.makeText(this,"Password should be longer than 4 characters ", Toast.LENGTH_SHORT).show()
                }

                textForgotPassword.setOnClickListener {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                textSignUp.setOnClickListener {
                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }

            }
            else{
                Toast.makeText(this,"Mobile Number should be 10 digits long", Toast.LENGTH_SHORT).show()
            }



        }
        fun onPause() {
            super.onPause()
            finish()
        }

    }

    private fun savePreferences(data: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
        sharedPreferences.edit().putString("name",data.getString("name")).apply()
        sharedPreferences.edit().putString("email",data.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
        sharedPreferences.edit().putString("address",data.getString("address")).apply()

    }


}