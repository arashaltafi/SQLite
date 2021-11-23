package com.arash.altafi.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.arash.altafi.sqlite.db.helper.UserDBHelper
import com.arash.altafi.sqlite.models.User
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    //region Variables
    private lateinit var userName : EditText
    private lateinit var firstName : EditText
    private lateinit var lastName : EditText
    private lateinit var btnAdd : MaterialButton
    private lateinit var btnDelete : MaterialButton
    private lateinit var listView : ListView
    private lateinit var userDBHelper : UserDBHelper
    private lateinit var adapter : ArrayAdapter<String>
    private var listItems = ArrayList<String>()
    private var allUsers = ArrayList<User>()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    private fun init() {
        bindViews()
        userDBHelper = UserDBHelper(this)
        refreshList()
        adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1 , listItems)
        listView.adapter = adapter

        btnAdd.setOnClickListener {
            if (userName.text.isEmpty()) {
                Toast.makeText(this , "Please Enter User Name" , Toast.LENGTH_SHORT).show()
            }
            if (firstName.text.isEmpty()) {
                Toast.makeText(this , "Please Enter First Name" , Toast.LENGTH_SHORT).show()
            }
            if (lastName.text.isEmpty()) {
                Toast.makeText(this , "Please Enter Last Name" , Toast.LENGTH_SHORT).show()
            }

            val user : User = User(userName.text.toString() , firstName.text.toString() , lastName.text.toString())
            val result = userDBHelper.insertUser(user)
            if (result) {
                Toast.makeText(this , "New User Added Successfully!" , Toast.LENGTH_SHORT).show()
                userName.text.clear()
                firstName.text.clear()
                lastName.text.clear()
                refreshList()
                adapter.notifyDataSetChanged()
            }
            else {
                Toast.makeText(this , "Error!!!" , Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val item = allUsers.get(position)
            userName.setText(item.userName)
            firstName.setText(item.firstName)
            lastName.setText(item.lastName)
        }

        btnDelete.setOnClickListener {
            if (userName.text.isEmpty()) {
                Toast.makeText(this , "Please Enter User Name" , Toast.LENGTH_SHORT).show()
            }
            else {
                val result = userDBHelper.deleteUserByUsername(userName.text.toString())
                if (result) {
                    Toast.makeText(this , "User Deleted Successfully!" , Toast.LENGTH_SHORT).show()
                    userName.text.clear()
                    firstName.text.clear()
                    lastName.text.clear()
                    refreshList()
                    adapter.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(this , "User Not Found!!!" , Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun refreshList()
    {
        allUsers.clear()
        allUsers.addAll(userDBHelper.getAllUsers())
        listItems.clear()
        listItems.addAll(
            allUsers.map { "${it.firstName} ${it.lastName} (${it.userName})" } as ArrayList<String>
        )
    }

    private fun bindViews() {
        userName = findViewById(R.id.edt_username)
        firstName = findViewById(R.id.edt_firstname)
        lastName = findViewById(R.id.edt_lastname)
        btnAdd = findViewById(R.id.btn_add)
        btnDelete = findViewById(R.id.btn_delete)
        listView = findViewById(R.id.list_view)
    }

}