package com.example.homeganizer_client

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.homeganizer_client.adapters.ListComponentAdapter
import com.example.homeganizer_client.models.Component
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

var collection: String? = null
class MainActivity : AppCompatActivity() {

    private lateinit var listComponentView : RecyclerView
    var buttonAdd :FloatingActionButton ?=null
    var listComponent: Array<Component> ?= null
    var current_component: String ?= null
    var editSearch: EditText?= null
    var btnSearch: Button?= null
    var textSearch: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1 )
        }
        editSearch=findViewById(R.id.editTextSearch)
        btnSearch=findViewById(R.id.buttonSearch)

        btnSearch?.setOnClickListener {
            textSearch=editSearch?.text.toString()
            // APPEL API AFIN DE RECUPERER LES COMPONENTS DANS LE CONTAINER COURANT
            val queue = Volley.newRequestQueue(this)
            var gson = Gson()
            var data: Array<Component>
            val url = "$COLLEC_URL/$collection/components/$textSearch"

            val StringRequest = object: StringRequest(Request.Method.GET, url,
                Response.Listener<String>
                { response ->

                    // RECUPERATION DE LA LISTE DE COMPONENTS
                    data = gson.fromJson(response, Array<Component>::class.java)
                    if(textSearch != ""){
                        for(cpn in data){
                            if (cpn.name == textSearch){
                                data = cpn.objects!!
                            }
                        }
                    }

                    // APPEL DE L'ADAPTER POUR AFFIHCER LA LISTE
                    listComponent = data
                    listComponentView = findViewById(R.id.list_component)
                    listComponentView.layoutManager = LinearLayoutManager(this)
                    var adapter = ListComponentAdapter(listComponent)
                    listComponentView.adapter = adapter

                    //
                    // DIFFERENCATION DU CLICK SUR UN COMPONENT DE TYPE CONTAINER OU ELEMENT :
                    //      si un container est cliqué, un appel recursif sur MainActivity avec
                    //      de nouveaux extras est lancé
                    //      sinon on accède aux details d'un element où on peut le supprimer
                    //
                    val intent1 = Intent(this, MainActivity::class.java)
                    val intent2 = Intent(this, ElementActivity::class.java)
                    adapter.setOnItemClickListener(object : ListComponentAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                           if(listComponent!![position].objects != null){
                                intent1.putExtra("current_component", listComponent!![position].name)
                                startActivity(intent1, null)                        }
                            else{
                                intent2.putExtra("textSearch", listComponent!![position].name)
                                intent2.putExtra("description", listComponent!![position].description)
                                intent2.putExtra("path", listComponent!![position].path)
                                intent2.putExtra("tag", listComponent!![position].tag)
                                startActivity(intent2, null)
                            }
                        }
                    })

                },

                Response.ErrorListener
                { error ->
                    Log.e("Error", "Error Appel : "+error)
                })
            {
                // PASSAGE DU TOKEN
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = token.toString()
                    return headers
                }
            }

            queue.add(StringRequest)
        }

        // RECUPERATION DE LA COLLECTION
        if(intent.getStringExtra("collection") != null) {
            collection = intent.getStringExtra("collection")
        }

        // RECUPERATION DU COMPONENT, SI CELUI-CI EST NULL ALORS ON EST A LA RACINE
        current_component = intent.getStringExtra("current_component")

        if(current_component == null){
            current_component = ""
        }

        // APPEL API AFIN DE RECUPERER LES COMPONENTS DANS LE CONTAINER COURANT
        val queue = Volley.newRequestQueue(this)
        var gson = Gson()
        var data: Array<Component>
        val url = "$COLLEC_URL/$collection/components/$current_component"

        val StringRequest = object: StringRequest(Request.Method.GET, url,
            Response.Listener<String>
            { response ->

                // RECUPERATION DE LA LISTE DE COMPONENTS
                data = gson.fromJson(response, Array<Component>::class.java)
                if(current_component != ""){
                    for(cpn in data){
                        if (cpn.name == current_component){
                            data = cpn.objects!!
                        }
                    }
                }

                // APPEL DE L'ADAPTER POUR AFFIHCER LA LISTE
                listComponent = data
                listComponentView = findViewById(R.id.list_component)
                listComponentView.layoutManager = LinearLayoutManager(this)
                var adapter = ListComponentAdapter(listComponent)
                listComponentView.adapter = adapter

                //
                // DIFFERENCATION DU CLICK SUR UN COMPONENT DE TYPE CONTAINER OU ELEMENT :
                //      si un container est cliqué, un appel recursif sur MainActivity avec
                //      de nouveaux extras est lancé
                //      sinon on accède aux details d'un element où on peut le supprimer
                //
                val intent1 = Intent(this, MainActivity::class.java)
                val intent2 = Intent(this, ElementActivity::class.java)
                adapter.setOnItemClickListener(object : ListComponentAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        if(listComponent!![position].objects != null){
                            intent1.putExtra("current_component", listComponent!![position].name)
                            startActivity(intent1, null)                        }
                        else{
                            intent2.putExtra("current_component", listComponent!![position].name)
                            intent2.putExtra("description", listComponent!![position].description)
                            intent2.putExtra("path", listComponent!![position].path)
                            intent2.putExtra("tag", listComponent!![position].tag)
                            startActivity(intent2, null)
                        }
                    }
                })
            },

            Response.ErrorListener
            { error ->
                Log.e("Error", "Error Appel : "+error)
            })
        {
            // PASSAGE DU TOKEN
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = token.toString()
                return headers
            }
        }

        queue.add(StringRequest)

        // APPEL DE L'ACTIVITE ADDOBJECT LORS DU CLICK SUR LE BOUTON "+"
        buttonAdd = findViewById(R.id.floatingActionButton1)
        buttonAdd?.setOnClickListener {
            val intent = Intent(this, AddObject::class.java)
            intent.putExtra("current_component", current_component)
            intent.putExtra("token", token)
            startActivityForResult(intent,1)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){

            // RECUPERATION DES DONNEES APRES L'AJOUT D'UN COMPONENT POUR APPEL API
            val parent = data?.getStringExtra("current_component").toString()
            val nom = data?.getStringExtra("nom").toString()
            val description = data?.getStringExtra("description").toString()
            val tag = data?.getStringExtra("tag").toString()
            val checkbox = data?.getStringExtra("checkbox").toString()

            // APPEL API POST POUR AJOUTER UN ELEMENT
            val url = "$COLLEC_URL/$collection/components/$nom"
            val queue = Volley.newRequestQueue(this)

            val StringRequest = object: StringRequest(Request.Method.POST, url,
                Response.Listener<String>
                { response ->
                    // MISE A JOUR DE LA RECYCLERVIEW
                    Toast.makeText(this, nom+" ajouté à "+parent, Toast.LENGTH_LONG)
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("current_component", parent)
                    startActivity(intent)

                },
                Response.ErrorListener
                { error ->
                    Log.e("Error", "Error Appel : "+error)
                })
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["desc"] = description
                    params["parent"] = parent
                    params["tag"] = tag
                    params["container"] = checkbox

                    return params
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = token.toString()
                    return headers
                }
            }

            queue.add(StringRequest!!)

        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean -> if (isGranted) {
            Log.i("Permission: ", "Granted")
        }
        else {
            Log.i("Permission: ", "Denied")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        current_component = intent.getStringExtra("current_component")
        if(current_component == null) current_component = "Your Collection"

        when(item.itemId){
            R.id.menuQRCodeG -> {
                val intent = Intent(this, QRCodeGenerate::class.java)
                intent.putExtra("CurrentName", current_component)
                this.startActivity(intent)
                return true
            }

            R.id.menuQRCodeGColl -> {

                val queue = Volley.newRequestQueue(this)
                val url = "$COLLEC_URL/$collection"

                val StringRequest = object: StringRequest(Request.Method.GET, url,
                    Response.Listener<String>
                    { response ->
                        val intent = Intent(this, QRCodeGenerate::class.java)
                        intent.putExtra("CurrentName", response)
                        this.startActivity(intent)
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



                return true
            }

            R.id.menuQRCodeS -> {
                this.startActivity(Intent(this, QRCodeScanner::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}








