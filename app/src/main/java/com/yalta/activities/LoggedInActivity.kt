package com.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.yalta.R
import com.yalta.services.SessionService

class LoggedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        val field: TextView = findViewById(R.id.yay)

        field.setOnClickListener {
            startActivity(Intent(this@LoggedInActivity, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                SessionService.discardSession()
                startActivity(Intent(this@LoggedInActivity, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
