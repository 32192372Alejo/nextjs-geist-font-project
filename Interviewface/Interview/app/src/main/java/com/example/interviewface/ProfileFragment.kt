package com.example.interviewface

import android.content.BroadcastReceiver
import android.content.Context
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.interviewface.ui.theme.InterviewfaceTheme
import com.example.interviewface.ui.theme.StandardGray
import android.graphics.Color as AndroidColor
import com.example.interviewface.InterviewSelectionFragment
import com.example.interviewface.model.User
import com.example.interviewface.InterviewHistoryActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    companion object {
        private const val EDIT_PROFILE_REQUEST_CODE = 100
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var userData: Map<String, Any>? = null
    private var hasInterviews: Boolean = false
    
    // Receptor de broadcast para actualizar el perfil cuando se recibe una notificación
    private val profileUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.interviewface.PROFILE_UPDATED") {
                Log.d("ProfileFragment", "Recibida notificación de actualización de perfil")
                // Forzar la recarga de los datos del usuario directamente desde el servidor
                forceRefreshUserData()
                // Mostrar un mensaje informativo
                context?.let {
                    Toast.makeText(it, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    // Función para obtener las iniciales de un nombre
    private fun getInitials(fullName: String): String {
        return fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().toString().uppercase() }
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
                        PerfilScreen(userData, hasInterviews)
                    }
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Registrar el receptor de broadcast con el flag RECEIVER_NOT_EXPORTED para Android 13+
        val filter = IntentFilter("com.example.interviewface.PROFILE_UPDATED")
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                // Para Android 13 (API 33) y superior, debemos especificar el flag
                requireContext().registerReceiver(profileUpdateReceiver, filter, android.content.Context.RECEIVER_NOT_EXPORTED)
            } else {
                // Para versiones anteriores a Android 13
                requireContext().registerReceiver(profileUpdateReceiver, filter)
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error al registrar el receptor: ${e.message}")
        }
        
        // Cargar los datos del usuario de forma optimizada
        // Primero intentamos cargar desde caché local si está disponible
        val currentUser = auth.currentUser
        if (currentUser != null && userData != null) {
            // Si ya tenemos datos, actualizar la UI inmediatamente con ellos
            updateUI()
            // Luego actualizamos en segundo plano
            loadUserData()
        } else {
            // Si no tenemos datos en caché, cargar normalmente
            loadUserData()
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Desregistrar el receptor de broadcast para evitar fugas de memoria
        try {
            requireContext().unregisterReceiver(profileUpdateReceiver)
        } catch (e: Exception) {
            // Ignorar si el receptor no estaba registrado
            Log.e("ProfileFragment", "Error al desregistrar receptor: ${e.message}")
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("ProfileFragment", "Perfil actualizado, actualizando UI inmediatamente...")
            
            try {
                // Forzar una actualización inmediata desde Firebase
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Mostrar un mensaje de carga
                    Toast.makeText(requireContext(), "Actualizando perfil...", Toast.LENGTH_SHORT).show()
                    
                    // Cargar datos directamente desde el servidor (sin caché)
                    db.collection("users").document(currentUser.uid)
                        .get(com.google.firebase.firestore.Source.SERVER)
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                // Actualizar los datos locales
                                userData = document.data
                                
                                // Forzar actualización de UI en el hilo principal
                                activity?.runOnUiThread {
                                    // Recrear completamente la vista de Compose
                                    view?.let { view ->
                                        (view as ComposeView).apply {
                                            // Forzar la recreación completa del contenido
                                            setContent {
                                                InterviewfaceTheme {
                                                    Surface(
                                                        modifier = Modifier.fillMaxSize(),
                                                        color = MaterialTheme.colorScheme.background
                                                    ) {
                                                        // Usar una copia fresca de los datos
                                                        val freshUserData = HashMap<String, Any>(userData ?: mapOf())
                                                        PerfilScreen(freshUserData, hasInterviews)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                    // Mensaje de éxito
                                    Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                }
                                
                                // Verificar entrevistas en segundo plano
                                checkUserInterviews(currentUser.uid)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ProfileFragment", "Error al actualizar perfil: ${e.message}")
                            Toast.makeText(requireContext(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                        }
                }
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error en onActivityResult: ${e.message}")
                Toast.makeText(requireContext(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Actualiza los datos del usuario con la información recibida del intent
     */
    private fun updateUserDataFromIntent(data: Intent) {
        try {
            // Si userData es null, crear un nuevo mapa
            if (userData == null) {
                userData = HashMap<String, Any>()
            }
            
            // Crear una copia mutable de los datos actuales
            val updatedData = HashMap<String, Any>(userData ?: mapOf())
            
            // Actualizar con los datos recibidos
            if (data.hasExtra("profileImageBase64")) {
                val imageBase64 = data.getStringExtra("profileImageBase64")
                if (imageBase64 != null) {
                    updatedData["profileImageBase64"] = imageBase64
                    Log.d("ProfileFragment", "Imagen de perfil actualizada desde intent")
                }
            }
            
            if (data.hasExtra("useInitialsForProfile")) {
                updatedData["useInitialsForProfile"] = data.getBooleanExtra("useInitialsForProfile", true)
            }
            
            if (data.hasExtra("profileInitials")) {
                val initials = data.getStringExtra("profileInitials")
                if (initials != null) {
                    updatedData["profileInitials"] = initials
                }
            }
            
            if (data.hasExtra("profileColor")) {
                val color = data.getStringExtra("profileColor")
                if (color != null) {
                    updatedData["profileColor"] = color
                }
            }
            
            if (data.hasExtra("fullName")) {
                val fullName = data.getStringExtra("fullName")
                if (fullName != null) {
                    updatedData["fullName"] = fullName
                }
            }
            
            // Actualizar los datos del usuario
            userData = updatedData
            Log.d("ProfileFragment", "Datos de usuario actualizados desde intent: $userData")
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error al actualizar datos desde intent: ${e.message}")
        }
    }
    
    /**
     * Fuerza una actualización de los datos del usuario directamente desde el servidor (sin caché)
     */
    private fun forceRefreshUserData() {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Cargar datos del usuario directamente desde el servidor, sin usar caché
                db.collection("users").document(currentUser.uid)
                    .get(com.google.firebase.firestore.Source.SERVER) // Forzar obtener desde servidor
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            // Crear una nueva copia de los datos para evitar problemas de caché
                            userData = HashMap(document.data ?: mapOf())
                            Log.d("ProfileFragment", "Datos actualizados correctamente: $userData")
                            
                            // Actualizar la UI inmediatamente
                            updateUI()
                            
                            // Verificar entrevistas después de actualizar la UI para que la foto se muestre primero
                            checkUserInterviews(currentUser.uid)
                        } else {
                            Log.w("ProfileFragment", "El documento del usuario no existe")
                            userData = mapOf()
                            hasInterviews = false
                            updateUI()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileFragment", "Error al actualizar datos: ${e.message}")
                        // Intentar cargar desde caché como respaldo
                        loadUserData()
                    }
            } else {
                loadUserData() // Cargar normalmente si no hay usuario
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Excepción al forzar actualización: ${e.message}")
            loadUserData() // Cargar normalmente en caso de error
        }
    }
    
    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Primero intentamos obtener de la caché local para mostrar algo rápidamente
            db.collection("users").document(currentUser.uid)
                .get(com.google.firebase.firestore.Source.CACHE)
                .addOnSuccessListener { cachedDocument ->
                    if (cachedDocument != null && cachedDocument.exists()) {
                        // Actualizar con datos de caché primero para mostrar algo rápido
                        userData = cachedDocument.data
                        updateUI()
                        
                        // Luego obtener los datos actualizados del servidor
                        refreshFromServer(currentUser.uid)
                    } else {
                        // Si no hay caché, cargar directamente del servidor
                        refreshFromServer(currentUser.uid)
                    }
                }
                .addOnFailureListener {
                    // Si falla la caché, intentar desde el servidor
                    refreshFromServer(currentUser.uid)
                }
        }
    }
    
    private fun refreshFromServer(userId: String) {
        // Obtener datos actualizados del servidor
        db.collection("users").document(userId)
            .get(com.google.firebase.firestore.Source.SERVER)
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userData = document.data
                    
                    // Verificar si el usuario tiene entrevistas
                    checkUserInterviews(userId)
                }
            }
    }
    
    private fun checkUserInterviews(userId: String) {
        db.collection("interviews")
            .whereEqualTo("userId", userId)
            .limit(1) // Solo necesitamos saber si hay al menos una
            .get()
            .addOnSuccessListener { querySnapshot ->
                hasInterviews = !querySnapshot.isEmpty
                
                // Actualizar la UI después de verificar las entrevistas
                updateUI()
            }
            .addOnFailureListener {
                // En caso de error, asumimos que no hay entrevistas
                hasInterviews = false
                updateUI()
            }
    }
    
    private fun updateUI() {
        try {
            if (isAdded && !isDetached && view != null) {
                Log.d("ProfileFragment", "Actualizando UI con datos actualizados")
                
                // Asegurarse de que la actualización de UI se ejecute en el hilo principal
                activity?.runOnUiThread {
                    view?.let { view ->
                        // Forzar la recreación del contenido para asegurar que se usen los datos más recientes
                        (view as ComposeView).setContent {
                            InterviewfaceTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    // Usar una copia de los datos para evitar problemas de referencia
                                    val userDataCopy = userData?.let { HashMap(it) }
                                    PerfilScreen(userDataCopy, hasInterviews)
                                }
                            }
                        }
                    }
                }
            } else {
                Log.d("ProfileFragment", "No se puede actualizar UI: fragmento no está adjunto a la actividad")
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error al actualizar UI: ${e.message}")
        }
    }

    @Composable
    private fun showInitialsAvatar(userData: Map<String, Any>?) {
        // Obtener las iniciales y el color guardados
        val initials = userData?.get("profileInitials") as? String
            ?: getInitials(userData?.get("fullName") as? String ?: "")
        
        // Usar un color más suave para el fondo
        val colorString = userData?.get("profileColor") as? String ?: "#1A80E5"
        val color = Color(AndroidColor.parseColor(colorString))
        
        // Crear un bitmap con las iniciales y el fondo de color - precalculado para evitar retrasos
        val initialsAvatar = remember(initials, colorString) {
            createInitialsAvatar(initials, colorString)
        }
        
        // Mostrar el bitmap como imagen
        if (initialsAvatar != null) {
            Image(
                bitmap = initialsAvatar.asImageBitmap(),
                contentDescription = "Iniciales de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback si no se pudo crear el bitmap - usar un diseño más ligero
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
    
    /**
     * Crea un bitmap circular con las iniciales del usuario y un fondo de color
     * Optimizado para evitar retrasos en la UI
     */
    private fun createInitialsAvatar(initials: String, colorString: String): Bitmap? {
        return try {
            // Crear un bitmap cuadrado con tamaño optimizado
            val size = 200 // Tamaño del bitmap en píxeles
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            
            // Dibujar el fondo circular con antialiasing para mejor calidad
            val paint = android.graphics.Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.isFilterBitmap = true
            paint.color = AndroidColor.parseColor(colorString)
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
            
            // Configurar el texto para las iniciales con mejor renderizado
            paint.color = AndroidColor.WHITE
            paint.textSize = size / 2f
            paint.textAlign = android.graphics.Paint.Align.CENTER
            
            // Usar una fuente más redondeada y optimizada
            val typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
            paint.typeface = typeface
            
            // Calcular la posición del texto (centrado)
            val xPos = canvas.width / 2f
            val yPos = (canvas.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)
            
            // Dibujar las iniciales
            canvas.drawText(initials, xPos, yPos, paint)
            
            bitmap
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error al crear avatar con iniciales: ${e.message}")
            null
        }
    }
    
    @Composable
    fun PerfilScreen(userData: Map<String, Any>?, hasInterviews: Boolean = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar de usuario (foto o iniciales) con placeholder transparente
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        // Añadir un fondo transparente en lugar del azul
                        .background(Color.Transparent)
                        .clickable {
                            val intent = Intent(requireContext(), EditProfileActivity::class.java)
                            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Verificar si debemos usar iniciales para el perfil
                    val useInitials = userData?.get("useInitialsForProfile") as? Boolean ?: true
                    
                    // Intentar cargar la imagen de perfil en formato Base64
                    val profileImageBase64 = userData?.get("profileImageBase64") as? String
                    
                    if (!useInitials && profileImageBase64 != null && profileImageBase64.isNotEmpty()) {
                        // Convertir Base64 a Bitmap fuera de la composición con optimización
                        val bitmap = remember(profileImageBase64) {
                            try {
                                val imageBytes = android.util.Base64.decode(profileImageBase64, android.util.Base64.DEFAULT)
                                // Usar opciones para decodificar más rápido
                                val options = BitmapFactory.Options().apply {
                                    inPreferredConfig = Bitmap.Config.ARGB_8888
                                    inSampleSize = 1 // No reducir la calidad para fotos de perfil
                                }
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
                            } catch (e: Exception) {
                                Log.e("ProfileFragment", "Error al decodificar imagen Base64: ${e.message}")
                                null
                            }
                        }
                        
                        // Mostrar la imagen o las iniciales según el resultado
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Log.d("ProfileFragment", "Mostrando imagen desde Base64")
                        } else {
                            // Si no hay bitmap, mostrar iniciales
                            showInitialsAvatar(userData)
                        }
                    } else if (!useInitials) {
                        // Intentar cargar la imagen de perfil si existe una URL (método antiguo)
                        val profileImageUrl = userData?.get("profileImageUrl") as? String
                        if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
                            // Aquí iría el código para cargar la imagen desde URL
                            // Por ahora, mostraremos iniciales
                            showInitialsAvatar(userData)
                        } else {
                            // Si no hay URL de imagen, mostrar iniciales
                            showInitialsAvatar(userData)
                        }
                    } else {
                        // Usar iniciales como se especificó
                        showInitialsAvatar(userData)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userData?.get("fullName") as? String ?: "Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = userData?.get("email") as? String ?: "correo@ejemplo.com",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Mostrar especialidad y nivel de estudios
                val specialty = userData?.get("specialty") as? String
                val educationLevel = userData?.get("educationLevel") as? String
                
                if (!specialty.isNullOrEmpty() || !educationLevel.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildString {
                            if (!specialty.isNullOrEmpty()) {
                                append(specialty)
                            }
                            if (!specialty.isNullOrEmpty() && !educationLevel.isNullOrEmpty()) {
                                append(" | ")
                            }
                            if (!educationLevel.isNullOrEmpty()) {
                                append(educationLevel)
                            }
                        },
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        val intent = Intent(requireContext(), EditProfileActivity::class.java)
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF414548)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Editar Perfil", color = Color.White, fontSize = 14.sp)
                }
                
                Button(
                    onClick = {
                        // Navegar al historial de entrevistas
                        val intent = Intent(requireContext(), InterviewHistoryActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A80E5)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Historial", color = Color.White, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección de historial de entrevistas
            Text(
                text = "Historial de entrevistas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
            
            // Mostrar datos de entrevistas solo si el usuario tiene entrevistas
            if (hasInterviews) {
                // Aquí se mostrarían las entrevistas reales del usuario cuando las tenga
                // Esta sección se llenaría dinámicamente con los datos de Firebase
                // Por ahora, solo mostraremos un mensaje de que hay entrevistas
                Text(
                    text = "Toca el botón 'Historial' para ver tus entrevistas realizadas",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
            } else {
                // Mensaje cuando no hay entrevistas
                Text(
                    text = "No has realizado ninguna entrevista aún",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección de rendimiento
            Text(
                text = "Rendimiento",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
            
            // Mostrar datos de rendimiento solo si el usuario tiene entrevistas
            if (hasInterviews) {
                // Aquí se mostrarían las métricas de rendimiento reales del usuario
                // basadas en sus entrevistas completadas
                Text(
                    text = "Toca el botón 'Historial' para ver tus estadísticas de rendimiento",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
            } else {
                // Mensaje cuando no hay datos de rendimiento
                Text(
                    text = "Realiza entrevistas para ver tu rendimiento",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Obtener la actividad principal
                    val mainActivity = requireActivity() as MainActivity
                    // Cargar el fragmento de selección de entrevista y seleccionar la pestaña correspondiente
                    mainActivity.loadFragmentAndSelectNavItem(InterviewSelectionFragment(), R.id.navigation_interviews)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF414548)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Iniciar una nueva simulación", color = Color.White)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    @Composable
    fun CardInfo(title: String, value: String, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier.padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF414548))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontSize = 15.sp, color = Color.White)
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}