package com.cice.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.pm.ShortcutInfoCompatSaver

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var operando1: Double = 0.0

    private var currentValue =
        "" // Esta variable tendra el valor activo sobre el que estamos trabajando
    private var isFirst: Boolean =
        true // Esta variable nos indica que estamos empezando a escribir un numero

    private var operacionAlmacenada: Int = 0
    private var isFirstOperation: Boolean = true
    private var isFirstValue: Boolean = true
    private var isSecondValue: Boolean = true

    private var isDot: Boolean = false
    private var afterDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var result = findViewById<TextView>(R.id.displayResult)
        fun valorActual(): Double {
            if (currentValue == "") {
                return 0.0
            } else {
                return currentValue.toDouble()
            }
        }

        fun readDigit(tecla: String) {
            if (isFirst) {
                /* Es la primera tecla de un nuevo numero
                    el valor en proceso lo ponemos a nulo para almacenar la primera pulsacion
                    ponemos isFirst a false, porque la siguiente pulsacion ya no será la primera de
                    este numero
                 */
                currentValue = ""
                isDot = false   // reiniciamos el flag de "."
                isFirst = false
                afterDot = false

            }
            /*
            if(isDot){
                // Si ya hemos pulsado en el pasado para este numero el ".", cambiamos el after para no volverlo a poner
                if(afterDot == false){
                    afterDot = true
                }
            }*/
            // por fin procesamos las teclas
            if (currentValue.length < 10) {
                when (tecla) {
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> currentValue += tecla // si es un digito, lo añadimos al final del valor actual
                    "." -> {
                        // si es un . tenemos varios casos, si es el primer digito del numero tenemos que poner el 0 delante, si no, lo añadimos.
                        if (!isDot) {
                            // Es el primer .
                            currentValue = if (currentValue == "") {
                                "0."
                            } else {
                                "$currentValue."
                            }
                            isDot = true // cambio el isDot para no precesarlo en las siguientes

                        }
                    }
                }
            }

            result.setText(currentValue)
        }
        findViewById<Button>(R.id.button_0).setOnClickListener { readDigit("0") }
        findViewById<Button>(R.id.button_1).setOnClickListener { readDigit("1") }
        findViewById<Button>(R.id.button_2).setOnClickListener { readDigit("2") }
        findViewById<Button>(R.id.button_3).setOnClickListener { readDigit("3") }
        findViewById<Button>(R.id.button_4).setOnClickListener { readDigit("4") }
        findViewById<Button>(R.id.button_5).setOnClickListener { readDigit("5") }
        findViewById<Button>(R.id.button_6).setOnClickListener { readDigit("6") }
        findViewById<Button>(R.id.button_7).setOnClickListener { readDigit("7") }
        findViewById<Button>(R.id.button_8).setOnClickListener { readDigit("8") }
        findViewById<Button>(R.id.button_9).setOnClickListener { readDigit("9") }
        findViewById<Button>(R.id.buttonDot).setOnClickListener { readDigit(".") }

        fun calcularOperacion(operacion: Int): Double {

            var operando2: Double = valorActual()

            var resultado: Double = 0.0
            when (operacion) {
                SUMAR -> resultado = operando1 + operando2
                RESTAR -> resultado = operando1 - operando2
                MULTIPLICAR -> resultado = operando1 * operando2
                DIVIDIR -> {
                    if (operando2 == 0.0) {
                        Log.d(TAG, "No se puede dividir por cero")
                        resultado = 0.0
                    } else {
                        resultado = operando1 / operando2
                    }
                }
                NOOP -> {
                    resultado = operando2
                }

            }
            return resultado
        }

        fun performOperation(operation: Int) {
            var resultado: Double = 0.0
            when (operation) {
                CALCULAR -> {
                    if (operacionAlmacenada == NOOP) {
                        resultado = valorActual()
                        operando1 = resultado
                        operacionAlmacenada = NOOP
                    } else {
                        resultado = calcularOperacion(operacionAlmacenada)
                        operando1 = resultado
                        operacionAlmacenada = NOOP
                    }
                    currentValue = resultado.toString()
                    result.setText(currentValue)
                    isFirst = true
                }
                else -> {
                    resultado = calcularOperacion(operacionAlmacenada)
                    operando1 = resultado
                    currentValue = resultado.toString()
                    operacionAlmacenada = operation
                    result.setText(currentValue)
                    isFirst = true
                }
            }
        }

        fun performClear() {
            isDot = false
            afterDot = false
            currentValue = ""
            isFirst = true
            operando1 = 0.0
            operacionAlmacenada = NOOP
            result.setText("0")
        }
        findViewById<Button>(R.id.buttonPlus).setOnClickListener { performOperation(SUMAR) }
        findViewById<Button>(R.id.buttonMenos).setOnClickListener { performOperation(RESTAR) }
        findViewById<Button>(R.id.buttonDiv).setOnClickListener { performOperation(DIVIDIR) }
        findViewById<Button>(R.id.buttonMult).setOnClickListener { performOperation(MULTIPLICAR) }
        findViewById<Button>(R.id.button_equal).setOnClickListener { performOperation(CALCULAR) }

        findViewById<Button>(R.id.button_CE).setOnClickListener { performClear() }


    }

    companion object {
        const val SUMAR = 1
        const val RESTAR = 2
        const val MULTIPLICAR = 3
        const val DIVIDIR = 4
        const val CALCULAR = 5
        const val NOOP = 0
    }
}