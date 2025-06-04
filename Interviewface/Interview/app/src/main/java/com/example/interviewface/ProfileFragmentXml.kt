package com.example.interviewface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragmentXml : Fragment() {

    private lateinit var editProfileButton: Button
    private lateinit var historyButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        // Inicializar botones
        editProfileButton = view.findViewById(R.id.editProfileButton)
        historyButton = view.findViewById(R.id.historyButton)
        
        // Configurar listeners
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        
        historyButton.setOnClickListener {
            val intent = Intent(requireContext(), InterviewHistoryActivity::class.java)
            startActivity(intent)
        }
        
        return view
    }
}
