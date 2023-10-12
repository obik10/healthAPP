package com.robiul.healthapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.robiul.healthapp.Pendaftaran
import com.robiul.healthapp.R
import com.robiul.healthapp.model.KlinikModel
class DaftarKlinikAdapter(private val klinikList: ArrayList<KlinikModel>) : RecyclerView.Adapter<DaftarKlinikAdapter.KlinikViewHolder>() {

    var listener: clickListener? = null

    interface clickListener{
        fun onItemClicked(view: View, klinik: KlinikModel)
    }

    class  KlinikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val namaDok : TextView = itemView.findViewById(R.id.tvNama)
        val jenisK : TextView = itemView.findViewById(R.id.tvJenisK)
        val klinik : TextView = itemView.findViewById(R.id.tvKlinik)
        val jadwal : TextView = itemView.findViewById(R.id.tvJadwal)

//        init {
//            itemView.setOnClickListener {v ->
//                Intent(v.context, Pendaftaran::class.java).also {
//
//                    Toast.makeText(v.context, "$namaDok berhasil di klik", Toast.LENGTH_SHORT).show()
////                    (v.context as Activity).finish()
//                }
//
//
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KlinikViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_klinik,
        parent, false)
        return KlinikViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KlinikViewHolder, position: Int) {
        val currentItem = klinikList[position]

        holder.namaDok.text = currentItem.namaDokter
        holder.jenisK.text = currentItem.jenisK
        holder.klinik.text = currentItem.jenisLayanan
        holder.jadwal.text = currentItem.jadwal

        holder.itemView.setOnClickListener {v->
            val resultIntent = Intent().apply {
                putExtra("klinik", currentItem.jenisLayanan)
                putExtra("dokter", currentItem.namaDokter)
            }
                (v.context as Activity).apply {
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
//                Intent(v.context, Pendaftaran::class.java).also {
//                    it.putExtra("klinik", holder.klinik.text.toString())
//                    it.putExtra("dokter", holder.namaDok.text.toString())
////                    v.context.startActivity(it)
////                    Toast.makeText(v.context, "${holder.namaDok.text.toString()} berhasil di klik", Toast.LENGTH_SHORT).show()
//                    (v.context as Activity).setResult(Activity.RESULT_OK, it)
//                    (v.context as Activity).finish()
//                }
        }


    }

    override fun getItemCount(): Int {
        return  klinikList.size
    }

}
