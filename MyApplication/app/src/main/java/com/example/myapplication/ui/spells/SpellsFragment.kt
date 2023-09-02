package com.example.myapplication.ui.spells

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSpellsBinding
import org.json.JSONObject

class SpellsFragment : Fragment() {

    private var _binding: FragmentSpellsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = (activity as MainActivity)

        _binding = FragmentSpellsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val level = binding.levelPicker
        val form = binding.formPicker
        val school = binding.schoolPicker
        val effect = binding.effectPicker
        val addBtn = binding.addBtn

        level.minValue = 1
        level.maxValue = activity.config.getInt("max_lvl")
        level.value = 1
        level.setOnValueChangedListener { _, _, newVal ->
            activity.level = newVal
        }


        val formNames = activity.config.getJSONArray("forms")
        val formNamesList = mutableListOf<String>()
        for (i in 0 until formNames.length()) {
            formNamesList.add(formNames.getJSONObject(i).getString("name"))
        }
        form.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            formNamesList
        )


        form.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val formName = formNames.getJSONObject(pos).getString("name")
                val formDesc = formNames.getJSONObject(pos).getString("description")
                val formType = formNames.getJSONObject(pos).getString("type")
                val formCost = formNames.getJSONObject(pos).getString("cost")
                val formDescView = root.findViewById<TextView>(R.id.formDetail)
                activity.form = formName
                formDescView.text = "$formName\n$formDesc\n$formType\ncost: $formCost"

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val formName = formNames.getJSONObject(0).getString("name")
                val formDesc = formNames.getJSONObject(0).getString("description")
                val formType = formNames.getJSONObject(0).getString("type")
                val formCost = formNames.getJSONObject(0).getString("cost")
                val formDescView = root.findViewById<TextView>(R.id.formDetail)
                activity.form = formName
                formDescView.text = "$formName\n$formDesc\n$formType\ncost: $formCost"
            }
        }


        val schools = activity.config.getJSONObject("effects").keys()
        val schoolsList = mutableListOf<String>()
        for (school in schools) {
            schoolsList.add(school)
        }
        school.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            schoolsList
        )


        school.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                pos: Int,
                id: Long
            ) {
                activity.school = schoolsList[pos]
                val effects = activity.config.getJSONObject("effects").getJSONArray(schoolsList[pos])
                val effectsList = mutableListOf<String>()
                for (i in 0 until effects.length()) {
                    effectsList.add(effects.getJSONObject(i).getString("Efecto"))
                }
                effect.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    effectsList
                )
                val effect = effects.getJSONObject(0).getString("Efecto")
                val effectRegla = effects.getJSONObject(0).getString("Regla")
                val effectParam = effects.getJSONObject(0).getString("Parámetro")
                val effectCoste = effects.getJSONObject(0).getString("Coste")
                val effectDescView = root.findViewById<TextView>(R.id.effectDetail)
                effectDescView.text = "$effect\n$effectRegla\n$effectParam\ncost: $effectCoste"
                activity.effect = effect
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // set to first item
                activity.school = schoolsList[0]
                val effects = activity.config.getJSONObject("effects").getJSONArray(schoolsList[0])
                val effectsList = mutableListOf<String>()
                for (i in 0 until effects.length()) {
                    effectsList.add(effects.getJSONObject(i).getString("Efecto"))
                }
                effect.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    effectsList
                )
                val effect = effects.getJSONObject(0).getString("Efecto")
                val effectRegla = effects.getJSONObject(0).getString("Regla")
                val effectParam = effects.getJSONObject(0).getString("Parámetro")
                val effectCoste = effects.getJSONObject(0).getString("Coste")
                val effectDescView = root.findViewById<TextView>(R.id.effectDetail)
                effectDescView.text = "$effect\n$effectRegla\n$effectParam\ncost: $effectCoste"
                activity.effect = effect
            }
        }


        effect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val effect = activity.config.getJSONObject("effects").getJSONArray(activity.school)
                    .getJSONObject(pos).getString("Efecto")
                val effectRegla =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(pos).getString("Regla")
                val effectParam =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(pos).getString("Parámetro")
                val effectCoste =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(pos).getString("Coste")
                val effectDescView = root.findViewById<TextView>(R.id.effectDetail)
                effectDescView.text = "$effect\n$effectRegla\n$effectParam\ncost: $effectCoste"
                activity.effect = effect
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val effect = activity.config.getJSONObject("effects").getJSONArray(activity.school)
                    .getJSONObject(0).getString("Efecto")
                val effectRegla =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(0).getString("Regla")
                val effectParam =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(0).getString("Parámetro")
                val effectCoste =
                    activity.config.getJSONObject("effects").getJSONArray(activity.school)
                        .getJSONObject(0).getString("Coste")
                val effectDescView = root.findViewById<TextView>(R.id.effectDetail)
                effectDescView.text = "$effect\n$effectRegla\n$effectParam\ncost: $effectCoste"
                activity.effect = effect
            }
        }

        addBtn.setOnClickListener {
            val spell = JSONObject()
            spell.put("level", activity.level)
            spell.put("form", activity.form)
            spell.put("school", activity.school)
            spell.put("effect", activity.effect)
            activity.spells.add(spell)
            Log.i("SpellsFragment", activity.spells.toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}