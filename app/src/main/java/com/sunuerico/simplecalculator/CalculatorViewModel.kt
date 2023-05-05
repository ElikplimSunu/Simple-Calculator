package com.sunuerico.simplecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> calculate()
        }
    }

    private fun delete() {
        when {
            state.secondNumber.isNotBlank() -> {
                state = state.copy(secondNumber = state.secondNumber.dropLast(1))
            }
            state.operation != null -> {
                state = state.copy(operation = null)
            }
            state.firstNumber.isNotBlank() -> {
                state = state.copy(firstNumber = state.firstNumber.dropLast(1))
            }
        }
    }

    private fun calculate() {
        val firstNumber = state.firstNumber.toDoubleOrNull()
        val secondNumber = state.secondNumber.toDoubleOrNull()

        val result = when (state.operation){
            is CalculatorOperation.Add -> firstNumber?.plus(secondNumber!!)
            is CalculatorOperation.Subtract -> firstNumber?.minus(secondNumber ?: 0.0)
            is CalculatorOperation.Multiply -> firstNumber?.times(secondNumber ?: 0.0)
            is CalculatorOperation.Divide -> firstNumber?.div(secondNumber ?: 0.0)
            null -> return
        }
            state = state.copy(firstNumber = result.toString().take(15), secondNumber = "", operation = null)

    }

    private fun enterDecimal() {
        if (state.operation == null && !state.firstNumber.contains(".") && state.firstNumber.isNotBlank()){
            state = state.copy(firstNumber = state.firstNumber + ".")
            return
        }
        if (!state.secondNumber.contains(".") && state.secondNumber.isNotBlank()){
            state = state.copy(secondNumber = state.secondNumber + ".")
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.firstNumber.isNotBlank()){
            state = state.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null){
           if (state.firstNumber.length >= MAX_NUMBER_LENGTH){
                return
           }
            state = state.copy(firstNumber = state.firstNumber + number)
            return
        }
        if (state.secondNumber.length >= MAX_NUMBER_LENGTH){
            return
        }
        state = state.copy(secondNumber = state.secondNumber + number)
    }

    companion object {
        private const val MAX_NUMBER_LENGTH = 8
    }
}