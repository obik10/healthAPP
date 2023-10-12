package com.robiul.healthapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.robiul.healthapp.adapter.EditDokterAdapter
import com.robiul.healthapp.databinding.ActivityEditDokterJadwalBinding
import com.robiul.healthapp.model.DokterModel
import com.robiul.healthapp.model.User

class EditDokterJadwal : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var editDokRecyclerView : RecyclerView
    lateinit var editDokArray :ArrayList<DokterModel>
    lateinit var binding : ActivityEditDokterJadwalBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDokterJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_edit_dokter_jadwal)

        editDokRecyclerView = binding.editDokterJadwalList
        editDokRecyclerView.layoutManager = LinearLayoutManager(this)
        editDokRecyclerView.setHasFixedSize(true)

        editDokArray = arrayListOf<DokterModel>()

        getDataDokter()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnTambah.setOnClickListener {
            var dialog = LayoutInflater.from(it.context).inflate(R.layout.tambah_dokter, null)

            val mBuilder = AlertDialog.Builder(it.context)
                .setView(dialog)

            val alertDialog = mBuilder.show()
            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edJenisK = dialog.findViewById<EditText>(R.id.edJenisK)
            val edKlinik = dialog.findViewById<EditText>(R.id.edKlinik)
            val edJadwal = dialog.findViewById<EditText>(R.id.edJadwal)
            val btnTambah = dialog.findViewById<Button>(R.id.btnAdd)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            dbRef = FirebaseDatabase.getInstance().getReference("dataDokter")
            val dbDok = dbRef.child("dokter")

            val dbId = Firebase.database
            val idDok = dbId.getReference("numbers")

            idDok.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val id = mutableData.child("idDokter").value as Long?
                    val incrementId = (id ?: 0) + 1
                    mutableData.child("idDokter").value = incrementId

                    val increment = mutableData.child("idDokter").value as Long
//                            mutableData.child(incrementId.toString()).child("data1").value = increment
//                            mutableData.child(incrementId.toString()).child("data2").value = increment

                    btnTambah.setOnClickListener {
                        val dokAdapter = DokterModel(increment.toString(), edNama.text.toString(), edJenisK.text.toString(), edKlinik.text.toString(), edJadwal.text.toString())

                        dbDok.child(increment.toString()).setValue(dokAdapter).addOnSuccessListener {
                            alertDialog.dismiss()
                            Toast.makeText(dialog.context, "berhasil", Toast.LENGTH_LONG).show()

                        }.addOnFailureListener {
                            Toast.makeText(dialog.context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btnBatal.setOnClickListener {
                        alertDialog.dismiss()
                    }

                    return  Transaction.success(mutableData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null) {
                        println("Transaction failed: ${error.message}")
                    }
                }

            })

//            btnTambah.setOnClickListener {
//                val dokAdapter = DokterModel(edNama.text.toString(), edJenisK.text.toString(), edKlinik.text.toString(), edJadwal.text.toString())
//
//                dbDok.child(edNama.text.toString()).setValue(dokAdapter).addOnSuccessListener {
//                    alertDialog.dismiss()
//                    Toast.makeText(dialog.context, "berhasil", Toast.LENGTH_LONG).show()
//
//                }.addOnFailureListener {
//                    Toast.makeText(dialog.context, "Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            btnBatal.setOnClickListener {
//                alertDialog.dismiss()
//            }



        }

    }

    private fun getDataDokter() {
        dbRef = FirebaseDatabase.getInstance().getReference("dataDokter")

        dbRef.child("dokter").addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    editDokArray.clear()
                    for (dokSnapshot in snapshot.children){
                        val dok = dokSnapshot.getValue(DokterModel::class.java)
                        editDokArray.add(dok!!)
                    }

                    editDokRecyclerView.adapter = EditDokterAdapter(editDokArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}