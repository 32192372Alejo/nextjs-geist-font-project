package com.example.interviewface

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.interviewface.TipsFragment

class InterviewSelectionFragment : Fragment() {

    private lateinit var areasContainer: LinearLayout
    private lateinit var careersContainer: LinearLayout
    private lateinit var careersRadioGroup: RadioGroup
    private lateinit var selectedAreaTitle: TextView
    private lateinit var startSimulationButton: Button
    private lateinit var faqButton: Button
    private lateinit var closeButton: ImageButton
    private lateinit var backToAreasButton: Button
    private lateinit var buttonsContainer: LinearLayout
    private lateinit var interviewTypeTitle: TextView
    
    // Mapa de áreas y sus carreras correspondientes
    private val areasWithCareers = mapOf(
        "Ciencias de la Salud" to listOf(
            "Medicina", "Enfermería", "Psicología", "Odontología", "Fisioterapia", "Nutrición"
        ),
        "Ingeniería y Tecnología" to listOf(
            "Programación de Software", "Ingeniería Civil", "Ingeniería Mecánica", 
            "Ingeniería Eléctrica", "Ciencias de la Computación", "Ciberseguridad", "Análisis de Datos"
        ),
        "Ciencias Sociales" to listOf(
            "Derecho", "Economía", "Sociología", "Ciencias Políticas", "Trabajo Social", "Antropología"
        ),
        "Negocios y Administración" to listOf(
            "Administración de Empresas", "Marketing", "Finanzas", "Recursos Humanos", 
            "Contabilidad", "Comercio Internacional"
        ),
        "Arte y Humanidades" to listOf(
            "Diseño Gráfico", "Arquitectura", "Literatura", "Historia del Arte", 
            "Música", "Comunicación Audiovisual"
        )
    )
    
    // Variable para almacenar la carrera seleccionada actualmente
    private var selectedCareer: String? = null
    private var selectedArea: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_interview_selection, container, false)

        // Inicializar vistas
        areasContainer = view.findViewById(R.id.areasContainer)
        selectedAreaTitle = view.findViewById(R.id.selectedAreaTitle)
        startSimulationButton = view.findViewById(R.id.startSimulationButton)
        faqButton = view.findViewById(R.id.faqButton)
        closeButton = view.findViewById(R.id.closeButton)
        buttonsContainer = view.findViewById(R.id.buttonsContainer)
        interviewTypeTitle = view.findViewById(R.id.interviewTypeTitle)

        // Configurar áreas
        setupAreas()
        
        // Ocultar el título de selección de carrera inicialmente
        selectedAreaTitle.visibility = View.GONE
        
        // Ocultar los botones de acción inicialmente
        buttonsContainer.visibility = View.GONE

        // Configurar botón de inicio de simulación
        startSimulationButton.setOnClickListener {
            if (selectedCareer == null) {
                Toast.makeText(context, "Por favor, seleccione una carrera", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convertir el nombre de la carrera a un código para la API
            val careerCode = selectedCareer?.lowercase()?.replace(" ", "_") ?: "general"

            // Iniciar la actividad de entrevista
            val intent = Intent(context, InterviewStartActivity::class.java)
            intent.putExtra("interviewType", careerCode)
            startActivity(intent)
        }

        // Configurar botón de preguntas frecuentes para ir a la pantalla de consejos
        faqButton.setOnClickListener {
            // Obtener la actividad principal
            val mainActivity = requireActivity() as MainActivity
            
            // Crear una instancia de TipsFragment con un argumento para mostrar directamente la pestaña de FAQ
            val tipsFragment = TipsFragment()
            val bundle = Bundle()
            bundle.putBoolean("showFaq", true)
            tipsFragment.arguments = bundle
            
            // Cargar el fragmento de consejos
            mainActivity.loadFragment(tipsFragment)
            
            // Actualizar la selección en la barra de navegación inferior
            mainActivity.binding.bottomNavigation.selectedItemId = R.id.navigation_comments
        }

        // Configurar botón de cerrar para ir a la pantalla de inicio
        closeButton.setOnClickListener {
            // Obtener la actividad principal
            val mainActivity = requireActivity() as MainActivity
            
            // Cargar el fragmento de inicio (HomeFragment)
            mainActivity.loadFragment(HomeFragment())
            
            // Actualizar la selección en la barra de navegación inferior
            mainActivity.binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }

        return view
    }
    
    private fun setupAreas() {
        // Limpiar el contenedor de áreas
        areasContainer.removeAllViews()
        
        // Agregar cada área como un botón con menú desplegable
        areasWithCareers.keys.forEach { area ->
            val areaButton = createAreaButton(area)
            areasContainer.addView(areaButton)
        }
        
        // Inicializar la vista
        showAreas()
    }
    
    private fun createAreaButton(area: String): CardView {
        // Crear un CardView principal para el botón de área
        val cardView = CardView(requireContext()).apply {
            radius = resources.getDimension(R.dimen.card_corner_radius) 
            cardElevation = resources.getDimension(R.dimen.card_elevation) 
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background)) 
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24) // Margen inferior
            }
        }
        
        // Crear un LinearLayout vertical para contener el botón y el menú desplegable
        val containerLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        // Crear un LinearLayout horizontal para el botón principal (área)
        val buttonLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = android.view.Gravity.CENTER_VERTICAL
            setPadding(32, 24, 32, 24) // Padding interno
        }
        
        // Texto del área
        val textView = TextView(requireContext()).apply {
            text = area
            setTextColor(Color.WHITE)
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f // Toma el espacio disponible
            }
        }
        
        // Flecha (inicialmente apuntando hacia abajo)
        val arrowImageView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_arrow_down)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            contentDescription = "Flecha indicadora"
        }
        
        // Agregar vistas al layout del botón
        buttonLayout.addView(textView)
        buttonLayout.addView(arrowImageView)
        
        // Crear el contenedor para las carreras (inicialmente invisible)
        val careersLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background))
            setPadding(32, 0, 32, 16)
            visibility = View.GONE
        }
        
        // Obtener las carreras para esta área
        val careers = areasWithCareers[area] ?: listOf()
        
        // Crear un RadioGroup para las carreras
        val radioGroup = RadioGroup(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = RadioGroup.VERTICAL
            setPadding(16, 8, 16, 8)
        }
        
        // Agregar cada carrera como un RadioButton en el menú desplegable
        careers.forEach { career ->
            // Crear el RadioButton para la carrera
            val radioButton = RadioButton(requireContext()).apply {
                text = career
                textSize = 16f
                setTextColor(Color.WHITE)
                buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue_accent))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(8, 16, 8, 8)
            }
            
            // Agregar el RadioButton al grupo
            radioGroup.addView(radioButton)
            
            // Agregar una descripción debajo del RadioButton
            val description = TextView(requireContext()).apply {
                // Descripción específica para cada carrera
                text = when (career) {
                    // Ciencias de la Salud
                    "Medicina" -> "Preguntas sobre diagnóstico, tratamiento y atención médica."
                    "Enfermería" -> "Preguntas sobre cuidados, procedimientos y atención al paciente."
                    "Psicología" -> "Preguntas sobre comportamiento humano, terapias y salud mental."
                    "Odontología" -> "Preguntas sobre salud bucal, tratamientos dentales y prevención."
                    "Fisioterapia" -> "Preguntas sobre rehabilitación física, tratamiento y prevención."
                    "Nutrición" -> "Preguntas sobre alimentación, dietas y salud nutricional."
                    
                    // Ingeniería y Tecnología
                    "Programación de Software" -> "Preguntas sobre desarrollo de aplicaciones, algoritmos y lenguajes de programación."
                    "Ingeniería Civil" -> "Preguntas sobre construcción, estructuras y diseño de infraestructuras."
                    "Ingeniería Mecánica" -> "Preguntas sobre diseño de máquinas, sistemas mecánicos y termodinámica."
                    "Ingeniería Eléctrica" -> "Preguntas sobre sistemas eléctricos, electrónica y automatización."
                    "Ciencias de la Computación" -> "Preguntas sobre algoritmos, sistemas computacionales y teoría de la computación."
                    "Ciberseguridad" -> "Preguntas sobre seguridad informática, protección de datos y hacking ético."
                    
                    // Ciencias Sociales
                    "Derecho" -> "Preguntas sobre leyes, procedimientos legales y jurisprudencia."
                    "Economía" -> "Preguntas sobre mercados, políticas económicas y análisis financiero."
                    "Sociología" -> "Preguntas sobre comportamiento social, estructuras sociales y cambio social."
                    "Ciencias Políticas" -> "Preguntas sobre sistemas políticos, relaciones internacionales y gobernanza."
                    "Trabajo Social" -> "Preguntas sobre intervención social, bienestar comunitario y políticas sociales."
                    "Antropología" -> "Preguntas sobre culturas humanas, evolución social y diversidad cultural."
                    
                    // Negocios y Administración
                    "Administración de Empresas" -> "Preguntas sobre gestión empresarial, estrategia y liderazgo organizacional."
                    "Marketing" -> "Preguntas sobre estrategias de mercado, publicidad y comportamiento del consumidor."
                    "Finanzas" -> "Preguntas sobre gestión financiera, inversiones y mercados de capitales."
                    "Recursos Humanos" -> "Preguntas sobre gestión de personal, reclutamiento y desarrollo organizacional."
                    "Contabilidad" -> "Preguntas sobre registros financieros, auditoría y normativas contables."
                    "Comercio Internacional" -> "Preguntas sobre comercio global, logística internacional y aduanas."
                    
                    // Arte y Humanidades
                    "Diseño Gráfico" -> "Preguntas sobre diseño visual, tipografía y comunicación gráfica."
                    "Arquitectura" -> "Preguntas sobre diseño arquitectónico, urbanismo y construcción sostenible."
                    "Literatura" -> "Preguntas sobre obras literarias, análisis textual y teoría literaria."
                    "Historia del Arte" -> "Preguntas sobre movimientos artísticos, obras clásicas y análisis estético."
                    "Música" -> "Preguntas sobre teoría musical, composición e interpretación."
                    "Comunicación Audiovisual" -> "Preguntas sobre producción audiovisual, cinematografía y medios digitales."
                    
                    // Descripción genérica para cualquier otra carrera
                    else -> "Preguntas sobre $career y temas relacionados."
                }
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                setPadding(32, 0, 0, 16) // Padding a la izquierda para indentar
            }
            
            // Agregar la descripción al grupo
            radioGroup.addView(description)
        }
        
        // Configurar el listener para cuando se selecciona un RadioButton
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            if (radioButton != null) {
                selectedCareer = radioButton.text.toString()
                selectedArea = area
                
                // Mostrar los botones de acción
                buttonsContainer.visibility = View.VISIBLE
                
                // Actualizar el título seleccionado
                selectedAreaTitle.text = "$area: $selectedCareer"
                selectedAreaTitle.visibility = View.VISIBLE
            }
        }
        
        // Agregar el RadioGroup al layout de carreras
        careersLayout.addView(radioGroup)
        
        // Agregamos un espacio al final del menú para mejor apariencia
        val spacer = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                16
            )
        }
        
        // Agregar el espacio al layout de carreras
        careersLayout.addView(spacer)
        
        // Agregar los layouts al contenedor principal
        containerLayout.addView(buttonLayout)
        containerLayout.addView(careersLayout)
        
        // Agregar el contenedor al CardView
        cardView.addView(containerLayout)
        
        // Variable para controlar el estado de expansión
        val isExpanded = false.also { expansionState -> 
            // Guardar el estado inicial en una propiedad de la vista para acceder después
            cardView.tag = expansionState
        }
        
        // Configurar el clic del botón principal
        buttonLayout.setOnClickListener {
            // Obtener el estado actual y cambiarlo
            val currentExpanded = cardView.tag as Boolean
            val newExpanded = !currentExpanded
            cardView.tag = newExpanded
            
            if (newExpanded) {
                // Expandir el menú y cambiar la flecha
                careersLayout.visibility = View.VISIBLE
                arrowImageView.setImageResource(R.drawable.ic_arrow_right_new)
            } else {
                // Contraer el menú y restaurar la flecha
                careersLayout.visibility = View.GONE
                arrowImageView.setImageResource(R.drawable.ic_arrow_down)
            }
        }
        
        return cardView
    }
    
    // Esta función ya no es necesaria porque ahora usamos menús desplegables
    // La mantenemos vacía por si hay referencias a ella en otras partes del código
    private fun showCareersForArea(area: String) {
        // No hacemos nada aquí, la lógica se ha movido al menú desplegable
    }
    
    // Esta función ahora solo se usa para la inicialización
    private fun showAreas() {
        // Mostrar el contenedor de áreas
        areasContainer.visibility = View.VISIBLE
        
        // Ocultar el título de selección de carrera
        selectedAreaTitle.visibility = View.GONE
        
        // Mostrar el título principal
        interviewTypeTitle.visibility = View.VISIBLE
        
        // Ocultar los botones de acción
        buttonsContainer.visibility = View.GONE
        
        // Resetear la carrera seleccionada
        selectedCareer = null
    }
    
    // Esta función ya no es necesaria porque usamos menús desplegables
    private fun showCareers() {
        // No hacemos nada aquí, la lógica se ha movido al menú desplegable
    }

    companion object {
        fun newInstance(): InterviewSelectionFragment {
            return InterviewSelectionFragment()
        }
    }
}
