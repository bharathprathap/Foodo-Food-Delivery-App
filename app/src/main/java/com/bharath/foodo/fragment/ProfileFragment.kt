package com.bharath.foodo.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bharath.foodo.R


class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtUserName:TextView
    lateinit var txtUserMobile:TextView
    lateinit var txtUserEmail:TextView
    lateinit var txtUserAddress:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = activity?.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)!!
        txtUserName = view.findViewById(R.id.txtUserName)
        txtUserEmail = view.findViewById(R.id.txtUserEmail)
        txtUserMobile = view.findViewById(R.id.txtUserMobile)
        txtUserAddress = view.findViewById(R.id.txtUserAddress)

        txtUserName.setText(sharedPreferences.getString("name","Name"))
        txtUserEmail
            .setText(sharedPreferences.getString("email","Email Address"))
        txtUserMobile.setText(sharedPreferences.getString("mobile_number","Mobile Number"))
        txtUserAddress.setText(sharedPreferences.getString("address","Address"))

        return view
    }

}