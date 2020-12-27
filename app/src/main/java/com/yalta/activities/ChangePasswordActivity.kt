package com.yalta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yalta.R
import com.yalta.databinding.ActivityChangePasswordBinding
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(ChangePasswordViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        val binding: ActivityChangePasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.viewModel?.closeActivity?.observeForever { closeActivity ->
            if (closeActivity) {
                closeActivity()
            }
        }

        passwordField.doOnTextChanged { _, _, _, _ -> viewModel.showPasswordError(false) }
        passwordConfirmationField.doOnTextChanged { _, _, _, _ -> viewModel.showPasswordError(false) }

        passwordField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Selection.setSelection(passwordConfirmationField.editableText, passwordConfirmationField.selectionStart)
                passwordConfirmationField.requestFocus()
                true
            } else {
                false
            }
        }

        passwordConfirmationField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                okButton.requestFocus()
                okButton.performClick()
                true
            } else {
                false
            }
        }

        changePasswordCard.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
    }

    override fun onBackPressed() {
        closeActivity()
    }

    private fun closeActivity() {
        changePasswordCard.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        finish()
        overridePendingTransition(0,0)
    }
}
