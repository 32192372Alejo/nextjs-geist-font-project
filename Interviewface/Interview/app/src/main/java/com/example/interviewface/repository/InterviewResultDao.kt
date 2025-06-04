package com.example.interviewface.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.interviewface.model.InterviewResult

@Dao
interface InterviewResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterviewResult(result: InterviewResult)

    @Query("SELECT * FROM interview_results ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestInterviewResult(): InterviewResult?
}
