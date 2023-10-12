package com.robiul.healthapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robiul.healthapp.Pendaftaran
import com.robiul.healthapp.R
import com.robiul.healthapp.model.KlinikModel
class ViewKlinikEdit(private val klinikList: ArrayList<KlinikModel>) : RecyclerView.Adapter<ViewKlinikEdit.ViewKlinikViewHolder>() {
//
//    var listener: clickListener? = null
//
//    interface clickListener{
//        fun onItemClicked(view: View, klinik: KlinikModel)
//    }

    class  ViewKlinikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val namaKlinik : TextView = itemView.findViewById(R.id.namaKlinik)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewKlinikViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_edit_view_klinik,
            parent, false)
        return ViewKlinikViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewKlinikViewHolder, position: Int) {
        val currentItem = klinikList[position]

        holder.namaKlinik.text = currentItem.jenisLayanan

//        holder.itemView.setOnClickListener {v->
//         Intent(v.context, Pendaftaran::class.java).also {
//             v.context.startActivity(it)
//         }
////                Intent(v.context, Pendaftaran::class.java).also {
////                    it.putExtra("klinik", holder.klinik.text.toString())
////                    it.putExtra("dokter", holder.namaDok.text.toString())
//////                    v.context.startActivity(it)
//////                    Toast.makeText(v.context, "${holder.namaDok.text.toString()} berhasil di klik", Toast.LENGTH_SHORT).show()
////                    (v.context as Activity).setResult(Activity.RESULT_OK, it)
////                    (v.context as Activity).finish()
////                }
//        }


    }

    override fun getItemCount(): Int {
        return  klinikList.size
    }

}
