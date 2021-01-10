package com.yalta.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.yalta.R
import com.yalta.databinding.ActivityAddPointBinding
import com.yalta.di.YaltaApplication
import com.yalta.utils.ViewUtils.grantedLocationPermission
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewModel.AddPointViewModel
import kotlinx.android.synthetic.main.activity_add_point.*
import javax.inject.Inject

class AddPointActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: AddPointViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        val binding: ActivityAddPointBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_point)

        YaltaApplication.appComponent.inject(this)

        viewModel.arePermissionsGranted = { grantedLocationPermission() }
        viewModel.requestPermissions = {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        pointNameField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }
        pointLatitudeField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }
        pointLongitudeField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }

        pointLongitudeField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                createPointButton.requestFocus()
                createPointButton.performClick()
                true
            } else {
                false
            }
        }

        addPointCard.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        binding.viewModel?.closeActivity?.observeForever { closeActivity ->
            if (closeActivity) {
                closeActivity()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION
            ) {
                viewModel.setCurrentLocation()
            }
        }
    }

    override fun onBackPressed() {
        closeActivity()
    }

    private fun closeActivity() {
        addPointCard.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        finish()
        overridePendingTransition(0, 0)
    }
}
