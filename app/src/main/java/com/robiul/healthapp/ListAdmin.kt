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
import com.robiul.healthapp.adapter.ListAdminAdapter
import com.robiul.healthapp.adapter.UserAdapter
import com.robiul.healthapp.databinding.ActivityListAdminBinding
import com.robiul.healthapp.databinding.ActivityListUserBinding
import com.robiul.healthapp.model.AdminModel
import com.robiul.healthapp.model.DokterModel
import com.robiul.healthapp.model.User

class ListAdmin : AppCompatActivity() {
    lateinit var dbRef : DatabaseReference
    lateinit var adminRecyclerView : RecyclerView
    lateinit var adminArrayList : ArrayList<AdminModel>
    lateinit var binding: ActivityListAdminBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_list_admin)
        val idAdmin = intent.getStringExtra("id").toString()

        binding.btnBack.setOnClickListener {
            finish()
        }

        adminRecyclerView = binding.adminList
        adminRecyclerView.layoutManager = LinearLayoutManager(this)
        adminRecyclerView.setHasFixedSize(true)

        adminArrayList = arrayListOf<AdminModel>()
        getUserData()

        binding.btnTambah.setOnClickListener {
            var dialog = LayoutInflater.from(it.context).inflate(R.layout.tambah_admin, null)

            val mBuilder = AlertDialog.Builder(it.context)
                .setView(dialog)

            val alertDialog = mBuilder.show()
            val id = dialog.findViewById<EditText>(R.id.edId)
            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edEmail = dialog.findViewById<EditText>(R.id.edEmail)
            val edPass = dialog.findViewById<EditText>(R.id.edPass)
            val btnTambah = dialog.findViewById<Button>(R.id.btnAdd)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            dbRef = FirebaseDatabase.getInstance().getReference("login")
            val dbAdmin = dbRef.child("admin")
            val dbUser = Firebase.database
            val userRef = dbUser.getReference("numbers")


            btnTambah.setOnClickListener {

                userRef.runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                        val id = mutableData.child("adminId").value as Long?
                        val incrementId = (id ?: 0) + 1
                    if (incrementId == null){
                        mutableData.child("adminId").value = 1L
                    } else {

                        val increment = mutableData.child("adminId").value as Long
//                            mutableData.child(incrementId.toString()).child("data1").value = increment
//                            mutableData.child(incrementId.toString()).child("data2").value = increment

                        mutableData.child("adminId").value = incrementId

                        val user = AdminModel(
                            increment.toString(),
                            edNama.text.toString(),
                            edEmail.text.toString(),
                            edPass.text.toString()
                        )

                        dbAdmin.child(increment.toString()).setValue(user)
                            .addOnSuccessListener {
                                alertDialog.dismiss()
                                Toast.makeText(this@ListAdmin, "Berhasil", Toast.LENGTH_LONG).show()

                            }.addOnFailureListener {
                                Toast.makeText(this@ListAdmin, "Failed", Toast.LENGTH_SHORT).show()
                            }

                    }

                        return Transaction.success(mutableData)
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

                btnBatal.setOnClickListener {
                    alertDialog.dismiss()
                }

            }

        }

    }

    private fun getUserData() {
        dbRef = FirebaseDatabase.getInstance().getReference("login")

        dbRef.child("admin").addValueEventListener(object  : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    adminArrayList.clear()
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(AdminModel::class.java)
                        adminArrayList.add(user!!)

                    }

                    adminRecyclerView.adapter = ListAdminAdapter(adminArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}