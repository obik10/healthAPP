package com.robiul.healthapp.adapter

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robiul.healthapp.Homepage
import com.robiul.healthapp.R
import com.robiul.healthapp.model.AntreanModel
import java.util.*
import kotlin.collections.ArrayList

class EditAntreanAdapter(private val antreList: ArrayList<AntreanModel>) :
    RecyclerView.Adapter<EditAntreanAdapter.EditAntreViewHolder>() {

    lateinit var dbRef: DatabaseReference

    var removedItem: Int? = null

    var cal = Calendar.getInstance()


    class EditAntreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.tvNamaAntre)
        val tgl: TextView = itemView.findViewById(R.id.tvTanggalAntre)
        val antre: TextView = itemView.findViewById(R.id.tvAntrean)
        val klinikAntre: TextView = itemView.findViewById(R.id.tvKlinikAntre)
        val btnKonfirm: Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditAntreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            (R.layout.item_edit_antrean), parent, false
        )
        return EditAntreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EditAntreViewHolder, position: Int) {
        val curentItem = antreList[position]

        holder.nama.text = curentItem.nama
        holder.tgl.text = curentItem.tanggal
        holder.antre.text = curentItem.antrean
        holder.klinikAntre.text = curentItem.klinik

//        editData(holder.btnEdit, holder.nama.text.toString(),
//            holder.tgl.text.toString(),
//        holder.antre.text.toString())

        val currentuser = curentItem.nama
        val currentAntrean = curentItem.antrean
        val noAntre = curentItem.antrean

        holder.btnKonfirm.setOnClickListener { v ->

            dbRef = FirebaseDatabase.getInstance().getReference("antrean")
            if (currentAntrean!!.toInt() > 2) {
                val hasil = currentAntrean.toInt() - 2
                dbRef.child(currentuser.toString()).child("berita")
                    .setValue("Anda antrian ke-$noAntre Sekarang Antrian ke-$hasil segera  datang ke puskesmas untuk konfirmasi pendaftaran.")
            } else {
                dbRef.child(currentuser.toString()).child("berita")
                    .setValue("Anda antrian ke-$currentAntrean. Sekarang Antrian ke-$currentAntrean silahkan datang untuk konfirmasi pendaftaran.")
            }

//            val resultIntent = Intent().apply {
//                putExtra("Pesan", "Silahkan datang")
//            }

//            val notifId = 152
//
//            sendNotification(v.context, notifId, currentuser!!)

//            (v.context as Activity).apply {
//                setResult(Activity.RESULT_OK, resultIntent)
//                finish()
//            }

        }


        // menghapus data
        holder.btnHapus.setOnClickListener { v ->

            dbRef = FirebaseDatabase.getInstance().getReference("antrean")

            // menampilkan dialog peringatan

            val builder =
                AlertDialog.Builder(v.context).setTitle("Hapus data ${holder.nama.text.toString()}")
                    .setMessage("Yakin Dihapus?").setPositiveButton("Iya") { dialog, id ->
                        antreList.removeAt(position)
                        removedItem = position
                        notifyItemRemoved(position)

                        dbRef.child(holder.nama.text.toString()).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    v.context, "Data berhasil dihapus", Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(v.context, "error", Toast.LENGTH_SHORT).show()
                            }
                    }.setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()

        }
    }

    private fun sendNotification(context: Context?, notifId: Int, namaUser: String) {
//        val resultIntent = Intent(context, Homepage::class.java).apply {
//            putExtra("Pesan", "Silahkan datang")
//        }
//            val pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelID = "pesanID"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = context!!.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, Homepage::class.java)
        intent.putExtra("Pesan", "Silahkan Datang!!")
        intent.putExtra("namaUser", namaUser)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val notification =
            NotificationCompat.Builder(context!!, channelID).setSmallIcon(R.drawable.ic_lonceng)
                .setContentTitle("PEMBERITAHUAN!!").setContentText("Silahkan Datang 15 menit lagi")
                .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true).build()

        val notifManage = NotificationManagerCompat.from(context)
        notifManage.notify(notifId, notification)

    }


    //
//    @SuppressLint("MissingInflatedId")
//    private fun editData(btnEdit: Button, nama: String, antre: String, tgl: String) {
//
//        dbRef = FirebaseDatabase.getInstance().getReference("antrean")
//
//        btnEdit.setOnClickListener {
//            // membuat dialog
//            var dialog = LayoutInflater.from(it.context).inflate(R.layout.activity_update_antrean, null)
//
//            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(it.context)
//                .setView(dialog)
//
//            val alertDialog = mBuilder.show()
//
//            val edNama = dialog.findViewById<EditText>(R.id.edNama)
//            val edTgl = dialog.findViewById<EditText>(R.id.edTgl)
//            val edAntre = dialog.findViewById<EditText>(R.id.edAntrean)
//            val btnKonfirm = dialog.findViewById<Button>(R.id.btnKonfirm)
//            val btnBatal = dialog.findViewById<Button>(R.id.btnCancel)
//
//
//            val dateSetListener = object : DatePickerDialog.OnDateSetListener{
//                override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//                    cal.set(Calendar.YEAR, year)
//                    cal.set(Calendar.MONTH, month)
//                    cal.set(Calendar.DAY_OF_MONTH, day)
//                    updateDateInView(edTgl)
//                }
//            }
//
//            edTgl.setOnClickListener(object  : View.OnClickListener{
//                override fun onClick(view: View) {
//                    DatePickerDialog(view.context,
//                        dateSetListener,
//                        cal.get(Calendar.YEAR),
//                        cal.get(Calendar.MONTH),
//                        cal.get(Calendar.DAY_OF_MONTH)).show()
//                }
//            })
//
//            edNama!!.setText(nama)
//            edTgl!!.setText(tgl)
//            edAntre!!.setText(antre)
//
//            btnKonfirm.setOnClickListener {
//                val dataAntre = mapOf<String, String>(
//                    "nama" to edNama.text.toString(),
//                    "antrean" to edAntre.text.toString(),
//                    "tanggal" to edTgl.text.toString()
//                )
//
//                dbRef = FirebaseDatabase.getInstance().getReference("antrean")
//                val dbantre = dbRef.child(nama)
//
//                dbantre.updateChildren(dataAntre).addOnSuccessListener {
//                    alertDialog.dismiss()
//                    Toast.makeText(dialog.context, "Berhasil Update", Toast.LENGTH_SHORT).show()
//                }.addOnFailureListener {
//                    Toast.makeText(dialog.context, "Gagal Update", Toast.LENGTH_SHORT).show()
//
//                }
//
//
//            }
//
//            btnBatal.setOnClickListener {
//                alertDialog.dismiss()
//            }
//
//        }
//
//    }
//
//    private fun updateDateInView(edTgl: TextView?) {
//        val formatTgl = "dd/MM/yyy"
//        val sdf = SimpleDateFormat(formatTgl, Locale.getDefault())
//        edTgl!!.setText(sdf.format(cal.time))
//    }
//
//
    override fun getItemCount(): Int {
        return antreList.size
    }
}