package com.example.interviewface

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class InterviewResourcesActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_resources)

        // Inicializar vistas
        backButton = findViewById(R.id.backButton)

        // Configurar el botón de volver
        backButton.setOnClickListener {
            finish()
        }
    }
}
