package com.example.interviewface

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import kotlin.random.Random

class RecursosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el manejo del botón de atrás
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar a la pantalla de consejos y seleccionar el botón correspondiente
                val mainActivity = requireActivity() as MainActivity
                mainActivity.loadFragmentAndSelectNavItem(TipsFragment(), R.id.navigation_comments)
            }
        })
        

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(
                    colorScheme = darkColorScheme(
                        background = Color(0xFF121A21),
                        surface = Color(0xFF17222E),
                        primary = Color(0xFF1A80E5),
                        secondary = Color(0xFFFFB800),
                        tertiary = Color(0xFF00C853)
                    )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RecursosScreen(
                            onBackPressed = {
                                // Navegar a la pantalla de consejos y seleccionar el botón correspondiente
                                val mainActivity = requireActivity() as MainActivity
                                mainActivity.loadFragmentAndSelectNavItem(TipsFragment(), R.id.navigation_comments)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecursosScreen(onBackPressed: () -> Unit) {
    // Estados para las animaciones y expansiones
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val categories = listOf("Libros", "Cursos", "Podcasts", "Videos")
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con gradiente y efecto de partículas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1721),
                            Color(0xFF121A21),
                            Color(0xFF17222E)
                        )
                    )
                )
        )
        
        // Contenido principal
        Column(modifier = Modifier.fillMaxSize()) {
            // Encabezado con efecto de cristal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Botón de regreso con animación
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Color(0xFF1A80E5), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Título con efecto de resaltado
                Text(
                    text = "Recursos Gratuitos",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tarjeta de destacados con animación
            FeaturedResourceCard()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Pestañas de categorías con indicador animado
            CategoryTabs(
                categories = categories,
                selectedIndex = selectedCategoryIndex,
                onCategorySelected = { selectedCategoryIndex = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Contenido según la categoría seleccionada
            when (selectedCategoryIndex) {
                0 -> BooksContent()
                1 -> CoursesContent()
                2 -> PodcastsContent()
                3 -> VideosContent()
            }
        }
    }
}

@Composable
fun FeaturedResourceCard() {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "shine")
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shine"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp),
            // Se quitó la funcionalidad de enlace pero se mantiene la animación
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1A3A6E),
                            Color(0xFF1A80E5)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
                .drawBehind {
                    // Efecto de brillo que se mueve
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0x00FFFFFF),
                                Color(0x40FFFFFF),
                                Color(0x00FFFFFF)
                            ),
                            start = Offset(shimmerPosition, 0f),
                            end = Offset(shimmerPosition + 500f, 0f)
                        )
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
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
                        color = Color(0xFFFFB800),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Guía Completa de Entrevistas",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Recursos gratuitos para preparar tus entrevistas: guías, ejemplos y consejos prácticos",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Visible
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón de acceso (sin funcionalidad de enlace)
                    Button(
                        onClick = { /* Se quitó la funcionalidad de enlace */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1A80E5)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Ver guía gratuita",
                            fontWeight = FontWeight.Bold
                        )
                    }
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
fun CategoryTabs(categories: List<String>, selectedIndex: Int, onCategorySelected: (Int) -> Unit) {
    val indicatorOffset = remember { Animatable(0f) }
    val tabWidth = 80.dp
    val density = LocalDensity.current.density
    
    LaunchedEffect(selectedIndex) {
        indicatorOffset.animateTo(
            targetValue = selectedIndex * tabWidth.value,
            animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow)
        )
    }
    
    Box(modifier = Modifier.fillMaxWidth()) {
        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            categories.forEachIndexed { index, category ->
                val selected = index == selectedIndex
                
                Box(
                    modifier = Modifier
                        .width(tabWidth)
                        .clickable { onCategorySelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
        }
        
        // Indicador animado
        Box(
            modifier = Modifier
                .offset(x = with(LocalDensity.current) { (indicatorOffset.value * density).toDp() })
                .width(tabWidth)
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomStart)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = Color(0xFF1A80E5)
            )
        }
    }
}

@Composable
fun BooksContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            BookCard(
                title = "Guía de Entrevistas",
                author = "Salt Lake Community College",
                description = "Guía completa con consejos prácticos para prepararte antes, durante y después de la entrevista. Incluye ejemplos de respuestas a preguntas comunes.",
                rating = 4.8f,
                bookColor = Color(0xFF1A73E8),
                url = "https://www.slcc.edu/careerservices/docs-and-images/resource-documents/guia-de-entrevista-cuaderno-de-trabajo-espanol.pdf"
            )
        }
        
        item {
            BookCard(
                title = "Guía Práctica para Entrevistas",
                author = "Liderazgo Sin Límites",
                description = "Manual práctico para entrevistadores y candidatos con técnicas efectivas para destacar en el proceso de selección laboral.",
                rating = 4.6f,
                bookColor = Color(0xFF43A047),
                url = "https://liderazgosinlimites.com/wp-content/uploads/2016/06/5.1-Guia-Entrevistas.pdf"
            )
        }
        
        item {
            BookCard(
                title = "Preguntas Comunes de Entrevista",
                author = "Santa Rosa Junior College",
                description = "Colección de las preguntas más frecuentes en entrevistas de trabajo con estrategias para responderlas de manera efectiva.",
                rating = 4.7f,
                bookColor = Color(0xFFE53935),
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
fun PodcastsContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
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
fun VideoCard(title: String, creator: String, description: String, duration: String, url: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF17222E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Miniatura del video con icono de reproducción
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F1621)),
                    contentAlignment = Alignment.Center
                ) {
                    // Fondo oscuro para la miniatura
                    Box(
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Icono de reproducción
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Información del video
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Título del video
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Creador y duración
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = creator,
                            color = Color(0xFF1A80E5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Separador
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color.Gray.copy(alpha = 0.5f), CircleShape)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Duración
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(2.dp))
                            
                            Text(
                                text = duration,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Descripción
            Text(
                text = description,
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ToolsContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            ToolCard(
                title = "CV Builder Pro",
                description = "Crea un currículum de impacto con plantillas profesionales y consejos personalizados.",
                icon = Icons.Default.Description,
                url = "https://example.com/cv-builder"
            )
        }
        
        item {
            ToolCard(
                title = "Interview Prep AI",
                description = "Practica con un asistente de IA que analiza tus respuestas y ofrece feedback en tiempo real.",
                icon = Icons.Default.Psychology,
                url = "https://example.com/interview-ai"
            )
        }
    }
}

@Composable
fun BookCard(title: String, author: String, description: String, rating: Float, bookColor: Color, url: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF17222E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Portada del libro estilizada
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bookColor)
                    .border(1.dp, bookColor.copy(alpha = 0.7f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Detalles del libro
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Autor
                Text(
                    text = "por $author",
                    color = Color(0xFF1A80E5),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Valoración
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFB800) else Color.Gray.copy(alpha = 0.3f),
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
                
                // Descripción
                Text(
                    text = description,
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Botón de ver más
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF1A80E5)
                    )
                ) {
                    Text("Ver detalles")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CourseCard(title: String, instructor: String, description: String, duration: String, level: String, url: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF17222E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono del curso
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFB800).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Título e instructor
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = instructor,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Descripción
            Text(
                text = description,
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Detalles del curso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Duración
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = duration,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
                
                // Nivel
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = level,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón de inscripción
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB800),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Inscribirse",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PodcastCard(title: String, host: String, description: String, episodes: Int, url: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF17222E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Imagen del podcast
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF00C853).copy(alpha = 0.1f))
                    .border(1.dp, Color(0xFF00C853).copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color(0xFF00C853),
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Detalles del podcast
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Presentado por $host",
                    color = Color(0xFF00C853),
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = description,
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Episodios
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$episodes episodios",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botón de escuchar
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.End),
                    border = BorderStroke(1.dp, Color(0xFF00C853)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF00C853)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Escuchar")
                }
            }
        }
    }
}

@Composable
fun ToolCard(title: String, description: String, icon: ImageVector, url: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF17222E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de la herramienta
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A80E5).copy(alpha = 0.1f))
                    .border(1.dp, Color(0xFF1A80E5).copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1A80E5),
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Detalles de la herramienta
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            
            // Flecha para acceder
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF1A80E5),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
