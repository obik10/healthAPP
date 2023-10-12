package com.robiul.healthapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.DaftarKlinikAdapter
import com.robiul.healthapp.adapter.ViewKlinikEdit
import com.robiul.healthapp.databinding.ActivityDaftarKlinikBinding
import com.robiul.healthapp.model.KlinikModel

class DaftarKlinik : AppCompatActivity() {
    lateinit var dbRef : DatabaseReference
    lateinit var klinikRecyclerView : RecyclerView
    lateinit var klinikArrayList : ArrayList<KlinikModel>
    lateinit var binding: ActivityDaftarKlinikBinding
    lateinit var adapter: DaftarKlinikAdapter
    lateinit var context: Context

    private lateinit var alertDialog: AlertDialog

    companion object{
        const val REQUEST_CODE = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarKlinikBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_daftar_klinik)

        binding.btnBack.setOnClickListener {
            finish()
        }

        klinikRecyclerView = binding.klinikList
        klinikRecyclerView.layoutManager = LinearLayoutManager(this)
        klinikRecyclerView.setHasFixedSize(true)

        klinikArrayList = arrayListOf<KlinikModel>()
        getKlinikData()

        val dialogView = layoutInflater.inflate(R.layout.activity_update_pendaftaran, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        alertDialog = builder.create()


    }


    private fun getKlinikData() {
        dbRef = FirebaseDatabase.getInstance().getReference("dataDokter")

        dbRef.child("dokter").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    klinikArrayList.clear()
                    for (klinikSnapshot in snapshot.children){
                        val klinik = klinikSnapshot.getValue(KlinikModel::class.java)
                        klinikArrayList.add(klinik!!)
                    }

                    klinikRecyclerView.adapter = DaftarKlinikAdapter(klinikArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedKlinik = data?.getStringExtra("klinik")
            val edKlinik = alertDialog.findViewById<EditText>(R.id.edKlinik)
            edKlinik.setText(selectedKlinik)
            val selectedDok = data?.getStringExtra("dokter")
        }
    }



}