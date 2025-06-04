package com.example.interviewface.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interview_results")
data class InterviewResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val interviewType: String,
    val transcriptionText: String,
    val analysisSummary: String,
    val timestamp: Long
)
