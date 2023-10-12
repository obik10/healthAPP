package com.robiul.healthapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.robiul.healthapp.adapter.UserAdapter
import com.robiul.healthapp.databinding.ActivityListUserBinding
import com.robiul.healthapp.model.User


class ListUser : AppCompatActivity() {
    lateinit var dbRef : DatabaseReference
    lateinit var userRecyclerView : RecyclerView
    lateinit var userArrayList : ArrayList<User>
    lateinit var binding: ActivityListUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_user_list)

        binding.btnBack.setOnClickListener {
            finish()
        }

        userRecyclerView = binding.userList
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()
        getUserData()


    }

    private fun getUserData() {

        dbRef = FirebaseDatabase.getInstance().getReference("login")

        dbRef.child("user").addValueEventListener(object  : ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    userArrayList.clear()
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)

                    }

                    userRecyclerView.adapter = UserAdapter(userArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}