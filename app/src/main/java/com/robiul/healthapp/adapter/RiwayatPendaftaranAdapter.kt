package com.robiul.healthapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.R
import com.robiul.healthapp.model.PendaftaranModel

class RiwayatPendaftaranAdapter (private val rwtPenList: ArrayList<PendaftaranModel>) : RecyclerView.Adapter<RiwayatPendaftaranAdapter.RwtPenViewHolder>() {

    lateinit var dbAntre: DatabaseReference

    class RwtPenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val nama: TextView = itemView.findViewById(R.id.tvNama)
        val tgl: TextView = itemView.findViewById(R.id.tvTanggal)
        val jenisK: TextView = itemView.findViewById(R.id.tvJenisK)
        val klinik: TextView = itemView.findViewById(R.id.tvKlinik)
        val dokter: TextView = itemView.findViewById(R.id.tvDokter)
        val antrean: TextView = itemView.findViewById(R.id.tvAntrean)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RwtPenViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_riwayat_pendaftaran),
            parent, false)
        return RwtPenViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RwtPenViewHolder, position: Int) {
        val curentItem = rwtPenList[position]

        holder.nama.text = curentItem.nama
        holder.tgl.text = curentItem.tanggal
        holder.jenisK.text = curentItem.jenisK
        holder.klinik.text = curentItem.klinik
        holder.dokter.text = curentItem.dokter

        dbAntre = FirebaseDatabase.getInstance().getReference("antrean")

        dbAntre.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (rwtSnapshot in snapshot.children){
                        if (rwtSnapshot.key!!.equals(holder.nama.text.toString())){
                            val noAntre: String = rwtSnapshot.child("antrean").value.toString()

                            holder.antrean.setText(noAntre)
                        }
                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun getItemCount(): Int {
        return rwtPenList.size
    }
}