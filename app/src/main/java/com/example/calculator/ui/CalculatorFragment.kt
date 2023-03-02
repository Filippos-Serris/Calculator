package com.example.calculator.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.calculator.R
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.viewmodel.CalculatorViewModel

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private val viewModel = CalculatorViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
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

            plusButton.setOnClickListener { printToTextView(getString(R.string.plus)) }
            minusButton.setOnClickListener { printToTextView(getString(R.string.minus)) }
            multiplyButton.setOnClickListener { printToTextView(getString(R.string.multiplication)) }
            divisionButton.setOnClickListener { printToTextView(getString(R.string.division)) }
            dotButton.setOnClickListener { printToTextView(getString(R.string.dot)) }

            equalButton.setOnClickListener {
                if (calculationsText.text.isNotEmpty() && !containLetter(calculationsText.text.toString()) && lastCharIsDigit(
                        calculationsText.text.toString()
                    )
                ) {
                    historyText.text = calculationsText.text
                    calculationsText.text = viewModel.calculate(calculationsText.text.toString())
                }
            }
            clearButton.setOnClickListener { calculationsText.text = null }
            allClearButton.setOnClickListener {
                calculationsText.text = null
                historyText.text = null
            }
            deleteButton.setOnClickListener {
                if (calculationsText.text.isNotEmpty()) {
                    calculationsText.text = calculationsText.text.substring(
                        0,
                        calculationsText.text.length - 1
                    )
                }
            }

            currencyConverter.setOnClickListener {
                findNavController().navigate(R.id.action_calculatorFragment_to_currencyConverterFragment)
            }

        }
    }

    private fun printToTextView(char: String) {

        var newText: String?

        if (binding.calculationsText.text.isNullOrEmpty()) {
            newText = when (char) { // adding 0 before operators when field is blank
                getString(R.string.plus), getString(R.string.minus), getString(R.string.multiplication), getString(
                    R.string.dot
                ) -> {
                    getString(R.string.zero) + char
                }
                getString(R.string.division) -> { // prevent division by 0
                    Toast.makeText(
                        context,
                        getText(R.string.division_by_zero),
                        Toast.LENGTH_LONG
                    ).show()
                    null
                }
                else -> {
                    "${binding.calculationsText.text}$char"
                }
            }
        } else { // prevent consecutive operators
            if (char == getString(R.string.plus) ||
                char == getString(R.string.minus) ||
                char == getString(R.string.multiplication) ||
                char == getString(R.string.division) ||
                char == getString(R.string.dot)
            ) {
                if (binding.calculationsText.text.last().toString() == getString(R.string.plus) ||
                    binding.calculationsText.text.last().toString() == getString(R.string.minus) ||
                    binding.calculationsText.text.last()
                        .toString() == getString(R.string.multiplication) ||
                    binding.calculationsText.text.last()
                        .toString() == getString(R.string.division) ||
                    binding.calculationsText.text.last().toString() == getString(R.string.dot)
                ) {
                    Toast.makeText(
                        context,
                        getText(R.string.consecutive_operators),
                        Toast.LENGTH_LONG
                    ).show()
                    newText = binding.calculationsText.text.toString()
                } else {
                    newText = "${binding.calculationsText.text}${char}"
                }
            } else {
                newText = "${binding.calculationsText.text}$char"
            }
        }
        binding.calculationsText.text = newText
    }

    private fun containLetter(string: String): Boolean {
        for (char in string) {
            if (char !in 'A'..'Z' && char !in 'a'..'z') {
                return false
            }
        }
        return true
    }

    private fun lastCharIsDigit(string: String): Boolean {
        if (string.last().toString() == getString(R.string.plus) ||
            string.last().toString() == getString(R.string.minus) ||
            string.last().toString() == getString(R.string.multiplication) ||
            string.last().toString() == getString(R.string.division) ||
            string.last().toString() == getString(R.string.dot)
        ){
            Toast.makeText(context,getString(R.string.last_char),Toast.LENGTH_SHORT).show()
            return  false
        }else
            return true
    }
}

