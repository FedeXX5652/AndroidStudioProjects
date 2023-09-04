package com.example.myapplication.ui.loadSaveSpells

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentLoadSaveSpellsBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter


class LoadSaveSpellsFragment : Fragment() {

    private var _binding: FragmentLoadSaveSpellsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadSaveSpellsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)

        val saveButton = binding.saveButton
        val loadButton = binding.loadButton

        val saveSpellsLayout = binding.saveSpellsLayout
        val loadSpellsLayout = binding.loadSpellsLayout

        val saveTitle = binding.title
        val saveDescription = binding.description
        val saveAccept = binding.acceptSave

        saveButton.setOnClickListener {
            saveSpellsLayout.visibility = View.VISIBLE
            loadSpellsLayout.visibility = View.GONE
        }

        loadButton.setOnClickListener {
            saveSpellsLayout.visibility = View.GONE
            loadSpellsLayout.visibility = View.VISIBLE

            val file = File("${activity.filesDir}/saved.json")

            // load the saved spells
            val savedSpellsString = file.bufferedReader().use { it.readText() }
            val savedSpellsJSONArray = JSONArray(savedSpellsString)

            Log.i("savedSpellsJSONArray", savedSpellsJSONArray.toString())

            // add the saved spells to the list
            val savedSpellsList = binding.savedSpellsListView
            val savedSpells = mutableListOf<String>()
            for (i in 0 until savedSpellsJSONArray.length()) {
                savedSpells.add(savedSpellsJSONArray.getJSONObject(i).getString("title").toString())
            }
            Log.i("savedSpells", savedSpells.toString())
            savedSpellsList.adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                savedSpells
            )
        }

        saveAccept.setOnClickListener {
            val spells = activity.spells
            val file = File("${activity.filesDir}/saved.json")

            // load the saved spells
            val savedSpellsString = file.bufferedReader().use { it.readText() }
            val savedSpellsJSONArray = JSONArray(savedSpellsString)

            Log.i("savedSpellsJSONArray", savedSpellsJSONArray.toString())

            // add the new spells
            val entry = JSONObject()
            entry.put("title", saveTitle.text.toString())
            entry.put("description", saveDescription.text.toString())
            entry.put("spells", spells)
            savedSpellsJSONArray.put(entry)
            savedSpellsJSONArray.put(entry)

            // open the file
            val fileWriter = FileWriter(file)

            // write the JSONArray to the file
            fileWriter.write(savedSpellsJSONArray.toString())

            // close the file
            fileWriter.close()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}