package com.devsync.numberfinder.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentWorldProfilesBinding
import com.devsync.numberfinder.remote.models.CountriesInfo
import com.devsync.numberfinder.remote.models.Country
import com.devsync.numberfinder.remote.models.JsonParsing
import com.devsync.numberfinder.ui.adapters.WorldProfilesAdp
import com.devsync.numberfinder.ui.interfaces.CountryClickListener


class WorldProfilesFragment : Fragment(), CountryClickListener {
    private val TAG = "WorldProfilesFragment"
    private lateinit var jsonParsing: JsonParsing
    private var countries: List<CountriesInfo> = ArrayList()
    private var countriesDetails: List<Country> = ArrayList()
    private var filteredList: List<CountriesInfo> = ArrayList()
    private lateinit var adapter: WorldProfilesAdp
    private var check = 0
    private var _binding: FragmentWorldProfilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorldProfilesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getList()
        getDetails()
        searchCountry()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.searchCountryIcon.setOnClickListener {
            searchCountry()
        }
        binding.backIcon.setOnClickListener {
            backPress()
        }
    }

    private fun searchCountry() {
        binding.searchCountryEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = p0.toString().trim()
                filteredList = countries.filter { it.name.official?.contains(searchText, ignoreCase = true) == true }
                filteredList.forEach { println("${it.name.common} - ${it.name.official}") }
                Log.d(TAG, "onTextChanged: size of list is  " + filteredList.size)

                if (filteredList.isEmpty()) {
                    binding.countryNotFound.visibility = View.VISIBLE
                } else {
                    binding.countryNotFound.visibility = View.GONE
                }
                loadAdapter(filteredList)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun getDetails() {
        jsonParsing = JsonParsing()
        val jsonString = jsonParsing.loadJsonFromAssetsCountry(requireContext(), "country_detail_json.json")
        if (jsonString != null) {
            countriesDetails = jsonParsing.parseJsonData(jsonString)
            Log.d(TAG, "getDetails: " + countriesDetails.size)
        }
    }

    private fun getList() {
        jsonParsing = JsonParsing()
        val jsonString = jsonParsing.loadJsonFromAssetsCountry(requireContext(), "countries_json.json")
        if (jsonString != null) {
            countries = jsonParsing.parseJsonDataCountry(jsonString)
            loadAdapter(countries)
        }
    }

    private fun loadAdapter(list: List<CountriesInfo> = ArrayList()) {
        adapter = WorldProfilesAdp(requireContext(), list, this)
        binding.countriesView.layoutManager = LinearLayoutManager(requireContext())
        binding.countriesView.adapter = adapter
    }

    private fun showBottomSheet() {
        check = 1
        binding.countryInfoBottomSheet.visibility = View.VISIBLE
        binding.countriesView.visibility = View.GONE
        binding.searchCountryEtLayout.visibility = View.GONE
    }

    private fun hideBottomSheet() {
        check = 0
        binding.countryInfoBottomSheet.visibility = View.GONE
        binding.countriesView.visibility = View.VISIBLE
        binding.searchCountryEtLayout.visibility = View.VISIBLE
    }

    private fun backPress() {
        if (check == 0) {
            requireActivity().onBackPressed()
        } else if (check == 1) {
            hideBottomSheet()
        } else {
            requireActivity().onBackPressed()
        }
    }

    override fun onDataReceived(countriesInfo: CountriesInfo) {
        val country: Country?
        hideKeyboard()
        country = findCountryByName(countriesDetails, countriesInfo.name.common)
        showBottomSheet()

        if (checkInternet()) {
            Glide.with(requireContext()).load(countriesInfo.flags.png).into(binding.flagBanner)
        } else {
            Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
        }

        Glide.with(requireContext()).load(countriesInfo.flags.png).into(binding.flagBanner)
        binding.countryNameView.text = countriesInfo.name.common
        binding.CountryProfileName.text = countriesInfo.name.common
        binding.countryISOCode.text = countriesInfo.idd.root + countriesInfo.idd.suffixes[0]
        binding.countryLocation.text = "${countriesInfo.latlng?.get(0)}, ${countriesInfo.latlng?.get(1)}"

        try {
            if (countriesInfo.capital?.size ?: 0 > 1) {
                binding.countryCapitalCity.text = "No capital Info"
            } else {
                binding.countryCapitalCity.text = countriesInfo.capital?.get(0)
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
        }

        val list: List<String> = countriesInfo.languages.values.toList()
        Log.d(TAG, "onDataReceived: " + list.size)
        if (list.size > 1) {
            binding.countryNationalLanguage.text = list[1]
        } else if (list.isNotEmpty()) {
            binding.countryNationalLanguage.text = list[0]
        }

        binding.countryArea.text = "${countriesInfo.area} km²"

        if (country != null) {
            setText(country.majorReligion, binding.countryOfficialReligion)
            setText(country.policeNumber, binding.countryPoliceNumber)
            setText(country.fireBrigadeNumber, binding.countryFireNumber)
            setText(country.nationalAnimal, binding.countryNationalAnimal)
            setText(country.currencyCode, binding.countryCurrency)
        }
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
    }


    private fun setText(info: String?, textView: TextView) {
        if (info.isNullOrEmpty() || info == "null") {
            textView.text = "N/A "
        } else {
            textView.text = info
        }
    }

    private fun findCountryByName(countries: List<Country>, targetCountryName: String?): Country? {
        for (country in countries) {
            if (country.name.equals(targetCountryName, ignoreCase = true)) {
                return country
            }
        }
        return null
    }

    private fun hideKeyboard() {
        val activity = activity ?: return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    // Utility method to check internet connectivity
    private fun checkInternet(): Boolean {
        // Implement connectivity check here
        // This was referenced in the original code but implementation wasn't shown
        return true  // Default implementation, replace with actual check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = WorldProfilesFragment()
    }
}