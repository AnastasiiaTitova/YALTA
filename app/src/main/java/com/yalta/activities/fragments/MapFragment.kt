package com.yalta.activities.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

import com.yalta.R
import com.yalta.databinding.FragmentMapBinding
import com.yalta.viewmodel.MapViewModel

class MapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel by lazy { ViewModelProvider(this).get(MapViewModel::class.java) }
    private lateinit var mapView: MapView

    private var location: Location? = null
    private val defaultZoom = 16.0F
    private var map: GoogleMap? = null
    private var locationPermissionGranted = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val binding: FragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.viewModel?.currentLocation?.observe(viewLifecycleOwner, { location ->
            this.location = location
            mapView.getMapAsync(this)
        })

        mapView = view.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        mapView.getMapAsync(this)

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
        getLocationPermission()
        if (locationPermissionGranted) {
            map?.uiSettings?.isMyLocationButtonEnabled = true
            map?.isMyLocationEnabled = true
        }
        else {
            map?.uiSettings?.isMyLocationButtonEnabled = false
            map?.isMyLocationEnabled = false
            return
        }
        if (location != null) {
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location!!.latitude,
                        location!!.longitude
                    ), defaultZoom
                )
            )
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity().parent, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
            ) {
                locationPermissionGranted = true
            }
        }
    }
}
