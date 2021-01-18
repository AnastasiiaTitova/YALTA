package com.yalta.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.yalta.R
import com.yalta.databinding.ActivityLoginBinding
import com.yalta.di.YaltaApplication
import com.yalta.services.*
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewModel.LoginViewModel
import common.Admin
import common.Driver
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        YaltaApplication.appComponent.inject(this)

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

    private fun successfulLogin() {
        if (SessionService.session != null) {
            when (SessionService.session!!.role) {
                is Driver ->
                    startActivity(Intent(this@LoginActivity, DriverMainActivity::class.java))
                is Admin ->
                    startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {}
}
