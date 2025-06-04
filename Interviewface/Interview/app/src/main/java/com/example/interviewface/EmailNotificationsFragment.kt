package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmailNotificationsFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el manejo del botón de atrás
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar a la pantalla de configuración
                val mainActivity = requireActivity() as MainActivity
                mainActivity.loadFragment(ConfiguracionFragment())
            }
        })
        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        EmailNotificationsScreen(
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
    fun EmailNotificationsScreen(onBackPressed: () -> Unit) {
        // Estado para las preferencias de notificaciones por correo
        var notifyWeeklySummary by remember { mutableStateOf(true) }
        var notifyNewFeatures by remember { mutableStateOf(true) }
        var isLoading by remember { mutableStateOf(false) }
        
        // Cargar preferencias del usuario
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        
        LaunchedEffect(key1 = userId) {
            if (userId != null) {
                isLoading = true
                
                // Cargar desde SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("InterviewFacePrefs", android.content.Context.MODE_PRIVATE)
                notifyWeeklySummary = sharedPreferences.getBoolean("${userId}_email_weekly_summary", true)
                notifyNewFeatures = sharedPreferences.getBoolean("${userId}_email_new_features", true)
                
                isLoading = false
            }
        }
        
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
                    text = "Notificaciones por correo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Descripción
            Text(
                text = "Configura qué correos electrónicos deseas recibir de InterviewFace",
                fontSize = 16.sp,
                color = Color.LightGray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                // Opciones de notificaciones por correo
                EmailNotificationSwitchOption(
                    title = "Resumen semanal",
                    description = "Recibe un resumen semanal de tu progreso y actividades en la plataforma",
                    checked = notifyWeeklySummary,
                    onCheckedChange = { 
                        notifyWeeklySummary = it
                        saveEmailPreferences(
                            notifyWeeklySummary,
                            notifyNewFeatures
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EmailNotificationSwitchOption(
                    title = "Nuevas funcionalidades",
                    description = "Recibe información sobre nuevas funciones y mejoras en la aplicación",
                    checked = notifyNewFeatures,
                    onCheckedChange = { 
                        notifyNewFeatures = it
                        saveEmailPreferences(
                            notifyWeeklySummary,
                            notifyNewFeatures
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Información sobre la frecuencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF414548))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Frecuencia de correos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "• Resumen semanal: Una vez por semana\n• Nuevas funcionalidades: Solo cuando haya actualizaciones importantes",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Nota sobre correos importantes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Nota importante",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Siempre recibirás correos relacionados con tu cuenta, seguridad y cambios en los términos de servicio, independientemente de tu configuración.",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
    
    @Composable
    fun EmailNotificationSwitchOption(
        title: String,
        description: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
            
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2196F3),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF757575)
                )
            )
        }
    }
    
    private fun saveEmailPreferences(
        weeklySum: Boolean,
        newFeatures: Boolean
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        
        // Guardar realmente en SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("InterviewFacePrefs", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("${userId}_email_weekly_summary", weeklySum)
            putBoolean("${userId}_email_new_features", newFeatures)
            apply()
        }
        
        // Mostrar mensaje de éxito
        Toast.makeText(requireContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show()
    }
}
