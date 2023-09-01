package com.example.runecreator


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val config = JSONObject()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        // function to load from config.json into config
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadConfig()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment


//        val webView = findViewById<WebView>(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//
//        webView.loadUrl("file:///android_asset/index.html")
//
//        Log.i("MainActivity", config.toString())
//
//        showButton.setOnClickListener {
//            // Call the JavaScript function to show the runes
//            val script = """
//                show();
//            """
//            webView.evaluateJavascript(script, null)
//        }
    }

    private fun loadConfig() {
        val configString = assets.open("config.json").bufferedReader().use { it.readText() }
        val configJSON = JSONObject(configString)
        /*
        * file has the following structure:
        * {
        *   "max_lvl": 5,
        *  "forms": [{},{}]
        * "effects": {"a": [{},{}], "b": [{},{}]}
        * */
        config.put("max_lvl", configJSON.getInt("max_lvl"))
        config.put("forms", configJSON.getJSONArray("forms"))
        config.put("effects", configJSON.getJSONObject("effects"))
    }
}


