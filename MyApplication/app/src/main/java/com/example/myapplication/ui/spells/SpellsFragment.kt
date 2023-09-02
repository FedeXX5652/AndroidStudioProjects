package com.example.myapplication.ui.spells

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentSpellsBinding

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
        val spellsViewModel =
            ViewModelProvider(this).get(SpellsViewModel::class.java)

        _binding = FragmentSpellsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val level = binding.levelPicker
        val form = binding.formPicker
        val school = binding.schoolPicker
        val effect = binding.effectPicker

        // set accepted values to level picker
        level.minValue = 0
        level.maxValue = 9

        // add listener to level picker and update spellsViewModel.level and print
        level.setOnValueChangedListener { _, _, newVal ->
            spellsViewModel.level = newVal
            Log.i("SpellsFragment", "level: ${spellsViewModel.level}")
            (activity as MainActivity).count++
            Log.i("SpellsFragment", "count: ${(activity as MainActivity).count}")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}