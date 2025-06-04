package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme

class GuiaPreparacionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el manejo del botón de atrás
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar a la pantalla de consejos con la pestaña de Consejos seleccionada
                val mainActivity = requireActivity() as MainActivity
                // Crear una nueva instancia de TipsFragment sin argumentos para que muestre la pestaña de Consejos por defecto
                val tipsFragment = TipsFragment()
                mainActivity.loadFragmentAndSelectNavItem(tipsFragment, R.id.navigation_comments)
            }
        })
        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GuiaPreparacionScreen(
                            onBackPressed = {
                                // Volver a la pantalla de consejos con la pestaña de Consejos seleccionada
                                val mainActivity = requireActivity() as MainActivity
                                // Crear una nueva instancia de TipsFragment sin argumentos para que muestre la pestaña de Consejos por defecto
                                val tipsFragment = TipsFragment()
                                mainActivity.loadFragmentAndSelectNavItem(tipsFragment, R.id.navigation_comments)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun GuiaPreparacionScreen(onBackPressed: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera con botón de regreso y título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Botón de flecha a la izquierda
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                
                // Título centrado
                Text(
                    text = "Guía de preparación",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Contenido principal
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
                        text = "Antes de la entrevista",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Investiga sobre la empresa y el puesto\n• Prepara respuestas para preguntas comunes\n• Revisa tu currículum y prepárate para hablar sobre tu experiencia\n• Prepara preguntas para hacer al entrevistador",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
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
                        text = "Durante la entrevista",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Llega 10-15 minutos antes\n• Mantén contacto visual y postura corporal adecuada\n• Habla con claridad y confianza\n• Utiliza el método STAR para responder preguntas situacionales\n• Muestra entusiasmo por el puesto y la empresa",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
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
                        text = "Después de la entrevista",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Envía un correo de agradecimiento dentro de las 24 horas\n• Reflexiona sobre la entrevista y anota puntos a mejorar\n• Haz seguimiento si no has recibido respuesta después de una semana\n• Prepárate para posibles entrevistas adicionales",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
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
                        text = "Consejos adicionales",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Vístete apropiadamente para la cultura de la empresa\n• Practica entrevistas con amigos o familiares\n• Prepara ejemplos concretos de tus logros\n• Investiga el rango salarial para el puesto\n• Mantén una actitud positiva y auténtica",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
