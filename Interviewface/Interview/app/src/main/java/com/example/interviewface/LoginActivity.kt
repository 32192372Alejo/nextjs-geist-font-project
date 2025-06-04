package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
// import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.interviewface.databinding.ActivityLoginBinding
import com.example.interviewface.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val authRepository = AuthRepository()
    
    companion object {
        private const val TAG = "LoginActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Configurar botón de volver atrás
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        // Configurar validación en tiempo real para el correo
        binding.etEmail.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                validateEmail()
            }
        })
        
        // Configurar validación en tiempo real para la contraseña
        binding.etPassword.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                validatePassword()
            }
        })
        
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
        
        // Configurar botón de iniciar sesión
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            
            // Validación completa
            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()
            
            if (!isEmailValid || !isPasswordValid) {
                return@setOnClickListener
            }
            
            // Mostrar progreso
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false
            
            // Iniciar sesión con Firebase
            lifecycleScope.launch {
                try {
                    // Verificar si el usuario existe antes de intentar iniciar sesión
                    val result = authRepository.loginUser(email, password)
                    
                    result.fold(
                        onSuccess = { firebaseUser ->
                            // Inicio de sesión exitoso
                            Log.d(TAG, "Inicio de sesión exitoso: ${firebaseUser.uid}")
                            Toast.makeText(this@LoginActivity, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                            
                            // Navegar a la pantalla principal
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        },
                        onFailure = { exception ->
                            // Error en el inicio de sesión
                            Log.e(TAG, "Error al iniciar sesión", exception)
                            Toast.makeText(this@LoginActivity, "Credenciales incorrectas. Por favor, verifica tu correo y contraseña.", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                        }
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error inesperado", e)
                    Toast.makeText(this@LoginActivity, "Error de conexión. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                }
            }
        }
        
        // Configurar botón de registro
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        
        // Configurar botón de olvidé mi contraseña
        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, introduce un correo válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Mostrar progreso
            binding.progressBar.visibility = View.VISIBLE
            
            // Enviar correo de restablecimiento
            lifecycleScope.launch {
                try {
                    val result = authRepository.sendPasswordResetEmail(email)
                    
                    result.fold(
                        onSuccess = {
                            Toast.makeText(this@LoginActivity, "Se ha enviado un correo para restablecer tu contraseña", Toast.LENGTH_LONG).show()
                        },
                        onFailure = { exception ->
                            Toast.makeText(this@LoginActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        
        // Configurar botones de redes sociales
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Inicio de sesión con Google no implementado", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnFacebook.setOnClickListener {
            Toast.makeText(this, "Inicio de sesión con Facebook no implementado", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnLinkedin.setOnClickListener {
            Toast.makeText(this, "Inicio de sesión con LinkedIn no implementado", Toast.LENGTH_SHORT).show()
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
            binding.tvEmailError.text = "Por favor, introduce un correo válido"
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
            binding.tvPasswordError.text = "La contraseña debe tener al menos 6 caracteres"
            binding.tvPasswordError.setTextColor(resources.getColor(android.R.color.holo_red_light, null))
            binding.tvPasswordError.visibility = View.VISIBLE
            return false
        }
        
        binding.tvPasswordError.visibility = View.GONE
        return true
    }
}
