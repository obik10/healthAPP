package com.robiul.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.adapter.ViewKlinikEdit
import com.robiul.healthapp.databinding.ActivityDashboardAdminBinding

class Dashboard_Admin : AppCompatActivity() {
    lateinit var binding: ActivityDashboardAdminBinding
    lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_dashboard_admin)

        val bundle = intent.extras
        if (bundle != null){
            binding.tvPggna.text = bundle.getString("nama")?.uppercase()
        }

        val idAdmin = intent.getStringExtra("id").toString()

        binding.imgBPendaftaran.setOnClickListener {
            Intent(this@Dashboard_Admin, EditPendaftaran::class.java).also {
                startActivity(it)
            }
        }

        binding.imgBAntrean.setOnClickListener {
            Intent(this@Dashboard_Admin, EditAntrean::class.java).also {
                startActivity(it)
            }
        }

        binding.imgBDokterDanJadwal.setOnClickListener {
            Intent(this@Dashboard_Admin, EditDokterJadwal::class.java).also {
                startActivity(it)
            }
        }

        binding.imgBUser.setOnClickListener {
            Intent(this@Dashboard_Admin, ListUser::class.java).also {
                startActivity(it)
            }
        }

        binding.listAdmin.setOnClickListener {
            Intent(this@Dashboard_Admin, ListAdmin::class.java).also {
                intent.putExtra("id", idAdmin)
                startActivity(it)
            }
        }

        binding.btnEdSaran.setOnClickListener {
            Intent(this@Dashboard_Admin, Edit_Saran::class.java).also {
                startActivity(it)
            }
        }

        binding.btnEdBantuan.setOnClickListener {
            val inputEditText = EditText(this)
            inputEditText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            inputEditText.isSingleLine = false

            val dialog = AlertDialog.Builder(this)
                .setTitle("Bantuan")
                .setView(inputEditText)
                .setPositiveButton("Kirim") { dialog, which ->
                    val inputBantuan = inputEditText.text.toString()
                    menyimpan(inputBantuan)
                }
                .setNegativeButton("Batal", null)
                .create()

            dialog.show()
        }

    }

    private fun menyimpan(inputSaran: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Bantuan")
        dbRef.child("isiBantuan").setValue(inputSaran)
    }

}