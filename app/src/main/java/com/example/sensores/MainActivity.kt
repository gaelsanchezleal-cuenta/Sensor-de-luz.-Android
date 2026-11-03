package com.example.sensores

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(),  SensorEventListener {

    private lateinit var tvValores : TextView
    private lateinit var tvEstado : TextView
    private lateinit var laPantalla : LinearLayout
    private lateinit var tvTitulo : TextView

    //objeto que permite ocupar cualquier sensor
    private lateinit var sensorManager: SensorManager

    //atributo que permite ocupar un sensor en particular
    // el signo de interrogacion para ver si existe
    private var sensorLuz:  Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvEstado = findViewById(R.id.tvEstado)
        tvValores = findViewById(R.id.tvValores)
        laPantalla = findViewById(R.id.laPantalla)
        tvTitulo = findViewById(R.id.tvTitulo)

        //permitir el uso del sensor que se va a ocupar
        //ocupamos el get y adentro para ocupar todoo
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //ocupar el sensor de luz
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        //verificar que el sensor existe
        if (sensorLuz == null){
            tvEstado.text = "No hay sensor de luz :("
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //metodos obligatorios de los sensores

    // metodo para detectar cambios en el sensor
    override fun onSensorChanged(event: SensorEvent?) {

        //evaluamos si el sensor seleccionado es de luz
        if (event?.sensor?.type == Sensor.TYPE_LIGHT){
            //variable que guarde los valores del sensor
            val luz = event.values[0]

            //imprimir el valor del sensor
            tvValores.text = "Luz es igual a $luz lx"

            //verificar la cantidad de luz en el ambiente
            if (luz < 50){
                tvEstado.text = "Ambiente oscuro :)"

                //cambiamos colores
                laPantalla.setBackgroundColor(Color.BLACK)
                tvTitulo.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvEstado.setTextColor(Color.WHITE)

            } else if (luz > 50) {
                tvEstado.text = "Ambiente iluminado"

                //cambiamos colores
                laPantalla.setBackgroundColor(Color.WHITE)
                tvTitulo.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
                tvEstado.setTextColor(Color.BLACK)
            }
        }
    }

    //metodo para verificar los cambios en valores matematicos
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    //metodo para la deteccion del evento
    override fun onResume() {
        super.onResume()

        //si la aplicacion se quita, entonces se deja de usar el sensor
        sensorLuz?.let {
            sensorManager.registerListener(
                this,it, SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}