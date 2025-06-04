package com.example.interviewface

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
// import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.os.Handler
import android.os.Looper
import java.util.concurrent.TimeUnit
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.example.interviewface.model.InterviewResult
import com.example.interviewface.repository.AppDatabase

class InterviewActivity : BaseActivity() {

    companion object {
        private const val TAG = "InterviewActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    private lateinit var questionTextView: TextView
    private lateinit var cameraContainer: FrameLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var textResponseEditText: EditText
    private lateinit var recordButton: Button
    private lateinit var continueButton: Button
    private lateinit var nextQuestionButton: Button
    // Elementos eliminados del layout
    // private lateinit var backButton: ImageButton
    // private lateinit var homeButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private lateinit var progressPercentageTextView: TextView
    private lateinit var chronometer: TextView
    
    // Variables para el cronómetro
    private var chronometerStartTime: Long = 0
    private var chronometerHandler = Handler(Looper.getMainLooper())
    private var chronometerRunnable: Runnable? = null
    private var maxInterviewTime: Int = 60 // Tiempo máximo en segundos (por defecto)
    
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    private var responseType: String = "text"
    private var interviewType: String = ""
    private var currentQuestionIndex: Int = 0
    private var isRecording: Boolean = false

    // Lista de preguntas según el tipo de entrevista
    
    // Preguntas para psicología
    private val technicalQuestions = listOf(
        "¿Cuál ha sido su caso clínico más desafiante y cómo lo resolvió?",
        "¿Qué estrategias utiliza cuando un paciente muestra resistencia al tratamiento?",
        "¿Cuáles son los límites éticos más importantes en su práctica profesional?",
        "¿Cómo sabe si su intervención terapéutica está funcionando?",
        "¿Qué enfoque terapéutico prefiere y por qué?"
    )
    
    // Preguntas para ventas
    private val salesQuestions = listOf(
        "¿Qué te gusta de trabajar en ventas?",
        "¿Cómo te presentas a un cliente nuevo?",
        "¿Qué haces cuando un cliente dice que no?",
        "¿Cuál ha sido tu mejor experiencia vendiendo algo?",
        "¿Cómo te llevas con diferentes tipos de clientes?"
    )
    
    // Preguntas para gerente de producto
    private val productManagerQuestions = listOf(
        "¿Qué te gusta de ser gerente de producto?",
        "¿Cómo decides qué es lo más importante para un producto?",
        "¿Cómo trabajas con los equipos de diseño y programación?",
        "¿Cómo sabes si un producto está teniendo éxito?",
        "¿Cuál es tu producto favorito y por qué?"
    )
    
    // Preguntas para ingeniero de software
    private val softwareEngineerQuestions = listOf(
        "¿Por qué te gusta ser ingeniero de software?",
        "¿Qué lenguajes de programación conoces mejor?",
        "¿Cómo trabajas en equipo con otros programadores?",
        "¿Cuál es tu proyecto favorito en el que has trabajado?",
        "¿Cómo aprendes sobre nuevas tecnologías?"
    )
    
    // Preguntas para diseñador de producto
    private val productDesignerQuestions = listOf(
        "¿Qué te inspira cuando diseñas?",
        "¿Qué herramientas de diseño usas más?",
        "¿Cómo haces que tus diseños sean fáciles de usar?",
        "¿Cuál es tu diseño favorito que has creado?",
        "¿Cómo descubres lo que les gusta a los usuarios?"
    )
    
    // Preguntas para marketing
    private val marketingQuestions = listOf(
        "¿Qué redes sociales usas para marketing?",
        "¿Cómo sabes si una campaña de marketing funciona bien?",
        "¿Qué tipo de contenido crees que atrae más a la gente?",
        "¿Cómo cambias tus mensajes para diferentes tipos de público?",
        "¿Cuál es tu campaña favorita que hayas creado?"
    )
    
    // Preguntas para análisis de datos
    private val dataAnalysisQuestions = listOf(
        "¿Qué programas usas para analizar datos?",
        "¿Qué te gusta más de trabajar con datos?",
        "¿Cómo organizas la información cuando está desordenada?",
        "¿Cómo explicas tus resultados a personas que no son expertas?",
        "¿Cuál ha sido tu proyecto favorito de análisis de datos?"
    )

    // Preguntas generales para otras carreras
    private val generalQuestions = listOf(
        "¿Qué es lo que más te gusta de tu trabajo?",
        "¿En qué crees que eres bueno?",
        "¿Qué te gustaría mejorar de ti mismo?",
        "¿Qué te gustaría estar haciendo en unos años?",
        "¿Por qué crees que eres bueno para este trabajo?"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        // Obtener el tipo de entrevista del intent
        interviewType = intent.getStringExtra("interviewType") ?: "general"
        responseType = intent.getStringExtra("responseType") ?: "text"

        // Inicializar vistas
        questionTextView = findViewById(R.id.questionTextView)
        cameraContainer = findViewById(R.id.cameraContainer)
        viewFinder = findViewById(R.id.viewFinder)
        textResponseEditText = findViewById(R.id.textResponseEditText)
        recordButton = findViewById(R.id.recordButton)
        continueButton = findViewById(R.id.continueButton)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        progressBar = findViewById(R.id.progressBar)
        progressTextView = findViewById(R.id.progressTextView)
        progressPercentageTextView = findViewById(R.id.progressPercentageTextView)
        chronometer = findViewById(R.id.chronometer)
        
        // Configurar el tiempo máximo según el tipo de entrevista
        setMaxInterviewTime()

        // Mostrar la primera pregunta
        showQuestion(0)

        // Añadimos un listener al botón atrás del sistema para finalizar la actividad
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        // Configurar botón de continuar para respuestas de texto
        continueButton.setOnClickListener {
            // Guardar la respuesta de texto (en una aplicación real)
            val response = textResponseEditText.text.toString()
            if (response.isNotEmpty()) {
                // Avanzar a la siguiente pregunta o finalizar
                if (currentQuestionIndex < getQuestions().size - 1) {
                    showQuestion(currentQuestionIndex + 1)
                    textResponseEditText.text.clear()
                } else {
                    navigateToResults()
                }
            }
        }

        // Configurar botón de grabación
        recordButton.setOnClickListener {
            if (isRecording) {
                // Detener la grabación y navegar a la pantalla de resultados
                stopRecording()
                stopChronometer()
                // Run analysis and save results to database
                lifecycleScope.launch {
                    val transcriptionText = "" // TODO: get actual transcription text
                    val analysisSummary = analyzeTranscription(transcriptionText)
                    saveInterviewResult(interviewType, transcriptionText, analysisSummary)
                }
                navigateToResults()
            } else {
                // Iniciar la grabación de toda la entrevista
                startRecording()
                // Iniciar el cronómetro
                startChronometer()
                // Ocultar el botón de grabación y mostrar solo el botón de siguiente pregunta
                recordButton.visibility = View.GONE
                updateButtonsForCurrentQuestion()
            }
        }
        
        // Configurar botón de siguiente pregunta
        nextQuestionButton.setOnClickListener {
            if (currentQuestionIndex < getQuestions().size - 1) {
                // Avanzar a la siguiente pregunta
                showQuestion(currentQuestionIndex + 1)
            } else {
                // Finalizar la entrevista
                stopRecording()
                stopChronometer()
                // Run analysis and save results to database
                lifecycleScope.launch {
                    val transcriptionText = "" // TODO: get actual transcription text
                    val analysisSummary = analyzeTranscription(transcriptionText)
                    saveInterviewResult(interviewType, transcriptionText, analysisSummary)
                }
                navigateToResults()
            }
        }
        


        // Configurar la interfaz según el tipo de respuesta
        setupResponseInterface()
    }

    // Simple text analysis function (placeholder)
    private fun analyzeTranscription(transcription: String): String {
        val wordCount = transcription.split("\\s+".toRegex()).size
        return "La transcripción tiene $wordCount palabras."
    }

    // Save interview result to database
    private suspend fun saveInterviewResult(interviewType: String, transcriptionText: String, analysisSummary: String) {
        val db = AppDatabase.getDatabase(applicationContext)
        val interviewResult = InterviewResult(
            interviewType = interviewType,
            transcriptionText = transcriptionText,
            analysisSummary = analysisSummary,
            timestamp = System.currentTimeMillis()
        )
        db.interviewResultDao().insertInterviewResult(interviewResult)
    }

    private fun setupResponseInterface() {
        // Ocultar el botón de siguiente pregunta inicialmente en todos los casos
        nextQuestionButton.visibility = View.GONE
        
        // Asegurar que los botones de grabación estén en el contenedor de botones de grabación
        recordButton.visibility = View.VISIBLE
        nextQuestionButton.visibility = View.GONE
        

        
        when (responseType) {
            "camera" -> {
                // Mostrar la interfaz de cámara
                cameraContainer.visibility = View.VISIBLE
                textResponseEditText.visibility = View.GONE
                continueButton.visibility = View.GONE
                
                // Verificar y solicitar permisos de cámara si es necesario
                if (allPermissionsGranted()) {
                    // Asegurar que el contenedor de cámara sea visible antes de iniciar la cámara
                    cameraContainer.post {
                        startCamera()
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, CAMERA_PERMISSION_REQUEST_CODE)
                }
            }
            "text" -> {
                cameraContainer.visibility = View.GONE
                recordButton.visibility = View.GONE
                textResponseEditText.visibility = View.VISIBLE
                continueButton.visibility = View.VISIBLE
            }
        }
    }

    private fun getQuestions(): List<String> {
        return when (interviewType) {
            // Ciencias de la Salud
            "medicina" -> listOf(
                "¿Qué te motivó a estudiar medicina?",
                "¿Cómo manejarías una situación de emergencia médica?",
                "¿Cuál es tu especialidad de interés y por qué?",
                "¿Cómo te mantienes actualizado con los avances médicos?",
                "¿Cómo manejarías un caso donde el paciente no sigue tus recomendaciones?"
            )
            "enfermería" -> listOf(
                "¿Qué te motivó a elegir la enfermería como profesión?",
                "¿Cómo priorizas la atención cuando tienes múltiples pacientes?",
                "¿Cómo manejas situaciones de alto estrés en el entorno hospitalario?",
                "¿Cómo te comunicas efectivamente con pacientes difíciles?",
                "¿Cuál ha sido tu experiencia más desafiante como enfermero/a?"
            )
            "psicología", "technical" -> technicalQuestions  // Mantener compatibilidad con "technical"
            "odontología" -> listOf(
                "¿Qué te atrajo de la odontología como profesión?",
                "¿Cómo manejas pacientes con ansiedad dental?",
                "¿Cuáles son las últimas técnicas que has aprendido?",
                "¿Cómo explicas procedimientos complejos a tus pacientes?",
                "¿Cómo te mantienes actualizado en nuevos tratamientos dentales?"
            )
            "fisioterapia" -> listOf(
                "¿Qué enfoque terapéutico prefieres y por qué?",
                "¿Cómo evalúas a un paciente nuevo?",
                "¿Cómo adaptas el tratamiento cuando no hay progreso?",
                "¿Cómo educas a tus pacientes sobre prevención de lesiones?",
                "¿Cuál ha sido tu caso más desafiante y cómo lo abordaste?"
            )
            "nutrición" -> listOf(
                "¿Cómo personalizas planes nutricionales para diferentes necesidades?",
                "¿Cómo abordas a pacientes con trastornos alimenticios?",
                "¿Qué estrategias usas para promover cambios de hábitos alimenticios?",
                "¿Cómo te mantienes actualizado en investigación nutricional?",
                "¿Cómo evalúas el progreso de tus pacientes?"
            )
            
            // Ingeniería y Tecnología
            "programación_de_software" -> listOf(
                "¿Cómo resolvió un problema de rendimiento en alguno de sus proyectos?",
                "¿Qué patrones de diseño utiliza con mayor frecuencia y por qué?",
                "¿Cómo maneja la integración de sistemas diferentes en sus proyectos?",
                "¿Cómo equilibra el desarrollo de nuevas funciones con la mejora del código existente?",
                "¿Qué enfoque utiliza para las pruebas de software?"
            )
            "ingeniería_civil" -> listOf(
                "¿Qué te atrajo de la ingeniería civil?",
                "¿Cómo abordas los desafíos de sostenibilidad en tus proyectos?",
                "¿Cómo manejas proyectos con restricciones presupuestarias?",
                "¿Cómo te aseguras de que tus diseños cumplan con los códigos y regulaciones?",
                "¿Cuál ha sido tu proyecto más significativo y por qué?"
            )
            "ingeniería_mecánica" -> listOf(
                "¿Qué área de la ingeniería mecánica te interesa más?",
                "¿Cómo aplicas principios de eficiencia energética en tus diseños?",
                "¿Cómo utilizas software de simulación en tu trabajo?",
                "¿Cómo abordas la resolución de problemas en sistemas mecánicos complejos?",
                "¿Cuál ha sido tu proyecto más innovador?"
            )
            "ingeniería_eléctrica" -> listOf(
                "¿Qué te motivó a estudiar ingeniería eléctrica?",
                "¿Cómo te mantienes al día con las nuevas tecnologías en tu campo?",
                "¿Cómo abordas proyectos que requieren integración de sistemas?",
                "¿Cómo manejas los aspectos de seguridad en tus diseños eléctricos?",
                "¿Cuál ha sido tu experiencia con energías renovables?"
            )
            "ciencias_de_la_computación", "software_engineer" -> softwareEngineerQuestions  // Mantener compatibilidad
            "ciberseguridad" -> listOf(
                "¿Cómo te mantienes actualizado sobre las últimas amenazas de seguridad?",
                "¿Cómo realizas evaluaciones de vulnerabilidad en sistemas?",
                "¿Cómo equilibras seguridad y usabilidad en tus soluciones?",
                "¿Cuál ha sido tu experiencia más desafiante en respuesta a incidentes?",
                "¿Cómo implementas estrategias de seguridad en capas?"
            )
            
            // Ciencias Sociales
            "derecho" -> listOf(
                "¿Qué área del derecho te apasiona más y por qué?",
                "¿Cómo te preparas para un caso complejo?",
                "¿Cómo manejas situaciones éticamente desafiantes?",
                "¿Cómo te mantienes actualizado con cambios en la legislación?",
                "¿Cuál ha sido tu caso más significativo y por qué?"
            )
            "economía" -> listOf(
                "¿Qué escuela de pensamiento económico te influencia más?",
                "¿Cómo analizas tendencias económicas complejas?",
                "¿Cómo comunicas conceptos económicos complejos a audiencias no especializadas?",
                "¿Cómo evalúas el impacto de políticas económicas?",
                "¿Cuál ha sido tu proyecto de investigación más interesante?"
            )
            "sociología" -> listOf(
                "¿Qué te atrajo de la sociología como disciplina?",
                "¿Cómo diseñas y conduces investigación social?",
                "¿Cómo analizas fenómenos sociales complejos?",
                "¿Cómo aplicas teorías sociológicas a problemas contemporáneos?",
                "¿Cuál ha sido tu hallazgo de investigación más sorprendente?"
            )
            "ciencias_políticas" -> listOf(
                "¿Qué área de las ciencias políticas te interesa más?",
                "¿Cómo analizas sistemas políticos comparativamente?",
                "¿Cómo evalúas la efectividad de políticas públicas?",
                "¿Cómo te mantienes objetivo en temas políticamente polarizados?",
                "¿Cuál ha sido tu proyecto de investigación más significativo?"
            )
            "trabajo_social" -> listOf(
                "¿Qué te motivó a elegir el trabajo social como profesión?",
                "¿Cómo manejas casos con múltiples necesidades complejas?",
                "¿Cómo evitas el agotamiento profesional en este campo?",
                "¿Cómo abordas dilemas éticos en tu práctica?",
                "¿Cuál ha sido tu intervención más exitosa y por qué?"
            )
            "antropología" -> listOf(
                "¿Qué rama de la antropología te interesa más?",
                "¿Cómo diseñas y conduces trabajo de campo etnográfico?",
                "¿Cómo abordas cuestiones éticas en la investigación antropológica?",
                "¿Cómo aplicas perspectivas antropológicas a problemas contemporáneos?",
                "¿Cuál ha sido tu experiencia de investigación más transformadora?"
            )
            
            // Negocios y Administración
            "administración_de_empresas" -> listOf(
                "¿Qué estilo de liderazgo prefieres y por qué?",
                "¿Cómo abordas la gestión del cambio organizacional?",
                "¿Cómo tomas decisiones estratégicas en entornos inciertos?",
                "¿Cómo motivas a equipos diversos?",
                "¿Cuál ha sido tu mayor logro como administrador?"
            )
            "marketing", "marketing" -> marketingQuestions  // Mantener compatibilidad
            "finanzas" -> listOf(
                "¿Cómo evalúas oportunidades de inversión?",
                "¿Cómo analizas la salud financiera de una empresa?",
                "¿Cómo gestionas el riesgo en decisiones financieras?",
                "¿Cómo te mantienes actualizado con regulaciones financieras?",
                "¿Cuál ha sido tu análisis financiero más desafiante?"
            )
            "recursos_humanos" -> listOf(
                "¿Cómo atraes y retienes talento en un mercado competitivo?",
                "¿Cómo manejas situaciones de conflicto laboral?",
                "¿Cómo diseñas programas de desarrollo profesional efectivos?",
                "¿Cómo evalúas el desempeño de manera justa y objetiva?",
                "¿Cómo promueves la diversidad e inclusión en el lugar de trabajo?"
            )
            "contabilidad" -> listOf(
                "¿Cómo te mantienes actualizado con cambios en normativas contables?",
                "¿Cómo identificas y abordas irregularidades contables?",
                "¿Cómo comunicas información financiera compleja a no especialistas?",
                "¿Cómo manejas plazos ajustados en periodos de cierre fiscal?",
                "¿Cuál ha sido tu caso contable más desafiante?"
            )
            "comercio_internacional" -> listOf(
                "¿Cómo evalúas oportunidades en mercados internacionales?",
                "¿Cómo manejas diferencias culturales en negociaciones internacionales?",
                "¿Cómo te mantienes actualizado con regulaciones comerciales globales?",
                "¿Cómo gestionas riesgos en operaciones internacionales?",
                "¿Cuál ha sido tu proyecto internacional más exitoso?"
            )
            
            // Arte y Humanidades
            "diseño_gráfico" -> listOf(
                "¿Cuál es tu proceso creativo cuando abordas un nuevo proyecto?",
                "¿Cómo equilibras visión artística con necesidades del cliente?",
                "¿Cómo te mantienes actualizado con tendencias de diseño?",
                "¿Cómo manejas la crítica constructiva sobre tu trabajo?",
                "¿Cuál ha sido tu proyecto de diseño más desafiante?"
            )
            "arquitectura" -> listOf(
                "¿Qué influencias arquitectónicas te inspiran más?",
                "¿Cómo integras sostenibilidad en tus diseños?",
                "¿Cómo equilibras estética y funcionalidad?",
                "¿Cómo colaboras con ingenieros y otros profesionales?",
                "¿Cuál ha sido tu proyecto arquitectónico más significativo?"
            )
            "literatura" -> listOf(
                "¿Qué géneros literarios te interesan más y por qué?",
                "¿Cómo analizas obras literarias complejas?",
                "¿Cómo aplicas teoría literaria en tu análisis?",
                "¿Cómo comunicas interpretaciones literarias a diferentes audiencias?",
                "¿Cuál ha sido tu proyecto de investigación literaria más interesante?"
            )
            "historia_del_arte" -> listOf(
                "¿Qué periodo artístico te apasiona más y por qué?",
                "¿Cómo analizas obras de arte en su contexto histórico?",
                "¿Cómo comunicas el valor cultural de obras de arte?",
                "¿Cómo te mantienes actualizado en investigación de historia del arte?",
                "¿Cuál ha sido tu descubrimiento más interesante en tu campo?"
            )
            "música" -> listOf(
                "¿Qué géneros musicales influencian más tu trabajo?",
                "¿Cómo abordas el proceso de composición o interpretación?",
                "¿Cómo te mantienes técnicamente preparado?",
                "¿Cómo manejas la presión en actuaciones o plazos creativos?",
                "¿Cuál ha sido tu proyecto musical más significativo?"
            )
            "comunicación_audiovisual" -> listOf(
                "¿Qué aspectos de la producción audiovisual te interesan más?",
                "¿Cómo conceptualizas y planificas proyectos audiovisuales?",
                "¿Cómo te adaptas a nuevas tecnologías en tu campo?",
                "¿Cómo equilibras visión creativa con limitaciones técnicas?",
                "¿Cuál ha sido tu proyecto audiovisual más desafiante?"
            )
            
            // Mantener compatibilidad con tipos antiguos
            "sales" -> salesQuestions
            "product_manager" -> productManagerQuestions
            "product_designer" -> productDesignerQuestions
            "data_analysis", "análisis_de_datos" -> dataAnalysisQuestions
            
            // Si no coincide con ninguno, usar preguntas generales
            else -> generalQuestions
        }
    }

    private fun showQuestion(index: Int) {
        val questions = getQuestions()
        if (index < questions.size) {
            questionTextView.text = questions[index]
            currentQuestionIndex = index
            
            // Actualizar la barra de progreso
            val progress = ((index + 1) * 100) / questions.size
            progressBar.progress = progress
            progressTextView.text = "${index + 1}/${questions.size}"
            progressPercentageTextView.text = "$progress%"
            
            // Actualizar botones según la pregunta actual
            updateButtonsForCurrentQuestion()
        } else {
            // Todas las preguntas han sido respondidas
            Toast.makeText(this, "¡Entrevista completada!", Toast.LENGTH_SHORT).show()
            navigateToResults()
        }
    }

    private fun updateButtonsForCurrentQuestion() {
        val questions = getQuestions()
        val isLastQuestion = currentQuestionIndex == questions.size - 1
        
        if (isRecording) {
            // Si estamos grabando
            if (isLastQuestion) {
                // En la última pregunta, mostrar solo el botón FINALIZAR ENTREVISTA en rojo
                nextQuestionButton.text = "FINALIZAR ENTREVISTA"
                nextQuestionButton.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, theme))
            } else {
                // En preguntas intermedias, mostrar solo SIGUIENTE PREGUNTA en verde
                nextQuestionButton.text = "SIGUIENTE PREGUNTA"
                nextQuestionButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark, theme))
            }
            
            // Mostrar solo el botón de siguiente pregunta durante la grabación
            nextQuestionButton.visibility = View.VISIBLE
            // Ocultar el botón de finalizar entrevista
            recordButton.visibility = View.GONE
        } else {
            // Si no estamos grabando, mostrar solo el botón de iniciar grabación
            recordButton.visibility = View.VISIBLE
            nextQuestionButton.visibility = View.GONE
        }
    }

    private fun stopRecording() {
        // Detener la grabación actual
        recording?.stop()
        recording = null
        isRecording = false
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                // Permisos concedidos, iniciar la cámara
                startCamera()
            } else {
                // Permisos denegados
                // Cambiar a modo texto si no hay permisos
                responseType = "text"
                setupResponseInterface()
            }
        }
    }
    
    private fun startCamera() {
        // Inicializar el executor para la cámara
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        // Configurar la vista previa
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
                
                val recorder = Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                    .build()
                
                videoCapture = VideoCapture.withOutput(recorder)
                
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                
                cameraProvider.unbindAll()
                
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )
                
            } catch (e: Exception) {
                Log.e(TAG, "Error al iniciar la cámara: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun startRecording() {
        val videoCapture = this.videoCapture ?: return
        
        if (recording != null) {
            return
        }
        
        val name = "Entrevista_${interviewType}_" + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/InterviewFace")
            }
        }
        
        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutput)
            .apply {
                if (ContextCompat.checkSelfPermission(this@InterviewActivity, Manifest.permission.RECORD_AUDIO) == 
                    PackageManager.PERMISSION_GRANTED) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this), object : Consumer<VideoRecordEvent> {
                override fun accept(event: VideoRecordEvent) {
                    when(event) {
                        is VideoRecordEvent.Start -> {
                            isRecording = true
                            // Ocultar el indicador de grabación
                            findViewById<TextView>(R.id.recordingIndicator).visibility = View.GONE
                            Log.d(TAG, "Grabación iniciada para la entrevista completa")
                            
                            // Asegurar que el botón de siguiente pregunta sea visible
                            runOnUiThread {
                                updateButtonsForCurrentQuestion()
                                // Forzar visibilidad del botón de siguiente pregunta y ocultar el de grabación
                                nextQuestionButton.visibility = View.VISIBLE
                                recordButton.visibility = View.GONE
                            }
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (!event.hasError()) {
                                val msg = "Entrevista grabada correctamente: ${event.outputResults.outputUri}"
                                Log.d(TAG, msg)
                            } else {
                                recording?.close()
                                recording = null
                                val msg = "Error en la grabación: ${event.error}"
                                Log.e(TAG, msg)
                            }
                            isRecording = false
                            recordButton.text = "INICIAR GRABACIÓN"
                        }
                    }
                }
            })
    }
    
    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
        // Asegurarse de detener el cronómetro al destruir la actividad
        chronometerRunnable?.let { chronometerHandler.removeCallbacks(it) }
    }

    /**
     * Configura el tiempo máximo de entrevista según el tipo seleccionado
     */
    private fun setMaxInterviewTime() {
        maxInterviewTime = when (interviewType) {
            "software" -> 180 // 3 minutos para entrevistas de software
            "marketing" -> 150 // 2.5 minutos para entrevistas de marketing
            else -> 120 // 2 minutos para entrevistas generales
        }
    }

    /**
     * Inicia el cronómetro para la entrevista
     */
    private fun startChronometer() {
        chronometer.visibility = View.VISIBLE
        chronometerStartTime = System.currentTimeMillis()
        
        // Detener cualquier cronómetro existente
        chronometerRunnable?.let { chronometerHandler.removeCallbacks(it) }
        
        // Crear un nuevo runnable para actualizar el cronómetro
        chronometerRunnable = object : Runnable {
            override fun run() {
                val elapsedMillis = System.currentTimeMillis() - chronometerStartTime
                val elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis)
                
                // Calcular el tiempo restante
                val remainingSeconds = maxInterviewTime - elapsedSeconds
                
                if (remainingSeconds <= 0) {
                    // Tiempo agotado
                    chronometer.text = "00:00"
                    chronometer.setTextColor(resources.getColor(android.R.color.holo_red_light, theme))
                    // Aplicar efecto de parpadeo
                    animateChronometer(true)
                    // Detener el cronómetro y la grabación si está activa
                    chronometerRunnable?.let { chronometerHandler.removeCallbacks(it) }
                    if (isRecording) {
                        stopRecording()
                    }
                    // Mostrar diálogo personalizado de tiempo agotado
                    showTimeUpDialog()
                } else {
                    // Formatear y mostrar el tiempo restante
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    chronometer.text = String.format("%02d:%02d", minutes, seconds)
                    
                    // Cambiar el color y aplicar efectos según el tiempo restante
                    when {
                        remainingSeconds < 10 -> {
                            // Últimos 10 segundos: rojo con parpadeo para alertar
                            chronometer.setTextColor(resources.getColor(android.R.color.holo_red_light, theme))
                            animateChronometer(true)
                        }
                        else -> {
                            // Tiempo normal: blanco
                            chronometer.setTextColor(resources.getColor(R.color.white, theme))
                            animateChronometer(false)
                        }
                    }
                    
                    // Programar la próxima actualización
                    chronometerHandler.postDelayed(this, 1000)
                }
            }
        }
        
        // Iniciar el cronómetro
        chronometerRunnable?.let { chronometerHandler.post(it) }
    }

    /**
     * Detiene el cronómetro
     */
    private fun stopChronometer() {
        chronometerRunnable?.let { chronometerHandler.removeCallbacks(it) }
        chronometer.clearAnimation()
        chronometer.visibility = View.GONE
    }
    
    /**
     * Navega a la pantalla de resultados de la entrevista
     */
    private fun navigateToResults() {
        val intent = Intent(this, ResultadoEntrevistaActivity::class.java)
        // Aquí puedes pasar datos adicionales si es necesario
        intent.putExtra("INTERVIEW_TYPE", interviewType)
        startActivity(intent)
        finish() // Finaliza esta actividad después de iniciar la de resultados
    }
    
    /**
     * Aplica animaciones al cronómetro para hacerlo más llamativo
     * @param blink Si es true, aplica efecto de parpadeo
     */
    private fun animateChronometer(blink: Boolean) {
        // Detener animaciones anteriores
        chronometer.clearAnimation()
        
        if (blink) {
            // Animación de parpadeo elegante para alertar cuando queda poco tiempo
            val blinkAnimation = AlphaAnimation(1.0f, 0.6f).apply {
                duration = 800
                repeatMode = Animation.REVERSE
                repeatCount = Animation.INFINITE
            }
            chronometer.startAnimation(blinkAnimation)
        }
    }
    
    /**
     * Muestra un diálogo personalizado cuando se acaba el tiempo
     */
    private fun showTimeUpDialog() {
        // Crear el diálogo personalizado
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .create()
        
        // Inflar la vista personalizada
        val view = layoutInflater.inflate(R.layout.dialog_time_up, null)
        dialog.setView(view)
        
        // Configurar el botón de aceptar
        view.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
            finish() // Cerrar la actividad cuando el usuario presione el botón
        }
        
        // Asegurar que el diálogo no se pueda cerrar tocando fuera de él
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        
        // Mostrar el diálogo
        dialog.show()
    }
}
