package com.devsync.numberfinder.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsync.numberfinder.databinding.FragmentSimInfoBinding
import com.devsync.numberfinder.remote.models.CountryDetails
import com.devsync.numberfinder.remote.models.JsonParsing
import com.devsync.numberfinder.remote.models.NetworkProvider
import com.devsync.numberfinder.ui.adapters.CompaniesAdapter
import com.devsync.numberfinder.ui.adapters.SimCountryAdapter
import com.devsync.numberfinder.ui.interfaces.BlockedNumClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SimInfoFragment : Fragment(), BlockedNumClickListener {
    private val TAG = "SimInfoFragment"
    private var _binding: FragmentSimInfoBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var jsonParsing: JsonParsing
    @Inject lateinit var simCountryAdapter: SimCountryAdapter
    @Inject lateinit var companiesAdapter: CompaniesAdapter


    private var countriesList: List<CountryDetails> = ArrayList()
    private var dash: List<NetworkProvider> = ArrayList()
    private var countryName: String = ""
    private var check = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getList()

        binding.backIcon.setOnClickListener {
            backPress()
        }
    }

    private fun getList() {
        jsonParsing = JsonParsing()
        val jsonString = jsonParsing.loadJsonFromAssetsCountry(requireContext(), "service_json.json")
        if (jsonString != null) {
            countriesList = jsonParsing.parseNetworkData(jsonString)
            Log.d(TAG, "getList: " + countriesList.size)
            loadAdapter(countriesList)
        }
    }

    private fun loadAdapter(list: List<CountryDetails> = ArrayList()) {
        binding.networkLayout1.visibility = View.GONE
        binding.simLayout1.visibility = View.VISIBLE

        simCountryAdapter = SimCountryAdapter(requireContext(), list, this)
        binding.simRecycler1.layoutManager = LinearLayoutManager(requireContext())
        binding.simRecycler1.adapter = simCountryAdapter
    }

    private fun loadSimAdapter(list: List<NetworkProvider> = ArrayList()) {
        check = 2
        binding.networkLayout1.visibility = View.VISIBLE
        binding.simLayout1.visibility = View.GONE

        companiesAdapter = CompaniesAdapter(requireContext(), list, this)
        binding.networkRecycler1.layoutManager = LinearLayoutManager(requireContext())
        binding.networkRecycler1.adapter = companiesAdapter
    }

    override fun onDataReceived(countryDetails: CountryDetails) {
        dash = countryDetails.networkProviders
        countryName = countryDetails.name
        Log.d(TAG, "onDataReceived: " + dash.size)
        loadSimAdapter(dash)
    }

    override fun onDataReceivedd(networkProvider: NetworkProvider) {
        showInfo(networkProvider)
    }

    private fun showInfo(networkProvider: NetworkProvider) {
        check = 3
        binding.Bar.text = countryName
        binding.networkLayout1.visibility = View.GONE
        binding.simInfoBottomSheet.visibility = View.VISIBLE
        binding.networkName.text = networkProvider.name
        binding.helpLine.text = networkProvider.helpline
        binding.recharge.text = networkProvider.recharge
        binding.remainingBalance.text = networkProvider.balanceInquiry
        binding.remainingMBs.text = networkProvider.mbsInquiry
        binding.remainingMinutes.text = networkProvider.minutesInquiry
        binding.remainingSMS.text = networkProvider.smsInquiry
        binding.findMyNumber.text = networkProvider.findMyNumber
    }

    private fun backPress() {
        if (check == 3) {
            check = 2
            binding.networkLayout1.visibility = View.VISIBLE
            binding.simInfoBottomSheet.visibility = View.GONE
        } else if (check == 2) {
            check = 0
            binding.simLayout1.visibility = View.VISIBLE
            binding.networkLayout1.visibility = View.GONE
        } else if (check == 0) {
            requireActivity().onBackPressed()
        } else {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SimInfoFragment()
    }
}