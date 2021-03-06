package com.yalta.activities.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

import com.yalta.R
import com.yalta.databinding.FragmentMapBinding
import com.yalta.di.YaltaApplication
import com.yalta.utils.MapUtils.convertToBounds
import com.yalta.utils.MapUtils.convertToMarkerOptions
import com.yalta.utils.ViewUtils.grantedLocationPermission
import com.yalta.viewModel.MapViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MapFragment : Fragment(), OnMapReadyCallback {
    @Inject
    lateinit var viewModel: MapViewModel

    private lateinit var mapView: MapView

    private var markers: MutableList<MarkerOptions> = mutableListOf()
    private var bounds: LatLngBounds? = null
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        YaltaApplication.appComponent.inject(this)

        val binding: FragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        runBlocking {
            mapView.getMapAsync(this@MapFragment)
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.viewModel?.points?.observe(viewLifecycleOwner, { points ->
            markers = points.convertToMarkerOptions()
            bounds = points.convertToBounds()
            mapView.getMapAsync(this)
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        for (marker in markers) {
            map?.addMarker(marker)
        }
        googleMap.setOnMapLoadedCallback {
            if (bounds != null) {
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
            }
        }
        if (grantedLocationPermission()) {
            viewModel.locationPermissionsGranted.value = true
            setLocationButtonEnabled(true)
        } else {
            viewModel.locationPermissionsGranted.value = false
            setLocationButtonEnabled(false)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationButtonEnabled(state: Boolean) {
        map?.uiSettings?.isMyLocationButtonEnabled = state
        map?.isMyLocationEnabled = state
    }


}
