package com.example.mad_assignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}