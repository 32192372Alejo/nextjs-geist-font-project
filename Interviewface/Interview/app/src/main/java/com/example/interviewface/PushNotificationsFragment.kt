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

class PushNotificationsFragment : Fragment() {
    
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
                        PushNotificationsScreen(
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
    fun PushNotificationsScreen(onBackPressed: () -> Unit) {
        // Estado para las preferencias de notificaciones
        var notifyNewInterviews by remember { mutableStateOf(true) }
        var notifyTips by remember { mutableStateOf(true) }
        var notifyUpdates by remember { mutableStateOf(true) }
        var isLoading by remember { mutableStateOf(false) }
        
        // Cargar preferencias del usuario
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        
        LaunchedEffect(key1 = userId) {
            if (userId != null) {
                isLoading = true
                
                // Cargar desde SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("InterviewFacePrefs", android.content.Context.MODE_PRIVATE)
                notifyNewInterviews = sharedPreferences.getBoolean("${userId}_notify_new_interviews", true)
                notifyTips = sharedPreferences.getBoolean("${userId}_notify_tips", true)
                notifyUpdates = sharedPreferences.getBoolean("${userId}_notify_updates", true)
                
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
                    text = "Notificaciones push",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Descripción
            Text(
                text = "Configura qué notificaciones push deseas recibir en tu dispositivo",
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
                // Opciones de notificaciones
                NotificationSwitchOption(
                    title = "Nuevas entrevistas disponibles",
                    description = "Recibe notificaciones cuando haya nuevas entrevistas disponibles para practicar",
                    checked = notifyNewInterviews,
                    onCheckedChange = { 
                        notifyNewInterviews = it
                        saveNotificationPreferences(
                            notifyNewInterviews,
                            notifyTips,
                            notifyUpdates
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                NotificationSwitchOption(
                    title = "Consejos y trucos",
                    description = "Recibe consejos periódicos para mejorar tus habilidades en entrevistas",
                    checked = notifyTips,
                    onCheckedChange = { 
                        notifyTips = it
                        saveNotificationPreferences(
                            notifyNewInterviews,
                            notifyTips,
                            notifyUpdates
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                NotificationSwitchOption(
                    title = "Actualizaciones de la aplicación",
                    description = "Recibe notificaciones sobre nuevas funciones y mejoras en la aplicación",
                    checked = notifyUpdates,
                    onCheckedChange = { 
                        notifyUpdates = it
                        saveNotificationPreferences(
                            notifyNewInterviews,
                            notifyTips,
                            notifyUpdates
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF414548))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Las notificaciones push te ayudan a mantenerte al día con las novedades de la aplicación y mejorar tu preparación para entrevistas. Puedes cambiar estas preferencias en cualquier momento.",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
    
    @Composable
    fun NotificationSwitchOption(
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
    
    private fun saveNotificationPreferences(
        notifyNewInterviews: Boolean,
        notifyTips: Boolean,
        notifyUpdates: Boolean
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        
        // Guardar realmente en SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("InterviewFacePrefs", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("${userId}_notify_new_interviews", notifyNewInterviews)
            putBoolean("${userId}_notify_tips", notifyTips)
            putBoolean("${userId}_notify_updates", notifyUpdates)
            apply()
        }
        
        // Mostrar mensaje de éxito
        Toast.makeText(requireContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show()
    }
}
