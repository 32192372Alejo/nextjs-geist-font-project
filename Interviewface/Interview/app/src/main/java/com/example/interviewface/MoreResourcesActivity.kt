package com.example.interviewface

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MoreResourcesActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    // URLs de los videos de YouTube
    private val videoUrl1 = "https://youtu.be/51tcTh8jDus?si=X_h41gDynR2LO3s1" // Tips para una buena entrevista (0:45)
    private val videoUrl2 = "https://youtu.be/jQ5Ua5zv-Y4?si=BAPzonY08HITSC0V" // Preguntas mas desafiantes en una entrevista (2:35)
    private val videoUrl3 = "https://youtu.be/l-jgyTmqPHs?si=xbMDmMrQOLJswsO1" // Lenguaje corporal efectivo (3:55)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_resources)

        // Inicializar vistas
        backButton = findViewById(R.id.backButton)

        // Configurar el botón de volver
        backButton.setOnClickListener {
            finish()
        }
        
        // Configurar los botones de video manualmente para asegurar que funcionen
        findViewById<android.widget.Button>(R.id.btnVerVideo1)?.setOnClickListener {
            openYouTubeVideo(videoUrl1)
        }
        
        findViewById<android.widget.Button>(R.id.btnVerVideo2)?.setOnClickListener {
            openYouTubeVideo(videoUrl2)
        }
        
        findViewById<android.widget.Button>(R.id.btnVerVideo3)?.setOnClickListener {
            openYouTubeVideo(videoUrl3)
        }
    }
    
    // Métodos para abrir los videos en YouTube
    fun openVideo1(view: View) {
        openYouTubeVideo(videoUrl1)
    }
    
    fun openVideo2(view: View) {
        openYouTubeVideo(videoUrl2)
    }
    
    fun openVideo3(view: View) {
        openYouTubeVideo(videoUrl3)
    }
    
    // Método auxiliar para abrir videos de YouTube
    private fun openYouTubeVideo(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
