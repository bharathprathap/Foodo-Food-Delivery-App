package com.bharath.foodo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.bharath.foodo.R

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var FtPassToolbar: Toolbar
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        FtPassToolbar=findViewById(R.id.FtPassToolbar)
        setUpToolbar()

        btnNext=findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            val intent = Intent(this, OtpEnterActivity::class.java)
            startActivity(intent)
        }
    }
    fun setUpToolbar(){
        setSupportActionBar(FtPassToolbar)
        supportActionBar?.title=""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}