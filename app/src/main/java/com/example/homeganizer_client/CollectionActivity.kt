package com.example.homeganizer_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.homeganizer_client.adapters.ListCollectionAdapter
import com.google.gson.Gson

// URL ET TOKEN
const val COLLEC_URL = "https://homeganizer.herokuapp.com/collections"
var token: String? = null

class CollectionActivity : AppCompatActivity() {

    private lateinit var listCollectionView : RecyclerView
    var listCollection: Array<String> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        var buttonAddCollection : Button? = null
        var buttonImportCollection : Button? = null
        var txtAddColl : EditText? = null

        // RECUPERATION DU TOKEN DE L'UTILISATEUR
        if(intent.getStringExtra("token") != null) {
            token = intent.getStringExtra("token")
        }


        // APPEL API POUR AFFICHER LES COLLECTIONS DE L'UTILISATEUR
        val queue = Volley.newRequestQueue(this)
        var gson = Gson()
        var data: Array<String>
        val url = "$COLLEC_URL"

        val StringRequest = object: StringRequest(
            Request.Method.GET, url,
            Response.Listener<String>
            { response ->

                data = gson.fromJson(response, Array<String>::class.java)


                // PASSAGE DE LA LISTE DE COLLECTION A L'ADAPTER POUR L'AFFICHAGE
                listCollection = data
                listCollectionView = findViewById(R.id.list_collection)
                listCollectionView.layoutManager = LinearLayoutManager(this)
                var adapter = ListCollectionAdapter(listCollection)
                listCollectionView.adapter = adapter
                val intent = Intent(this, MainActivity::class.java)

                adapter.setOnItemClickListener(object : ListCollectionAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        intent.putExtra("collection", listCollection!![position])
                        startActivity(intent, null)
                    }
                })
            },

            Response.ErrorListener
            { error ->
                Log.e("Error", "Error Appel : "+error)
            })
        {
            // PASSAGE DU TOKEN POUR L'AUTORISATION D'ACCES AUX COLLECTIONS
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = token.toString()
                return headers
            }
        }

        queue.add(StringRequest)




        buttonAddCollection =  findViewById(R.id.buttonAddColl)
        buttonImportCollection = findViewById(R.id.buttonImpColl)
        txtAddColl = findViewById(R.id.txtAddColl)

        buttonAddCollection?.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url = "$COLLEC_URL/${txtAddColl.text}"

            val StringRequest = object: StringRequest(Request.Method.POST, url,
                Response.Listener<String>
                { response ->
                    Log.e("main", response)
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

        buttonImportCollection?.setOnClickListener{
            this.startActivity(Intent(this, QRCodeScannerColl::class.java))
        }

    }
}