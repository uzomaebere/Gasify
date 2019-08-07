package com.uzoebere.gasify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.parse.ParseInstallation
import com.uzoebere.gasify.distributors.DistributorLogin
import com.parse.ParseUser
import com.uzoebere.gasify.customer.CustomerWelcome
import com.uzoebere.gasify.distributors.DistDashboard

class MainActivity : AppCompatActivity() {

    /** Duration of wait  */
    private val SPLASH_DISPLAY_LENGTH = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground()

        Handler().postDelayed(Runnable {
         //    Create an Intent that will start the Menu-Activity.
            /*     val mainIntent = Intent(this@MainActivity, DistributorLogin::class.java)
                this@MainActivity.startActivity(mainIntent)
                this@MainActivity.finish()*/
                   val currentUser = ParseUser.getCurrentUser()
                    if (currentUser != null) {
                        // do stuff with the user
                        val usertype = currentUser.getString("userType")
                        if (usertype == null) {
                            val intent = Intent(this, IntroScreen::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else if (usertype == "Customer") {
                            val intent = Intent(this, CustomerWelcome::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else if (usertype == "Distributor") {
                            val intent = Intent(this, DistDashboard::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                    } else {
                        // show the signup or login screen
                        val randomIntent = Intent(this, DistributorLogin::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(randomIntent)
                    }

        }, SPLASH_DISPLAY_LENGTH.toLong())



    }


    }

