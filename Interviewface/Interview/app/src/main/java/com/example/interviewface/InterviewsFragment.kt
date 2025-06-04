package com.example.interviewface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.interviewface.databinding.FragmentInterviewsBinding

class InterviewsFragment : Fragment() {

    private var _binding: FragmentInterviewsBinding? = null
    private val binding get() = _binding!!

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

        
        _binding = FragmentInterviewsBinding.inflate(inflater, container, false)
        
        // Al entrar en este fragmento, mostrar directamente el fragmento de selección de entrevistas
        val interviewSelectionFragment = InterviewSelectionFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, interviewSelectionFragment)
            .addToBackStack(null)
            .commit()
            
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}