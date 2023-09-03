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

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var config = JSONObject()

    private var nodeCount = 0
    private var edgeMatrix = arrayOf<Array<Int>>()

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

        reloadBtn.setOnClickListener {
            // Call the JavaScript function to show the runes
            val script = """
                show();
            """
            webView.evaluateJavascript(script, null)
        }
        return root
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
        Log.d("nodeCount", nodeCount.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}