package com.robiul.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.robiul.healthapp.databinding.ActivityDaftarBinding
//import com.robiul.healthapp.room.HealthDB
import com.robiul.healthapp.model.User

class Daftar : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var binding: ActivityDaftarBinding
    lateinit var getJenisK: String
    lateinit var auth: FirebaseAuth
    //lateinit var btnRegister: Button
    lateinit var dbRef: DatabaseReference


   // val db by lazy { HealthDB(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setUpListener()

        val spinner: Spinner = findViewById(R.id.sp_jenisK)


        binding.registerBTN.setOnClickListener {


            val nama = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val pass2 = binding.etKonfirmasiPass.text.toString()

                dbRef = FirebaseDatabase.getInstance().getReference("login")

                if (nama.isEmpty()) {
                    binding.etFullName.error = "Nama harus diisi!"
                    binding.etFullName.requestFocus()
                    return@setOnClickListener
                }
                if (email.isEmpty()) {
                    binding.etEmail.error = "Email harus diisi!"
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }
                if (pass.isEmpty()) {
                    binding.etPassword.error = "Password harus diisi!"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }
                if (pass2 == pass) {

                    val dbUser = Firebase.database
                    val userRef = dbUser.getReference("numbers")

                    userRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            val id = mutableData.child("random").value as Long?
                            val incrementId = (id ?: 0) + 1

                            if (incrementId == null){
                                mutableData.child("random").value = 1L
                            } else{
                                mutableData.child("random").value = incrementId
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
                            } else if (committed){
                                val increment = currentData?.child("random")?.value as Long
//                            mutableData.child(incrementId.toString()).child("data1").value = increment
//                            mutableData.child(incrementId.toString()).child("data2").value = increment
                                val user = User(increment.toString(), nama, email, getJenisK, pass)

                                dbRef.child("user").child(increment.toString()).setValue(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@Daftar,
                                            "id anda adalah $increment",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }.addOnFailureListener {
                                        Toast.makeText(this@Daftar, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                    })


//                    val user = User(idUser++.toString(), nama, email, getJenisK, pass)
//                    dbRef.child("user").child((myId++).toString()).setValue(user).addOnSuccessListener {
//                        Toast.makeText(this, "id anda adalah $myId", Toast.LENGTH_LONG).show()
//
//                    }.addOnFailureListener {
//                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//                    }

                } else {
                    binding.etKonfirmasiPass.error = "Password yang dimasukkan salah!!"
                    binding.etKonfirmasiPass.requestFocus()
                    return@setOnClickListener

                }
            finish()
        }


        //membuat ArrayAdapter dengan string array dan spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.jenisK,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // menampilkan pilihan pada layout
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // menerapkan adapter ke spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val jenisK: String = parent?.getItemAtPosition(pos).toString()
        getJenisK = jenisK
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

//    btnRegister.setOnClickListener {
//        val email = etEmail.text.toString().trim()
//        val password = etPassword.text.toString().trim()
//        val password2 = etKonfirmPass.text.toString().trim()
//
//        if (email.isEmpty()){
//            etEmail.error = "Email Harus Diisi!"
//            etEmail.requestFocus()
//            return@setOnClickListener
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            etEmail.error = "Email Tidak Valid!"
//            etEmail.requestFocus()
//            return@setOnClickListener
//        }
//        if (password.isEmpty() && password2.isEmpty() || password.length < 6 || password2.length < 6){
//            etPassword.error = "Password harus lebih dari 6 karakter!"
//            etPassword.requestFocus()
//            etKonfirmPass.error = "Password harus lebih dari 6 karakter!"
//            etKonfirmPass.requestFocus()
//            return@setOnClickListener
//        }
//
//        if (password2 != password){
//            etKonfirmPass.error = "Password yang dimasukkan salah!"
//            etKonfirmPass.requestFocus()
//            return@setOnClickListener
//        }
//        registerUser(email, password)
//
//    }



//    fun setUpListener() {
//        btnRegister.setOnClickListener {
//
//          //  if (etPassword.text.toString().trim() == etKonfirmPass.text.toString().trim()){
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.userDao().addUser(
//                        User(0,
//                            etFullName.text.toString(),
//                            etEmail.text.toString(),
//                            getJenisK,
//                            etPassword.text.toString())
//                    )
//                    finish()
//                }
////            } else{
////                etKonfirmPass.error = "Password yang dimasukkan salah!"
////                etKonfirmPass.requestFocus()
////                return@setOnClickListener
////            }
//
//        }
//    }

    // fungsi register
//    private fun registerUser (email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this){ it ->
//                if (it.isSuccessful){
////                    Intent(this@Daftar, Homepage::class.java).also {
////                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                        startActivity(it)
////                    }
//                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
//
//                } else {
//                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }



}