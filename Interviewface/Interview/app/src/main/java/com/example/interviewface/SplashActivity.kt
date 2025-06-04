package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
// import androidx.appcompat.app.AppCompatActivity
import com.example.interviewface.repository.AuthRepository

class SplashActivity : BaseActivity() {
    
    private val authRepository = AuthRepository()
    
    companion object {
        private const val TAG = "SplashActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Mostrar la pantalla de splash durante 1.5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Verificar si el usuario ya tiene sesión iniciada
            checkUserSession()
        }, 1500)
    }
    
    /**
     * Verifica si el usuario ya tiene sesión iniciada y navega a la pantalla correspondiente
     */
    private fun checkUserSession() {
        val currentUser = authRepository.getCurrentUser()
        
        if (currentUser != null) {
            // Usuario autenticado, ir a la pantalla principal
            Log.d(TAG, "Usuario autenticado: ${currentUser.uid}")
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Usuario no autenticado, ir a la pantalla inicial
            Log.d(TAG, "Usuario no autenticado")
            startActivity(Intent(this, InitialActivity::class.java))
        }
        
        finish()
    }
}
