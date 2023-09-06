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

            try {
                file.createNewFile()
            } catch (e: Exception) {
                Log.i("file", e.toString())
            }

            // load the saved spells
            val savedSpellsString = file.bufferedReader().use { it.readText() }
            val savedSpellsJSONArray = JSONArray(savedSpellsString)

            Log.i("savedSpellsJSONArray", savedSpellsJSONArray.toString())

            // add the saved spells to the list
            val savedSpellsList = binding.savedSpellsListView
            val savedSpells = mutableListOf<String>()
            for (i in 0 until savedSpellsJSONArray.length()) {
                savedSpells.add("${savedSpellsJSONArray.getJSONObject(i).getString("title")}:    " +
                        savedSpellsJSONArray.getJSONObject(i).getString("description")
                )
            }
            Log.i("savedSpells", savedSpells.toString())
            savedSpellsList.adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                savedSpells
            )

            savedSpellsList.setOnItemClickListener{ _, _, position, _ ->
                val selected = savedSpellsJSONArray.getJSONObject(position)
                saveTitle.setText(selected.getString("title"))
                saveDescription.setText(selected.getString("description"))
                val spellsArray = JSONArray(selected.getString("spells"))
                for (i in 0 until spellsArray.length()) {
                    val spell = spellsArray.getJSONObject(i)
                    activity.spells.add(spell)
                }
            }
            
            savedSpellsList.setOnItemLongClickListener{ _, _, position, _ ->
                val selected = savedSpellsJSONArray.getJSONObject(position)
                val newSavedSpellsJSONArray = JSONArray()
                for (i in 0 until savedSpellsJSONArray.length()) {
                    if (i != position) {
                        newSavedSpellsJSONArray.put(savedSpellsJSONArray.getJSONObject(i))
                    }
                }
                val fileWriter = FileWriter(file)
                fileWriter.write(newSavedSpellsJSONArray.toString())
                fileWriter.close()
                loadButton.performClick()
            }
        }

        saveAccept.setOnClickListener {
            val spells = activity.spells
            if (spells.size > 0 || saveTitle.text.toString() != ""){
                val file = File("${activity.filesDir}/saved.json")

                Log.i("file", file.absolutePath.toString())

                // load the saved spells
                try {
                    file.createNewFile()

                } catch (e: Exception) {
                    Log.i("file", e.toString())
                }
                val savedSpellsString = file.bufferedReader().use { it.readText() }

                try{
                    JSONArray(savedSpellsString)
                } catch (e: Exception) {
                    Log.i("file", e.toString())
                    val fileWriter = FileWriter(file)
                    fileWriter.write("[]")
                    fileWriter.close()
                }

                var savedSpellsJSONArray = JSONArray()

                if (savedSpellsString.isNotEmpty()) {
                    savedSpellsJSONArray = JSONArray(savedSpellsString)
                }

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
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}