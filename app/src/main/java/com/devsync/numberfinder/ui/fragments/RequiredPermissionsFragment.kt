package com.devsync.numberfinder.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentRequiredPermissionsBinding
import com.devsync.numberfinder.localDB.preferences.PreferenceManager


class RequiredPermissionsFragment : Fragment() {
    private lateinit var binding :FragmentRequiredPermissionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequiredPermissionsBinding.inflate(layoutInflater,container,false)



        load()


        return binding.root


    }

    private fun load(){
        preferenceManager = PreferenceManager(requireContext())
        binding.acceptBtn.setOnClickListener{
            preferenceManager.setInformation("complete")
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.requiredPermissionsFragment, true)
                .build()
            findNavController().navigate(R.id.homeFragment, null, navOptions)

        }
    }
    companion object {
        private lateinit var preferenceManager: PreferenceManager
        private const val TAG = "RequiredPermissionsFrag"


    }
}