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

class TermsOfServiceFragment : Fragment() {
    
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
                        TermsOfServiceScreen(
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
    fun TermsOfServiceScreen(onBackPressed: () -> Unit) {
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
                    text = "Términos de servicio",
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
            
            // Contenido de los términos de servicio
            TermsSection(
                title = "1. Aceptación de los términos",
                content = "Al acceder y utilizar InterviewFace, aceptas estar legalmente obligado por estos Términos de Servicio. Si no estás de acuerdo con alguno de estos términos, no debes utilizar nuestros servicios."
            )
            
            TermsSection(
                title = "2. Descripción del servicio",
                content = "InterviewFace es una plataforma diseñada para ayudar a los usuarios a prepararse para entrevistas laborales mediante simulaciones, consejos y recursos educativos. Nos reservamos el derecho de modificar, suspender o interrumpir cualquier aspecto del servicio en cualquier momento."
            )
            
            TermsSection(
                title = "3. Registro y cuenta",
                content = "Para utilizar ciertas funciones de InterviewFace, debes crear una cuenta proporcionando información precisa y completa. Eres responsable de mantener la confidencialidad de tu contraseña y de todas las actividades que ocurran bajo tu cuenta."
            )
            
            TermsSection(
                title = "4. Uso aceptable",
                content = "Te comprometes a utilizar InterviewFace únicamente para fines legales y de acuerdo con estos términos. No debes:\n\n• Utilizar el servicio para actividades ilegales o fraudulentas\n• Intentar acceder a cuentas o sistemas sin autorización\n• Distribuir malware o contenido dañino\n• Interferir con el funcionamiento normal del servicio\n• Infringir derechos de propiedad intelectual"
            )
            
            TermsSection(
                title = "5. Contenido del usuario",
                content = "Al compartir contenido en InterviewFace, conservas tus derechos de propiedad intelectual, pero nos otorgas una licencia mundial, no exclusiva y libre de regalías para usar, reproducir y distribuir dicho contenido en relación con el servicio."
            )
            
            TermsSection(
                title = "6. Privacidad",
                content = "Tu privacidad es importante para nosotros. Nuestra Política de Privacidad describe cómo recopilamos, usamos y protegemos tu información personal."
            )
            
            TermsSection(
                title = "7. Limitación de responsabilidad",
                content = "InterviewFace se proporciona 'tal cual' y 'según disponibilidad', sin garantías de ningún tipo. No seremos responsables por daños indirectos, incidentales, especiales o consecuentes que resulten del uso o la imposibilidad de usar nuestros servicios."
            )
            
            TermsSection(
                title = "8. Modificaciones a los términos",
                content = "Nos reservamos el derecho de modificar estos términos en cualquier momento. Las modificaciones entrarán en vigor inmediatamente después de su publicación. El uso continuado de InterviewFace después de cualquier cambio constituye tu aceptación de los nuevos términos."
            )
            
            TermsSection(
                title = "9. Terminación",
                content = "Podemos suspender o terminar tu acceso a InterviewFace en cualquier momento, por cualquier motivo, sin previo aviso. Tú también puedes terminar tu cuenta en cualquier momento."
            )
            
            TermsSection(
                title = "10. Ley aplicable",
                content = "Estos términos se regirán e interpretarán de acuerdo con las leyes del país donde operamos, sin tener en cuenta sus disposiciones sobre conflictos de leyes."
            )
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
    
    @Composable
    fun TermsSection(title: String, content: String) {
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
