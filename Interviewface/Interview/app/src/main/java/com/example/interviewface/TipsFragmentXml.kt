package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class TipsFragmentXml : Fragment() {

    private lateinit var moreResourcesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tips, container, false)
        
        // Inicializar botón de Más Recursos
        moreResourcesButton = view.findViewById(R.id.moreResourcesButton)
        
        // Configurar listener para el botón de Más Recursos
        moreResourcesButton.setOnClickListener {
            val intent = Intent(requireContext(), InterviewResourcesActivity::class.java)
            startActivity(intent)
        }
        
        return view
    }
}
