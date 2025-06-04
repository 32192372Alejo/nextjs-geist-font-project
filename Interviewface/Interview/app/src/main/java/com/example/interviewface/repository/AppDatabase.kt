package com.example.interviewface.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.interviewface.model.InterviewResult

@Database(entities = [InterviewResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun interviewResultDao(): InterviewResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "interviewface_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
