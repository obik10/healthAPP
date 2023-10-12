package com.robiul.healthapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.robiul.healthapp.databinding.ActivityPendaftaranBinding
import com.robiul.healthapp.model.PendaftaranModel
import com.robiul.healthapp.model.User
import java.text.SimpleDateFormat
import java.util.*


class Pendaftaran : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    lateinit var binding: ActivityPendaftaranBinding
    lateinit var getJenisK: String

    lateinit var dbRef: DatabaseReference
    lateinit var dbRefAntre: DatabaseReference

    var maxId: Int = 1

    // Pick Tanggal
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_pendaftaran)

        val bundleN = intent.getStringExtra("nama").toString()
        binding.etFullName.setText(bundleN)

        val idUser = intent.getStringExtra("id").toString()

        val klinikExtra = intent.getStringExtra("klinik")
        val dokterExtra = intent.getStringExtra("dokter")

        binding.imgBBack.setOnClickListener {
                finish()
        }

        binding.etKlinik.setOnClickListener{
            Intent(this@Pendaftaran, DaftarKlinik::class.java).also {
                startActivityForResult(it, REQUEST_CODE)
            }

        }

        binding.pendaftaranBTN.setOnClickListener {

            val nama = bundleN
            val tgl = binding.etTanggal.text.toString()
            val jenisK = getJenisK
            val getKlinik = binding.etKlinik.text.toString()

            if (tgl.isEmpty()){
                binding.etTanggal.error = "Silahkan pilih tanggal!"
                binding.etTanggal.requestFocus()
                return@setOnClickListener
            }
            if (getKlinik.isEmpty()){
                binding.etKlinik.error = "Silahkan pilih klinik terlebih dahulu!"
                binding.etKlinik.requestFocus()
                return@setOnClickListener
            }

            dbRef  = FirebaseDatabase.getInstance().getReference("pendaftaran")
            dbRefAntre = FirebaseDatabase.getInstance().getReference("antrean")

            val daftarPasien = PendaftaranModel(idUser, nama, tgl, jenisK,
                binding.etKlinik.text.toString(), binding.etDokter.text.toString())

            dbRef.child("pasien").child(idUser).setValue(daftarPasien).addOnSuccessListener{
                //klinikm

                dbRefAntre.child(nama).child("nama").setValue(nama)
                dbRefAntre.child(nama).child("tanggal").setValue(tgl)
                dbRefAntre.child(nama).child("klinik").setValue(getKlinik)

                // mendapat nomor antrean
                val dbUser = Firebase.database
                val userRef = dbUser.getReference("numbers").child(getKlinik)

                // input no antrean
                userRef.runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                        val id = mutableData.child(tgl).child("antre").value as Long?
                        val incrementId = (id ?: 0) + 1


                        mutableData.child(tgl).child("antre").value = incrementId
                        mutableData.child(tgl).child("nama").value = nama

                        val increment = mutableData.child(tgl).child("antre").value as Long
                        dbRefAntre.child(nama).child("antrean").setValue(increment.toString())

//                        dbRefAntre.child(getKlinik).child(tgl).child("nama").setValue(nama)
//                        dbRefAntre.child(getKlinik).child(tgl).child("antrean").setValue(increment.toString())

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


                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, "Gagal Input", Toast.LENGTH_SHORT).show()

            }
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                updateDateInView()
            }
        }


        binding.etTanggal.setOnClickListener(object  : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(view: View) {
                val datePicker = DatePickerDialog(this@Pendaftaran,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

                datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

                // Set the date picker's listener to block Sundays
                datePicker.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    if (dayOfWeek == Calendar.SUNDAY) {
                        // Block selecting Sundays
                        Toast.makeText(this@Pendaftaran, "Tidak bisa memilih hari minggu",  Toast.LENGTH_SHORT).show()
                        datePicker.datePicker.updateDate(year, monthOfYear, dayOfMonth + 1)
                    }
                }

                datePicker.show()
            }
        })

        val spinner: Spinner = findViewById(R.id.sp_jenisK)
        // membuat ArrayAdapter dengan string array dan spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.jenisK,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // menampilkan pilihan pada layout
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // menerapkan adapter kespinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }


    }

    private fun updateDateInView() {
        val formatTgl = "dd-MM-yyy"
        val sdf = SimpleDateFormat(formatTgl, Locale.getDefault())
        binding.etTanggal.setText(sdf.format(cal.time))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val jenisK: String = parent?.getItemAtPosition(pos).toString()
        getJenisK = jenisK

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val klinikX = data?.getStringExtra("klinik")
            val dokterX = data?.getStringExtra("dokter")

            binding.etKlinik.setText(klinikX)
            binding.etDokter.setText(dokterX)        }
    }

    companion object{
        const val REQUEST_CODE = 1
    }


}