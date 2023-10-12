package com.robiul.healthapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.DaftarKlinik
import com.robiul.healthapp.Pendaftaran
import com.robiul.healthapp.R
import com.robiul.healthapp.UpdatePendaftaran
import com.robiul.healthapp.model.EditPendaftaranModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditPendaftaranAdapter(private val penList: ArrayList<EditPendaftaranModel>) : RecyclerView.Adapter<EditPendaftaranAdapter.EditPenViewHolder>() {

    lateinit var dbRef: DatabaseReference
    var removedItem: Int? = null
    var cal = Calendar.getInstance()



    class EditPenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val id: TextView = itemView.findViewById(R.id.tvId)
        val nama: TextView = itemView.findViewById(R.id.tvNama)
        val tgl: TextView = itemView.findViewById(R.id.tvTanggal)
        val jenisK: TextView = itemView.findViewById(R.id.tvJenisK)
        val klinik: TextView = itemView.findViewById(R.id.tvKlinik)
        val dokter: TextView = itemView.findViewById(R.id.tvDokter)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)

//        init {
//            btnEdit.setOnClickListener { v->
//                Intent(v.context, UpdatePendaftaran::class.java).also {
//                    v.context.startActivity(it)
//                }
//            }
//        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditPenViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_edit_pendaftaran),
        parent, false)
        return EditPenViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EditPenViewHolder, position: Int) {
        val curentItem = penList[position]

        holder.id.text = curentItem.id
        holder.nama.text = curentItem.nama
        holder.tgl.text = curentItem.tanggal
        holder.jenisK.text = curentItem.jenisK
        holder.klinik.text = curentItem.klinik
        holder.dokter.text = curentItem.dokter

        editData(holder.id.text.toString(),
            holder.nama.text.toString(),
            holder.tgl.text.toString(),
        holder.jenisK.text.toString(),
        holder.klinik.text.toString(),
        holder.dokter.text.toString(),
            holder.btnEdit)
//
//        holder.btnEdit.setOnClickListener { v->
//           Intent(context, UpdatePendaftaran::class.java).also {
//               context.startActivity(it)
//           }
//        }

        holder.btnHapus.setOnClickListener { v ->

            dbRef = FirebaseDatabase.getInstance().getReference("pendaftaran")
            val dbPasien = dbRef.child("pasien")

            // menampilkan dialog peringatan
            val builder = AlertDialog.Builder(v.context)
                .setTitle("Hapus data ${holder.nama.text.toString()}")
                .setMessage("Yakin Dihapus?")
                .setPositiveButton("Iya"){ dialog, id ->
                    penList.removeAt(position)
                    removedItem = position
                    notifyItemRemoved(position)

                    dbPasien.child(holder.id.text.toString()).removeValue().addOnSuccessListener{
                        Toast.makeText(v.context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(v.context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak") { dialog, id -> dialog.cancel()  }
            val alert =  builder.create()
            alert.show()
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun editData(
        idPend: String,
        nama: String,
        tgl: String,
        jenisK: String,
        klinik: String,
        dokter: String,
        btnEdit: Button
    ) {

        btnEdit.setOnClickListener {

            var dialog = LayoutInflater.from(it.context).inflate(R.layout.activity_update_pendaftaran, null)

            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(it.context)
                .setView(dialog)

            val alertDialog = mBuilder.show()
            val edId = dialog.findViewById<TextView>(R.id.edId)
            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edTgl = dialog.findViewById<EditText>(R.id.edTgl)
            val edJenisK = dialog.findViewById<EditText>(R.id.edJenisK)
            val edKlinik = dialog.findViewById<EditText>(R.id.edKlinik)
            val edDokter = dialog.findViewById<EditText>(R.id.edDokter)
            val btnKonfirm = dialog.findViewById<Button>(R.id.btnKonfirm)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            val dateSetListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, day)
                    updateDateInView(edTgl)
                }
            }

            edTgl.setOnClickListener(object  : View.OnClickListener{
                override fun onClick(view: View) {
                    DatePickerDialog(view.context,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            })

            edNama!!.setText(nama)
            edId!!.setText(idPend)
            edTgl!!.setText(tgl)
            edJenisK!!.setText(jenisK)
            edKlinik!!.setText(klinik)
            edDokter!!.setText(dokter)

//            edKlinik.setOnClickListener {v->
//                Intent(v.context, DaftarKlinik::class.java).also {
//                    (v.context as Activity).startActivityForResult(it, REQUEST_CODE)
//                }
//
//            }

            btnKonfirm.setOnClickListener {
                val dataPendaf = mapOf<String, String>(
                    "nama" to edNama.text.toString(),
                    "jenisK" to edJenisK.text.toString(),
                    "tanggal" to edTgl.text.toString(),
                    "klinik" to edKlinik.text.toString(),
                    "dokter" to edDokter.text.toString()
                )

                dbRef = FirebaseDatabase.getInstance().getReference("pendaftaran")
                val dbPasien = dbRef.child("pasien")

                dbPasien.child(idPend).updateChildren(dataPendaf).addOnSuccessListener {
                    alertDialog.dismiss()
                    Toast.makeText(dialog.context, "Berhasil Update", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(dialog.context, "Gagal Update", Toast.LENGTH_SHORT).show()

                }

            }

            btnBatal.setOnClickListener {v->
                alertDialog.dismiss()

            }


        }

    }


    companion object{
        const val REQUEST_CODE = 123
    }

    private fun updateDateInView(tgl: TextView) {
        val formatTgl = "dd/MM/yyy"
        val sdf = SimpleDateFormat(formatTgl, Locale.getDefault())
        tgl.setText(sdf.format(cal.time))
    }

    override fun getItemCount(): Int {
        return penList.size
    }

}
