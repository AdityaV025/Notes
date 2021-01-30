package com.adityaverma.notes


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ThemeNotes)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}