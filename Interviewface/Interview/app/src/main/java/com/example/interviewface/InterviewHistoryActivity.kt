package com.example.interviewface

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class InterviewHistoryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var backButton: ImageButton
    private lateinit var filterSpinner: Spinner
    private lateinit var interviewCardsContainer: LinearLayout
    
    // Lista de todas las entrevistas (vacía inicialmente)
    private val allInterviews = listOf<Interview>()
    
    // Entrevistas filtradas que se mostrarán
    private var filteredInterviews = allInterviews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_history)

        // Inicializar vistas
        backButton = findViewById(R.id.backButton)
        filterSpinner = findViewById(R.id.filterSpinner)
        
        // Buscar el contenedor de tarjetas dentro del ScrollView
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        interviewCardsContainer = scrollView.getChildAt(0) as LinearLayout

        // Configurar el botón de volver
        backButton.setOnClickListener {
            finish()
        }

        // Configurar el spinner de filtros
        val filterOptions = arrayOf("Todas las entrevistas", "Última semana", "Último mes", "Mejor puntuación")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = adapter
        filterSpinner.onItemSelectedListener = this
        
        // Mostrar todas las entrevistas inicialmente
        displayInterviews(allInterviews)
    }
    
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Filtrar entrevistas según la opción seleccionada
        when (position) {
            0 -> { // Todas las entrevistas
                filteredInterviews = allInterviews
            }
            1 -> { // Última semana
                filteredInterviews = allInterviews.filter { 
                    val date = it.date.split("/")
                    val day = date[0].toInt()
                    val month = date[1].toInt()
                    // Simulamos que las entrevistas de mayo son de la última semana
                    month == 5 && day >= 7
                }
            }
            2 -> { // Último mes
                filteredInterviews = allInterviews.filter { 
                    val date = it.date.split("/")
                    val month = date[1].toInt()
                    // Simulamos que las entrevistas de mayo son del último mes
                    month == 5
                }
            }
            3 -> { // Mejor puntuación
                filteredInterviews = allInterviews.filter { it.score == "Alta" }
            }
        }
        
        // Mostrar las entrevistas filtradas
        displayInterviews(filteredInterviews)
    }
    
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // No hacer nada
    }
    
    private fun displayInterviews(interviews: List<Interview>) {
        // Limpiar el contenedor
        interviewCardsContainer.removeAllViews()
        
        // Añadir cada entrevista como una tarjeta
        for (interview in interviews) {
            val cardView = layoutInflater.inflate(R.layout.item_interview_card, interviewCardsContainer, false) as CardView
            
            // Configurar los datos de la entrevista en la tarjeta
            val dateTextView = cardView.findViewById<TextView>(R.id.dateTextView)
            val typeTextView = cardView.findViewById<TextView>(R.id.typeTextView)
            val scoreTextView = cardView.findViewById<TextView>(R.id.scoreTextView)
            
            dateTextView.text = "Fecha: ${interview.date}"
            typeTextView.text = "Tipo: ${interview.type}"
            scoreTextView.text = "Puntuación: ${interview.score}"
            
            // Añadir la tarjeta al contenedor
            interviewCardsContainer.addView(cardView)
        }
        
        // Si no hay entrevistas, mostrar un mensaje
        if (interviews.isEmpty()) {
            val textView = TextView(this)
            textView.text = "No has realizado ninguna entrevista aún. Realiza tu primera entrevista para ver tu historial aquí."
            textView.setTextColor(resources.getColor(R.color.white, theme))
            textView.textSize = 16f
            textView.setPadding(32, 32, 32, 32)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            interviewCardsContainer.addView(textView)
        }
    }
    
    // Clase de datos para representar una entrevista
    data class Interview(val date: String, val type: String, val score: String)
}
