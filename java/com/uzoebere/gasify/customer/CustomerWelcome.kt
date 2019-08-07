package com.uzoebere.gasify.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.parse.ParseUser
import com.uzoebere.gasify.AboutGasify
import com.uzoebere.gasify.R
import com.uzoebere.gasify.UserProfile
import com.uzoebere.gasify.distributors.DistributorLogin
import kotlinx.android.synthetic.main.activity_customer_welcome.*


class CustomerWelcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_customer_welcome)

        viewRefill.setOnClickListener{
            val intent = Intent(this, GasRefillOrder::class.java)
        //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        viewAccessories.setOnClickListener{
            val intent = Intent(this, AccessoriesOrder::class.java)
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        viewExchange.setOnClickListener{
            val intent = Intent(this, CylinderExchange::class.java)
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        viewProfile.setOnClickListener{
            val intent = Intent(this, UserProfile::class.java)
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        imgLogout.setOnClickListener{
            confirmDialog()
        }

        closeImageView.setOnClickListener{
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.customer_home_menu, menu)
        return true
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this@CustomerWelcome)

        builder
            .setMessage("Are you sure you would like to log out?")
            .setPositiveButton("Yes") { _, _ ->
                // Yes-code
                ParseUser.logOut()
            //    val currentUser = ParseUser.getCurrentUser()
                val intent = Intent(this@CustomerWelcome, DistributorLogin::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_about -> {
            // User chose the "Settings" item, show the app settings UI...
            val intent = Intent(this, AboutGasify::class.java)
            startActivity(intent)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}
