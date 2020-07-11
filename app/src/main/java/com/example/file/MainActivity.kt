package com.example.file


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.getDataDirectory
import android.os.Environment.getExternalStorageState
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    val REQUEST_PERMISSION_CODE = 1
    lateinit var fileText: EditText
    lateinit var btnSave: Button
    lateinit var btnRead: Button
    lateinit var MyExternalFile: File
    lateinit var strData: String

    val file_name = "fileText.txt"
    val file_path = "MyFileStorage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fileText = findViewById(R.id.fileEditText)
        btnSave = findViewById(R.id.saveButton)
        btnRead = findViewById(R.id.readButton)
        if (!checkPermission())
            requestPermission()
        if (checkPermission()) {
            btnSave.setOnClickListener {
                saveFile()
            }
        } else {
            requestPermission()
        }
        btnRead.setOnClickListener {
            readFile()
        }
    }

    private fun readFile() {
        try {
            val directory = File(getExternalFilesDir().toString() + "/testdata")
            directory.mkdir()
            val file = File(getExternalStorageState(), file_name)
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String? = null
            while ({ line = bufferReader.readLine();line }() != null) {
                stringBuilder.append(line)
            }
            fileInputStream.close()
            inputStreamReader.close()
            fileText.setText(stringBuilder.toString())
            Toast.makeText(applicationContext, "Data Retreived", Toast.LENGTH_SHORT).show()
        } catch (exp: java.io.IOException) {
            exp.printStackTrace()
        }
    }

    private fun saveFile() {
        val directory = File(getExternalFilesDir().toString() + "/testdata")
        directory.mkdir()
        val file = File(getDataDirectory(), file_name)
        val fileOutputStream = FileOutputStream(file)
        strData = fileText.text.toString()
        fileOutputStream.write(strData.toByteArray())
        // fileOutputStream.write(strData.toByteArray())
        fileOutputStream.close()
        Toast.makeText(applicationContext, "Data Saved", Toast.LENGTH_SHORT).show()
        fileText.setText("")
    }

    private fun checkPermission(): Boolean {
        val writeExternalStorage = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return writeExternalStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> if (grantResults!!.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
            }
        }

    }
}