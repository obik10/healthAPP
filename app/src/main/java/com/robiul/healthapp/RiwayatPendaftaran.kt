package com.robiul.healthapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.EditPendaftaranAdapter
import com.robiul.healthapp.adapter.RiwayatPendaftaranAdapter
import com.robiul.healthapp.adapter.UserAdapter
import com.robiul.healthapp.databinding.ActivityRiwayatPendaftaranBinding
import com.robiul.healthapp.model.PendaftaranModel
import com.robiul.healthapp.model.User

class RiwayatPendaftaran : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var rwtPenRecyclerView: RecyclerView
    lateinit var rwtPenArrayList: ArrayList<PendaftaranModel>
    lateinit var binding: ActivityRiwayatPendaftaranBinding
    lateinit var namaTemp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_riwayat_pendaftaran)

        binding.btnBack.setOnClickListener {
            finish()
        }

        rwtPenRecyclerView = binding.rwtPenList
        rwtPenRecyclerView.layoutManager = LinearLayoutManager(this)
        rwtPenRecyclerView.setHasFixedSize(true)

        rwtPenArrayList = arrayListOf<PendaftaranModel>()

        val bundle = intent.extras
        if (bundle != null){
            namaTemp = bundle.getString("id").toString()
        }

        getData()

    }

    private fun getData() {

        dbRef = FirebaseDatabase.getInstance().getReference("pendaftaran")

        dbRef.child("pasien").addValueEventListener(object  : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (rwtSnapshot in snapshot.children){
                        if (rwtSnapshot.key.equals(namaTemp)){
                            val rwtPen = rwtSnapshot.getValue(PendaftaranModel::class.java)
                            rwtPenArrayList.add(rwtPen!!)
                        }
                    }

                    rwtPenRecyclerView.adapter = RiwayatPendaftaranAdapter(rwtPenArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}