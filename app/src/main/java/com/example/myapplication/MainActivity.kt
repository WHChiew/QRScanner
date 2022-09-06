package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val btnScan = findViewById<Button>(R.id.btnScan)
        val imgQR = findViewById<ImageView>(R.id.imgQR)

        btnGenerate.setOnClickListener(){
            val data = "{'accNo':'aaaa', 'name':'Jane'}"
            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                imgQR.setImageBitmap(bmp)
            }catch (e: WriterException){
                e.printStackTrace()
            }

        }

        btnScan.setOnClickListener(){
            val intent = Intent(this, CaptureActivity::class.java)
            resultLauncher.launch(intent)
        }

    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultIntent: Intent? = result.data
            val contents = resultIntent?.getStringExtra("SCAN_RESULT")
            val obj = JSONObject(contents)
            findViewById<TextView>(R.id.tvResult).text = obj.getString("name")
        }
    }
}