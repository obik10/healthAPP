package com.robiul.healthapp.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.R
import com.robiul.healthapp.model.DokterModel

class EditDokterAdapter(private val editDokList: ArrayList<DokterModel>) :
    RecyclerView.Adapter<EditDokterAdapter.editDokViewHolder>() {

    lateinit var dbRef: DatabaseReference
    var removedItem: Int? = null


    class editDokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaDok: TextView = itemView.findViewById(R.id.tvNamaDok)
        val idDok: TextView = itemView.findViewById(R.id.tvIdDok)
        val jenisK: TextView = itemView.findViewById(R.id.tvJenisKDok)
        val klinik: TextView = itemView.findViewById(R.id.tvKlinikDok)
        val jadwal: TextView = itemView.findViewById(R.id.tvJadwalDok)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): editDokViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_edit_dokter, parent, false
        )
        return editDokViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return editDokList.size
    }

    override fun onBindViewHolder(holder: editDokViewHolder, position: Int) {
        val currentItem = editDokList[position]

        holder.namaDok.text = currentItem.namaDokter
        holder.idDok.text = currentItem.idDOk
        holder.jenisK.text = currentItem.jenisK
        holder.klinik.text = currentItem.jenisLayanan
        holder.jadwal.text = currentItem.jadwal

        editData(
            holder.namaDok.text.toString(),
            holder.idDok.text.toString(),
            holder.jenisK.text.toString(),
            holder.klinik.text.toString(),
            holder.jadwal.text.toString(),
            holder.btnEdit
        )

        // menghapus data
        holder.btnHapus.setOnClickListener { v ->
            dbRef = FirebaseDatabase.getInstance().getReference("dataDokter")
            val dbDokter = dbRef.child("dokter")

            // menampilkan dialog peringatan
            val builder = AlertDialog.Builder(v.context)
                .setTitle("Hapus data ${holder.namaDok.text.toString()}")
                .setMessage("Yakin Dihapus?").setPositiveButton("Iya") { dialog, id ->
                    editDokList.removeAt(position)
                    removedItem = position
                    notifyItemRemoved(position)


                    dbDokter.child(holder.idDok.text.toString()).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(v.context, "Data berhasil dihapus", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(v.context, "error", Toast.LENGTH_SHORT).show()
                        }
                }.setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()

        }
    }

    private fun editData(
        nama: String, idDok: String, jenisK: String, klinik: String, jadwal: String, btnEdit: Button
    ) {

        btnEdit.setOnClickListener {
            var dialog =
                LayoutInflater.from(it.context).inflate(R.layout.activity_update_dokter, null)

            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(it.context).setView(dialog)

            val alertDialog = mBuilder.show()
            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edjenisK = dialog.findViewById<EditText>(R.id.edJenisK)
            val edKlinik = dialog.findViewById<EditText>(R.id.edKlinik)
            val edJadwal = dialog.findViewById<EditText>(R.id.edJadwal)
            val btnKonfirm = dialog.findViewById<Button>(R.id.btnKonfirm)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            edNama!!.setText(nama)
            edjenisK!!.setText(jenisK)
            edKlinik!!.setText(klinik)
            edJadwal!!.setText(jadwal)

            btnKonfirm.setOnClickListener {
                val dataDokter = mapOf<String, String>(
                    "namaDokter" to edNama.text.toString(),
                    "jenisK" to edjenisK.text.toString(),
                    "jenisLayanan" to edKlinik.text.toString(),
                    "jadwal" to edJadwal.text.toString()
                )

                dbRef = FirebaseDatabase.getInstance().getReference("dataDokter")
                val dbDokter = dbRef.child("dokter")

                dbDokter.child(idDok).updateChildren(dataDokter).addOnSuccessListener {
                    alertDialog.dismiss()
                    Toast.makeText(dialog.context, "Berhasil Update", Toast.LENGTH_SHORT).show()


                }.addOnFailureListener {
                    Toast.makeText(dialog.context, "Gagal Update", Toast.LENGTH_SHORT).show()

                }

            }

            btnBatal.setOnClickListener {
                alertDialog.dismiss()
            }

        }

    }


}