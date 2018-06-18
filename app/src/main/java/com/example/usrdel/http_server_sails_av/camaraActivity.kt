package com.example.usrdel.http_server_sails_av

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_camara.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class camaraActivity : AppCompatActivity() {

    var directorioActualImagen =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)
        button_tomar_foto.setOnClickListener{view ->tomarFoto()

        }
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
                "com.example.andreavillacis.ejemplo.fileprovider",
                archivo)
//donde se guarda al foto cuando ya se guarde la foto
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        //estamso chequeadno que esta enviando la intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        /*when (requestCode){
            TOMAR_FOTO_REQUEST -> {
                val extras =data.extras
                val imageBitmap = extras!!.get("data") as Bitmap
               // ImageView.setImageBitmap(imageBitmap)
            }
        }*/
    }

    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector
        Log.i("info", "------- Entro a detectar")
        val result = detector.detectInImage(image)
                .addOnSuccessListener { barCodes ->
                    Log.i("info", "------- tamano del barcode ${barCodes.size}")
                    respuestasBarCode.add("Ejemplo")
                    for (barcode in barCodes) {
                        val bounds = barcode.getBoundingBox()
                        val corners = barcode.getCornerPoints()

                        val rawValue = barcode.getRawValue()

                        Log.i("info", "------- $bounds")
                        Log.i("info", "------- $corners")
                        Log.i("info", "------- $rawValue")

                        respuestasBarCode.add(rawValue.toString())
                    }

                    val adaptadorListView = ArrayAdapter<String>(this, R.layout.simple_list_item_1, respuestasBarCode)
                    list_view.adapter = adaptadorListView
                }
                .addOnFailureListener {
                    Log.i("info", "------- No reconocio nada")
                }
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

    class GenericFileProvider : FileProvider(){

    }

}
