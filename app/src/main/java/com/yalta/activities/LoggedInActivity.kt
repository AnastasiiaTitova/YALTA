package com.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yalta.R
import com.yalta.services.SessionService

class LoggedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        val field: TextView = findViewById(R.id.yay)
        val button: Button = findViewById(R.id.logout)

        field.setOnClickListener {
            startActivity(Intent(this@LoggedInActivity, LoginActivity::class.java))
        }

        button.setOnClickListener {
            SessionService.discardSession()
            startActivity(Intent(this@LoggedInActivity, LoginActivity::class.java))
        }
    }
}
