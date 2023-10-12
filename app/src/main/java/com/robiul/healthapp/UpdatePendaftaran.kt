package com.robiul.healthapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.databinding.ActivityPendaftaranBinding
import com.robiul.healthapp.databinding.ActivityUpdatePendaftaranBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdatePendaftaran : AppCompatActivity() {

    lateinit var binding: ActivityUpdatePendaftaranBinding

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_update_pendaftaran)

//        binding.edKlinik.setOnClickListener {
//            Intent(this@UpdatePendaftaran, DaftarKlinik::class.java).also {
//                startActivityForResult(it, REQUEST_CODE)
//            }
//        }
//
//        binding.btnCancel.setOnClickListener {
//            finish()
//        }
//
//        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
//            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//                cal.set(Calendar.YEAR, year)
//                cal.set(Calendar.MONTH, month)
//                cal.set(Calendar.DAY_OF_MONTH, day)
//                updateDateInView()
//            }
//        }
//
//        binding.edTgl.setOnClickListener(object  : View.OnClickListener{
//            override fun onClick(view: View) {
//                DatePickerDialog(view.context,
//                    dateSetListener,
//                    cal.get(Calendar.YEAR),
//                    cal.get(Calendar.MONTH),
//                    cal.get(Calendar.DAY_OF_MONTH)).show()
//            }
//        })
//
//        binding.btnKonfirm.setOnClickListener {
//            val dataPendaf = mapOf<String, String>(
//                "nama" to binding.edNama.text.toString(),
//                "jenisK" to binding.edJenisK.text.toString(),
//                "tanggal" to binding.edTgl.text.toString(),
//                "klinik" to binding.edKlinik.text.toString(),
//                "dokter" to binding.edDokter.text.toString()
//            )
//
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val klinikX = data?.getStringExtra("klinik")
            val dokterX = data?.getStringExtra("dokter")

            binding.edKlinik.setText(klinikX)
            binding.edDokter.setText(dokterX)
        }
    }

    companion object{
        const val REQUEST_CODE = 1
    }
}