package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                InterviewfaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MoreScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun MoreScreen() {
        var showMenu by remember { mutableStateOf(true) }
        var currentScreen by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            // Menú emergente tipo WhatsApp
            if (showMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000))
                        .clickable { showMenu = false }
                ) {
                    WhatsAppStyleMenu(
                        onDismiss = { showMenu = false },
                        onOptionSelected = { option ->
                            currentScreen = option
                            showMenu = false
                        }
                    )
                }
            }
            
            // Mostrar pantallas cuando se selecciona una opción
            if (currentScreen.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (currentScreen) {
                        "Calificación" -> {
                            CalificacionScreen(onBackPressed = {
                                currentScreen = ""
                            })
                        }
                        "Configuración" -> {
                            ConfiguracionScreen(onBackPressed = {
                                currentScreen = ""
                            })
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WhatsAppStyleMenu(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Text(
                        text = "Opciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    Divider(color = Color.DarkGray, thickness = 1.dp)
                    
                    MenuItem(
                        text = "Calificación",
                        icon = Icons.Default.Star,
                        onClick = { onOptionSelected("Calificación") }
                    )
                    
                    MenuItem(
                        text = "Configuración",
                        icon = Icons.Default.Settings,
                        onClick = { onOptionSelected("Configuración") }
                    )
                }
            }
        }
    }
    
    @Composable
    fun MenuItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

    @Composable
    fun MenuOption(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ir a $text",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    @Composable
    fun CalificacionScreen(onBackPressed: () -> Unit) {
        var experiencia by remember { mutableStateOf("") }
        var mejora by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F1B20))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Calificación",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Column {
                    Text(text = "4", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Row {
                        repeat(3) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF2196F3))
                        }
                        repeat(2) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Gray)
                        }
                    }
                    Text(text = "1k reseñas", color = Color.Gray)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        5 to 0.30f,
                        4 to 0.35f,
                        3 to 0.20f,
                        2 to 0.10f,
                        1 to 0.05f
                    ).forEach { (stars, percent) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(text = stars.toString(), color = Color.White, modifier = Modifier.width(16.dp))
                            LinearProgressIndicator(
                                progress = percent,
                                color = Color(0xFF2196F3),
                                trackColor = Color.Gray,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .padding(horizontal = 8.dp),
                            )
                            Text(
                                text = "${(percent * 100).toInt()}%",
                                color = Color.White,
                                modifier = Modifier.width(40.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("¿Cómo calificarías tu experiencia?", color = Color.White, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                val row1 = listOf("1 estrella", "2 estrellas", "3 estrellas")
                val row2 = listOf("4 estrellas", "5 estrellas")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row1.forEach {
                        BotonEstrella(it)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row2.forEach {
                        BotonEstrella(it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Que fue lo que más te ayudó?", color = Color.White)
            OutlinedTextField(
                value = experiencia,
                onValueChange = { experiencia = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C2A33),
                    unfocusedContainerColor = Color(0xFF1C2A33),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("¿Qué se puede mejorar?", color = Color.White)
            OutlinedTextField(
                value = mejora,
                onValueChange = { mejora = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C2A33),
                    unfocusedContainerColor = Color(0xFF1C2A33),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Enviar comentarios", color = Color.White)
            }
        }
    }

    @Composable
    fun BotonEstrella(text: String) {
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C2A33)),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.defaultMinSize(minHeight = 40.dp)
        ) {
            Text(text = text, color = Color.White, fontSize = 14.sp)
        }
    }

    @Composable
    fun ConfiguracionScreen(onBackPressed: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121A21))
                .padding(18.dp)
        ) {
            // Botón de flecha hacia atrás
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(40.dp)  // Tamaño del botón
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Text(
                text = "Configuración",
                fontSize = 37.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SettingsSection(title = "Cuenta") {
                SettingsOption(text = "Correo y contraseña") { /*TODO*/ }
            }

            SettingsSection(title = "Notificaciones") {
                SettingsOption(text = "Notificaciones push") { /*TODO*/ }
                SettingsOption(text = "Notificaciones correo") { /*TODO*/ }
            }

            SettingsSection(title = "Idioma") {
                SettingsOption(text = "Español") { /*TODO*/ }
            }

            SettingsSection(title = "Otro") {
                SettingsOption(text = "Términos de servicio") { /*TODO*/ }
                SettingsOption(text = "Política de privacidad") { /*TODO*/ }
            }

            Spacer(modifier = Modifier.weight(1f))


            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cerrar sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Eliminar cuenta",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { /*TODO*/ }
                    .padding(16.dp)
            )
        }
    }

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
                tint = Color.Gray
            )
        }
    }
}