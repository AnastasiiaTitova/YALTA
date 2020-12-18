package com.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yalta.R
import com.yalta.databinding.ActivityLoginBinding
import com.yalta.services.*
import com.yalta.utils.ActivityUtils.hideKeyboard
import com.yalta.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SessionService.isLoggedIn()) {
            successfulLogin()
        }

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.viewModel?.loggedIn?.observeForever { loggedIn ->
            if (loggedIn) {
                successfulLogin()
            }
        }

        username.setOnEditorActionListener { _, actionId, _ ->
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
                hideKeyboard()
                goButton.requestFocus()
                goButton.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun successfulLogin() =
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
}
