package com.robiul.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.EditAntreanAdapter
import com.robiul.healthapp.adapter.Edit_SaranAdapter
import com.robiul.healthapp.databinding.ActivityEditAntreanBinding
import com.robiul.healthapp.databinding.ActivityEditSaranBinding
import com.robiul.healthapp.model.AntreanModel
import com.robiul.healthapp.model.SaranModel

class Edit_Saran : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var editSaranRecyclerView: RecyclerView
    lateinit var editSaranArray: ArrayList<SaranModel>
    lateinit var binding: ActivityEditSaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_edit_saran)

        binding.btnBack.setOnClickListener {
            finish()
        }

        editSaranRecyclerView = binding.EditantreanList
        editSaranRecyclerView.layoutManager = LinearLayoutManager(this)
        editSaranRecyclerView.setHasFixedSize(true)

        editSaranArray = arrayListOf<SaranModel>()
        getData()

    }

    private fun getData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Saran")

        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    editSaranArray.clear()
                    for (namaSnapshot in snapshot.children){
                        val namaSaran = namaSnapshot.getValue(SaranModel::class.java)
                        editSaranArray.add(namaSaran!!)
                    }

                    editSaranRecyclerView.adapter = Edit_SaranAdapter(editSaranArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })    }
}