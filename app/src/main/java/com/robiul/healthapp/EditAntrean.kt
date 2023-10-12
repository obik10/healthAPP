package com.robiul.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.EditAntreanAdapter
import com.robiul.healthapp.adapter.EditDokterAdapter
import com.robiul.healthapp.databinding.ActivityEditAntreanBinding
import com.robiul.healthapp.model.AntreanModel
import com.robiul.healthapp.model.DokterModel

class EditAntrean : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var editAntreRecyclerView: RecyclerView
    lateinit var editAntreArray: ArrayList<AntreanModel>
    lateinit var binding: ActivityEditAntreanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAntreanBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_edit_antrean)

        binding.btnBack.setOnClickListener {
            finish()
        }

        editAntreRecyclerView = binding.EditantreanList
        editAntreRecyclerView.layoutManager = LinearLayoutManager(this)
        editAntreRecyclerView.setHasFixedSize(true)

        editAntreArray = arrayListOf<AntreanModel>()
        getData()

    }

    private fun getData() {
        dbRef = FirebaseDatabase.getInstance().getReference("antrean")

        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    editAntreArray.clear()
                    for (antreSnapshot in snapshot.children){
                        val antre = antreSnapshot.getValue(AntreanModel::class.java)
                        editAntreArray.add(antre!!)
                    }

                    editAntreRecyclerView.adapter = EditAntreanAdapter(editAntreArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    }