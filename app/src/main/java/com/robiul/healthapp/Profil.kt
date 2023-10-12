package com.robiul.healthapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.databinding.ActivityProfilBinding

class Profil : AppCompatActivity() {
    lateinit var binding: ActivityProfilBinding
    lateinit var dbRef:DatabaseReference

    lateinit var namaTemp: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_profil)


        val bundle = intent.getStringExtra("nama")
        val bundle2 = intent.getStringExtra("email")
        if (bundle != null){
            binding.tvNama.text = bundle
            binding.tvEmail.text = bundle2
        } else {
          Toast.makeText(this, "data tidak ada!", Toast.LENGTH_SHORT).show()
        }

        binding.imgBBack.setOnClickListener {
            finish()
            }

        binding.imgBKeluar.setOnClickListener {
            Intent(this@Profil, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
        }





        }
