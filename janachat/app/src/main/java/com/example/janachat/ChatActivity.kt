package com.example.janachat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class ChatActivity : AppCompatActivity() {
    private var MessageList by mutableStateOf(emptyList<Message>())
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = com.google.firebase.ktx.Firebase.auth
        setContent {
            val name:String?=intent.getStringExtra("name")
            val senderemail: String? = auth.currentUser?.uid
            val receiveremail:String?=intent.getStringExtra("id")
            val senderroom:String=senderemail+receiveremail
            val receiverroom:String=receiveremail+senderemail
            @Composable
            fun Show(s:Message){
                if (auth.currentUser?.uid.equals(s.senderid) ){
                        s.message?.let {

                            Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(modifier = Modifier
                                    .padding(top = 10.dp, bottom = 10.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    ), onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(Color(0xFFFF1281))) {
                                    Text(style = TextStyle(fontSize = 20.sp), text= it, color = Color.White, textAlign = TextAlign.End)

                                }
                                 }
                            }

                    }
                    else{
                        s.message?.let { Row {
                            Button(modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                                .clip(
                                    RoundedCornerShape(10.dp)
                                ), onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                                Text(style = TextStyle(fontSize = 20.sp), text= it, color = Color.White, textAlign = TextAlign.End)

                            }
                        }}
                    }

            }
            fun last(){

                /*
                var lo=""
                if (MessageList.isNotEmpty()){
                    lo= MessageList[MessageList.size-1].message?:""}
                val intent=Intent(this,home::class.java)
                intent.putExtra("last",lo)*/


            }

            @Composable
            fun Bottombar() {
                var message by remember { mutableStateOf( "") }
                fun call(){
                    val intent=Intent(this,CallActivity::class.java)
                    startActivity(intent)
                }
                fun send(){

                    database = Firebase.database.reference
                    val mess=Message(message,senderemail)
                    database.child("chats").child(senderroom!!).child("messages")
                        .push().setValue(mess).addOnSuccessListener {
                            database.child("chats").child(receiverroom!!).child("messages").push().setValue(mess)
                        }
                }
                database = FirebaseDatabase.getInstance().reference
                database.child("chats").child(receiverroom!!).child("messages").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val msgs = mutableListOf<Message>()
                        for (postSnapshot in snapshot.children) { val msg = postSnapshot.getValue(Message::class.java)

                                msg?.let { msgs.add(it) }
                        }
                        MessageList = msgs

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })


                Column(modifier = Modifier

                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black)) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(Color.DarkGray),
                        title = {Row() {


                            IconButton(
                                onClick = {
                                    finish()
                                },
                                content = {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Sign Out")
                                },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                            )
                            IconButton(

                                onClick = {
                                },
                                content = {
                                    Icon(
                                        Icons.Filled.AccountCircle,
                                        contentDescription = "group chat"
                                    )

                                },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                            )
                            var lo by remember { mutableStateOf("")}
                            if (MessageList.isNotEmpty()){
                                lo= MessageList[MessageList.size-1].message?:""}

                            val shared=getSharedPreferences("lo", MODE_PRIVATE)
                            shared.edit().putString("$name","$lo").apply()


                            Text(text = "$name", color = Color.White, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp), modifier = Modifier.padding(top=10.dp))

                        }

                        },
                        actions = {
                            IconButton(
                                onClick = {
                                   call()
                                },
                                content = {
                                    Icon(Icons.Filled.Call, contentDescription = "call")
                                },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                            )
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
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        ) {
                        items(MessageList){msg-> Show(msg)}
                    }

                    Row(modifier = Modifier.fillMaxWidth() ) {

                            TextField(placeholder ={ Text("Type a message")}, modifier = Modifier
                                .weight(1f)
                                .padding(20.dp)
                                .clip(RoundedCornerShape(20.dp)),
                                value = message, onValueChange = { value -> message = value })



                        IconButton(
                            modifier = Modifier.padding(top = 20.dp),
                            onClick = {send()
                                
                                message=""
                                },
                            content = {
                                Icon(Icons.Filled.Send, contentDescription = "Send")
                            },
                            colors = IconButtonDefaults.iconButtonColors(Color.White)
                        )
                    }
                }
            }

                    Bottombar()

              }

    }
}

