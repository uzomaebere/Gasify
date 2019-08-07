package com.uzoebere.gasify

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about_gasify.*

class AboutGasify : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_gasify)

        closeImageView.setOnClickListener{
            finish()
        }
        whatsappImageView.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+2349038377222"))
            startActivity(i)
        }

        imageGmail.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            val data = Uri.parse("mailto:gasifyngr@gmail.com?subject=$&body=$")
            intent.data = data
            startActivity(intent)
        }

        facebookImageView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/508446695954965"))
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
        }

        instagramImageView.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/gasify_nigeria/"))
            startActivity(i)
        }
    }
}
