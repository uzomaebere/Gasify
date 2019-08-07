package com.uzoebere.gasify.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.uzoebere.gasify.R
import kotlinx.android.synthetic.main.activity_order_completed.*

class OrderCompleted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_completed)

        val nameTitleTextView = findViewById<TextView>(R.id.nameTitleTextView)
        val textCustDetails = findViewById<TextView>(R.id.textCustDetails)
        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val phone = intent.getStringExtra("phoneNo")
        val pymt = intent.getStringExtra("pymt")
        val total = intent.getDoubleExtra("total", 0.0)

        nameTitleTextView.setText("Thank You, " + name + "!")
        textCustDetails.setText(name + "\n" + address + "\n" + phone + "\nTotal Amount: " + total)
        if(pymt == "Bank Transfer"){
            textBank.visibility = View.VISIBLE
        } else {
            textBank.visibility = View.GONE
        }


        closeImageView.setOnClickListener{
            finish()
        }

        btnOrderCompleted.setOnClickListener{
            val intent = Intent(this, CustomerWelcome::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
