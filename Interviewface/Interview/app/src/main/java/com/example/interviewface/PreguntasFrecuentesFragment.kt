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

class PreguntasFrecuentesFragment : Fragment() {

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
                        PreguntasFrecuentesScreen(
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
    fun PreguntasFrecuentesScreen(onBackPressed: () -> Unit) {
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
                    text = "Preguntas frecuentes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Preguntas sobre experiencia y habilidades
            PreguntaRespuestaCard(
                categoria = "Sobre tu experiencia y habilidades",
                preguntas = listOf(
                    "Háblame de ti" to "Resume tu experiencia profesional, habilidades clave y logros relevantes para el puesto. Mantén la respuesta breve y enfocada en aspectos profesionales.",
                    "¿Cuáles son tus fortalezas?" to "Menciona 2-3 fortalezas relevantes para el puesto, con ejemplos concretos que demuestren cada una.",
                    "¿Cuáles son tus debilidades?" to "Menciona una debilidad real pero no crítica para el puesto, y explica qué estás haciendo para mejorarla.",
                    "¿Por qué deberíamos contratarte?" to "Destaca cómo tus habilidades y experiencia se alinean con los requisitos del puesto y cómo puedes aportar valor a la empresa."
                )
            )
            
            // Preguntas sobre motivación y objetivos
            PreguntaRespuestaCard(
                categoria = "Sobre tu motivación y objetivos",
                preguntas = listOf(
                    "¿Por qué quieres trabajar aquí?" to "Demuestra que has investigado sobre la empresa. Menciona aspectos específicos que te atraen como su cultura, proyectos o valores.",
                    "¿Dónde te ves en 5 años?" to "Muestra ambición realista y alineada con la trayectoria profesional del puesto, sin comprometerte con planes específicos.",
                    "¿Por qué dejaste/quieres dejar tu trabajo anterior?" to "Enfócate en lo que buscas en tu próximo rol, no en aspectos negativos de tu empleo actual o anterior.",
                    "¿Qué te motiva?" to "Menciona factores intrínsecos (crecimiento, aprendizaje) y extrínsecos (logros, reconocimiento) relevantes para el puesto."
                )
            )
            
            // Preguntas situacionales
            PreguntaRespuestaCard(
                categoria = "Preguntas situacionales",
                preguntas = listOf(
                    "Cuéntame sobre un desafío que hayas enfrentado y cómo lo resolviste" to "Utiliza el método STAR: Situación, Tarea, Acción y Resultado. Elige un ejemplo relevante para el puesto.",
                    "¿Cómo manejas el estrés y la presión?" to "Describe estrategias específicas que utilizas, con ejemplos de situaciones donde las aplicaste exitosamente.",
                    "Describe una situación donde hayas trabajado en equipo" to "Destaca tu rol específico, cómo colaboraste con otros y el resultado positivo que lograron juntos.",
                    "¿Cómo manejas los conflictos?" to "Explica tu enfoque para resolver conflictos de manera constructiva, con un ejemplo concreto."
                )
            )
            
            // Preguntas sobre el puesto y la empresa
            PreguntaRespuestaCard(
                categoria = "Sobre el puesto y la empresa",
                preguntas = listOf(
                    "¿Qué sabes sobre nuestra empresa?" to "Demuestra que has investigado: menciona productos/servicios, valores, logros recientes o noticias relevantes.",
                    "¿Tienes alguna pregunta para nosotros?" to "Siempre ten 2-3 preguntas preparadas sobre el puesto, el equipo, los objetivos o la cultura de la empresa.",
                    "¿Cuáles son tus expectativas salariales?" to "Investiga el rango salarial para el puesto y menciona un rango realista basado en tu experiencia y habilidades.",
                    "¿Estás dispuesto a viajar/reubicarte?" to "Responde honestamente, pero si tienes limitaciones, explica posibles soluciones o alternativas."
                )
            )
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(50.dp))
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
