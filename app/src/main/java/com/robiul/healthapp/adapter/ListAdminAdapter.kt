package com.robiul.healthapp.adapter

import android.annotation.SuppressLint
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
import com.robiul.healthapp.ListUser
import com.robiul.healthapp.R
import com.robiul.healthapp.model.AdminModel

class ListAdminAdapter (private val adminList: ArrayList<AdminModel>) : RecyclerView.Adapter<ListAdminAdapter.AdminViewHolder>(){
    lateinit var dbRef: DatabaseReference
    lateinit var dbRef2: DatabaseReference
    val activity : ListUser = ListUser() as ListUser

    var removedItem: Int? = null
    
    class AdminViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val fullName : TextView = itemView.findViewById(R.id.tvNama)
        val idAdmin : TextView = itemView.findViewById(R.id.tvIdAdmin)
        val emailR : TextView = itemView.findViewById(R.id.tvEmail)
        val pass : TextView = itemView.findViewById(R.id.tvPass)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus : Button = itemView.findViewById(R.id.btnHapus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_admin,
            parent, false)
        return ListAdminAdapter.AdminViewHolder(itemView) 
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val currentItem = adminList[position]

        holder.fullName.text = currentItem.nama
        holder.idAdmin.text = currentItem.id
        holder.emailR.text = currentItem.email
        holder.pass.text = currentItem.pass

        // memanggil fungsi editData
        editData(holder.fullName.text.toString(),
            holder.emailR.text.toString(),
            holder.idAdmin.text.toString(),
            holder.pass.text.toString(),
            holder.btnEdit)

        // menghapus data
        holder.btnHapus.setOnClickListener { v->

            dbRef = FirebaseDatabase.getInstance().getReference("login")
            val dbAdmin = dbRef.child("admin")


            // menampilkan dialog peringatan

            val builder = AlertDialog.Builder(v.context)
                .setTitle("Hapus data ${holder.fullName.text.toString()}")
                .setMessage("Yakin Dihapus?")
                .setPositiveButton("Iya"){ dialog, id ->
                    adminList.removeAt(position)
                    removedItem = position
                    notifyItemRemoved(position)

                    dbAdmin.child(holder.idAdmin.text.toString()).removeValue().addOnSuccessListener{
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
        return adminList.size
    }

    @SuppressLint("MissingInflatedId")
    private fun editData(
        nama: String,
        email: String,
        idAdmin: String,
        pass: String,
        btnEdit: Button
    ) {

        dbRef = FirebaseDatabase.getInstance().getReference("login")

        btnEdit.setOnClickListener {

            // membuat dialog
            var dialog = LayoutInflater.from(it.context).inflate(R.layout.tambah_admin, null)

            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(it.context)
                .setView(dialog)

            val alertDialog = mBuilder.show()

            val edId = dialog.findViewById<EditText>(R.id.edId)
            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edEmail = dialog.findViewById<EditText>(R.id.edEmail)
            val edPass = dialog.findViewById<EditText>(R.id.edPass)
            val btnTambah = dialog.findViewById<Button>(R.id.btnAdd)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            edNama!!.setText(nama)
            edEmail!!.setText(email)
            edPass!!.setText(pass)
            edId!!.setText(idAdmin)

            // update data user
            btnTambah.setOnClickListener {
                val dataAdmin = mapOf<String, String>(
                    "nama" to edNama.text.toString(),
                    "email" to edEmail.text.toString(),
                    "pass" to edPass.text.toString()
                )

                val dbAdmin = dbRef.child("admin")

                dbAdmin.child(idAdmin).updateChildren(dataAdmin).addOnSuccessListener {
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