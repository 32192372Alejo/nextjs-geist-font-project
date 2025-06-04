package com.example.interviewface

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
// import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.interviewface.adapter.CustomSpinnerAdapter
import com.example.interviewface.databinding.ActivityRegisterBinding
import com.example.interviewface.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : BaseActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    
    companion object {
        private const val TAG = "RegisterActivity"
    }
    
    /**
     * Configura los spinners con adaptadores personalizados para mejorar su apariencia
     */
    private fun setupSpinners() {
        // Obtener los arrays de recursos
        val educationLevels = resources.getStringArray(R.array.education_levels)
        val specialties = resources.getStringArray(R.array.specialties)
        
        // Configurar adaptador personalizado para el spinner de nivel de estudios
        val educationAdapter = CustomSpinnerAdapter(
            this,
            R.layout.spinner_item_improved,
            educationLevels
        )
        binding.spinnerEducationLevel.adapter = educationAdapter
        
        // Configurar adaptador personalizado para el spinner de especialidad
        val specialtyAdapter = CustomSpinnerAdapter(
            this,
            R.layout.spinner_item_improved,
            specialties
        )
        binding.spinnerSpecialty.adapter = specialtyAdapter
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupSpinners()
        
        // Configurar botón de volver atrás
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        // Configurar validación de campos
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
        
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }
        
        // Configurar botón para mostrar/ocultar contraseña
        var passwordVisible = false
        binding.btnTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                // Mostrar contraseña
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            } else {
                // Ocultar contraseña
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view)
            }
            // Mantener el cursor al final del texto
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
        
        // Configurar validación en tiempo real
        binding.etEmail.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                validateEmail()
            }
        })
        
        binding.etPassword.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                validatePassword()
            }
        })
        
        // Configurar botón de registro
        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            
            // Validación completa
            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()
            val isTermsAccepted = validateTerms()
            
            // Validación de campos adicionales
            var isValid = isEmailValid && isPasswordValid && isTermsAccepted
            
            if (fullName.isEmpty()) {
                Toast.makeText(this, getString(R.string.full_name_required), Toast.LENGTH_SHORT).show()
                isValid = false
            }
            
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, getString(R.string.phone_required), Toast.LENGTH_SHORT).show()
                isValid = false
            }
            
            if (!isValid) {
                return@setOnClickListener
            }
            
            // Mostrar progreso
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRegister.isEnabled = false
            
            // Registrar usuario con Firebase - Proceso simplificado
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            
            // Obtener valores de los spinners
            val educationLevel = if (binding.spinnerEducationLevel.selectedItemPosition > 0) {
                binding.spinnerEducationLevel.selectedItem.toString()
            } else {
                ""
            }
            
            val specialty = if (binding.spinnerSpecialty.selectedItemPosition > 0) {
                binding.spinnerSpecialty.selectedItem.toString()
            } else {
                ""
            }
            
            // Usar Firebase Auth directamente para un registro más rápido
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        if (firebaseUser != null) {
                            Log.d(TAG, "Usuario registrado exitosamente: ${firebaseUser.uid}")
                            
                            // Guardar datos adicionales en Firestore en segundo plano
                            val user = User(
                                id = firebaseUser.uid,
                                email = email,
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                educationLevel = educationLevel,
                                specialty = specialty,
                                createdAt = System.currentTimeMillis()
                            )
                            
                            // Guardar en Firestore y esperar respuesta antes de mostrar el diálogo
                            val db = FirebaseFirestore.getInstance()
                            Log.d(TAG, "Intentando guardar datos en Firestore: ${user.toMap()}")
                            
                            // Verificar si la colección existe y crearla si no
                            db.collection("users")
                                .document(firebaseUser.uid)
                                .set(user.toMap())
                                .addOnSuccessListener {
                                    Log.d(TAG, "Datos de usuario guardados exitosamente en Firestore")
                                    
                                    // Ocultar progreso después de guardar los datos
                                    binding.progressBar.visibility = View.GONE
                                    binding.btnRegister.isEnabled = true
                                    
                                    // Mostrar diálogo de alerta estándar
                                    showSimpleSuccessDialog()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error al guardar datos en Firestore: ${e.message}", e)
                                    Toast.makeText(this@RegisterActivity, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                                    
                                    // Ocultar progreso en caso de error
                                    binding.progressBar.visibility = View.GONE
                                    binding.btnRegister.isEnabled = true
                                }
                        }
                    } else {
                        // Error en el registro
                        Log.e(TAG, "Error al registrar usuario", task.exception)
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(this@RegisterActivity, 
                            "Error: ${task.exception?.message ?: "Error desconocido"}", 
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    
    // Función para validar el formato del correo electrónico
    private fun validateEmail(): Boolean {
        val email = binding.etEmail.text.toString()
        
        if (email.isEmpty()) {
            binding.tvEmailError.visibility = View.GONE
            return false
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tvEmailError.text = getString(R.string.invalid_email)
            binding.tvEmailError.setTextColor(resources.getColor(android.R.color.holo_red_light, null))
            binding.tvEmailError.visibility = View.VISIBLE
            return false
        }
        
        binding.tvEmailError.visibility = View.GONE
        return true
    }
    
    // Función para validar la contraseña
    private fun validatePassword(): Boolean {
        val password = binding.etPassword.text.toString()
        
        if (password.isEmpty()) {
            binding.tvPasswordError.visibility = View.GONE
            return false
        }
        
        if (password.length < 6) {
            binding.tvPasswordError.text = getString(R.string.password_hint)
            binding.tvPasswordError.setTextColor(resources.getColor(android.R.color.holo_red_light, null))
            binding.tvPasswordError.visibility = View.VISIBLE
            return false
        }
        
        binding.tvPasswordError.visibility = View.GONE
        return true
    }
    
    // Función para validar los términos y condiciones
    private fun validateTerms(): Boolean {
        if (!binding.cbTerms.isChecked) {
            Toast.makeText(this, getString(R.string.accept_terms_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    
    /**
     * Muestra un diálogo de alerta estándar cuando el registro es exitoso
     */
    private fun showSimpleSuccessDialog() {
        // Usar AlertDialog.Builder para un diálogo estándar y simple
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("¡Registro Exitoso!")
        builder.setMessage("Registro exitoso")
        builder.setIcon(android.R.drawable.ic_dialog_info)
        builder.setCancelable(false) // No se puede cancelar tocando fuera
        
        // Solo un botón de Aceptar que lleva a la pantalla de inicio de sesión
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // Crear y mostrar el diálogo
        val alertDialog = builder.create()
        alertDialog.show()
        
        // Personalizar el color del botón
        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.green_accent))
    }
}
