package com.uzoebere.gasify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import com.uzoebere.gasify.customer.CustomerWelcome
import com.uzoebere.gasify.distributors.DistRegistrationPage
import kotlinx.android.synthetic.main.activity_intro_screen.*


class IntroScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)

        val currentUser = ParseUser.getCurrentUser()

        buttonBuyGas.setOnClickListener {
            // Update the user to a customer
            currentUser.put("userType", "Customer")
            currentUser.saveInBackground()
            // Display the customer interface
            val intent = Intent(this, CustomerWelcome::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        buttonDistributor.setOnClickListener {
            // Display the distributor interface
            val intent = Intent(this, DistRegistrationPage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
