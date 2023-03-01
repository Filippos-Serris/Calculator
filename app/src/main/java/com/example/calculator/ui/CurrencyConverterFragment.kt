package com.example.calculator.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.LiveData
import com.example.calculator.R
import com.example.calculator.databinding.FragmentCurrencyConverterBinding
import com.example.calculator.network.models.AvailableCurrencies
import com.example.calculator.viewmodel.CalculatorViewModel

class CurrencyConverterFragment : Fragment() {

    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!

    private val viewModel = CalculatorViewModel()

    init {
        viewModel.getAvailableCurrencies()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            zeroButton.setOnClickListener { printToTextView(getString(R.string.zero)) }
            oneButton.setOnClickListener { printToTextView(getString(R.string.one)) }
            twoButton.setOnClickListener { printToTextView(getString(R.string.two)) }
            threeButton.setOnClickListener { printToTextView(getString(R.string.three)) }
            fourButton.setOnClickListener { printToTextView(getString(R.string.four)) }
            fiveButton.setOnClickListener { printToTextView(getString(R.string.five)) }
            sixButton.setOnClickListener { printToTextView(getString(R.string.six)) }
            sevenButton.setOnClickListener { printToTextView(getString(R.string.seven)) }
            eightButton.setOnClickListener { printToTextView(getString(R.string.eight)) }
            nineButton.setOnClickListener { printToTextView(getString(R.string.nine)) }

            dotButton.setOnClickListener { printToTextView(getString(R.string.dot)) }
            deleteButton.setOnClickListener {
                if (fromText.text.isNotEmpty()) {
                    fromText.text = fromText.text.substring(0, fromText.text.length - 1)
                }
            }

            convert.setOnClickListener {
                viewModel.getCurrencyConversion(
                    fromText.text.toString(),
                    viewModel.listOfAvailableCurrencies.value!![currencyOptionFrom.selectedItemId.toInt()].acronym,
                    viewModel.listOfAvailableCurrencies.value!![currencyOptionTo.selectedItemId.toInt()].acronym
                )
            }

            viewModel.result.observe(
                viewLifecycleOwner,
                { newResult -> binding.toText.text = newResult })

            viewModel.listOfAvailableCurrencies.observe(viewLifecycleOwner,
                { spinnerList ->
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        spinnerList
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        binding.currencyOptionFrom.adapter = adapter
                        binding.currencyOptionTo.adapter = adapter
                    }
                })
        }
    }


    private fun printToTextView(char: String) {
        var newText: String? = ""

        if (binding.fromText.text.isNullOrEmpty()) {
            newText = if (char == getString(R.string.dot)) {
                getString(R.string.zero) + char
            } else
                char
        } else {
            if (char == getString(R.string.dot) && binding.fromText.text.last()
                    .toString() == getString(R.string.dot)
            ) {
                Toast.makeText(
                    context,
                    getText(R.string.consecutive_operators),
                    Toast.LENGTH_LONG
                ).show()
                newText = binding.fromText.text.toString()
            } else
                newText = "${binding.fromText.text}$char"
        }
        binding.fromText.text = newText
    }


}
