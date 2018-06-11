package com.example.usrdel.http_server_sails_av

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class camaraActivity : AppCompatActivity() {

    var directorioActualImagen =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)
    }

    private fun tomarFoto() {
        val archivoImagen = crearArchivo("JPEG_", Environment.DIRECTORY_PICTURES, ".jpg")
        directorioActualImagen = archivoImagen.absolutePath
        enviarIntentFoto(archivoImagen)
    }

    private fun enviarIntentFoto(archivo: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //estamso obteiendo la uri de la foto
        val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.adrianeguez.mlkit.fileprovider",
                archivo)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        //estamso chequeadno que esta enviando la intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);
        }

    }
    companion object {
        val TOMAR_FOTO_REQUEST =1
    }

    private fun crearArchivo(prefijo: String, directorio: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefijo + timeStamp + "_"
        val storageDir = getExternalFilesDir(directorio)

        return File.createTempFile(
                imageFileName, /* prefix */
                extension, /* suffix */
                storageDir      /* directory */
        )
    }

}