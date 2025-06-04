package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.interviewface.databinding.ActivityInitialBinding

class InitialActivity : BaseActivity() {
    
    private lateinit var binding: ActivityInitialBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Configurar botones de idioma
        setupLanguageButtons()
        
        // Configurar botón de iniciar sesión
        binding.btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        
        // Configurar botón de registro
        binding.btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            // No finalizamos esta actividad para que el usuario pueda volver a ella
        }
    }
    
    private fun setupLanguageButtons() {
        // Configurar apariencia inicial de los botones
        binding.btnEspanol.setBackgroundColor(getColor(R.color.blue_accent))
        binding.btnEnglish.setBackgroundColor(getColor(R.color.dark_background))
        
        // Configurar botón de español (funcional)
        binding.btnEspanol.setOnClickListener {
            // Mostrar mensaje de confirmación
            Toast.makeText(this, "Idioma: Español", Toast.LENGTH_SHORT).show()
            
            // Actualizar apariencia de los botones
            binding.btnEspanol.setBackgroundColor(getColor(R.color.blue_accent))
            binding.btnEnglish.setBackgroundColor(getColor(R.color.dark_background))
        }
        
        // Configurar botón de inglés (no funcional)
        binding.btnEnglish.setOnClickListener {
            // No hacer nada o mostrar mensaje de que no está disponible
            Toast.makeText(this, "English language not available", Toast.LENGTH_SHORT).show()
        }
    }
    
    // No necesitamos métodos adicionales para el manejo de idiomas
}
