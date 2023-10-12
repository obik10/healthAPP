package com.robiul.healthapp.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.R
import com.robiul.healthapp.model.SaranModel

class Edit_SaranAdapter(private val saranList: ArrayList<SaranModel>) : RecyclerView.Adapter<Edit_SaranAdapter.SaranViewHolder>() {
    lateinit var dbRef: DatabaseReference

    var removedItem: Int? = null

    class SaranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaSaran: TextView = itemView.findViewById(R.id.tvNamaSaran)
        val isiText: TextView = itemView.findViewById(R.id.TVisiText)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaranViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_edit_saran),
            parent, false)
        return SaranViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaranViewHolder, position: Int) {
        val currentItem = saranList[position]

        holder.namaSaran.text = currentItem.Nama
        holder.isiText.text = currentItem.isiSaran

        holder.btnHapus.setOnClickListener { v->

            dbRef = FirebaseDatabase.getInstance().getReference("Saran")
            val dbUser = dbRef.child(holder.namaSaran.text.toString())


            // menampilkan dialog peringatan

            val builder = AlertDialog.Builder(v.context)
                .setTitle("Hapus data ${holder.namaSaran.text.toString()}")
                .setMessage("Yakin Dihapus?")
                .setPositiveButton("Iya"){ dialog, id ->
                    saranList.removeAt(position)
                    removedItem = position
                    notifyItemRemoved(position)

                    dbUser.child(holder.namaSaran.text.toString()).removeValue().addOnSuccessListener{
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

    override fun getItemCount(): Int {
        return saranList.size
    }
}