package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme

class PrivacyPolicyFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el manejo del botón de atrás
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    // Navegar a la pantalla de configuración
                    if (requireActivity() is MainActivity) {
                        val mainActivity = requireActivity() as MainActivity
                        mainActivity.loadFragment(ConfiguracionFragment())
                    } else {
                        // Si no es MainActivity, simplemente volver atrás
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                } catch (e: Exception) {
                    // En caso de error, intentar volver atrás de forma segura
                    try {
                        requireActivity().supportFragmentManager.popBackStack()
                    } catch (e2: Exception) {
                        // Ignorar si hay algún error
                    }
                }
            }
        })
        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PrivacyPolicyScreen(
                            onBackPressed = {
                                // Volver al fragmento de configuración
                                val mainActivity = requireActivity() as MainActivity
                                mainActivity.loadFragment(ConfiguracionFragment())
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun PrivacyPolicyScreen(onBackPressed: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            // Cabecera con botón de regreso y título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
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
                
                Text(
                    text = "Política de privacidad",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Fecha de actualización
            Text(
                text = "Última actualización: 30 de mayo de 2025",
                fontSize = 14.sp,
                color = Color.LightGray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Introducción
            Text(
                text = "En InterviewFace, valoramos y respetamos tu privacidad. Esta Política de Privacidad describe cómo recopilamos, utilizamos, compartimos y protegemos tu información personal cuando utilizas nuestra aplicación.",
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Contenido de la política de privacidad
            PrivacySection(
                title = "1. Información que recopilamos",
                content = "Recopilamos los siguientes tipos de información:\n\n• Información de registro: nombre, correo electrónico, número de teléfono, nivel educativo y especialidad.\n\n• Información de uso: cómo interactúas con la aplicación, tus respuestas en las entrevistas de práctica y tu progreso.\n\n• Información del dispositivo: modelo, sistema operativo, identificadores únicos y datos de red."
            )
            
            PrivacySection(
                title = "2. Cómo utilizamos tu información",
                content = "Utilizamos tu información para:\n\n• Proporcionar, mantener y mejorar nuestros servicios\n• Personalizar tu experiencia\n• Comunicarnos contigo sobre actualizaciones, ofertas y eventos\n• Analizar tendencias de uso y mejorar la aplicación\n• Prevenir actividades fraudulentas y proteger la seguridad de nuestros usuarios"
            )
            
            PrivacySection(
                title = "3. Compartición de información",
                content = "No vendemos tu información personal a terceros. Podemos compartir información en las siguientes circunstancias:\n\n• Con proveedores de servicios que nos ayudan a operar la aplicación\n• Para cumplir con obligaciones legales\n• En caso de fusión, venta o transferencia de activos\n• Con tu consentimiento"
            )
            
            PrivacySection(
                title = "4. Seguridad de datos",
                content = "Implementamos medidas de seguridad técnicas y organizativas para proteger tu información personal contra acceso no autorizado, pérdida o alteración. Sin embargo, ningún sistema es completamente seguro, por lo que no podemos garantizar la seguridad absoluta de tu información."
            )
            
            PrivacySection(
                title = "5. Tus derechos",
                content = "Dependiendo de tu ubicación, puedes tener derechos relacionados con tus datos personales, como:\n\n• Acceder a tu información\n• Corregir información inexacta\n• Eliminar tu información\n• Oponerte al procesamiento de tus datos\n• Retirar tu consentimiento\n\nPara ejercer estos derechos, contáctanos a través de la aplicación o por correo electrónico."
            )
            
            PrivacySection(
                title = "6. Retención de datos",
                content = "Conservamos tu información personal mientras mantengas una cuenta activa o según sea necesario para proporcionarte servicios. Puedes solicitar la eliminación de tu cuenta en cualquier momento, tras lo cual eliminaremos o anonimizaremos tu información personal, a menos que debamos conservarla por motivos legales."
            )
            
            PrivacySection(
                title = "7. Cambios a esta política",
                content = "Podemos actualizar esta Política de Privacidad periódicamente. Te notificaremos sobre cambios significativos a través de la aplicación o por correo electrónico. Te recomendamos revisar esta política regularmente."
            )
            
            PrivacySection(
                title = "8. Contacto",
                content = "Si tienes preguntas o inquietudes sobre esta Política de Privacidad o sobre cómo manejamos tus datos personales, contáctanos en: interviewface032@gmail.com"
            )
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
    
    @Composable
    fun PrivacySection(title: String, content: String) {
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = content,
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 24.sp
            )
        }
    }
}
