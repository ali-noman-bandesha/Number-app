package com.devsync.numberfinder.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.FragmentHomeBinding
import com.devsync.numberfinder.localDB.preferences.PreferenceManager


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var drawerLayout : DrawerLayout
    private var lang:String = "en"
    private var check=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        init()
        clicks()
        manageLanguages()
        loadIcon()
        navigationDrawer()



        return binding.root
    }
    private fun clicks(){

        binding.homeIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.numberCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_numberLocatorFragment)
        }
        binding.simInfoCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_simInfoFragment)
//            startActivity(Intent(this, SimInfoActivity::class.java))

        }
        binding.findNetworkCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_findNetworkFragment)

        }
        binding.searchCoardCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchCoordinatesFragment)


        }
        binding.findTrafficCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_findTrafficFragment)

        }
        binding.nearByPlacesCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nearbyPlacesFragment)

        }
        binding.worldProfilesCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_worldProfilesFragment)

        }

        binding.view.setOnClickListener {
            hideDialog()
        }

        binding.hindi.setOnClickListener {
            if (preferenceManager.getLoginCount() != "hi") {preferenceManager.setLoginCount("hi")
                loadSplash()

            }
        }
        binding.english.setOnClickListener {
            if (preferenceManager.getLoginCount() != "en") {preferenceManager.setLoginCount("en")
                loadSplash()

            }
        }
        binding.german.setOnClickListener {
            if (preferenceManager.getLoginCount() != "de") {preferenceManager.setLoginCount("de")
                loadSplash()
            }
        }
        binding.french.setOnClickListener {
            if (preferenceManager.getLoginCount() != "fr") {preferenceManager.setLoginCount("fr")
                loadSplash()

            }
        }
        binding.azerbaijani.setOnClickListener {
            if (preferenceManager.getLoginCount() != "az") {preferenceManager.setLoginCount("az")
                loadSplash()

            }
        }
        binding.navDrawer.empty.setOnClickListener {

        }
        binding.navDrawer.privacyPolicy.setOnClickListener {
            showPrivacyPolicy()
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.navDrawer.shareApp.setOnClickListener {
            shareApp()
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.navDrawer.rateApp.setOnClickListener {
            rateApp()
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.navDrawer.settings.setOnClickListener {
            showDialog()
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun loadSplash(){
        val intent = requireActivity().intent
        requireActivity().finish()
//        requireActivity().overridePendingTransition(0, 0) // optional: no animation
        startActivity(intent)
//        requireActivity().overridePendingTransition(0, 0)

//        val bundle = Bundle().apply {
//            putString("lang", "new")
//        }
//
//        val navOptions = NavOptions.Builder()
//            .setPopUpTo(R.id.homeFragment, true)
//            .build()
//
//        findNavController().navigate(R.id.splashFragment, bundle, navOptions)
    }
    private fun init(){
        preferenceManager = PreferenceManager(requireContext())
        lang = preferenceManager.getLoginCount()

    }

    private fun showPrivacyPolicy()
    {
        check = 4
        binding.webVeiw.visibility = View.VISIBLE
        binding.homeList.visibility = View.GONE
        loadPrivacyPolicy()
        binding.homeText.text = getString(R.string.privacyPolicy)
        binding.homeDescription.text = ""

    }

    private fun loadPrivacyPolicy() {
        val webView: WebView = binding.webVeiw

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Load your privacy policy URL here
        val privacyPolicyUrl = "https://www.google.com"
        webView.loadUrl(privacyPolicyUrl)
    }

    private fun hidePrivacyPolicy()
    {
        check = 0
        binding.webVeiw.visibility = View.GONE
        binding.homeList.visibility = View.VISIBLE
        binding.homeText.text = getString(R.string.Home)
        binding.homeDescription.text = getString(R.string.HomeDescription)
    }


    private fun loadIcon()
    {
        loadIcons(binding.numberLocatorArrow)
        loadIcons(binding.worldProfilesArrow)
        loadIcons(binding.nearByPlacesArrow)
        loadIcons(binding.findTrafficArrow)
        loadIcons(binding.SearchCoordinatesArrow)
        loadIcons(binding.findNetworkArrow)
        loadIcons(binding.blockArrow)
    }
    private fun loadIcons( imageView: ImageView)
    {
        Glide.with(this)
            .asGif()
            .load(R.drawable.next_icon)
            .into(imageView)
    }


    private fun navigationDrawer()
    {
        drawerLayout  = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
   /* override fun onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else if (check==1)
        {
            hideDialog()
        }
        else if (check == 4)
        {
            hidePrivacyPolicy()
        }
        else if (check==0)
            super.onBackPressed()
    }*/

    private fun hideDialog()
    {
        check=0
        binding.view.visibility = View.GONE
        binding.homeList.visibility = View.VISIBLE
        binding.dialogView.visibility = View.GONE
    }
    private fun showDialog()
    {
        check=1
        binding.view.visibility = View.VISIBLE
        binding.homeList.visibility = View.GONE
        binding.dialogView.visibility = View.VISIBLE
    }
    private fun manageLanguages()
    {
        if (lang=="en")
        {
            binding.english.setBackgroundResource(R.drawable.selected_card_bg)
            binding.englishText.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.englishTV.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }
        else if (lang=="hi")
        {
            binding.hindi.setBackgroundResource(R.drawable.selected_card_bg)
            binding.hindiTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.hindiTv1.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }
        else if (lang=="de"){
            binding.german.setBackgroundResource(R.drawable.selected_card_bg)
            binding.germanTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.germanTv1.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }
        else if (lang=="fr")
        {
            binding.french.setBackgroundResource(R.drawable.selected_card_bg)
            binding.frenchTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.frenchTv1.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }
        else if (lang=="az")
        {
            binding.azerbaijani.setBackgroundResource(R.drawable.selected_card_bg)
            binding.azerbijaniTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.azerbijaniTv1.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }
    }
    private fun rateApp() {
        val packageName = "PackageManager.pack"
        val marketUri = Uri.parse("market://details?id=$packageName")

        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        try {
            startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            // If the Play Store app is not available, open the web version
            val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            startActivity(webIntent)
        }
    }
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My App Name")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out My App Name at: https://play.google.com/store/apps/details?id=" + "packageName")
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    companion object {
        private const val TAG = "HomeFragment"
        private lateinit var preferenceManager: PreferenceManager
    }
}