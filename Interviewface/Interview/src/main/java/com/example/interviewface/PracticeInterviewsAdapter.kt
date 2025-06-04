package com.example.interviewface

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
            binding.apply {
                titleTextView.text = interview.title
                descriptionTextView.text = interview.description
                
                Glide.with(interviewImage)
                    .load(interview.imageResId)
                    .centerCrop()
                    .into(interviewImage)
            }
        }
    }
}
