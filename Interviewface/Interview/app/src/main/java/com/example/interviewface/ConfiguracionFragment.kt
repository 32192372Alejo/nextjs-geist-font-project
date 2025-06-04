package com.example.interviewface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme
import com.example.interviewface.ui.theme.StandardGray
import com.example.interviewface.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
// Importar los nuevos fragmentos
import com.example.interviewface.AccountSettingsFragment
import com.example.interviewface.PushNotificationsFragment
import com.example.interviewface.EmailNotificationsFragment
import com.example.interviewface.TermsOfServiceFragment
import com.example.interviewface.PrivacyPolicyFragment


class ConfiguracionFragment : Fragment() {
    
    private fun deleteUserAccount() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        
        if (userId == null) {
            Toast.makeText(requireContext(), "Error: No se pudo identificar al usuario", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Mostrar mensaje de carga
        Toast.makeText(requireContext(), "Eliminando cuenta...", Toast.LENGTH_SHORT).show()
        
        // 1. Eliminar documentos del usuario en Firestore
        val db = FirebaseFirestore.getInstance()
        
        // Eliminar el documento principal del usuario
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                // 2. Eliminar la imagen de perfil en Storage si existe
                val storageRef = FirebaseStorage.getInstance().reference
                val profileImageRef = storageRef.child("profile_images/$userId.jpg")
                
                profileImageRef.delete().addOnCompleteListener { _ ->
                    // 3. Eliminar la cuenta de autenticación
                    currentUser.delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                            
                            // Redirigir a la pantalla inicial
                            val intent = Intent(requireContext(), InitialActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error al eliminar la cuenta: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al eliminar datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

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
                        ConfiguracionScreen(
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
    fun ConfiguracionScreen(onBackPressed: () -> Unit) {
        // Usar un ScrollView para permitir desplazamiento vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            // Botón de flecha hacia atrás y título centrado
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
                    text = "Configuración",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            SettingsSection(title = "Cuenta") {
                SettingsOption(text = "Correo y contraseña") { 
                    // Navegar al fragmento de configuración de cuenta
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.loadFragment(AccountSettingsFragment())
                }
            }

            SettingsSection(title = "Notificaciones") {
                SettingsOption(text = "Notificaciones push") { 
                    // Navegar al fragmento de notificaciones push
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.loadFragment(PushNotificationsFragment())
                }
                SettingsOption(text = "Notificaciones correo") { 
                    // Navegar al fragmento de notificaciones por correo
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.loadFragment(EmailNotificationsFragment())
                }
            }

            SettingsSection(title = "Idioma") {
                // Opción de Español
                SettingsOption(text = "Español") {
                    // Mostrar mensaje de confirmación
                    Toast.makeText(requireContext(), "Idioma: Español", Toast.LENGTH_SHORT).show()
                }
                
                // Opción de Inglés (no funcional)
                SettingsOption(text = "English") {
                    // Mostrar mensaje de que no está disponible
                    Toast.makeText(requireContext(), "English language not available", Toast.LENGTH_SHORT).show()
                }
            }

            SettingsSection(title = "Otro") {
                SettingsOption(text = "Términos de servicio") { 
                    // Navegar al fragmento de términos de servicio
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.loadFragment(TermsOfServiceFragment())
                }
                SettingsOption(text = "Política de privacidad") { 
                    // Navegar al fragmento de política de privacidad
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.loadFragment(PrivacyPolicyFragment())
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    // Cerrar sesión y redirigir a la pantalla principal
                    val intent = Intent(requireContext(), InitialActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF414548)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cerrar sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Estado para controlar la visibilidad del diálogo de confirmación
            var showDeleteConfirmDialog by remember { mutableStateOf(false) }
            
            // Diálogo de confirmación para eliminar cuenta
            if (showDeleteConfirmDialog) {
                Dialog(onDismissRequest = { showDeleteConfirmDialog = false }) {
                    Surface(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2A2D30)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "¿Estás seguro?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Esta acción eliminará permanentemente tu cuenta y todos tus datos. No podrás recuperarlos.",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(
                                    onClick = { showDeleteConfirmDialog = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF414548))
                                ) {
                                    Text("Cancelar", color = Color.White)
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Button(
                                    onClick = { 
                                        deleteUserAccount()
                                        showDeleteConfirmDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                                ) {
                                    Text("Eliminar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
            
            Button(
                onClick = { showDeleteConfirmDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)), // Color rojo para indicar acción peligrosa
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Eliminar cuenta", color = Color.White)
            }
            
            // Espacio adicional al final para asegurar que el botón sea visible
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
    
    // Se ha eliminado la funcionalidad de cambio de idioma

    @Composable
    fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Column(content = content)
    }

    @Composable
    fun SettingsOption(text: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 18.sp, color = Color.White)

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow",
                modifier = Modifier.size(22.dp),
                tint = Color.White
            )
        }
    }
}
