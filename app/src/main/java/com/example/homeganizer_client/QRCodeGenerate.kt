package com.example.homeganizer_client

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.homeganizer_client.models.Component

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeGenerate: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcodegenerator)

        //Variables
        var imgQRCode: ImageView? = null
        var idQRCode: TextView? = null
        var bmp: Bitmap? = null

        //Récupération du nom du Container actuel
        val currentName: String = intent.getStringExtra("CurrentName").toString()


        //Initialisation des Variables
        imgQRCode = findViewById(R.id.imgQRCode)
        idQRCode = findViewById(R.id.idQRCode)

        if(currentName.isEmpty()){
            idQRCode.setText("Null")
        }
        else{

            val wrQR = QRCodeWriter()
            try{
                val qrMatrix = wrQR.encode(currentName, BarcodeFormat.QR_CODE, 512, 512)
                bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
                for(i in 0 until 512){
                    for(j in 0 until 512){
                        bmp.setPixel(i, j, if(qrMatrix[i, j]) Color.BLACK else Color.WHITE)
                    }
                }
                idQRCode.setText(currentName)
                imgQRCode.setImageBitmap(bmp)

            }
            catch(e: WriterException){
                e.printStackTrace()
            }

        }
    }

}