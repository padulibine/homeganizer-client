package com.example.homeganizer_client

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ElementActivity: AppCompatActivity() {
    var nameView : TextView?=null
    var pathView : TextView?=null
    var descriptionView : TextView?=null
    var checkBoxView : CheckBox?=null
    var button_suppress : Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.element_activity)

        nameView = findViewById(R.id.nom)
        pathView = findViewById(R.id.path)
        descriptionView = findViewById(R.id.description)
        checkBoxView = findViewById(R.id.add_object_checkBox)
        button_suppress = findViewById(R.id.supprimer)

        // RECUPERATION DES DONNEES POUR L'AFFICHAGE DES DETAIL D'UN ELEMENT
        nameView!!.text = intent.getStringExtra("current_component")
        pathView!!.text = intent.getStringExtra("path")
        descriptionView!!.text = intent.getStringExtra("description")

        // APPEL API AFIN DE SUPPRIMER UN ELEMENT
        button_suppress?.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url = "$COLLEC_URL/$collection/components/${intent.getStringExtra("current_component")}"
            val StringRequest = object: StringRequest(
                Request.Method.DELETE, url,
                Response.Listener<String>
                { response ->
                    Log.e("ElementActivity",response)
                    finish()
                },
                Response.ErrorListener
                { error ->
                    Log.e("Error", "Error Appel : "+error)
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = token.toString()
                    return headers
                }
            }

            queue.add(StringRequest)

        }


    }
}
