package com.yalta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yalta.R
import com.yalta.databinding.ActivityChangePasswordBinding
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
                close_button.performClick()
            }
        }

        binding.viewModel?.showError?.observeForever { showError ->
            error.visibility = if (showError) View.VISIBLE else View.INVISIBLE
        }

        password_field.setOnFocusChangeListener { _, _ -> onFocusChange(binding) }
        password_confirmation_field.setOnFocusChangeListener { _, _ -> onFocusChange(binding) }

        password_field.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Selection.setSelection(password_confirmation_field.editableText, password_confirmation_field.selectionStart)
                password_confirmation_field.requestFocus()
                true
            } else {
                false
            }
        }

        password_confirmation_field.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ok_button.requestFocus()
                ok_button.performClick()
                true
            } else {
                false
            }
        }

        change_password_card.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        close_button.setOnClickListener {
            change_password_card.animate().alpha(0f).setDuration(500).setInterpolator(
                DecelerateInterpolator()
            ).start()

            finish()
            overridePendingTransition(0,0)
        }
    }

    private fun onFocusChange(binding: ActivityChangePasswordBinding) {
        if (hasWindowFocus()) {
            binding.viewModel?.showError?.value = false
        }
    }
}
