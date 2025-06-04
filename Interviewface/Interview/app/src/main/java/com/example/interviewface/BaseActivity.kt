package com.example.interviewface

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Base activity class for all activities in the app.
 */
open class BaseActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Base initialization if needed
    }
}
