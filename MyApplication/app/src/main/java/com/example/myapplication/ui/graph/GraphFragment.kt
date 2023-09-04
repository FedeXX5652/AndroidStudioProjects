package com.example.myapplication.ui.graph

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentGraphBinding
import org.json.JSONObject
import kotlin.math.log

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var config = JSONObject()

    private var nodeCount = 0
    private var edgeMatrix = arrayOf<Array<Int>>()

    private var spell = JSONObject()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val activity = activity as MainActivity
        config = activity.config

        val webView = binding.webView
        val reloadBtn = binding.reloadBtn

        webView.settings.javaScriptEnabled = true

        webView.loadUrl("file:///android_asset/index.html")

        calculateNodeCount()

        for (addedSpells in activity.spells) {
            spell = addedSpells
            createEmptyMatrix()
            loadLevelMatrix()
            loadFormMatrix()
            loadEffectMatrix()
        }

        reloadBtn.setOnClickListener {
            // Call the JavaScript function to show the runes
            val script = """
                show();
            """
            webView.evaluateJavascript(script, null)
        }
        return root
    }

    private fun loadEffectMatrix() {
        val effect = spell.getString("effect")
        val school = spell.getString("school")
        val availableEffects = config.getJSONObject("effects").getJSONArray(school)
        var effectIndex = 0
        for (i in 0 until availableEffects.length()) {
            if (availableEffects.getJSONObject(i).getString("Efecto") == effect) {
                effectIndex = i
                break
            }
        }

        // add the other effects from previous schools
        // as if effects: {a: [{a1},{a2}], b: [{b1},{b2}]}
        // ad selected effect is b1, then the index should be:
        // 2 + 1 = 3
        for (i in 0 until config.getJSONObject("effects").length()) {
            if (config.getJSONObject("effects").names()[i] == school) {
                break
            }
            effectIndex += config.getJSONObject("effects").getJSONArray(
                config.getJSONObject("effects").names()?.get(i).toString()).length()
        }

        edgeMatrix[2] = rowFactory(effectIndex)
    }

    private fun loadFormMatrix() {
        val form = spell.getString("form")
        val availableForms = config.getJSONArray("forms")
        var formIndex = 0
        for (i in 0 until availableForms.length()) {
            if (availableForms.getJSONObject(i).getString("name") == form) {
                formIndex = i
                break
            }
        }
        edgeMatrix[1] = rowFactory(formIndex)
    }

    private fun loadLevelMatrix() {
        val lvl = spell.getInt("level")
        edgeMatrix[0] = rowFactory(lvl-1)
    }

    private fun createEmptyMatrix() {
        edgeMatrix = Array(config.length()) { Array(nodeCount+1) { 0 } }
    }

    private fun calculateNodeCount() {
        val lvlNodes = config.getInt("max_lvl")
        val formNodes = config.getJSONArray("forms").length()
        var effects = 0
        for (school in config.getJSONObject("effects").keys()) {
            val schoolEffects = config.getJSONObject("effects").getJSONArray(school).length()
            if (schoolEffects > effects) {
                effects = schoolEffects
            }
        }

        nodeCount = lvlNodes
        if (formNodes > nodeCount) {
            nodeCount = formNodes
        }
        if (effects > nodeCount) {
            nodeCount = effects
        }
    }

    private fun rowFactory(num:Int): Array<Int> {
        // receives a number and returns an array of 0s and 1s
        // its binary representation but with an added 1 at the ending
        // ex: 3 -> [0, 1, 1, 1]
        val binary = Integer.toBinaryString(num)
        val arr = Array(nodeCount+1) { 0 }
        for (i in binary.indices) {
            // add the binary representation to the array from the end
            // as shown: 5 -> 101 -> [0, 1, 0, 1] or 6 -> 110 -> [0, 1, 1, 0]
            arr[nodeCount - binary.length + i] = binary[i].toString().toInt()
        }
        // add extra position at the end and set it to 1
        arr[nodeCount] = 1
        return arr
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}