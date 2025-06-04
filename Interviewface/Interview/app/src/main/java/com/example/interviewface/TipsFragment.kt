package com.example.interviewface

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.fragment.app.Fragment
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.interviewface.ui.theme.InterviewfaceTheme
import com.example.interviewface.HomeFragment
import com.example.interviewface.MainActivity

class TipsFragment : Fragment() {
    // Data class for FAQ items
    data class FaqItem(val question: String, val answer: String, val icon: ImageVector)

    // Variable para controlar si se debe mostrar directamente la pestaña de FAQ
    private var showFaq: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener el argumento showFaq si existe
        arguments?.let {
            showFaq = it.getBoolean("showFaq", false)
        }
    }

    // Método para navegar de vuelta a la pantalla principal
    private fun navigateBack() {
        // Intenta usar popBackStack primero
        requireActivity().supportFragmentManager.popBackStack()

        // Si no hay entradas en el backstack, finaliza la actividad
        if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
            requireActivity().finish()
        }
    }

    // Método para navegar directamente a la pantalla de inicio
    private fun navigateToHome() {
        try {
            // Obtenemos la referencia a MainActivity
            val mainActivity = requireActivity() as MainActivity

            // Cargamos el fragmento de inicio y seleccionamos el ítem de navegación correspondiente
            mainActivity.loadFragmentAndSelectNavItem(HomeFragment(), R.id.navigation_home)
        } catch (e: Exception) {
            // Si hay algún error, intentamos volver atrás como alternativa
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el callback para el botón de retroceso
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar de vuelta a la actividad principal
                navigateBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF0A1622)
                    ) {
                        ConsejosRecursosScreen()
                    }
                }
            }
        }
    }

    // Colores para las tarjetas de preguntas frecuentes
    val faqColors = listOf(
        Color(0xFF2196F3), // Azul
        Color(0xFF4CAF50), // Verde
        Color(0xFFFF9800), // Naranja
        Color(0xFF9C27B0), // Púrpura
        Color(0xFFE91E63), // Rosa
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFFC107), // Ámbar
        Color(0xFF795548)  // Marrón
    )

    @Composable
    fun ExpandableFaqCard(faqItem: FaqItem, color: Color) {
        var expanded by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1F2937) // Fondo oscuro de la tarjeta
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(color, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = faqItem.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = faqItem.question,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Contraer" else "Expandir",
                        tint = Color(0xFF2196F3)
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = faqItem.answer,
                            fontSize = 15.sp,
                            color = Color(0xFFB0B0B0),
                            modifier = Modifier.padding(start = 40.dp) // Indent answer slightly
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ConsejosRecursosScreen() {
        // Función para mostrar preguntas y respuestas frecuentes

        // Secciones principales
        var mainSectionIndex by remember { mutableStateOf(if (showFaq) 1 else 0) }
        val mainSections = listOf("Consejos", "Preguntas Frecuentes", "Guía de Preparación")

        // Categorías de recursos
        var resourceCategoryIndex by remember { mutableStateOf(0) }
        val resourceCategories = listOf("Libros", "Cursos", "Podcasts", "Videos")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1622))
                .padding(top = 16.dp)
        ) {
            // Título con flecha a la izquierda
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Botón de flecha para volver a la pantalla de inicio
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { navigateToHome() }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver a inicio",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Consejos y Recursos",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Pestañas de secciones principales que ocupan todo el ancho
            TabRow(
                selectedTabIndex = mainSectionIndex,
                containerColor = Color(0xFF0A1622),
                contentColor = Color.White,
                divider = { /* Sin divisor */ },
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[mainSectionIndex]),
                        height = 3.dp,
                        color = Color(0xFF1A80E5)
                    )
                }
            ) {
                mainSections.forEachIndexed { index, section ->
                    Tab(
                        selected = mainSectionIndex == index,
                        onClick = { mainSectionIndex = index },
                        modifier = Modifier.weight(1f), // Hacer que cada pestaña ocupe el mismo espacio
                        text = {
                            // Organizar los títulos en dos líneas si es necesario
                            when (index) {
                                1 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "Preguntas",
                                            fontWeight = if (mainSectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center // Centrar el texto
                                        )
                                        Text(
                                            text = "Frecuentes",
                                            fontWeight = if (mainSectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center // Centrar el texto
                                        )
                                    }
                                }

                                2 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "Guía de",
                                            fontWeight = if (mainSectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center // Centrar el texto
                                        )
                                        Text(
                                            text = "Preparación",
                                            fontWeight = if (mainSectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center // Centrar el texto
                                        )
                                    }
                                }

                                else -> {
                                    Text(
                                        text = section,
                                        fontWeight = if (mainSectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center // Centrar el texto
                                    )
                                }
                            }
                        },
                        selectedContentColor = Color(0xFF1A80E5),
                        unselectedContentColor = Color.LightGray
                    )
                }
            }

            // Contenido según la sección principal seleccionada
            when (mainSectionIndex) {
                0 -> { // Sección de Consejos con recursos
                    Column {
                        // Tarjeta destacada con animación
                        FeaturedResourceCard()

                        Spacer(modifier = Modifier.height(16.dp))

                        // Pestañas de categorías de recursos
                        ScrollableTabRow(
                            selectedTabIndex = resourceCategoryIndex,
                            containerColor = Color(0xFF0A1622),
                            contentColor = Color.White,
                            edgePadding = 16.dp,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[resourceCategoryIndex]),
                                    height = 2.dp,
                                    color = Color(0xFF1A80E5)
                                )
                            }
                        ) {
                            resourceCategories.forEachIndexed { index, category ->
                                Tab(
                                    selected = resourceCategoryIndex == index,
                                    onClick = { resourceCategoryIndex = index },
                                    text = {
                                        Text(
                                            text = category,
                                            fontWeight = if (resourceCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    },
                                    selectedContentColor = Color(0xFF1A80E5),
                                    unselectedContentColor = Color.LightGray
                                )
                            }
                        }

                        // Contenido según la categoría de recursos seleccionada
                        when (resourceCategoryIndex) {
                            0 -> BooksContent()
                            1 -> CoursesContent()
                            2 -> PodcastsContent()
                            3 -> VideosContent()
                        }
                    }
                }

                1 -> { // Sección de Preguntas Frecuentes (Nuevo diseño)
                    val allFaqs = remember {
                        listOf(
                            // Preguntas principales con variedad de íconos
                            FaqItem(
                                "¿Qué es Interviewface?",
                                "Es una aplicación que te ayuda a prepararte para entrevistas de trabajo con simulaciones interactivas y recursos de aprendizaje.",
                                Icons.Filled.Info
                            ),
                            FaqItem(
                                "¿Cómo funciona la simulación de entrevistas?",
                                "Te presentamos preguntas comunes, grabas tus respuestas y recibes retroalimentación para mejorar.",
                                Icons.Filled.VideoLibrary
                            ),

                            // Preparación de entrevistas
                            FaqItem(
                                "¿Cómo me preparo para una entrevista?",
                                "Investiga la empresa, revisa el puesto y prepara ejemplos de tus logros más relevantes.",
                                Icons.Filled.Article
                            ),
                            FaqItem(
                                "¿Qué debo investigar sobre la empresa?",
                                "Revisa su sitio web, misión, visión, cultura organizacional y noticias recientes.",
                                Icons.Filled.Search
                            ),

                            // Uso de la aplicación
                            FaqItem(
                                "¿Cómo inicio una simulación?",
                                "Ve a 'Entrevistas', selecciona el tipo de entrevista y presiona 'Iniciar'.",
                                Icons.Filled.PlayArrow
                            ),
                            FaqItem(
                                "¿Puedo ver mi historial?",
                                "Sí, todas tus entrevistas se guardan en la sección de 'Historial' para que puedas revisarlas.",
                                Icons.Filled.History
                            ),

                            // Recursos y aprendizaje
                            FaqItem(
                                "¿Qué recursos ofrecen?",
                                "Tenemos artículos, guías y enlaces a cursos para mejorar tus habilidades de entrevista.",
                                Icons.Filled.School
                            ),
                            FaqItem(
                                "¿Cómo mejoro mis habilidades?",
                                "Practica regularmente, revisa tus grabaciones y aplica los consejos que te damos.",
                                Icons.Filled.TrendingUp
                            )
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item {
                            // Título en cuadro azul más grande
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1A80E5)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Destacado",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Preguntas Clave",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Descripción fuera del cuadro azul
                            Text(
                                text = "Domina estas preguntas y destaca en tu próxima entrevista. Prepárate para impresionar a los reclutadores con respuestas sólidas y confiables.",
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(allFaqs.size) { index ->
                            val faqItem = allFaqs[index]
                            val colorIndex = index % faqColors.size
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                ExpandableFaqCard(
                                    faqItem = faqItem,
                                    color = faqColors[colorIndex]
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp)) // Espacio adicional al final
                        }
                    }
                }

                2 -> { // Sección de Guía de Preparación
                    val pasosPreparacion = listOf(
                        Triple(
                            "🔍 Investigación",
                            "Conoce a fondo la empresa, su misión, valores y la descripción del puesto.",
                            Color(0xFF4CAF50)
                        ),
                        Triple(
                            "📝 Prepara ejemplos",
                            "Prepara historias de éxito usando el método STAR (Situación, Tarea, Acción, Resultado).",
                            Color(0xFF2196F3)
                        ),
                        Triple(
                            "💡 Practica",
                            "Haz simulacros de entrevista y graba tus respuestas para mejorarlas.",
                            Color(0xFFFF9800)
                        ),
                        Triple(
                            "👔 Prepara tu atuendo",
                            "Elige ropa profesional acorde a la cultura de la empresa con anticipación.",
                            Color(0xFF9C27B0)
                        ),
                        Triple(
                            "📱 Desconecta",
                            "Descansa bien la noche anterior y evita distracciones antes de la entrevista.",
                            Color(0xFFE91E63)
                        )
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Encabezado atractivo
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFF6D00) // Naranja vibrante
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.Lightbulb,
                                            contentDescription = "Guía",
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Guía de Preparación",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Sigue estos pasos clave para llegar perfectamente preparado a tu entrevista y destacar entre los demás candidatos.",
                                        fontSize = 16.sp,
                                        lineHeight = 22.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            // Pasos de preparación
                            Text(
                                text = "Pasos para el Éxito",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }

                        // Lista de pasos
                        items(pasosPreparacion) { (icono, texto, color) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2D3748)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(color, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = icono,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = texto,
                                        fontSize = 15.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Sección de consejos rápidos
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Consejos Rápidos",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )

                            val consejos = listOf(
                                "✅ Llega 10-15 minutos antes",
                                "✅ Lleva copias de tu CV",
                                "✅ Prepara preguntas para el entrevistador",
                                "✅ Mantén contacto visual",
                                "✅ Sigue la regla 80/20: habla 20%, escucha 80%",
                                "✅ Toma agua para mantener la voz clara"
                            )

                            consejos.forEach { consejo ->
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = consejo,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(50.dp)) // Espacio al final
                        }
                    }
                }
            }
        }

        // Implementaciones directas de las funciones de contenido para evitar dependencias externas
        // Implementación de BookCard tomada de RecursosFragment
        @Composable
        fun FeaturedResourceCard() {
            val context = LocalContext.current
            val infiniteTransition = rememberInfiniteTransition(label = "shine")

            // Animación de brillo
            val brightness by infiniteTransition.animateFloat(
                initialValue = 0.9f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "brightness"
            )

            // Animación de línea brillante que recorre la tarjeta
            val shimmerTranslateAnim by infiniteTransition.animateFloat(
                initialValue = -1000f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "shimmer"
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espacio adicional antes de la tarjeta

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .graphicsLayer {
                        this.alpha = brightness
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Fondo con gradiente
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1E40AF), // Azul más oscuro (desde la izquierda)
                                        Color(0xFF3B82F6)  // Azul más claro (hacia la derecha)
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                                )
                            )
                    )

                    // Efecto de línea brillante
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0f),
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0f)
                                    ),
                                    startX = shimmerTranslateAnim - 300,
                                    endX = shimmerTranslateAnim + 300
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Contenido de texto
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = "RECOMENDADO",
                                color = Color(0xFFFFCC00),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Guía Completa\nde Entrevistas",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Recursos gratuitos para\npreparar tus entrevistas:\nguías, ejemplos y consejos",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }

                        // Icono
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
        }

        @Composable
        fun BookCard(
            title: String,
            author: String,
            description: String,
            rating: Float,
            bookColor: Color,
            url: String
        ) {
            val context = LocalContext.current

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Portada del libro
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bookColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Título en la portada con texto centrado
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = 12.sp,
                                maxLines = 3
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Información del libro y descripción
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "por $author",
                            color = Color(0xFF3B82F6),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Valoración
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                val starColor =
                                    if (index < rating) Color(0xFFFFC107) else Color.Gray
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = starColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = rating.toString(),
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Descripción completa
                        Text(
                            text = description,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón de ver detalles
                        Row(
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ver detalles",
                                color = Color(0xFF3B82F6),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun BooksContent() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Libro azul - Guía de Entrevistas
                item {
                    BookCard(
                        title = "Guía de Entrevistas",
                        author = "Salt Lake Community College",
                        description = "Guía completa con consejos prácticos para prepararte antes, durante y después de la entrevista. Incluye ejemplos de respuestas a preguntas comunes.",
                        rating = 4.8f,
                        bookColor = Color(0xFF2196F3), // Azul
                        url = "https://www.slcc.edu/careerservices/docs-and-images/resource-documents/guia-de-entrevista-cuaderno-de-trabajo-espanol.pdf"
                    )
                }

                // Libro verde - Guía Práctica para Entrevistas
                item {
                    BookCard(
                        title = "Guía Práctica para Entrevistas",
                        author = "Liderazgo Sin Límites",
                        description = "Manual práctico para entrevistadores y candidatos con técnicas efectivas para destacar en el proceso de selección laboral.",
                        rating = 4.6f,
                        bookColor = Color(0xFF4CAF50), // Verde
                        url = "https://liderazgosinlimites.com/wp-content/uploads/2016/06/5.1-Guia-Entrevistas.pdf"
                    )
                }

                // Libro rojo - Preguntas Comunes de Entrevista
                item {
                    BookCard(
                        title = "Preguntas Comunes de Entrevista",
                        author = "Santa Rosa Junior College",
                        description = "Colección de las preguntas más frecuentes en entrevistas de trabajo con estrategias para responderlas de manera efectiva.",
                        rating = 4.7f,
                        bookColor = Color(0xFFF44336), // Rojo
                        url = "https://careerhub.santarosa.edu/sites/careerhub.santarosa.edu/files/documents/Common%20Job%20Interview%20Questions%20%28Spanish%29.pdf"
                    )
                }
            }
        }

        @Composable
        fun CoursesContent() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp), // Mismo espaciado que los libros
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    CourseCard(
                        title = "El Arte de la Entrevista de Trabajo",
                        instructor = "Coursera - Big Interview",
                        description = "Aprende técnicas comprobadas para convertir tus entrevistas en ofertas de trabajo, con prácticas interactivas y consejos de expertos.",
                        duration = "5 semanas",
                        level = "Todos los niveles",
                        url = "https://www.coursera.org/learn/entrevista-de-trabajo"
                    )
                }

                item {
                    CourseCard(
                        title = "Mi Primer Empleo",
                        instructor = "Coursera - Universidad de Chile",
                        description = "Aprende a preparar tu curriculum, gestionar la entrevista laboral y transitar con éxito de la formación profesional al mundo del trabajo.",
                        duration = "4 semanas",
                        level = "Principiante",
                        url = "https://www.coursera.org/learn/mi-primer-empleo"
                    )
                }

                item {
                    CourseCard(
                        title = "Fundamentos para Entrevistar con Confianza",
                        instructor = "Coursera - SV Academy",
                        description = "Desarrolla tu marca personal, crea una cartera profesional poderosa y aprende a destacar en las entrevistas para conseguir el trabajo que deseas.",
                        duration = "4 semanas",
                        level = "Intermedio",
                        url = "https://www.coursera.org/learn/sales-interview-es"
                    )
                }
            }
        }

        @Composable
        fun PodcastCard(
            title: String,
            host: String,
            description: String,
            episodes: Int,
            url: String
        ) {
            val context = LocalContext.current
            val greenColor = Color(0xFF4CAF50) // Verde original para textos
            val brightGreenColor = Color(0xFF00FF00) // Verde fosforescente solo para el icono
            val lightGreenColor = Color(0xFF8AFF8A).copy(alpha = 0.2f)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)), // Mismo color gris que los libros
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Icono del podcast con fondo verde claro y borde en la esquina superior izquierda
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(lightGreenColor)
                            .border(
                                1.dp,
                                greenColor.copy(alpha = 0.7f),
                                RoundedCornerShape(8.dp)
                            ) // Borde verde normal
                            .align(Alignment.TopStart),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = brightGreenColor, // Verde fosforescente para el icono
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Contenido a la derecha
                    Column(
                        modifier = Modifier
                            .padding(start = 66.dp) // Espacio para el icono
                    ) {
                        // Título en negrita y más grande
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 18.sp, // Tamaño aumentado
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Autor con color verde original
                        Text(
                            text = "Presentado por $host",
                            color = greenColor, // Verde original
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Descripción con más espacio
                        Text(
                            text = description,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            maxLines = 4, // Aumentado para mostrar más texto
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Más espacio

                        // Contenedor para la duración y el botón escuchar
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Duración arriba a la izquierda
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Usando un icono alternativo para los audífonos
                                Icon(
                                    imageVector = Icons.Default.AccessTime, // Alternativa para audífonos
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$episodes episodios",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical entre elementos

                            // Botón de escuchar abajo a la derecha
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd // Alinear a la derecha
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    },
                                    border = BorderStroke(1.dp, greenColor), // Borde verde
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = greenColor // Verde original
                                    ),
                                    shape = RoundedCornerShape(50),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow, // Icono de reproducción
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Escuchar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = greenColor // Texto en verde
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        fun PodcastsContent() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ), // Mismo padding que BooksContent
                verticalArrangement = Arrangement.spacedBy(24.dp), // Mismo espaciado que BooksContent
                contentPadding = PaddingValues(bottom = 100.dp) // Mismo padding inferior que BooksContent
            ) {
                item {
                    PodcastCard(
                        title = "La clave para hacer bien una entrevista",
                        host = "Desarrollo profesional",
                        description = "Podcast reconocido que revela las claves esenciales para destacar en entrevistas laborales, con consejos prácticos de expertos en selección de personal.",
                        episodes = 6,
                        url = "https://open.spotify.com/episode/65TcatKtbtex7mrVKKS88n"
                    )
                }

                item {
                    PodcastCard(
                        title = "Entrevistas de trabajo: Desde el lado del que contrata",
                        host = "El Podcast de Wisdenn",
                        description = "Perspectiva única desde el punto de vista del reclutador, revelando lo que realmente buscan los empleadores durante las entrevistas.",
                        episodes = 5,
                        url = "https://open.spotify.com/episode/5GqpzeYNZtbyTxiyjKQ6ba"
                    )
                }

                item {
                    PodcastCard(
                        title = "Preguntas para destacar en una entrevista",
                        host = "Desarrollo profesional",
                        description = "Aprende qué preguntas hacer durante una entrevista para impresionar a los reclutadores y destacar entre los demás candidatos.",
                        episodes = 8,
                        url = "https://open.spotify.com/episode/35vYxhvxAXvu0eOzPEOwXz"
                    )
                }
            }
        }

        @Composable
        fun VideosContent() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ), // Mismo padding que BooksContent
                verticalArrangement = Arrangement.spacedBy(24.dp), // Mismo espaciado que BooksContent
                contentPadding = PaddingValues(bottom = 100.dp) // Mismo padding inferior que BooksContent
            ) {
                item {
                    VideoCard(
                        title = "Tips para una buena entrevista",
                        creator = "Popular",
                        description = "Aprende los consejos más efectivos para destacar en tus entrevistas laborales y causar una excelente primera impresión.",
                        duration = "0:45",
                        url = "https://youtu.be/51tcTh8jDus?si=X_h41gDynR2LO3s1"
                    )
                }

                item {
                    VideoCard(
                        title = "Preguntas mas desafiantes",
                        creator = "Michael Page",
                        description = "Estrategias para responder con confianza a las preguntas más complicadas que suelen hacer los reclutadores.",
                        duration = "2:35",
                        url = "https://youtu.be/jQ5Ua5zv-Y4?si=BAPzonY08HITSC0V"
                    )
                }

                item {
                    VideoCard(
                        title = "Lenguaje corporal efectivo",
                        creator = "Psicologia visual",
                        description = "Aprende a utilizar el lenguaje corporal a tu favor durante las entrevistas para proyectar seguridad y profesionalismo.",
                        duration = "3:55",
                        url = "https://youtu.be/l-jgyTmqPHs?si=xbMDmMrQOLJswsO1"
                    )
                }
            }
        }

        @Composable
        fun VideoCard(
            title: String,
            creator: String,
            description: String,
            duration: String,
            url: String
        ) {
            val context = LocalContext.current

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)), // Mismo color gris que los libros
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Icono de reproducción rectangular
                        Box(
                            modifier = Modifier
                                .size(width = 120.dp, height = 76.dp) // Tamaño rectangular
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0D1721)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp), // Icono más grande
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            // Autor y duración en la misma línea
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = creator,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1A80E5)
                                )

                                // Mayor separación antes del punto
                                Spacer(modifier = Modifier.width(8.dp))

                                // Punto separador
                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )

                                // Mayor separación después del punto
                                Spacer(modifier = Modifier.width(8.dp))

                                // Duración
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = duration,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        @Composable
        fun PreguntaRespuestaCard(categoria: String, preguntas: List<Pair<String, String>>) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = categoria,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    preguntas.forEach { (pregunta, respuesta) ->
                        Text(
                            text = pregunta,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = respuesta,
                            fontSize = 16.sp,
                            color = Color(0xFFB0B0B0),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (pregunta != preguntas.last().first) {
                            Divider(
                                color = Color(0xFF3A3F47),
                                thickness = 1.dp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}