package com.example.homeganizer_client

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import java.util.jar.Manifest

class QRCodeScanner: AppCompatActivity()  {

    private lateinit var qrScan: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcodescanner)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1 )
        }
        else{
            qrScanner()
        }

    }

    private fun qrScanner(){

        val camScan: CodeScannerView = findViewById<CodeScannerView>(R.id.camQRCode)

        //Initialisation du Layout du Scanner
        qrScan = CodeScanner(this, camScan)
        qrScan.camera = CodeScanner.CAMERA_BACK
        qrScan.scanMode = ScanMode.SINGLE
        qrScan.isAutoFocusEnabled = true
        qrScan.isFlashEnabled = false

        //Decodage lors du Scanner et renvoie vers la bonne page
        qrScan.decodeCallback = DecodeCallback {
            runOnUiThread{
                var container: String = it.text
                Toast.makeText(this, "Scan : $container", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("current_component", container)
                this.startActivity(intent)
                finish()
            }
        }

        //Erreur lors du Scan
        qrScan.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this, "Error : ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        camScan.setOnClickListener {
            qrScan.startPreview()
        }

    }

    override fun onResume(){
        super.onResume()
        if(::qrScan.isInitialized){
            qrScan?.startPreview()
        }
    }

    override fun onPause(){
        if(::qrScan.isInitialized){
            qrScan?.releaseResources()
        }
        super.onPause()
    }

    //Gestion des Permission de la caméra
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You can access the Camera :)", Toast.LENGTH_SHORT).show()
                qrScanner()
            }
            else{
                Toast.makeText(this, "You can't access the Camera :(", Toast.LENGTH_SHORT).show()
            }
        }

    }

}