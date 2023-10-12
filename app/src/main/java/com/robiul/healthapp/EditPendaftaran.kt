package com.robiul.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.EditPendaftaranAdapter
import com.robiul.healthapp.databinding.ActivityEditPendaftaranBinding
import com.robiul.healthapp.model.EditPendaftaranModel
import java.util.*
import kotlin.collections.ArrayList

class EditPendaftaran : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var editPenRecyclerView: RecyclerView
    lateinit var editPenArrayList: ArrayList<EditPendaftaranModel>
    lateinit var binding: ActivityEditPendaftaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_edit_pendaftaran)

        editPenRecyclerView = binding.pendaftaranList
        editPenRecyclerView.layoutManager = LinearLayoutManager(this)
        editPenRecyclerView.setHasFixedSize(true)

        editPenArrayList = arrayListOf<EditPendaftaranModel>()

        binding.imgBBack.setOnClickListener {
            finish()
        }

        getData()

    }

    private fun getData() {
        dbRef = FirebaseDatabase.getInstance().getReference("pendaftaran")

        dbRef.child("pasien").addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    editPenArrayList.clear()
                    for (pendafSnapshot in snapshot.children){
                        val pendaf = pendafSnapshot.getValue(EditPendaftaranModel::class.java)
                        editPenArrayList.add(pendaf!!)
                    }

                    editPenRecyclerView.adapter = EditPendaftaranAdapter(editPenArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}