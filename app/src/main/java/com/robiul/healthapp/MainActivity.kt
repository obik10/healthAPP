package com.robiul.healthapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import com.robiul.healthapp.databinding.ActivityMainBinding
import com.robiul.healthapp.model.User

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dbRef: DatabaseReference
    lateinit var dbUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        binding.loginBTN.setOnClickListener {

            val id = binding.etId.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()


            // validasi isian data
            if (id.isEmpty()){
                binding.etId.error = "Id harus diisi!"
                binding.etId.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.etEmail.error = "Email harus diisi!"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }else if (pass.isEmpty()){
                binding.etPassword.error = "Password harus diisi!"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Login Admin
            dbRef = FirebaseDatabase.getInstance().getReference("login")

            dbRef.child("admin").child(id).get().addOnSuccessListener {

                if (it.exists()){
                    val emailF = it.child("email").value
                    val passF = it.child("pass").value
                    // cek email dan pass yg dimasukkan
                    if (email == emailF && pass == passF){
                        val namaF = it.child("nama").value
                        val intent = Intent(this, Dashboard_Admin::class.java)

                        intent.putExtra("nama", namaF.toString())
                        intent.putExtra("email", email)
                        intent.putExtra("id", id)
                        startActivity(intent)

                        Toast.makeText(this, "berhasil login", Toast.LENGTH_SHORT).show()
                        finish()
                    }  else {
                        Toast.makeText(this, "email atau password salah!", Toast.LENGTH_SHORT).show()

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Username yang dimasukkan salah!!", Toast.LENGTH_SHORT).show()
            }

//             cek nilai nama dalam child user
            dbUser = FirebaseDatabase.getInstance().getReference("login")

            dbUser.child("user").child(id).get().addOnSuccessListener {

                if (it.exists()){
                    val namaF = it.child("nama").value
                    val emailF = it.child("email").value
                    val passF = it.child("pass").value
                    // cek email dan pass yg dimasukkan
                    if (email == emailF && pass == passF){
                        val intent = Intent(this, Homepage::class.java)
                        intent.putExtra("id", id)
                        intent.putExtra("nama", namaF.toString())
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()

                        Toast.makeText(this, "berhasil login", Toast.LENGTH_SHORT).show()
                    }  else {
                        Toast.makeText(this, "email atau password salah!", Toast.LENGTH_SHORT).show()

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Username yang dimasukkan salah!!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.clickRegister.setOnClickListener{
            val intent = Intent(this, Daftar::class.java)
            startActivity(intent)
        }

        binding.clickLupaID.setOnClickListener {

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_lupa_pwd_id, null)
            val edName = dialogView.findViewById<EditText>(R.id.editTextName)
            val edEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)


            // Di dalam fungsi showTwoInputDialog
            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                .setView(dialogView)
                .setTitle("Input Data")
                .setPositiveButton("Ok") { dialog, which ->
                    val inputName = edName.text.toString()
                    val inputEmail = edEmail.text.toString()

                    validateAndShowNotification(inputName, inputEmail)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }

            val dialog = alertDialogBuilder.create()
            dialog.show()

        }

    }

    private fun validateAndShowNotification(inputName: String, inputEmail: String) {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val CHANNEL_ID = "MY_chanel"

            val userReference = databaseReference.child("login").child("user")
                .orderByChild("nama")
                .equalTo(inputName)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user?.email == inputEmail) {
                            createNotificationChannel(CHANNEL_ID)

                            // Menampilkan notifikasi dengan informasi user
                            val notificationManager = NotificationManagerCompat.from(this@MainActivity)
                            val notificationBuilder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_lonceng)
                                .setContentTitle("User Info")
                                .setContentText("ID: ${user.id}, Password: ${user.pass}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)

                            notificationManager.notify(889, notificationBuilder.build())
                            return
                        }
                    }
                    // Tampilkan pesan kesalahan
                    Toast.makeText(this@MainActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Database eror", Toast.LENGTH_LONG).show()

                }
            })
        }

    private fun createNotificationChannel(CHANNEL_ID: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}