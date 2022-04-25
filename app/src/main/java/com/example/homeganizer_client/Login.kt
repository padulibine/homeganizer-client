package com.example.homeganizer_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.*

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.homeganizer_client.models.Component

import com.google.gson.Gson
const val URL_LOGIN = "https://homeganizer.herokuapp.com/users/signin"

class Login: AppCompatActivity() {
    var user_name : EditText?=null
    var password : EditText?=null
    var btn_submit: Button?=null
    var btn_subscribe: Button?=null
    var login: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        btn_submit = findViewById(R.id.login_button)
        user_name = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btn_subscribe = findViewById(R.id.subscribe)

        login = intent.getStringExtra("signup")
        if(login == null){
            login = ""
        }
        // set on-click listener
        btn_submit?.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            var gson = Gson()
            var data: Array<Component>
            val url = "$URL_LOGIN?user=${user_name?.text}&password=${password?.text}"

            Log.d("url", url)

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { JsonResponse ->
                    Log.d("Main", "reponse: "+ JsonResponse )
                    val intent = Intent(this, CollectionActivity::class.java)
                    intent.putExtra("token",JsonResponse);
                    startActivityForResult(intent, 1)
                },
                {error ->
                    Log.e("Error", "Error Appel : "+error)
                    Toast.makeText(applicationContext, "Mot de passe incorrect", Toast.LENGTH_LONG).show()
                }
            )
            queue!!.add(stringRequest!!)
        }
        btn_subscribe?.setOnClickListener {
            val intent = Intent(this, Inscription::class.java)
            startActivityForResult(intent, 1)
        }
    }
}
