package com.devsync.numberfinder.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devsync.numberfinder.R
import com.devsync.numberfinder.localDB.preferences.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.status)

    }
    override fun attachBaseContext(newBase: Context) {
        val context = LocaleHelper.applyLocale(newBase)
        super.attachBaseContext(context)
    }
    object LocaleHelper {

        fun setLocale(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            return context.createConfigurationContext(config)
        }

        fun applyLocale(context: Context): Context {
            val lang = PreferenceManager(context).getLoginCount() // or saved language
            return setLocale(context, lang)
        }
    }
}