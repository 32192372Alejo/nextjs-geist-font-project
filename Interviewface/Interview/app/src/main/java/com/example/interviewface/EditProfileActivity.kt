package com.example.interviewface

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    /**
     * Función para obtener las iniciales de un nombre
     */
    private fun getInitials(fullName: String): String {
        return fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().toString().uppercase() }
    }

    private lateinit var nameEditText: EditText
    private lateinit var emailTextView: TextView
    private lateinit var specialtyTextView: TextView
    private lateinit var educationLevelTextView: TextView
    private lateinit var experienceEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var profileImageView: ImageView
    private lateinit var changePhotoButton: ImageButton
    
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    private var selectedImageUri: Uri? = null
    private var profileImageUrl: String? = null
    
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        const val RESULT_PROFILE_UPDATED = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inicializar vistas
        nameEditText = findViewById(R.id.nameEditText)
        emailTextView = findViewById(R.id.emailTextView)
        specialtyTextView = findViewById(R.id.specialtyTextView)
        educationLevelTextView = findViewById(R.id.educationLevelTextView)
        experienceEditText = findViewById(R.id.experienceEditText)
        bioEditText = findViewById(R.id.bioEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        backButton = findViewById(R.id.backButton)
        profileImageView = findViewById(R.id.profileImage)
        val photoOptionsButton = findViewById<ImageButton>(R.id.photoOptionsButton)

        // Cargar datos del usuario actual
        loadUserData()

        // Configurar listeners
        backButton.setOnClickListener {
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            saveUserData()
        }
        
        photoOptionsButton.setOnClickListener { view ->
            showPhotoOptionsMenu(view)
        }
    }
    
    /**
     * Muestra un menú emergente con opciones para la foto de perfil
     */
    private fun showPhotoOptionsMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.photo_options_menu, popupMenu.menu)
        
        // Hacer que los iconos sean visibles en el menú
        try {
            val method = PopupMenu::class.java.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            method.isAccessible = true
            method.invoke(popupMenu, true)
        } catch (e: Exception) {
            Log.e("EditProfile", "Error al mostrar iconos en el menú: ${e.message}")
        }
        
        // Cambiar el color del texto de la opción "Eliminar foto" a rojo
        val deleteItem = popupMenu.menu.findItem(R.id.option_delete_photo)
        val spanString = android.text.SpannableString(deleteItem.title.toString())
        spanString.setSpan(android.text.style.ForegroundColorSpan(android.graphics.Color.RED), 0, spanString.length, 0)
        deleteItem.title = spanString
        
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: android.view.MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.option_change_photo -> {
                        openImagePicker()
                        true
                    }
                    R.id.option_delete_photo -> {
                        deleteProfilePhoto()
                        true
                    }
                    else -> false
                }
            }
        })
        
        popupMenu.show()
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    
    /**
     * Elimina la foto de perfil y configura las iniciales con un fondo de color
     */
    private fun deleteProfilePhoto() {
        // Mostrar un diálogo de confirmación
        android.app.AlertDialog.Builder(this)
            .setTitle("Eliminar foto de perfil")
            .setMessage("¿Estás seguro de que deseas eliminar tu foto de perfil? Se mostrará un avatar con tus iniciales.")
            .setPositiveButton("Eliminar") { _, _ ->
                // Obtener el usuario actual
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Mostrar un mensaje de carga
                    val loadingToast = Toast.makeText(this, "Eliminando foto...", Toast.LENGTH_SHORT)
                    loadingToast.show()
                    
                    // Obtener el nombre completo para las iniciales
                    val fullName = nameEditText.text.toString()
                    val initials = getInitials(fullName)
                    
                    // Generar un color aleatorio para el fondo
                    val colors = arrayOf("#FF5733", "#33FF57", "#3357FF", "#F033FF", "#FF33A6")
                    val randomColor = colors.random()
                    
                    // Crear un mapa con los datos actualizados
                    val userUpdates = HashMap<String, Any>()
                    userUpdates["useInitialsForProfile"] = true
                    userUpdates["profileInitials"] = initials
                    userUpdates["profileColor"] = randomColor
                    
                    // Eliminar la imagen base64 si existe
                    userUpdates["profileImageBase64"] = ""
                    
                    // Actualizar la vista previa con las iniciales
                    // Crear un bitmap con las iniciales y el fondo de color
                    val initialsAvatar = createInitialsAvatar(initials, randomColor)
                    profileImageView.setImageBitmap(initialsAvatar)
                    
                    // Actualizar los datos del usuario en Firestore
                    db.collection("users").document(currentUser.uid)
                        .update(userUpdates)
                        .addOnSuccessListener {
                            // Cancelar el toast de carga
                            loadingToast.cancel()
                            
                            // Mostrar mensaje de éxito
                            Toast.makeText(this, "Foto eliminada correctamente", Toast.LENGTH_SHORT).show()
                            
                            // Actualizar la variable de estado
                            selectedImageUri = null
                            profileImageUrl = null
                        }
                        .addOnFailureListener { e ->
                            // Cancelar el toast de carga
                            loadingToast.cancel()
                            
                            // Mostrar mensaje de error
                            Toast.makeText(this, "Error al eliminar foto: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    /**
     * Crea un bitmap circular con las iniciales del usuario y un fondo de color
     */
    private fun createInitialsAvatar(initials: String, colorString: String): Bitmap {
        // Crear un bitmap cuadrado
        val size = 200 // Tamaño del bitmap en píxeles
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        
        // Dibujar el fondo circular
        val paint = android.graphics.Paint()
        paint.isAntiAlias = true
        paint.color = android.graphics.Color.parseColor(colorString)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        
        // Configurar el texto para las iniciales
        paint.color = android.graphics.Color.WHITE
        paint.textSize = size / 2f
        paint.textAlign = android.graphics.Paint.Align.CENTER
        
        // Usar una fuente más redondeada
        val typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
        paint.typeface = typeface
        
        // Calcular la posición del texto (centrado)
        val xPos = canvas.width / 2f
        val yPos = (canvas.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)
        
        // Dibujar las iniciales
        canvas.drawText(initials, xPos, yPos, paint)
        
        return bitmap
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            try {
                selectedImageUri = data.data
                
                // Cargar la imagen directamente usando MediaStore
                if (selectedImageUri != null) {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                        profileImageView.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        // Si falla, intentar con setImageURI
                        profileImageView.setImageURI(selectedImageUri)
                    }
                    
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Imagen seleccionada correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Obtener datos del usuario
                        val userData = document.data
                        
                        // Establecer los valores en los campos
                        nameEditText.setText(userData?.get("fullName") as? String ?: "")
                        emailTextView.text = userData?.get("email") as? String ?: ""
                        specialtyTextView.text = userData?.get("specialty") as? String ?: ""
                        educationLevelTextView.text = userData?.get("educationLevel") as? String ?: ""
                        experienceEditText.setText(userData?.get("experience") as? String ?: "")
                        bioEditText.setText(userData?.get("bio") as? String ?: "")
                        
                        // Verificar si el usuario usa iniciales para el perfil
                        val useInitialsForProfile = userData?.get("useInitialsForProfile") as? Boolean ?: false
                        
                        if (useInitialsForProfile) {
                            // Obtener las iniciales y el color de fondo
                            val initials = userData?.get("profileInitials") as? String ?: ""
                            val colorString = userData?.get("profileColor") as? String ?: "#3357FF"
                            
                            // Crear un avatar con las iniciales y el fondo de color
                            val initialsAvatar = createInitialsAvatar(initials, colorString)
                            profileImageView.setImageBitmap(initialsAvatar)
                            profileImageView.background = null
                            
                            Log.d("EditProfile", "Avatar con iniciales cargado")
                        } else {
                            // Intentar cargar la imagen de perfil en formato Base64
                            val profileImageBase64 = userData?.get("profileImageBase64") as? String
                            if (profileImageBase64 != null && profileImageBase64.isNotEmpty()) {
                                try {
                                    // Convertir la cadena Base64 a un bitmap
                                    val imageBytes = android.util.Base64.decode(profileImageBase64, android.util.Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                    
                                    // Mostrar el bitmap en el ImageView
                                    profileImageView.setImageBitmap(bitmap)
                                    
                                    // Quitar cualquier fondo que pudiera haber
                                    profileImageView.background = null
                                    
                                    Log.d("EditProfile", "Imagen cargada desde Base64")
                                } catch (e: Exception) {
                                    Log.e("EditProfile", "Error al decodificar imagen Base64: ${e.message}")
                                    // Si falla, mostrar un color de fondo
                                    profileImageView.setBackgroundResource(R.color.blue_accent)
                                }
                            } else {
                                // Verificar si hay una URL de imagen (método anterior)
                                profileImageUrl = userData?.get("profileImageUrl") as? String
                                if (profileImageUrl != null && profileImageUrl!!.isNotEmpty()) {
                                    // Aquí se cargaría la imagen usando Glide o Picasso
                                    // Por ahora, simplemente mostramos un color de fondo
                                    profileImageView.setBackgroundResource(R.color.blue_accent)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    private fun saveUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Obtener los valores de los campos editables
            val name = nameEditText.text.toString().trim()
            val experience = experienceEditText.text.toString().trim()
            val bio = bioEditText.text.toString().trim()
            
            // Validar que el nombre no esté vacío
            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor completa el nombre", Toast.LENGTH_SHORT).show()
                return
            }
            
            // Crear mapa con los datos a actualizar (solo los editables)
            val userUpdates = hashMapOf<String, Any>(
                "fullName" to name
            )
            
            // Añadir experiencia y biografía solo si no están vacías
            if (experience.isNotEmpty()) {
                userUpdates["experience"] = experience
            }
            
            if (bio.isNotEmpty()) {
                userUpdates["bio"] = bio
            }
            
            // Si se seleccionó una nueva imagen, subirla a Firebase Storage
            if (selectedImageUri != null) {
                uploadImageAndSaveData(currentUser.uid, userUpdates)
            } else {
                // Si no hay nueva imagen, actualizar directamente los datos
                updateUserData(currentUser.uid, userUpdates)
            }
        } else {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun uploadImageAndSaveData(userId: String, userUpdates: HashMap<String, Any>) {
        // Si no hay imagen seleccionada, actualizar solo los datos del usuario
        if (selectedImageUri == null) {
            updateUserData(userId, userUpdates)
            return
        }
        
        try {
            // Mostrar mensaje de carga
            Toast.makeText(this, "Procesando imagen...", Toast.LENGTH_SHORT).show()
            
            // En lugar de subir a Firebase, usaremos un enfoque basado en Base64
            // Convertir la URI a un bitmap
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            
            // Redimensionar la imagen para que no sea demasiado grande
            val resizedBitmap = resizeBitmap(bitmap, 300) // 300px de ancho máximo
            
            // Convertir a Base64
            val baos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val imageBytes = baos.toByteArray()
            val base64Image = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
            
            // Guardar la imagen codificada en Base64 directamente en Firestore
            // Nota: Esto solo es viable para imágenes pequeñas, ya que Firestore tiene un límite de 1MB por documento
            userUpdates["profileImageBase64"] = base64Image
            userUpdates["useInitialsForProfile"] = false
            
            // Actualizar los datos del usuario
            updateUserData(userId, userUpdates)
            
            // Mostrar mensaje de éxito
            Toast.makeText(this, "Imagen procesada correctamente", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            Log.e("EditProfile", "Error al procesar la imagen: ${e.message}")
            Toast.makeText(this, "Error al procesar la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            useInitialsAsFallback(userId, userUpdates)
        }
    }
    
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxWidth) {
            return bitmap // No es necesario redimensionar
        }
        
        val ratio = width.toFloat() / height.toFloat()
        val newWidth = maxWidth
        val newHeight = (newWidth / ratio).toInt()
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
    
    private fun useInitialsAsFallback(userId: String, userUpdates: HashMap<String, Any>) {
        try {
            // Obtener el nombre del usuario para las iniciales
            val fullName = nameEditText.text.toString()
            val initials = getInitials(fullName)
            
            // Generar un color aleatorio
            val colors = arrayOf("#FF5733", "#33FF57", "#3357FF", "#F033FF", "#FF33A6")
            val randomColor = colors.random()
            
            // Crear un mapa con los datos de la imagen alternativa
            userUpdates["useInitialsForProfile"] = true
            userUpdates["profileInitials"] = initials
            userUpdates["profileColor"] = randomColor
            
            // Informar al usuario
            Toast.makeText(this, "Usando iniciales como imagen de perfil", Toast.LENGTH_SHORT).show()
            
            // Actualizar los datos del usuario
            updateUserData(userId, userUpdates)
        } catch (e: Exception) {
            Log.e("EditProfile", "Error al procesar datos para iniciales: ${e.message}")
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            updateUserData(userId, userUpdates)
        }
    }
    
    // La constante RESULT_PROFILE_UPDATED ya está definida en el companion object principal
    
    private fun updateUserData(userId: String, userUpdates: HashMap<String, Any>) {
        // Mostrar un mensaje de carga
        val loadingToast = Toast.makeText(this, "Actualizando perfil...", Toast.LENGTH_SHORT)
        loadingToast.show()
        
        // Actualizar el documento del usuario en Firestore
        db.collection("users").document(userId)
            .update(userUpdates)
            .addOnSuccessListener {
                // Cancelar el toast de carga
                loadingToast.cancel()
                
                // Mostrar mensaje de éxito
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                
                // Crear un intent con los datos actualizados para devolver al fragment
                val resultIntent = Intent()
                
                // Añadir los datos relevantes del perfil al intent
                if (userUpdates.containsKey("profileImageBase64")) {
                    resultIntent.putExtra("profileImageBase64", userUpdates["profileImageBase64"] as String)
                }
                if (userUpdates.containsKey("useInitialsForProfile")) {
                    resultIntent.putExtra("useInitialsForProfile", userUpdates["useInitialsForProfile"] as Boolean)
                }
                if (userUpdates.containsKey("profileInitials")) {
                    resultIntent.putExtra("profileInitials", userUpdates["profileInitials"] as String)
                }
                if (userUpdates.containsKey("profileColor")) {
                    resultIntent.putExtra("profileColor", userUpdates["profileColor"] as String)
                }
                if (userUpdates.containsKey("fullName")) {
                    resultIntent.putExtra("fullName", userUpdates["fullName"] as String)
                }
                
                // Establecer el resultado con los datos actualizados
                setResult(Activity.RESULT_OK, resultIntent)
                
                // Enviar broadcast para notificar la actualización del perfil (como respaldo)
                val broadcastIntent = Intent("com.example.interviewface.PROFILE_UPDATED")
                sendBroadcast(broadcastIntent)
                
                // Cerrar la actividad y volver a la pantalla anterior
                finish()
            }
            .addOnFailureListener { e ->
                // Cancelar el toast de carga
                loadingToast.cancel()
                
                // Mostrar mensaje de error
                Toast.makeText(this, "Error al actualizar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
