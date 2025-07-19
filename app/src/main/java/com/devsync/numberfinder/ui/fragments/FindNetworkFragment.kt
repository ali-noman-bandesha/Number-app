package com.devsync.numberfinder.ui.fragments

/*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentFindNetworkBinding
import com.devsync.numberfinder.databinding.FragmentSplashBinding

class FindNetworkFragment : Fragment() {

    private lateinit var binding:FragmentFindNetworkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindNetworkBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        private const val TAG = "FindNetworkFragment"
    }
}*/


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentFindNetworkBinding
import com.devsync.numberfinder.remote.models.ApiModel
import com.devsync.numberfinder.remote.models.CountriesInfo
import com.devsync.numberfinder.remote.models.JsonParsing
import com.devsync.numberfinder.remote.retrofit.RetrofitClient
import com.devsync.numberfinder.ui.adapters.SelectCountriesAdp
import com.devsync.numberfinder.ui.interfaces.CountryClickListener
import com.devsync.numberfinder.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindNetworkFragment : Fragment(), CountryClickListener {

    private lateinit var binding: FragmentFindNetworkBinding
    private lateinit var adapter: SelectCountriesAdp
    private lateinit var jsonParsing: JsonParsing

    private var countries: List<CountriesInfo> = ArrayList()
    private var filteredList: List<CountriesInfo> = ArrayList()
    private var country: String = ""
    private var code: String = ""
    private var check = 0
    private lateinit var  utils: Utils
    private lateinit var myview: View

    private val access_key = "38bab3e05e49018ad0cb68d9d113aad4"
    private val TAG = "FindNetworkFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindNetworkBinding.inflate(layoutInflater, container, false)

        utils = Utils()
        searchCountry()
        getList()

        binding.backIcon.setOnClickListener {
            backPress()
        }

        binding.NetworkSearchBtn.setOnClickListener { view ->
            val number = binding.findNumberET.text.toString()
            myview = view
            binding.networkInfoLayout.visibility = View.GONE
            validate(number)
        }

        binding.selectCountryET.isFocusable = false
        binding.selectCountryET.setOnClickListener {
            showBottomSheet()
            binding.networkInfoLayout.visibility = View.GONE
        }

        return binding.root
    }

    private fun searchCountry() {
        binding.numberToFind.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = p0.toString().trim()
                filteredList = countries.filter { it.name.official?.contains(searchText, ignoreCase = true) == true }
                Log.d(TAG, "onTextChanged: size of list is  " + filteredList.size)

                if (filteredList.isEmpty()) {
                    binding.countryNotFound.visibility = View.VISIBLE
                } else {
                    binding.countryNotFound.visibility = View.GONE
                }
                loadAdapter(filteredList)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun validate(number: String) {
        Log.d(TAG, "validate: ")
        if (code.length < 2) {
            Toast.makeText(requireContext(), getString(R.string.selectCountryFirstToast), Toast.LENGTH_SHORT).show()
        } else if (number.length < 8 || number.length > 15) {
            Toast.makeText(requireContext(), getString(R.string.invalidNumberToast), Toast.LENGTH_SHORT).show()
        } else {
            checkPakistan(number)
        }
    }

    private fun showBottomSheet() {
        check = 1
        binding.findNetworkBottomSheet.visibility = View.VISIBLE
        binding.findLayout.visibility = View.GONE
        val utils = Utils()
        if (!utils.checkInternet(requireContext())) {
            Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideBottomSheet() {
        check = 0
        binding.findNetworkBottomSheet.visibility = View.GONE
        binding.findLayout.visibility = View.VISIBLE
    }

    private fun getList() {
        jsonParsing = JsonParsing()
        val jsonString = jsonParsing.loadJsonFromAssetsCountry(requireContext(), "countries_json.json")
        jsonString?.let {
            countries = jsonParsing.parseJsonDataCountry(it)
            loadAdapter(countries)
        }
    }

    private fun loadAdapter(list: List<CountriesInfo> = ArrayList()) {
        adapter = SelectCountriesAdp(requireContext(), list, this)
        binding.countriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.countriesRecyclerView.adapter = adapter
    }

    override fun onDataReceived(countriesInfo: CountriesInfo) {
        Log.d(TAG, "onDataReceived: $countriesInfo")
        country = countriesInfo.name.common ?: ""
        hideBottomSheet()
        code = countriesInfo.idd.root + countriesInfo.idd.suffixes[0]
        binding.selectCountryET.text = "$code $country"
    }

    private fun checkPakistan(number: String) {
        hideKeyboard(myview)
        val newNumber: String
        if (country == "Pakistan") {
            newNumber = if (number.startsWith("0")) {
                number.substring(1)
            } else {
                number
            }
            pakistaniOperators(newNumber)
        } else {
            apiCall(number)
        }
    }

    private fun pakistaniOperators(number: String) {
        val operators = number.substring(0, 2)
        val provider = when (operators) {
            "30" -> "Jazz"
            "31" -> "Zong"
            "32" -> "Warid"
            "33" -> "Ufone"
            "34" -> "Telenor"
            "35" -> "SCOM"
            else -> null
        }
        provider?.let {
            setProvider(it)
        } ?: run {
            Toast.makeText(requireContext(), getString(R.string.networkNotFoundToast), Toast.LENGTH_SHORT).show()
        }
    }

    private fun apiCall(number: String) {
        if (utils.checkInternet(requireContext())) {
            val call: Call<ApiModel> = RetrofitClient().apiService
                .getData(access_key, code + number)
            call.enqueue(object : Callback<ApiModel?> {
                override fun onResponse(call: Call<ApiModel?>, response: Response<ApiModel?>) {
                    if (response.isSuccessful) {
                        val apiModel = response.body()
                        if (apiModel?.carrier?.isNotEmpty() == true) {
                            setProvider(apiModel.carrier)
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.networkNotFoundToast), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.networkNotFoundToast), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiModel?>, t: Throwable) {
                    Toast.makeText(requireContext(), "onFailure: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        } else {
            Toast.makeText(requireContext(), getString(R.string.interToast), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setProvider(networkProvider: String) {
        binding.networkInfoLayout.visibility = View.VISIBLE
        binding.networkTextView.text = networkProvider
    }

    private fun backPress() {
        when (check) {
            0 -> {
                requireActivity().onBackPressed()
            }
            1 -> {
                hideBottomSheet()
            }
            else -> {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        backPress()
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val TAG = "FindNetworkFragment"
    }
}