package com.example.myapplication

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var config = JSONObject()
    var level = 1
    var form = ""
    var school = ""
    var effect = ""
    var spells = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = loadConfig()
        form = config.getJSONArray("forms").getJSONObject(0).getString("name")
        school = config.getJSONObject("effects").keys().next()
        effect = config.getJSONObject("effects").getJSONArray(school).getJSONObject(0).getString("Efecto")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_spells, R.id.navigation_edit_spells, R.id.navigation_edit_graph, R.id.navigation_graph
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun loadConfig(): JSONObject {
        val configString = assets.open("config.json").bufferedReader().use { it.readText() }
        val configJSON = JSONObject(configString)
        var json = JSONObject()
        /*
        * file has the following structure:
        * {
        *   "max_lvl": 5,
        *  "forms": [{},{}]
        * "effects": {"a": [{},{}], "b": [{},{}]}
        * */
        json.put("max_lvl", configJSON.getInt("max_lvl"))
        json.put("forms", configJSON.getJSONArray("forms"))
        json.put("effects", configJSON.getJSONObject("effects"))
        return json
    }
}