package com.example.janachat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*
import java.util.stream.IntStream.range


class home : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private var MessageList by mutableStateOf(emptyList<Message>())
    private lateinit var database: DatabaseReference
    private var userList by mutableStateOf(emptyList<User>())

    override fun onCreate(savedInstanceState: Bundle?) {
        auth= Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            MyAppBar()
        }

        database = FirebaseDatabase.getInstance().reference


        database.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (postSnapshot in snapshot.children) {
                    val user = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.email!=user?.email){
                    user?.let { users.add(it) }}
                    else{
                        val cu= user?.username
                        val shared=getSharedPreferences("o", MODE_PRIVATE)
                        shared.edit().putString("cu","$cu").apply()

                    }
                }
                userList = users

            } // wait... better idea... instead of intentin to this activity let us intent to no need

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyAppBar() {

        fun open(user:User){
            val intent=Intent(this,ChatActivity::class.java)
            intent.putExtra("name",user.username)
            intent.putExtra("id",user.uid)
            startActivity(intent)
        }
        fun groupact(){
            val intent= Intent(this,GroupActivity::class.java)
            startActivity(intent)
        }
        Column(){

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(Color.Black),
            title = {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.janachat),
                        contentDescription = "header",
                        modifier = Modifier
                            .height(50.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )
                    Text(
                        text = "Janachat",
                        fontSize = 25.sp,
                        color = Color.White,
                        style = TextStyle(fontWeight = FontWeight.ExtraBold),
                        modifier=Modifier.padding(top=10.dp)
                    )
                }
            },
                actions = {
               /* IconButton(
                    onClick = {
                        groupact()

                    },
                    content = {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "group chat")
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                )*/
                IconButton(
                    onClick = {
                        auth.signOut()
                        finish()
                    },
                    content = {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Sign Out")
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                )
            }
        )

        Column(modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .fillMaxHeight()
            .weight(1f)) {
            Text(text = "All Users", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top=10.dp,bottom = 10.dp))

            LazyColumn {
                items(userList) { users ->
                    Column(modifier=Modifier.clickable {
                       open(users)

                    }) {
                        Row(modifier = Modifier.padding(top=10.dp)) {
                            Image(modifier = Modifier
                                .height(50.dp)
                                .padding(end = 10.dp)
                                .clip(RoundedCornerShape(30.dp)), painter = painterResource(id = R.drawable.img), contentDescription = "profile")
                            Column {
                                Text(
                                    text = "${users.username} ",
                                    color = Color.White,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily.SansSerif
                                    )

                                )


                                val shared=getSharedPreferences("lo", MODE_PRIVATE)
                                var lo=shared.getString("${users.username}","")



                                Text(text ="$lo",color = Color.White)
                            }
                        }

                    }
                }
            }

        }
            Column(){
            Text(text = "Reserved only to friends of jana", color = Color.Black, modifier = Modifier.padding(start=100.dp))}
    }}
}

