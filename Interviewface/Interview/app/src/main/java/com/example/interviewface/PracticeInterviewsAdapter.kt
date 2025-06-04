package com.example.interviewface

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interviewface.databinding.ItemPracticeInterviewBinding

class PracticeInterviewsAdapter(
    private val interviews: List<PracticeInterview>
) : RecyclerView.Adapter<PracticeInterviewsAdapter.InterviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterviewViewHolder {
        val binding = ItemPracticeInterviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InterviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InterviewViewHolder, position: Int) {
        holder.bind(interviews[position])
    }

    override fun getItemCount(): Int = interviews.size

    class InterviewViewHolder(
        private val binding: ItemPracticeInterviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(interview: PracticeInterview) {
            binding.titleTextView.text = interview.title
            binding.descriptionTextView.text = interview.description
            Glide.with(binding.interviewImage.context)
                .load(interview.imageResId)
                .centerCrop()
                .into(binding.interviewImage)
            
            // Configurar el clic en la imagen para iniciar la entrevista correspondiente
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, InterviewStartActivity::class.java)
                
                // Determinar el tipo de entrevista según el título
                val interviewType = when {
                    interview.title.contains("marketing", ignoreCase = true) -> "marketing"
                    interview.title.contains("programación", ignoreCase = true) -> "programación_de_software"
                    interview.title.contains("análisis", ignoreCase = true) -> "data_analysis"
                    else -> "psicología"
                }
                
                intent.putExtra("interviewType", interviewType)
                context.startActivity(intent)
            }
        }
    }
}