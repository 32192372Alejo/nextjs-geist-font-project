package com.example.interviewface

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewface.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var practiceInterviewsAdapter: PracticeInterviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPracticeInterviews()
        setupBottomNavigation()
    }

    private fun setupPracticeInterviews() {
        val interviews = listOf(
            PracticeInterview(
                "Entrevista de trabajo de marketing",
                "Prepárate para tu puesto de marketing entrevista",
                R.drawable.img_marketing
            ),
            PracticeInterview(
                "Entrevista de ingeniero de software",
                "Perfecciona tus habilidades para tu entrevista de ingeniería de software",
                R.drawable.img_software
            ),
            PracticeInterview(
                "Entrevista de análisis de datos",
                "Prepárate para tu entrevista de análisis de datos",
                R.drawable.img_data
            )
        )

        practiceInterviewsAdapter = PracticeInterviewsAdapter(interviews)
        binding.practiceInterviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = practiceInterviewsAdapter
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_interviews -> true
                R.id.navigation_comments -> true
                R.id.navigation_profile -> true
                R.id.navigation_more -> true
                else -> false
            }
        }
    }
}

data class PracticeInterview(
    val title: String,
    val description: String,
    val imageResId: Int
)
