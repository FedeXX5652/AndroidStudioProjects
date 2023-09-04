package com.example.myapplication.ui.editSpells

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEditSpellsBinding
import org.json.JSONObject

class EditSpellsFragment : Fragment() {

    private var _binding: FragmentEditSpellsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val activity = (activity as MainActivity)

        _binding = FragmentEditSpellsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val config = activity.config
        val spells = activity.spells
        val spellsStringify = spells.map { it.getString("effect") + " " + it.getString("school") + " " + it.getString("form") + " " + it.getString("level") }

        val spellsListView = binding.spellsListView

        spellsListView.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spellsStringify
        )

        spellsListView.setOnItemClickListener { parent, view, position, id ->
            val selectedSpellLevel = spells[position].getString("level")
            val selectedSpellForm = spells[position].getString("form")
            val selectedSpellSchool = spells[position].getString("school")
            val selectedSpellEffect = spells[position].getString("effect")

            var form = JSONObject()
            var effect = JSONObject()

            for (i in 0 until config.getJSONArray("forms").length()){
                if (config.getJSONArray("forms").getJSONObject(i).getString("name") == selectedSpellForm){
                    form = config.getJSONArray("forms").getJSONObject(i)
                    break
                }
            }

            for (i in 0 until config.getJSONObject("effects").getJSONArray(selectedSpellSchool).length()){
                if (config.getJSONObject("effects").getJSONArray(selectedSpellSchool).getJSONObject(i).getString("Efecto") == selectedSpellEffect){
                    effect = config.getJSONObject("effects").getJSONArray(selectedSpellSchool).getJSONObject(i)
                    break
                }
            }

            showDialog(spells, position, selectedSpellLevel.toInt(), form, selectedSpellSchool, effect, spellsListView)
        }

        return root
    }

    private fun showDialog(spells: MutableList<JSONObject>, posSelectedSpell: Int, level: Int,
                           form: JSONObject, school: String, effect: JSONObject, spellsListView: ListView){

        Log.i("showDialog", "showDialog")
        val dialog: Dialog = Dialog(this.context as MainActivity, R.style.DialogStyle)

        dialog.setContentView(R.layout.dialog_edit_spells)

        val deleteButton = dialog.findViewById<TextView>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            Log.i("deleteButton", spells.toString())
            spells.removeAt(posSelectedSpell)
            Log.i("deleteButton", spells.toString())
            dialog.dismiss()
            spellsListView.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                spells.map { it.getString("effect") + " " + it.getString("school") + " " + it.getString("form") + " " + it.getString("level") }
            )
        }

        val levelTextView = dialog.findViewById<TextView>(R.id.levelTextView)
        val formName = dialog.findViewById<TextView>(R.id.formName)
        val formType = dialog.findViewById<TextView>(R.id.formType)
        val formCost = dialog.findViewById<TextView>(R.id.formCost)
        val formDescription = dialog.findViewById<TextView>(R.id.formDescription)
        val effectSchool = dialog.findViewById<TextView>(R.id.effectSchool)
        val effectEffect = dialog.findViewById<TextView>(R.id.effectEffect)
        val effectRule = dialog.findViewById<TextView>(R.id.effectRule)
        val effectParameter = dialog.findViewById<TextView>(R.id.effectParameter)
        val effectCost = dialog.findViewById<TextView>(R.id.effectCost)

        levelTextView.text = level.toString()
        formName.text = "form: ${form.getString("name")}"
        formType.text = "type: ${form.getString("type")}"
        formCost.text = "cost: ${form.getString("cost")}"
        formDescription.text = "Detail: ${form.getString("description")}"
        effectSchool.text = "school: ${school}"
        effectEffect.text = "effect: ${effect.getString("Efecto")}"
        effectRule.text = "rule: ${effect.getString("Regla")}"
        effectParameter.text = "parameter: ${effect.getString("Parámetro")}"
        effectCost.text = "cost: ${effect.getString("Coste")}"

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
