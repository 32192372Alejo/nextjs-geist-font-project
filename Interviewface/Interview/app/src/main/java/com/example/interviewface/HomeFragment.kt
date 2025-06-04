package com.example.interviewface

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewface.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupPracticeInterviewsRecyclerView()
        
        // Botón de inicio sin funcionalidad de selección de entrevistas
        binding.featuredButton.setOnClickListener {
            val intent = Intent(context, InterviewStartActivity::class.java)
            intent.putExtra("interviewType", "psicología")  // Cambiado de "technical" a "psicología"
            startActivity(intent)
        }
        
        // Botón de configuración
        binding.settingsButton.setOnClickListener {
            // Crear y mostrar el fragmento de configuración
            val configFragment = ConfiguracionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, configFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun setupPracticeInterviewsRecyclerView() {
        val practiceInterviews = listOf(
            PracticeInterview(
                title = "Entrevista de trabajo de marketing",
                description = "Prepárate para tu puesto de marketing entrevista",
                imageResId = R.drawable.img_marketing_interview_new
            ),
            PracticeInterview(
                title = "Entrevista de programación de software",
                description = "Perfecciona tus habilidades para tu entrevista de programación de software",
                imageResId = R.drawable.img_software_interview_new
            ),
            PracticeInterview(
                title = "Entrevista de análisis de datos",
                description = "Prepárate para tu entrevista de análisis de datos",
                imageResId = R.drawable.img_data_analysis_interview_new
            )
        )

        val adapter = PracticeInterviewsAdapter(practiceInterviews)
        binding.practiceInterviewsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.practiceInterviewsRecyclerView.adapter = adapter
        
        // Eliminar decoraciones existentes para evitar duplicados
        while (binding.practiceInterviewsRecyclerView.itemDecorationCount > 0) {
            binding.practiceInterviewsRecyclerView.removeItemDecorationAt(0)
        }
        
        // Ajustar el espaciado entre los elementos del carrusel y añadir un ItemDecoration
        val itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = 4
                outRect.right = 4
            }
        }
        binding.practiceInterviewsRecyclerView.addItemDecoration(itemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}