package com.devsync.numberfinder.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentNearbyPlacesBinding

class NearbyPlacesFragment : Fragment() {
    private var _binding: FragmentNearbyPlacesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearbyPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backIcon.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.bank.setOnClickListener {
            getLocation("banks")
        }

        binding.hospital.setOnClickListener {
            getLocation("hospitals")
        }

        binding.ATM.setOnClickListener {
            getLocation("ATM")
        }

        binding.gasStation.setOnClickListener {
            getLocation("gas stations")
        }

        binding.policeStation.setOnClickListener {
            getLocation("police stations")
        }

        binding.fireStation.setOnClickListener {
            getLocation("fire Stations")
        }
    }

    private fun getLocation(query: String) {
        // Create an intent to open Google Maps with the specified query
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:0,0?q=$query")
        )

        // Verify that the Google Maps app is installed before starting the intent
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.mapNotInstalled), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NearbyPlacesFragment()
    }
}