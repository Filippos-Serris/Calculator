package com.example.calculator.viewmodel


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.network.CurrencyConverterApi
import com.example.calculator.network.models.AvailableCurrencies
import com.example.calculator.network.models.Conversion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculatorViewModel : ViewModel() {
    private val expressionList = mutableListOf<String?>()

    private val _listOfAvailableCurrencies = MutableLiveData<List<Currency>>()
    val listOfAvailableCurrencies: LiveData<List<Currency>> get() = _listOfAvailableCurrencies

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun calculate(expression: String): String {
        var index = 0
        var before: Double
        var after: Double

        splitExpression(expression)

        // calculating multiplication and division operations first
        for (part in expressionList) {
            if (part == "*" || part == "/") {
                // number before multiplication/division symbol
                index = expressionList.indexOf(part)
                while (expressionList[index - 1] == null) {
                    index--
                }
                before = expressionList[index - 1]!!.toDouble()
                expressionList[index - 1] = null

                // number after multiplication/division symbol
                index = expressionList.indexOf(part)
                while (expressionList[index + 1] == null) {
                    index++
                }
                after = expressionList[index + 1]!!.toDouble()
                expressionList[index + 1] = null

                expressionList[expressionList.indexOf(part)] =
                    calculateSubExpression(before, after, part)
            }
        }

        // calculate the plus and minus operations next
        for (part in expressionList) {
            if (part == "+" || part == "-") {
                // number before plus/minus symbol
                index = expressionList.indexOf(part)
                while (expressionList[index - 1] == null) {
                    index--
                }
                before = expressionList[index - 1]!!.toDouble()
                expressionList[index - 1] = null

                // number after plus/minus symbol
                index = expressionList.indexOf(part)
                while (expressionList[index + 1] == null) {
                    index++
                }
                after = expressionList[index + 1]!!.toDouble()
                expressionList[index + 1] = null

                expressionList[expressionList.indexOf(part)] =
                    calculateSubExpression(before, after, part)
            }
        }

        for (result in expressionList) {
            if (expressionList[expressionList.indexOf(result)] != null) {
                val answer = expressionList[expressionList.indexOf(result)]!!
                expressionList.clear()
                return answer
            }
        }
        expressionList.clear()
        return "Error"
    }

    private fun splitExpression(expression: String) {
        var x = "0"
        var i = 0

        // split numbers and operators and store the to list
        while (i < expression.length) {
            while (i < expression.length && (expression[i].toString() == "0" ||
                        expression[i].toString() == "1" ||
                        expression[i].toString() == "2" ||
                        expression[i].toString() == "3" ||
                        expression[i].toString() == "4" ||
                        expression[i].toString() == "5" ||
                        expression[i].toString() == "6" ||
                        expression[i].toString() == "7" ||
                        expression[i].toString() == "8" ||
                        expression[i].toString() == "9" ||
                        expression[i].toString() == ".")
            ) {
                x = "$x${expression[i]}"
                i++
            }
            expressionList.add(x)
            Log.d("success", "$x")
            x = "0"

            while (i < expression.length && (expression[i].toString() == "+" ||
                        expression[i].toString() == "-" ||
                        expression[i].toString() == "*" ||
                        expression[i].toString() == "/")
            ) {
                expressionList.add(expression[i].toString())
                Log.d("success", "${expression[i]}")
                i++
            }
        }
    }

    private fun calculateSubExpression(num1: Double, num2: Double, operator: String): String {
        val result = when (operator) {
            "+" -> {
                num1 + num2
            }
            "-" -> {
                num1 - num2
            }
            "*" -> {
                num1 * num2
            }
            "/" -> {
                num1 / num2
            }
            else -> 0.0
        }
        Log.d("success", "$result")
        return result.toString()
    }

    fun getAvailableCurrencies() {
        viewModelScope.launch {
            try {
                val currencies =
                    CurrencyConverterApi.retrofitService.getAvailableCurrencies()
                currencyListBuilder(currencies)
            } catch (e: Exception) {
                Log.d("Error in getAvailableCurrencies", "Fail: ${e.message}")
            }
        }
    }

    private fun currencyListBuilder(currencies: AvailableCurrencies) {
        var list = mutableListOf<Currency>()

        if (currencies.symbols.AUD.isNotEmpty()) {
            list.add(Currency("AUD", currencies.symbols.AUD))
        }
        if (currencies.symbols.BGN.isNotEmpty()) {
            list.add(Currency("BNG", currencies.symbols.BGN))
        }
        if (currencies.symbols.BTC.isNotEmpty()) {
            list.add(Currency("BTC", currencies.symbols.BTC))
        }
        if (currencies.symbols.CNY.isNotEmpty()) {
            list.add(Currency("CNY", currencies.symbols.CNY))
        }
        if (currencies.symbols.CLP.isNotEmpty()) {
            list.add(Currency("CLP", currencies.symbols.CLP))
        }
        if (currencies.symbols.CUP.isNotEmpty()) {
            list.add(Currency("CUP", currencies.symbols.CUP))
        }
        if (currencies.symbols.EGP.isNotEmpty()) {
            list.add(Currency("EGP", currencies.symbols.EGP))
        }
        if (currencies.symbols.EUR.isNotEmpty()) {
            list.add(Currency("EUR", currencies.symbols.EUR))
        }
        if (currencies.symbols.HKD.isNotEmpty()) {
            list.add(Currency("HKD", currencies.symbols.HKD))
        }
        if (currencies.symbols.KPW.isNotEmpty()) {
            list.add(Currency("KPW", currencies.symbols.KPW))
        }
        if (currencies.symbols.KRW.isNotEmpty()) {
            list.add(Currency("KRW", currencies.symbols.KRW))
        }
        if (currencies.symbols.MXN.isNotEmpty()) {
            list.add(Currency("MXN", currencies.symbols.MXN))
        }
        if (currencies.symbols.RUB.isNotEmpty()) {
            list.add(Currency("RUB", currencies.symbols.RUB))
        }
        if (currencies.symbols.TRY.isNotEmpty()) {
            list.add(Currency("TRY", currencies.symbols.TRY))
        }
        if (currencies.symbols.USD.isNotEmpty()) {
            list.add(Currency("USD", currencies.symbols.USD))
        }
        if (currencies.symbols.XAU.isNotEmpty()) {
            list.add(Currency("XAU", currencies.symbols.XAU))
        }

        _listOfAvailableCurrencies.value = list
    }

    fun getCurrencyConversion(amount: String, from: String, to: String) {
        viewModelScope.launch {
            try {
                val conversion =
                    CurrencyConverterApi.retrofitService.getCurrencyConversion(amount, from, to)
                _result.value = conversion.result.toString()
            } catch (e: Exception) {
                Log.d("Error in getCurrencyConversion", "Fail: ${e.message}")
            }
        }
    }

    data class Currency(val acronym: String, val description: String) {
        override fun toString(): String = "$acronym: $description"
    }
}