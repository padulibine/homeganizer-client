package com.example.homeganizer_client

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.homeganizer_client.models.Component
import com.google.gson.Gson
const val URL_SIGNUP = "https://homeganizer.herokuapp.com/users/signup"

class Inscription: AppCompatActivity() {
    var btn_subscribe: Button? = null
    var confirm_password: EditText? = null
    var password: EditText? = null
    var login: EditText? = null

    var signup: String? = null
    var test: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)
        btn_subscribe = findViewById(R.id.signup)
        password = findViewById(R.id.password)
        confirm_password = findViewById(R.id.confirmPassword)
        login = findViewById(R.id.user)
        signup = intent.getStringExtra("signup")
        if (signup == null) {
            signup = ""
        }
        btn_subscribe?.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            var gson = Gson()
            var data: Array<Component>
            val url = "$URL_SIGNUP/$signup"
            val stringRequest = object: StringRequest(
                Request.Method.POST, url,
                Response.Listener<String>
                { JsonResponse ->
                    Log.d("Main", "reponse: " + JsonResponse)
                    val intent = Intent(this, CollectionActivity::class.java)
                    intent.putExtra("token",JsonResponse);
                    startActivityForResult(intent, 1)
                },
                Response.ErrorListener
                { error ->
                    Log.e("Error", "Error Appel : " + error)
                })
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["user"] = login?.text.toString()
                    params["password"] = password?.text.toString()
                    return params
                }
            }
            if(password?.text.toString()==confirm_password?.text.toString()){
            queue!!.add(stringRequest!!)
            Toast.makeText(applicationContext,"Inscription Faite", Toast.LENGTH_LONG).show()
               }
            else{
                Toast.makeText(applicationContext,"Les mots de passe ne sont pas égaux", Toast.LENGTH_LONG).show()
            }
        }
    }
}



