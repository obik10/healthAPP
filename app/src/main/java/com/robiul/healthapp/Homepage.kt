package com.robiul.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.robiul.healthapp.databinding.ActivityHomepageBinding

class Homepage : AppCompatActivity() {
    lateinit var binding: ActivityHomepageBinding
    lateinit var dbRef: DatabaseReference
    lateinit var emailPref: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_homepage)

        // mengambil value inputan nama
        val bundle = intent.extras
        if (bundle != null) {
            binding.tvPggna.text = bundle.getString("nama")?.uppercase()
            emailPref = bundle.getString("email").toString()
        }
        val id = intent.getStringExtra("id")
        val nama = binding.tvPggna.text.toString()

        binding.imgBProfil.setOnClickListener {
            Intent(this@Homepage, Profil::class.java).also {
                it.putExtra("nama", binding.tvPggna.text.toString())
                it.putExtra("email", emailPref)
                startActivity(it)
            }
        }

        binding.imgBPendaftaran.setOnClickListener {
            Intent(this@Homepage, Pendaftaran::class.java).also {
                it.putExtra("nama",  binding.tvPggna.text.toString())
                it.putExtra("id", id.toString())
                startActivity(it)
            }

        }

        binding.imgBRwytPendaftaran.setOnClickListener {
            Intent(this@Homepage, RiwayatPendaftaran::class.java).also {
                it.putExtra("id", id.toString())
                startActivity(it)
            }
        }

        binding.imgBKlinikDanJadwal.setOnClickListener {
            Intent(this@Homepage, DaftarKlinik::class.java).also {
                startActivity(it)
            }
        }

        binding.btnBerita.setOnClickListener {

            dbRef = FirebaseDatabase.getInstance().getReference("antrean")

            dbRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        for (rwtSnapshot in snapshot.children){
                            if (rwtSnapshot.key!!.equals(nama)){
                                val berita: String = rwtSnapshot.child("berita").value.toString()

                                AlertDialog.Builder(this@Homepage)
                                    .setTitle("PEMBERITAHUAN")
                                    .setMessage(berita.toString())
                                    .setPositiveButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                            else{
                                AlertDialog.Builder(this@Homepage)
                                    .setTitle("PEMBERITAHUAN")
                                    .setMessage("Anda tidak terdaftar dalam antrean!!")
                                    .setPositiveButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Homepage, "error", Toast.LENGTH_SHORT).show()
                }

            })

        }

        binding.btnSaran.setOnClickListener {
            val inputEditText = EditText(this)
            inputEditText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            inputEditText.isSingleLine = false

            val dialog = AlertDialog.Builder(this)
                .setTitle("Masukkan Saran")
                .setView(inputEditText)
                .setPositiveButton("Kirim") { dialog, which ->
                    val inputSaran = inputEditText.text.toString()
                    menyimpan(inputSaran, nama)
                }
                .setNegativeButton("Batal", null)
                .create()

            dialog.show()

        }

        binding.btnBantuan.setOnClickListener {
        dbRef = FirebaseDatabase.getInstance().getReference("Bantuan")

        dbRef.child("isiBantuan").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val isiBantuan = snapshot.getValue(String::class.java)
                    if (!isiBantuan.isNullOrEmpty()){
                        val dialog = AlertDialog.Builder(this@Homepage)
                            .setTitle("Bantuan")
                            .setMessage(isiBantuan)
                            .setPositiveButton("OK", null)
                            .create()

                        dialog.show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        }

    }

    private fun menyimpan(inputSaran: String, nama: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Saran")
        dbRef.child(nama).child("isiSaran").setValue(inputSaran)
        dbRef.child(nama).child("Nama").setValue(nama)
    }
}
