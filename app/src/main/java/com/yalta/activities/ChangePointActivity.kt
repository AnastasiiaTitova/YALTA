package com.yalta.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.yalta.R
import com.yalta.databinding.ActivityChangePointBinding
import com.yalta.di.YaltaApplication
import com.yalta.utils.ViewUtils.grantedLocationPermission
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewModel.ChangePointViewModel
import kotlinx.android.synthetic.main.activity_change_point.*
import javax.inject.Inject

class ChangePointActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: ChangePointViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        val binding: ActivityChangePointBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_point)

        YaltaApplication.appComponent.inject(this)

        viewModel.arePermissionsGranted = { grantedLocationPermission() }
        viewModel.requestPermissions = {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }

        val bundle = intent.extras
        if (bundle != null) {
            val name = bundle.getString("name")!!
            val lat = bundle.getDouble("lat")
            val lon = bundle.getDouble("lon")
            viewModel.oldPoint = common.Point(
                bundle.getLong("id"), lat, lon, name
            )
            viewModel.pointName.value = name
            viewModel.pointLat.value = lat.toString()
            viewModel.pointLon.value = lon.toString()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        pointNameField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }
        pointLatitudeField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }
        pointLongitudeField.doOnTextChanged { _, _, _, _ -> viewModel.showEmptyFieldError(false) }

        pointLongitudeField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                updatePointButton.requestFocus()
                updatePointButton.performClick()
                true
            } else {
                false
            }
        }

        changePointCard.animate().alpha(1f).setDuration(500).setInterpolator(
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
            } else {
                viewModel.isCurrentPositionEnabled.value = false
            }
        }
    }

    override fun onBackPressed() {
        closeActivity()
    }

    private fun closeActivity() {
        changePointCard.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        setResult(1, Intent())
        finish()
        overridePendingTransition(0, 0)
    }
}
