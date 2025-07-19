package com.devsync.numberfinder.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentSearchCoordinatesBinding
import com.devsync.numberfinder.remote.models.GoogleCoordinatesClass
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SearchCoordinatesFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentSearchCoordinatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var googleCoordinatesClass: GoogleCoordinatesClass
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCoordinatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMap()
        showBottomSheet()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backIcon.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.openBottomSheet.setOnClickListener {
            showBottomSheet()
        }
        binding.touchView.setOnClickListener {
            hideBottomSheet()
        }
        binding.searchCoordinatesBtn.setOnClickListener {
            getCoordinates()
        }
    }

    private fun getCoordinates() {
        val latET = binding.searchCoordinatesLat
        val lngET = binding.searchCoordinatesLon
        val lat = latET.text.toString()
        val lng = lngET.text.toString()

        if (isValidLatitude(lat) && isValidLongitude(lng)) {
            val latitude = lat.toDouble()
            val longitude = lng.toDouble()
            val location = LatLng(latitude, longitude)
            if (checkInternet()) {
                googleCoordinatesClass = GoogleCoordinatesClass()
                name = googleCoordinatesClass.getLocationName(location, requireContext())
                mMap.clear()
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.addMarker(MarkerOptions().position(location).title(name))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10f)
                mMap.animateCamera(cameraUpdate)
                hideBottomSheet()
            } else {
                Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
            }
        } else {
            if (!isValidLatitude(lat)) {
                latET.error = getString(R.string.invalidLat)
            }

            if (!isValidLongitude(lng)) {
                lngET.error = getString(R.string.invalidLng)
            }
        }
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.SearchCoordinatesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun showBottomSheet() {
        binding.openBottomSheet.visibility = View.GONE
        binding.getCoordinatesView.visibility = View.VISIBLE
        binding.touchView.visibility = View.VISIBLE
        binding.getCoordinatesView.setOnClickListener {
            // Empty click listener to prevent clicks from passing through
        }
    }

    private fun hideBottomSheet() {
        binding.touchView.visibility = View.GONE
        binding.openBottomSheet.visibility = View.VISIBLE
        binding.getCoordinatesView.visibility = View.GONE
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }

    private fun isValidLatitude(latitude: String): Boolean {
        try {
            val value = latitude.toDouble()
            return value in -90.0..90.0
        } catch (e: NumberFormatException) {
            return false
        }
    }

    private fun isValidLongitude(longitude: String): Boolean {
        try {
            val value = longitude.toDouble()
            return value in -180.0..180.0
        } catch (e: NumberFormatException) {
            return false
        }
    }

    // Utility method to check internet connectivity
    private fun checkInternet(): Boolean {
        // Implement connectivity check here
        // This was referenced in the original code but not implemented directly
        return true  // Default implementation, replace with actual check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SearchCoordinatesFragment()
    }
}