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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.robiul.healthapp.R
import com.robiul.healthapp.ListUser
import com.robiul.healthapp.model.User


class UserAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    lateinit var dbRef: DatabaseReference
    lateinit var dbRef2: DatabaseReference
    val activity : ListUser = ListUser() as ListUser

    var removedItem: Int? = null

    class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val fullName : TextView = itemView.findViewById(R.id.tvNama)
        val idUser : TextView = itemView.findViewById(R.id.tvID)
        val emailR : TextView = itemView.findViewById(R.id.tvEmail)
        val jenisK : TextView = itemView.findViewById(R.id.tvJenisK)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus : Button = itemView.findViewById(R.id.btnHapus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_user,
       parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.fullName.text = currentItem.nama
        holder.idUser.text = currentItem.id
        holder.emailR.text = currentItem.email
        holder.jenisK.text = currentItem.jenisK

        // memanggil fungsi editData
        editData(holder.btnEdit, holder.fullName.text.toString(),
            holder.emailR.text.toString(),
            holder.jenisK.text.toString(),
            holder.idUser.text.toString())

        // menghapus data
        holder.btnHapus.setOnClickListener { v->

            dbRef = FirebaseDatabase.getInstance().getReference("login")
            val dbUser = dbRef.child("user")


            // menampilkan dialog peringatan

            val builder = AlertDialog.Builder(v.context)
                .setTitle("Hapus data ${holder.fullName.text.toString()}")
                .setMessage("Yakin Dihapus?")
                .setPositiveButton("Iya"){ dialog, id ->
                    userList.removeAt(position)
                    removedItem = position
                    notifyItemRemoved(position)

                    dbUser.child(holder.idUser.text.toString()).removeValue().addOnSuccessListener{
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

    // fungsi edit data
    private fun editData(
        btnEdit: Button,
        nama: String,
        email: String,
        jenisK: String,
        idUser: String
    ) {
        dbRef = FirebaseDatabase.getInstance().getReference("login")

        btnEdit.setOnClickListener {

            // membuat dialog
            var dialog = LayoutInflater.from(it.context).inflate(R.layout.activity_update_user, null)

            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(it.context)
                .setView(dialog)

            val alertDialog = mBuilder.show()

            val edNama = dialog.findViewById<EditText>(R.id.edNama)
            val edEmail = dialog.findViewById<EditText>(R.id.edEmail)
            val edJenisK = dialog.findViewById<EditText>(R.id.edJenisK)
            val btnEdit = dialog.findViewById<Button>(R.id.editData)
            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)

            edNama!!.setText(nama)
            edEmail!!.setText(email)
            edJenisK!!.setText(jenisK)

            // update data user
            btnEdit.setOnClickListener {
                val dataUser = mapOf<String, String>(
                    "nama" to edNama.text.toString(),
                    "email" to edEmail.text.toString(),
                    "jenisK" to edJenisK.text.toString()
                )

                val dbUser = dbRef.child("user")

                dbUser.child(idUser).updateChildren(dataUser).addOnSuccessListener {
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