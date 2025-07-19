package com.devsync.numberfinder.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentSplashBinding
import com.devsync.numberfinder.localDB.preferences.PreferenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater,container,false)
        init()

//        var receivedText = arguments?.getString("lang")
//        if (receivedText=="new"){
//            receivedText = "jh"
//            requireActivity().recreate()
//
//            Log.d(TAG, "onCreateView: new ")
//
//        }
//        else{
//            Log.d(TAG, "onCreateView: first")
//        }
        return binding.root
    }

    private fun init(){
        preferenceManager= PreferenceManager(requireContext())

        val info = preferenceManager.getInformation()
        Log.d(TAG, "onCreateView: "+info)
//        Toast.makeText(requireContext(), preferenceManager.getLoginCount(), Toast.LENGTH_SHORT).show()


        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            Log.d(TAG, "onCreatddsdseView: ")

            val bundle = Bundle().apply {
                putString("key_name", "Hello, Home!")
            }
            if (info == "new") {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()

                findNavController().navigate(R.id.requiredPermissionsFragment, bundle, navOptions)
            }
            else{
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()
                findNavController().navigate(R.id.homeFragment, null, navOptions)
            }
        }
    }

    companion object {
        lateinit var preferenceManager: PreferenceManager
        private const val TAG = "SplashFragment"
    }
    object MyContextWrapper {
        fun wrap(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            return context.createConfigurationContext(config)
        }
    }
}