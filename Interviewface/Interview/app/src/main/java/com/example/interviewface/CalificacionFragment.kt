package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme
import com.example.interviewface.ui.theme.StandardGray
import com.example.interviewface.HomeFragment

class CalificacionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el manejo del botón de atrás
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar a la pantalla de inicio y seleccionar el botón correspondiente
                val mainActivity = requireActivity() as MainActivity
                mainActivity.loadFragmentAndSelectNavItem(HomeFragment(), R.id.navigation_home)
            }
        })
        

        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CalificacionScreen(
                            onBackPressed = {
                                // Obtener la actividad principal
                                val mainActivity = requireActivity() as MainActivity
                                // Cargar el fragmento de inicio y seleccionar la pestaña correspondiente
                                mainActivity.loadFragmentAndSelectNavItem(HomeFragment(), R.id.navigation_home)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CalificacionScreen(onBackPressed: () -> Unit) {
        var selectedRating by remember { mutableStateOf(0) }
        var ayudoText by remember { mutableStateOf("") }
        var mejorarText by remember { mutableStateOf("") }
        var showConfirmationDialog by remember { mutableStateOf(false) }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Botón de flecha hacia atrás y título centrado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de flecha a la izquierda
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                
                // Título centrado
                Text(
                    text = "Calificación",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // Espacio para equilibrar el diseño
                Spacer(modifier = Modifier.size(40.dp))
            }
            
            // Puntuación general y barras de progreso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Columna izquierda con número y estrellas
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.width(80.dp)
                ) {
                    // Número grande de calificación
                    Text(
                        text = "3",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    // Estrellas
                    Row {
                        repeat(3) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        repeat(2) {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    // Número de reseñas
                    Text(
                        text = "1k reseñas",
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Espacio entre columnas
                Spacer(modifier = Modifier.width(16.dp))
                
                // Columna derecha con barras de progreso
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Barras de progreso para cada estrella
                    BarraProgreso(numero = "5", porcentaje = 0.3f, texto = "30%")
                    BarraProgreso(numero = "4", porcentaje = 0.35f, texto = "35%")
                    BarraProgreso(numero = "3", porcentaje = 0.2f, texto = "20%")
                    BarraProgreso(numero = "2", porcentaje = 0.1f, texto = "10%")
                    BarraProgreso(numero = "1", porcentaje = 0.05f, texto = "5%")
                }
            }
            
            // Espacio adicional antes de la siguiente sección
            Spacer(modifier = Modifier.height(16.dp))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ¿Cómo calificarías tu experiencia?
            Text(
                text = "¿Cómo calificarías tu experiencia?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // Botones de estrellas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BotonEstrella("1", isSelected = selectedRating == 1) { selectedRating = 1 }
                BotonEstrella("2", isSelected = selectedRating == 2) { selectedRating = 2 }
                BotonEstrella("3", isSelected = selectedRating == 3) { selectedRating = 3 }
                BotonEstrella("4", isSelected = selectedRating == 4) { selectedRating = 4 }
                BotonEstrella("5", isSelected = selectedRating == 5) { selectedRating = 5 }
            }
            
            // Campo: Que fue lo que más te ayudó?
            Text(
                text = "Que fue lo que más te ayudó?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = ayudoText,
                onValueChange = { ayudoText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFF3A3F47), RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F2937),
                    unfocusedContainerColor = Color(0xFF1F2937),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFF3A3F47),
                    unfocusedBorderColor = Color(0xFF3A3F47)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            
            // Campo: Qué se puede mejorar?
            Text(
                text = "Qué se puede mejorar?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = mejorarText,
                onValueChange = { mejorarText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFF3A3F47), RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F2937),
                    unfocusedContainerColor = Color(0xFF1F2937),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFF3A3F47),
                    unfocusedBorderColor = Color(0xFF3A3F47)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Variable para controlar si se muestra un mensaje de error
            var showError by remember { mutableStateOf(false) }
            
            // Botón de enviar comentarios
            Button(
                onClick = { 
                    // Verificar que los campos estén completos
                    if (selectedRating > 0 && ayudoText.isNotBlank() && mejorarText.isNotBlank()) {
                        showConfirmationDialog = true
                        showError = false
                    } else {
                        // Mostrar mensaje de error si los campos no están completos
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Enviar comentarios", 
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Mostrar mensaje de error si es necesario
            if (showError) {
                Text(
                    text = "Por favor, completa todos los campos antes de enviar",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            // Diálogo de confirmación
            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    containerColor = Color(0xFF1F2937),
                    title = {
                        Text(
                            "¡Gracias por tu calificación!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            "Tu calificación ha sido enviada con éxito.",
                            color = Color.White
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showConfirmationDialog = false
                                // Limpiar los campos después de enviar
                                selectedRating = 0
                                ayudoText = ""
                                mejorarText = ""
                                
                                // Redireccionar a la pantalla de inicio
                                val mainActivity = requireActivity() as MainActivity
                                mainActivity.loadFragmentAndSelectNavItem(HomeFragment(), R.id.navigation_home)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Aceptar", color = Color.White)
                        }
                    }
                )
            }
            
            // Espacio adicional al final para asegurar que todo sea visible al desplazarse
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    @Composable
    fun BarraProgreso(numero: String, porcentaje: Float, texto: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Número de estrellas
            Text(
                text = numero,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.width(16.dp)
            )
            
            // Barra de progreso
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF1F2937))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(porcentaje)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF2196F3))
                )
            }
            
            // Porcentaje
            Text(
                text = texto,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.End
            )
        }
    }
    
    @Composable
    fun BotonEstrella(text: String, isSelected: Boolean = false, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color(0xFF2196F3) else StandardGray
            ),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Text(text = text, color = Color.White, fontSize = 14.sp)
        }
    }
}
