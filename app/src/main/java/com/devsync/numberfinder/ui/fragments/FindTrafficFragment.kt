package com.devsync.numberfinder.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentFindTrafficBinding
import com.devsync.numberfinder.remote.models.GoogleCoordinatesClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FindTrafficFragment : Fragment(), OnMapReadyCallback {
    private val PERMISSIONS_REQUEST_CODE = 1001
    private val GPS_REQUEST_CODE = 1002
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentFindTrafficBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleCoordinatesClass: GoogleCoordinatesClass
    private lateinit var progressBar: ProgressBar
    private var current_Location: String? = null
    private var check = 0
    private var latt: Double = 0.0
    private var lngi: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindTrafficBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMap()
        showBottomSheet()

        googleCoordinatesClass = GoogleCoordinatesClass()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backIcon.setOnClickListener {
            backPressed()
        }
        binding.findTrafficOpenBottomSheet.setOnClickListener {
            showBottomSheet()
        }
        binding.findTrafficTouchView.setOnClickListener {
            hideBottomSheet()
        }
        binding.myGpsIcon.setOnClickListener {
            permissionManager()
        }
        binding.findTrafficBtn.setOnClickListener {
            getData()
        }
        binding.switchIcon.setOnClickListener {
            switchText()
        }
    }

    private fun hideProgress() {
        check = 1
        progressBar.visibility = View.GONE
    }

    private fun showProgress() {
        check = 4
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
    }

    private fun permissionManager() {
        if (checkGpsStatus()) {
            if (checkAllPermissions()) {
                if (checkInternet()) {
                    showProgress()
                    getLastKnownLocation()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
                }
            } else {
                check()
            }
        } else {
            gpsDialog()
        }
    }

    private fun getLastKnownLocation() {
        lateinit var locationCallback: LocationCallback
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                if (location != null) {
                    latt = location.latitude
                    lngi = location.longitude
                    val latlng = LatLng(latt, lngi)

                    current_Location = googleCoordinatesClass.getExactLocationName(latlng, requireContext())
                    binding.startLocation.setText(current_Location)
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(latlng).title(getString(R.string.yourLocation)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 11f)
                    googleMap.animateCamera(cameraUpdate)
                    hideProgress()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.errorFetching), Toast.LENGTH_SHORT).show()
                }
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun getData() {
        val startET = binding.startLocation
        val endET = binding.destinationLocation
        startET.error = null
        endET.error = null
        val startingLocation = startET.text.toString()
        val destinationLocation = endET.text.toString()

        if (startingLocation.length < 4) {
            startET.error = getString(R.string.startLocationError)
        } else if (destinationLocation.length < 4) {
            endET.error = getString(R.string.endLocationError)
        } else {
            hideBottomSheet()
            openMapsForDirections(startingLocation, destinationLocation)
        }
    }

    private fun switchText() {
        val startET = binding.startLocation
        val endET = binding.destinationLocation
        startET.error = null
        endET.error = null
        val startingLocation = startET.text.toString()
        val destinationLocation = endET.text.toString()
        startET.setText(destinationLocation)
        endET.setText(startingLocation)
    }

    private fun openMapsForDirections(startingLocation: String, destinationLocation: String) {
        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$startingLocation&destination=$destinationLocation")

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // If Google Maps app is not installed, open the web version
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(webIntent)
        }
    }

    private fun showBottomSheet() {
        check = 1
        binding.findTrafficOpenBottomSheet.visibility = View.GONE
        binding.findTrafficTouchView.visibility = View.VISIBLE
        binding.findTrafficBottomSheet.visibility = View.VISIBLE
        binding.findTrafficBottomSheet.setOnClickListener {
            // Empty click listener to prevent clicks from passing through
        }
    }

    private fun hideBottomSheet() {
        check = 0
        binding.findTrafficOpenBottomSheet.visibility = View.VISIBLE
        binding.findTrafficTouchView.visibility = View.GONE
        binding.findTrafficBottomSheet.visibility = View.GONE
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.FindTrafficMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    private fun check() {
        if (!checkAllPermissions()) {
            requestPermissions()
        }
    }

    private fun checkGpsStatus(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun enableGps() {
        val settingsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(settingsIntent, GPS_REQUEST_CODE)
    }

    private fun gpsDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.gpsMsg))
            .setPositiveButton(getString(R.string.ok))
            { dialog, _ ->
                enableGps()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no))
            { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun checkAllPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        Log.d(TAG, "requestPermissions: requested " + permissions.size)
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                // Check if all requested permissions are granted
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // All permissions granted
                    permissionManager()
                } else {
                    // Some or all permissions were denied
                    showPermissionDeniedDialog()
                }
            }
            else -> {
                Log.d(TAG, "onRequestPermissionsResult: else")
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.gpsToast))
            .setPositiveButton(getString(R.string.ok))
            { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            GPS_REQUEST_CODE -> {
                if (checkGpsStatus()) {
                    permissionManager()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.errorFetching), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun backPressed() {
        if (check == 1) {
            hideBottomSheet()
        } else if (check == 4) {
            // Do nothing if progress is showing
        } else if (check == 0) {
            requireActivity().onBackPressed()
        }
    }

    // Utility method to check internet connectivity
    private fun checkInternet(): Boolean {
        // Implement connectivity check here
        // This was referenced in the original code but not implemented
        return true  // Default implementation, replace with actual check
    }

    companion object {
        private const val TAG = "FindTrafficFragment"
        fun newInstance() = FindTrafficFragment()
    }
}