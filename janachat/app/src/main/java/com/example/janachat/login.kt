package com.example.janachat


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth


class LoginActivity : ComponentActivity() {

     lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val user = Firebase.auth.currentUser
            if (user != null) {
                val intent=Intent(this,home::class.java)
                finish()
                startActivity(intent)
            } else {
                Login()
            }

        }
        auth = com.google.firebase.ktx.Firebase.auth
    }
@Preview
@Composable
fun Login() {
    val context= LocalContext.current
    auth = Firebase.auth
    fun navl(){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    var email by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }

    val karla = FontFamily(
        Font(R.font.karla),
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.janachat),
            contentDescription = "header",

            modifier = Modifier
                .padding(top = 100.dp, bottom = 25.dp)
                .shadow(elevation = 20.dp)
                .clip(RoundedCornerShape(200.dp))
                .height(250.dp),

            )
        Text(
            text = "Welcome!",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold,
                fontFamily = karla, color =Color.White),
            modifier=Modifier.padding(top=30.dp,bottom=30.dp,end=180.dp))

        TextField(value = email, label = {Text(text="Email")}, onValueChange ={it:String-> email=it},modifier= Modifier
            .padding(start = 10.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp)))

        TextField(value = password,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),label = {Text(text="Password")} ,onValueChange ={ it:String-> password=it},modifier= Modifier
            .padding(start = 10.dp, bottom = 50.dp)
            .clip(RoundedCornerShape(10.dp)))
        Button(onClick = { if(email.isEmpty()){
            Toast.makeText(
                context,
                "Login unsuccessful. Please enter all data.",
                Toast.LENGTH_LONG
            ).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(
                context,
                "Login unsuccessful. Please enter all data.",
                Toast.LENGTH_LONG
            ).show()
        }

                         Loginbutton(email, password)
        },shape= RoundedCornerShape(20.dp),modifier= Modifier
            .height(50.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth(),

            colors= ButtonDefaults.buttonColors(containerColor=Color(0xFFFF1281))) {
            Text(text="Login",style = TextStyle( color = Color.White, fontSize = 20.sp, fontWeight =FontWeight.ExtraBold,fontFamily = karla)
            )


        }
        Button(onClick = { navl() },shape= RoundedCornerShape(20.dp),modifier= Modifier
            .height(70.dp)
            .padding(start = 30.dp, end = 30.dp,top=20.dp)
            .fillMaxWidth(),

            colors= ButtonDefaults.buttonColors(containerColor=Color(0xFF222326))) {
            Text(text="Signup",style = TextStyle( color = Color.White, fontSize = 20.sp, fontWeight =FontWeight.ExtraBold,fontFamily = karla)
            )

        }

    }
}
    fun Loginbutton(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Login successful.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this, home::class.java)

                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Login unsuccessful.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }
}






