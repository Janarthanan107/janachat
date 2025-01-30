 package com.example.janachat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID
import kotlin.random.Random

class GroupActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userList by mutableStateOf(emptyList<User>())
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            database = FirebaseDatabase.getInstance().reference


            database.child("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = mutableListOf<User>()
                    for (postSnapshot in snapshot.children) {
                        val user = postSnapshot.getValue(User::class.java)
                        if (auth.currentUser?.email != user?.email) {
                            user?.let { users.add(it) }
                        }
                    }
                    userList = users
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
            userli() ////ik just wait after what it hapenned remove tht... wait

        }
    }
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
fun userli(){

     var group_created = false
     var uuid: String? = null
     Column(){

         TopAppBar(
             colors = TopAppBarDefaults.topAppBarColors(Color.Black),
             title = { Text(text = "Janachat", color = Color.White) },
             actions = {

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
             .fillMaxHeight()) {
    LazyColumn {
        items(userList) { user ->
            Column(modifier = Modifier.clickable {

               if (!group_created) {
                    uuid = UUID.randomUUID().toString()
                    var ref = FirebaseDatabase.getInstance().getReference("/groups/$uuid/users/${user.uid}")
                    ref.setValue(User(user.username, user.email, user.uid, user.password))
                        .addOnSuccessListener { group_created = true }
                }

                else {
                    var ref = FirebaseDatabase.getInstance().getReference("/groups/$uuid/users/${user.uid}")
                    ref.setValue(User(user.username, user.email, user.uid, user.password))
                        .addOnSuccessListener { group_created = true }
                }
                //
            })

            {
               Row {
                   Text(
                       text = "${user.username} ",
                       color = Color.White,
                       style = TextStyle(fontSize = 30.sp)
                   )
                   Checkbox(colors = CheckboxDefaults.colors(Color.White), checked =group_created , onCheckedChange = { group_created=!group_created
                   })
               }
                Divider(color = Color.Gray)


            }
        }

        }
             Divider(color = Color.Gray)
             Button(onClick = { },shape= RoundedCornerShape(20.dp),modifier= Modifier
                 .padding(start = 30.dp, end = 30.dp, top = 30.dp)//default itself wrap content... wait one min

                 .fillMaxWidth(),

                 colors= ButtonDefaults.buttonColors(containerColor=Color(0xFFFF1281))) {
                 Text(text="Finish Group Creation",style = TextStyle( color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                 )

    }
}
}}}