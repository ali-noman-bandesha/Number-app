 package com.devsync.numberfinder.ui.fragments
/*

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentNumberLocatorBinding
import com.devsync.numberfinder.remote.models.GoogleCoordinatesClass
import com.google.android.gms.maps.GoogleMap


 class NumberLocatorFragment : Fragment() {
    private lateinit var binding: FragmentNumberLocatorBinding
    private lateinit var googleMap: GoogleMap
    lateinit var googleCoordinatesClass: GoogleCoordinatesClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumberLocatorBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        private const val TAG = "NumberLocatorFragment"

    }
}*/
 import android.content.Context
 import android.net.ConnectivityManager
 import android.net.NetworkCapabilities
 import android.os.Bundle
 import android.text.Editable
 import android.text.TextWatcher
 import android.util.Log
 import android.view.*
 import android.view.inputmethod.InputMethodManager
 import android.widget.Toast
 import androidx.core.content.ContextCompat
 import androidx.fragment.app.Fragment
 import com.devsync.numberfinder.R
 import com.devsync.numberfinder.databinding.FragmentNumberLocatorBinding
 import com.devsync.numberfinder.remote.models.GoogleCoordinatesClass
 import com.devsync.numberfinder.utils.Utils
 import com.google.android.gms.maps.CameraUpdateFactory
 import com.google.android.gms.maps.GoogleMap
 import com.google.android.gms.maps.OnMapReadyCallback
 import com.google.android.gms.maps.SupportMapFragment
 import com.google.android.gms.maps.model.LatLng
 import com.google.android.gms.maps.model.MarkerOptions

 class NumberLocatorFragment : Fragment(), OnMapReadyCallback {

     private lateinit var binding: FragmentNumberLocatorBinding
     private lateinit var googleMap: GoogleMap
     private lateinit var googleCoordinatesClass: GoogleCoordinatesClass
     private val TAG = "NumberLocatorFragment"

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         binding = FragmentNumberLocatorBinding.inflate(inflater, container, false)
         return binding.root
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         googleCoordinatesClass = GoogleCoordinatesClass()

         binding.locateBtn.setBackgroundColor(
             ContextCompat.getColor(requireContext(), R.color.unselectedBtn)
         )

         setupListeners()
         loadMap()
     }

     private fun setupListeners() {
         binding.numberET.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
             override fun afterTextChanged(s: Editable?) {
                 binding.locateBtn.setBackgroundColor(
                     ContextCompat.getColor(requireContext(), R.color.selectedBtn)
                 )
             }
         })

         binding.locateBtn.setOnClickListener {
             getNumber()
         }

         binding.backIcon.setOnClickListener {
             requireActivity().onBackPressedDispatcher.onBackPressed()
         }
     }

     private fun getNumber() {
         val phone = binding.numberET.text.toString()
         Log.d(TAG, "getNumber: $phone")

         when {
             phone.length > 18 -> {
                 binding.numberET.error = getString(R.string.numberLong)
             }
             phone.length < 8 -> {
                 binding.numberET.error = getString(R.string.numberSHort)
             }
             else -> {
                 getName("+$phone")
             }
         }
     }

     private fun getName(phoneNumber: String) {
         Log.d(TAG, "getName: $phoneNumber")
         val countryName = googleCoordinatesClass.getCountryNameFromPhoneNumber(phoneNumber, "US")

         if (!countryName.isNullOrEmpty()) {
             getLatLng(countryName)
         } else {
             Toast.makeText(requireContext(), getString(R.string.invalidNumberToast), Toast.LENGTH_SHORT).show()
         }

         Log.d("CountryName", countryName ?: "null")
     }

     private fun getLatLng(countryName: String) {
         val utils=Utils()
         if (utils.checkInternet(requireContext())) {
             val coordinates = googleCoordinatesClass.getCoordinatesFromLocationName(requireContext(), countryName)
             if (coordinates != null) {
                 binding.locateBtn.setBackgroundColor(
                     ContextCompat.getColor(requireContext(), R.color.unselectedBtn)
                 )
                 hideKeyboard()

                 val latLng = LatLng(coordinates.latitude, coordinates.longitude)
                 googleMap.clear()
                 googleMap.addMarker(MarkerOptions().position(latLng).title(countryName))
                 googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))

                 Log.d("LocationCoordinates", "Latitude: ${coordinates.latitude}, Longitude: ${coordinates.longitude}")
             } else {
                 Log.e("LocationCoordinates", "Unable to get coordinates for: $countryName")
             }
         } else {
             Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
         }
     }

     private fun hideKeyboard() {
         val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
         imm.hideSoftInputFromWindow(requireView().windowToken, 0)
     }

     private fun loadMap() {
         val mapFragment = childFragmentManager.findFragmentById(R.id.numberLocatorMap) as? SupportMapFragment
         mapFragment?.getMapAsync(this)


     }

     override fun onMapReady(map: GoogleMap) {
         googleMap = map
     }
 }