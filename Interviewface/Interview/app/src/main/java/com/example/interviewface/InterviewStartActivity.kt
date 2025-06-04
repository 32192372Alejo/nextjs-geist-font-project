package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InterviewStartActivity : AppCompatActivity() {

    private lateinit var interviewTypeTextView: TextView
    private lateinit var cameraResponseButton: Button
    private lateinit var textResponseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_start)

        // Inicializar vistas
        interviewTypeTextView = findViewById(R.id.interviewTypeTextView)
        cameraResponseButton = findViewById(R.id.cameraResponseButton)
        textResponseButton = findViewById(R.id.textResponseButton)

        // Obtener datos del intent
        val interviewType = intent.getStringExtra("interviewType") ?: "technical"

        // Mostrar el tipo de entrevista formateado
        val displayText = when (interviewType) {
            // Ciencias de la Salud
            "medicina" -> "MEDICINA"
            "enfermería" -> "ENFERMERÍA"
            "psicología" -> "PSICOLOGÍA"
            "odontología" -> "ODONTOLOGÍA"
            "fisioterapia" -> "FISIOTERAPIA"
            "nutrición" -> "NUTRICIÓN"
            
            // Ingeniería y Tecnología
            "programación_de_software" -> "PROGRAMACIÓN DE SOFTWARE"
            "ingeniería_civil" -> "INGENIERÍA CIVIL"
            "ingeniería_mecánica" -> "INGENIERÍA MECÁNICA"
            "ingeniería_eléctrica" -> "INGENIERÍA ELÉCTRICA"
            "ciencias_de_la_computación" -> "CIENCIAS DE LA COMPUTACIÓN"
            "ciberseguridad" -> "CIBERSEGURIDAD"
            "análisis_de_datos" -> "ANÁLISIS DE DATOS"
            
            // Ciencias Sociales
            "derecho" -> "DERECHO"
            "economía" -> "ECONOMÍA"
            "sociología" -> "SOCIOLOGÍA"
            "ciencias_políticas" -> "CIENCIAS POLÍTICAS"
            "trabajo_social" -> "TRABAJO SOCIAL"
            "antropología" -> "ANTROPOLOGÍA"
            
            // Negocios y Administración
            "administración_de_empresas" -> "ADMINISTRACIÓN DE EMPRESAS"
            "marketing" -> "MARKETING"
            "finanzas" -> "FINANZAS"
            "recursos_humanos" -> "RECURSOS HUMANOS"
            "contabilidad" -> "CONTABILIDAD"
            "comercio_internacional" -> "COMERCIO INTERNACIONAL"
            
            // Arte y Humanidades
            "diseño_gráfico" -> "DISEÑO GRÁFICO"
            "arquitectura" -> "ARQUITECTURA"
            "literatura" -> "LITERATURA"
            "historia_del_arte" -> "HISTORIA DEL ARTE"
            "música" -> "MÚSICA"
            "comunicación_audiovisual" -> "COMUNICACIÓN AUDIOVISUAL"
            
            // Mantener compatibilidad con tipos antiguos
            "technical" -> "ENTREVISTA TÉCNICA"
            "sales" -> "VENTAS"
            "data_analysis" -> "ANÁLISIS DE DATOS"
            "product_manager" -> "GERENTE DE PRODUCTO"
            "software_engineer" -> "INGENIERÍA DE SOFTWARE"
            "product_designer" -> "DISEÑADOR DE PRODUCTOS"
            
            // Si no coincide con ninguno, mostrar el tipo formateado
            else -> interviewType.replace("_", " ").uppercase()
        }
        interviewTypeTextView.text = displayText

        // Configurar botón de respuesta con cámara
        cameraResponseButton.setOnClickListener {
            startInterviewActivity("camera", interviewType)
        }

        // Configurar botón de respuesta con texto
        textResponseButton.setOnClickListener {
            startInterviewActivity("text", interviewType)
        }
    }

    private fun startInterviewActivity(responseType: String, interviewType: String) {
        val intent = Intent(this, InterviewActivity::class.java)
        intent.putExtra("responseType", responseType)
        intent.putExtra("interviewType", interviewType)
        startActivity(intent)
    }
}
