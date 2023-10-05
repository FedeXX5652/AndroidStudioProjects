package com.example.myapplication.ui.graph

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
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
    private var usedNodes = mutableListOf<Int>()
    private var edgeMatrix = arrayOf<Array<Int>>()

    private var spell = JSONObject()

    private var elements = mutableListOf<JSONObject>() // this list contains the nodes and edges

    private var layoutState = "circle"
    private var nodeIndicatorState = true
    private var edgeColoringState = true

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
        val infoBtn = binding.infoBtn
        val styleBtn = binding.styleBtn

        webView.settings.javaScriptEnabled = true

        webView.loadUrl("file:///android_asset/index.html")

        binding.webView.performClick()

        reloadBtn.setOnClickListener {
            // set the default values
            nodeCount = 0
            usedNodes = mutableListOf<Int>()
            edgeMatrix = arrayOf<Array<Int>>()
            spell = JSONObject()
            elements = mutableListOf<JSONObject>()

            calculateNodeCount()
            addNodes()

            for (addedSpells in activity.spells) {
                spell = addedSpells
                createEmptyMatrix()
                loadLevelMatrix()
                loadFormMatrix()
                loadEffectMatrix()
                addEdges()
            }

            deleteUnnecessaryNodes()

            val elementsStr = elements.toString()

            val layoutJSON = JSONObject()
            layoutJSON.put("name", layoutState)
            val layoutStr = layoutJSON.toString()

            Log.i("elements", elementsStr)

            val script = """
                show(${elementsStr}, ${layoutStr});
            """
            webView.evaluateJavascript(script, null)
        }

        infoBtn.setOnClickListener {
            val info = binding.info
            if (info.visibility == View.GONE) {
                info.visibility = View.VISIBLE
            } else {
                info.visibility = View.GONE
            }
        }

        styleBtn.setOnClickListener {
            // starts the style dialog
            showDialog()
        }

        return root
    }

    private fun deleteUnnecessaryNodes() {
        // delete the nodes that are not connected to any other node
        val toDelete = mutableListOf<Int>()
        for (i in 0 until nodeCount) {
            if (!usedNodes.contains(i + 1)) {
                toDelete.add(i + 1)
            }
        }
        Log.i("selected to delete", toDelete.toString())
        for (i in toDelete) {
            for (j in elements.indices) {
                if (elements[j].getJSONObject("data").has("id") && elements[j].getJSONObject("data").getInt("id") == i) {
                    Log.i("selected elements", "deleted")
                    elements.removeAt(j)
                    break
                }
            }
        }
    }

    private fun addEdges() {
        Log.i("edgeMatrix", edgeMatrix.contentDeepToString())
        // if the matrix is [[0, 1, 1, 1], [0, 0, 1, 1], [0, 0, 0, 1]]
        // then this represents the following:
        // the row indicates the step, so the first row is: 1->2, 2->3, 3->4
        // the second row is: 1->3, 2->4
        // the third row is: 1->4
        // so the edges are: 1->2, 1->3, 1->4, 2->3, 2->4, 3->4
        // the edges must be added from {i+1} to {(node+step)%n+1}

        for (i in edgeMatrix.indices) {
            edgeMatrix[i].reverse()
            Log.i("edgeMatrix", i.toString())
            for (j in edgeMatrix[i].indices) {
                if (edgeMatrix[i][j] == 1) {
                    Log.i("edgeMatrix", j.toString())
                    Log.i("edgeMatrix", edgeMatrix[i].contentToString())
                    val edge = JSONObject("{data: {}}")
                    edge.getJSONObject("data").put("source", j+1)
                    // the target is the next node in the matrix plus the step
                    // so if the step is 2, then the target is 2+1=3
                    edge.getJSONObject("data").put("target", (i+j+1)%nodeCount+1)
                    if (edgeColoringState) {
                        edge.getJSONObject("data").put("label", "edge${i+1}")
                    }else{
                        edge.getJSONObject("data").put("label", "b")
                    }
                    elements.add(edge)
                    // add to used nodes if not already in list
                    if (!usedNodes.contains(j+1)) {
                        usedNodes.add(j+1)
                    }
                    if (!usedNodes.contains((i+j+1)%nodeCount+1)) {
                        usedNodes.add((i+j+1)%nodeCount+1)
                    }
                    Log.i("edgeMatrix", "edge added: ${j+1} -> ${(i+j+1)%nodeCount+1} as edge${i+1}")
                }
            }
        }

        Log.d("elements", elements.toString())
    }

    private fun addNodes() {
        for (i in 0 until nodeCount) {
            val node = JSONObject("{data: {}}")
            node.getJSONObject("data").put("id", i+1)
            elements.add(node)
        }
        Log.d("elements", elements.toString())
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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun showDialog() {
        val dialog: Dialog = Dialog(this.context as MainActivity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_graph_style)


        val layoutSpinner = dialog.findViewById<Spinner>(R.id.layoutSpinner)
        val layoutSpinnerOptions = arrayOf("grid", "random", "circle", "concentric", "breadthfirst", "cose")
        // set the spinner options
        layoutSpinner.adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                layoutSpinnerOptions
            )
        }

        val nodeIndicatorSwitch = dialog.findViewById<Switch>(R.id.nodeIndicatorSwitch)
        val edgeColoringSwitch = dialog.findViewById<Switch>(R.id.edgeColoringSwitch)

        // set defaults
        layoutSpinner.setSelection(layoutSpinnerOptions.indexOf(layoutState))
        nodeIndicatorSwitch.isChecked = nodeIndicatorState
        edgeColoringSwitch.isChecked = edgeColoringState

        // apply button
        val applyBtn = dialog.findViewById<TextView>(R.id.applyBtn)
        applyBtn.setOnClickListener {
            layoutState = layoutSpinner.selectedItem.toString()
            nodeIndicatorState = nodeIndicatorSwitch.isChecked
            edgeColoringState = edgeColoringSwitch.isChecked

            // click reload button
            binding.reloadBtn.performClick()

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}