package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.text.isNotEmpty
import kotlin.text.startsWith
import kotlin.text.substring
import kotlin.text.toDoubleOrNull

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Controls
    private lateinit var btnClear: Button
    private lateinit var btnSign: Button
    private lateinit var btnPercent: Button

    //Operators
    private lateinit var btnAdd: Button
    private lateinit var btnSub: Button
    private lateinit var btnMul: Button
    private lateinit var btnDiv: Button
    private lateinit var btnEqual: Button

    //Values
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btn0: Button
    private lateinit var btnDecimal: Button

    //Result
    private lateinit var resultTv: TextView

    //Input States
    private var currentInput: String = ""
    private var firstOperand: Double? = null
    private var pendingOperation: String? = null
    private var justCalculated: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Controls
        btnClear = findViewById(R.id.btn_clear)
        btnSign = findViewById(R.id.btn_sign)
        btnPercent = findViewById(R.id.btn_percent)

        //Operators
        btnAdd = findViewById(R.id.btn_add)
        btnSub = findViewById(R.id.btn_sub)
        btnMul = findViewById(R.id.btn_mul)
        btnDiv = findViewById(R.id.btn_div)
        btnEqual = findViewById(R.id.btn_equals)

        //Values
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        btn3 = findViewById(R.id.btn_3)
        btn4 = findViewById(R.id.btn_4)
        btn5 = findViewById(R.id.btn_5)
        btn6 = findViewById(R.id.btn_6)
        btn7 = findViewById(R.id.btn_7)
        btn8 = findViewById(R.id.btn_8)
        btn9 = findViewById(R.id.btn_9)
        btn0 = findViewById(R.id.btn_0)
        btnDecimal = findViewById(R.id.btn_decimal)

        //Result
        resultTv = findViewById(R.id.result)
        resultTv.text = "0"

        //Control Listeners
        btnClear.setOnClickListener(this)
        btnSign.setOnClickListener(this)
        btnPercent.setOnClickListener(this)

        //Operator Listeners
        btnAdd.setOnClickListener(this)
        btnSub.setOnClickListener(this)
        btnMul.setOnClickListener(this)
        btnDiv.setOnClickListener(this)
        btnEqual.setOnClickListener(this)

        //Value Listeners
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btn0.setOnClickListener(this)
        btnDecimal.setOnClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_0 -> appendNumber("0")
            R.id.btn_1 -> appendNumber("1")
            R.id.btn_2 -> appendNumber("2")
            R.id.btn_3 -> appendNumber("3")
            R.id.btn_4 -> appendNumber("4")
            R.id.btn_5 -> appendNumber("5")
            R.id.btn_6 -> appendNumber("6")
            R.id.btn_7 -> appendNumber("7")
            R.id.btn_8 -> appendNumber("8")
            R.id.btn_9 -> appendNumber("9")
            R.id.btn_decimal -> appendDecimal()

            // Operator Buttons
            R.id.btn_add -> handleOperation("+")
            R.id.btn_sub -> handleOperation("-")
            R.id.btn_mul -> handleOperation("*")
            R.id.btn_div -> handleOperation("/")

            // Equals Button
            R.id.btn_equals -> calculateResult()

            // Control Buttons
            R.id.btn_clear -> clearAll()
            R.id.btn_sign -> toggleSign()
            R.id.btn_percent -> calculatePercentage()
        }
    }

    private fun appendNumber(number: String) {
        currentInput += number

        if (resultTv.text.toString() == "0" || justCalculated) {
            resultTv.text = number
            justCalculated = false
        } else {
            // Append to existing display
            resultTv.text = "${resultTv.text}$number"
        }
    }

    private fun appendDecimal() {
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) {
                currentInput = "0."
                if (resultTv.text.toString() == "0" || justCalculated) {
                    resultTv.text = "0."
                    justCalculated = false
                } else {
                    resultTv.text = "${resultTv.text}0."
                }
            } else {
                currentInput += "."
                resultTv.text = "${resultTv.text}."
            }
        }
    }

    private fun handleOperation(operation: String) {
        if (currentInput.isNotEmpty()) {
            if (firstOperand != null && pendingOperation != null) {
                calculateIntermediateResult()
            } else {
                firstOperand = currentInput.toDoubleOrNull()
            }
        }

        pendingOperation = operation

        val currentDisplay = resultTv.text.toString()
        if (!currentDisplay.endsWith(" ")) {
            resultTv.text = "$currentDisplay $operation "
        } else {
            val displayWithoutLastOp = currentDisplay.dropLastWhile { it == ' ' || it in "+-*/" }
            resultTv.text = "$displayWithoutLastOp $operation "
        }

        currentInput = ""
        justCalculated = false
    }


    private fun calculateIntermediateResult() {
        if (firstOperand != null && pendingOperation != null && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toDoubleOrNull()
            if (secondOperand != null) {
                var resultValue = 0.0
                when (pendingOperation) {
                    "+" -> resultValue = firstOperand!! + secondOperand
                    "-" -> resultValue = firstOperand!! - secondOperand
                    "*" -> resultValue = firstOperand!! * secondOperand
                    "/" -> {
                        if (secondOperand == 0.0) {
                            resultTv.text = "Error"
                            clearAll(resetDisplay = false)
                            return
                        }
                        resultValue = firstOperand!! / secondOperand
                    }
                }
                firstOperand = resultValue
            }
        }
    }

    private fun calculateResult() {
        if (firstOperand != null && pendingOperation != null && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toDoubleOrNull()
            if (secondOperand != null) {
                var resultValue = 0.0
                when (pendingOperation) {
                    "+" -> resultValue = firstOperand!! + secondOperand
                    "-" -> resultValue = firstOperand!! - secondOperand
                    "*" -> resultValue = firstOperand!! * secondOperand
                    "/" -> {
                        if (secondOperand == 0.0) {
                            resultTv.text = "Error"
                            clearAll(resetDisplay = false)
                            return
                        }
                        resultValue = firstOperand!! / secondOperand
                    }
                }
                resultTv.text = formatResult(resultValue)
                firstOperand = resultValue
                pendingOperation = null
                currentInput = ""
                justCalculated = true
            }
        }
    }

    private fun formatResult(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }

    private fun clearAll(resetDisplay: Boolean = true) {
        currentInput = ""
        firstOperand = null
        pendingOperation = null
        justCalculated = false
        if (resetDisplay) {
            resultTv.text = "0"
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            resultTv.text = currentInput
        } else if (firstOperand != null) {
            val currentValue = resultTv.text.toString().toDoubleOrNull()
            if (currentValue != null) {
                val toggledValue = currentValue * -1
                resultTv.text = formatResult(toggledValue)
                currentInput = resultTv.text.toString()
                firstOperand = toggledValue
            }
        }
    }

    private fun calculatePercentage() {
        if (currentInput.isNotEmpty()) {
            val currentValue = currentInput.toDoubleOrNull()
            if (currentValue != null) {
                if (firstOperand != null && pendingOperation != null) {
                    val percentageValue = firstOperand!! * (currentValue / 100.0)
                    currentInput = formatResult(percentageValue)
                    resultTv.text = currentInput
                } else {
                    val percentageValue = currentValue / 100.0
                    currentInput = formatResult(percentageValue)
                    resultTv.text = currentInput
                    firstOperand = percentageValue
                }
            }
        }
    }
}