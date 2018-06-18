package com.example.usrdel.http_server_sails_av

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_camara.setOnClickListener{view ->
            irActividadCamara()
        }

        "http://172.29.21.249:1337/Entrenador/2".httpGet().responseString { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.i("http-ejemplo", "${ex.response}")
                }
                is Result.Success -> {
                    val jsonStringEntrenador = result.get()
                    Log.i("http-ejemplo", "${jsonStringEntrenador}")

                    val entrenador:Entrenador? = Klaxon().parse<Entrenador>(jsonStringEntrenador)

                    if(entrenador!=null){

                    Log.i("http-ejemplo", "Nombre: ${entrenador!!.nombre}")
                    Log.i("http-ejemplo", "Apellido: ${entrenador!!.apellido}")
                    Log.i("http-ejemplo", "ID ${entrenador!!.id}")
                    Log.i("http-ejemplo", "Medallas: ${entrenador!!.medallas}")
                    Log.i("http-ejemplo", "Edad: ${entrenador!!.edad}")
                    Log.i("http-ejemplo", "Creado: ${entrenador!!.createdAtDate}")
                    Log.i("http-ejemplo", "Actualizado: ${entrenador!!.updateAtDate}")
                        entrenador.pokemons.forEach{ pokemon:Pokemon ->
                            Log.i("Http-ejemplo", "Nombre: ${pokemon!!.nombre}")
                            Log.i("Http-ejemplo", "Tipo: ${pokemon!!.tipo}")
                            Log.i("Http-ejemplo", "Numero: ${pokemon!!.numero}")
                        }

                    }else{
                        Log.i("http-ejemplo", "entrenador nulo")
                    }
                }
            }
        }
    }

    fun irActividadCamara(){
        var intent =Intent(this, camaraActivity::class.java)
        startActivity(intent)
    }

    class Entrenador (
            var nombre:String,
            var apellido:String,
            var edad:Int,
            var medallas:Int,
            var createdAt:Long,
            var updateAt:Long,
            var id:Int,
            var pokemons:List<Pokemon> = ArrayList()){
        var createdAtDate = Date(updateAt)
        var updateAtDate = Date(createdAt)
    }


    class Pokemon (
            var nombre:String,
            var numero:Int,
            var tipo:String,
            var createdAt:Long,
            var updateAt:Long,
            var id:Int,
            var entrenadorId: Int){
        var createdAtDate = Date(updateAt)
        var updateAtDate = Date(createdAt)
    }
}