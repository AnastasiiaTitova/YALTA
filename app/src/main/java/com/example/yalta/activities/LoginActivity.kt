package com.example.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.yalta.services.HardcodedLocalRepo
import com.example.yalta.services.LoginService
import com.example.yalta.R
import com.example.yalta.services.SessionService

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
        val loginService = LoginService(
            HardcodedLocalRepo(),
            SessionService
        )

        user.setOnFocusChangeListener { _, _ -> user.onFocusChange(password, error) }
        password.setOnFocusChangeListener { _, _ -> password.onFocusChange(user, error) }

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
                if (loginService.login(user.text.toString(), password.text.toString())) {
                    successfulLogin()
                } else {
                    button.requestFocus()
                    button.performClick()
                }
                true
            } else {
                false
            }
        }

        button.setOnClickListener {
            if (loginService.login(user.text.toString(), password.text.toString())) {
                successfulLogin()
            } else {
                showError(user, password, error)
            }
        }
    }

    private fun EditText.onFocusChange(another: EditText, error: TextView) {
        if (hasWindowFocus()) {
            setBackgroundResource(android.R.drawable.edit_text)
            another.setBackgroundColor(android.R.drawable.edit_text)
            error.visibility = View.INVISIBLE
        }
    }

    private fun showError(user: EditText, password: EditText, error: TextView) {
        user.setBackgroundResource(R.color.colorAccent)
        password.setBackgroundResource(R.color.colorAccent)
        error.visibility = View.VISIBLE
    }

    private fun successfulLogin() =
        startActivity(Intent(this@LoginActivity, LoggedInActivity::class.java))
}
