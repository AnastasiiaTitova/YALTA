package com.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.yalta.R
import com.yalta.repositories.RealLoginRepo
import com.yalta.services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (SessionService.isLoggedIn()) {
            successfulLogin()
        }

        val user: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.goButton)
        val error: TextView = findViewById(R.id.errorText)
        val loginService = LoginService(RealLoginRepo())

        user.setOnFocusChangeListener { _, _ -> user.onFocusChange(error) }
        password.setOnFocusChangeListener { _, _ -> password.onFocusChange(error) }

        user.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Selection.setSelection(password.editableText, password.selectionStart)
                password.requestFocus()
                true
            } else {
                false
            }
        }

        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                button.requestFocus()
                button.performClick()
                true
            } else {
                false
            }
        }

        button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val loggedIn = loginService.login(user.text.toString(), password.text.toString())
                if (loggedIn) {
                    this@LoginActivity.runOnUiThread { successfulLogin() }
                } else {
                    this@LoginActivity.runOnUiThread { showError(error) }
                }
            }
        }
    }

    private fun EditText.onFocusChange(error: TextView) {
        if (hasWindowFocus()) {
            error.visibility = View.INVISIBLE
        }
    }

    private fun showError(error: TextView) {
        error.visibility = View.VISIBLE
    }

    private fun successfulLogin() =
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
}
