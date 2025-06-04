package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.interviewface.model.InterviewResult
import com.example.interviewface.repository.AppDatabase
import kotlinx.coroutines.launch
// import androidx.appcompat.app.AppCompatActivity

class ResultadoEntrevistaActivity : BaseActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvCalificacion: TextView
    private lateinit var tvCalificacionPromedio: TextView
    private lateinit var tvTendencia: TextView
    private lateinit var tvNumResenas: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var btnComenzarDeNuevo: Button
    
    // Progress bars para las calificaciones
    private lateinit var progressBar5: ProgressBar
    private lateinit var progressBar4: ProgressBar
    private lateinit var progressBar3: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private lateinit var progressBar1: ProgressBar
    
    // Porcentajes de calificaciones
    private lateinit var tvPorcentaje5: TextView
    private lateinit var tvPorcentaje4: TextView
    private lateinit var tvPorcentaje3: TextView
    private lateinit var tvPorcentaje2: TextView
    private lateinit var tvPorcentaje1: TextView
    
    // Layouts para las preguntas (enlaces a otras pantallas)
    private lateinit var layoutHablameDeTi: LinearLayout
    private lateinit var layoutFortalezas: LinearLayout
    private lateinit var layoutDebilidades: LinearLayout

    private lateinit var tvTranscription: TextView
    private lateinit var tvAnalysisSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_entrevista)
        
        // Inicializar vistas
        initViews()
        
        // Cargar y mostrar resultados de la última entrevista
        loadLatestInterviewResult()
        
        // Configurar listeners
        setupListeners()
    }
    
    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tvCalificacion = findViewById(R.id.tvCalificacion)
        tvCalificacionPromedio = findViewById(R.id.tvCalificacionPromedio)
        tvTendencia = findViewById(R.id.tvTendencia)
        tvNumResenas = findViewById(R.id.tvNumResenas)
        ratingBar = findViewById(R.id.ratingBar)
        btnComenzarDeNuevo = findViewById(R.id.btnComenzarDeNuevo)
        
        // Progress bars
        progressBar5 = findViewById(R.id.progressBar5)
        progressBar4 = findViewById(R.id.progressBar4)
        progressBar3 = findViewById(R.id.progressBar3)
        progressBar2 = findViewById(R.id.progressBar2)
        progressBar1 = findViewById(R.id.progressBar1)
        
        // Porcentajes
        tvPorcentaje5 = findViewById(R.id.tvPorcentaje5)
        tvPorcentaje4 = findViewById(R.id.tvPorcentaje4)
        tvPorcentaje3 = findViewById(R.id.tvPorcentaje3)
        tvPorcentaje2 = findViewById(R.id.tvPorcentaje2)
        tvPorcentaje1 = findViewById(R.id.tvPorcentaje1)
        
        // Layouts de preguntas
        layoutHablameDeTi = findViewById(R.id.layoutHablameDeTi)
        layoutFortalezas = findViewById(R.id.layoutFortalezas)
        layoutDebilidades = findViewById(R.id.layoutDebilidades)

        // New views for transcription and analysis
        tvTranscription = findViewById(R.id.tvTranscription)
        tvAnalysisSummary = findViewById(R.id.tvAnalysisSummary)
    }
    
    private fun loadLatestInterviewResult() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val latestResult: InterviewResult? = db.interviewResultDao().getLatestInterviewResult()
            if (latestResult != null) {
                // Display transcription and analysis summary
                tvTranscription.text = latestResult.transcriptionText
                tvAnalysisSummary.text = latestResult.analysisSummary
            } else {
                tvTranscription.text = "No hay resultados disponibles."
                tvAnalysisSummary.text = ""
            }
        }
    }
    
    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }
        
        btnComenzarDeNuevo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("select_tab", R.id.navigation_interviews)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        
        layoutHablameDeTi.setOnClickListener {
            navigateToQuestionDetail("Háblame de ti")
        }
        
        layoutFortalezas.setOnClickListener {
            navigateToQuestionDetail("Cuáles son tus fortalezas?")
        }
        
        layoutDebilidades.setOnClickListener {
            navigateToQuestionDetail("Cuáles son tus debilidades?")
        }
    }
    
    private fun navigateToQuestionDetail(questionTitle: String) {
        // Placeholder for navigation to question detail screen
    }
}
