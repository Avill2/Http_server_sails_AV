package com.example.usrdel.http_server_sails_av

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_google_maps.*
import java.util.*
import kotlin.collections.ArrayList

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback ,
GoogleMap.OnCameraMoveStartedListener,
GoogleMap.OnCameraMoveListener,
GoogleMap.OnCameraMoveCanceledListener,
GoogleMap.OnCameraIdleListener,
GoogleMap.OnPolylineClickListener,
GoogleMap.OnPolygonClickListener
{

    var usuariosTienenPermisosLocalizacion = false

    private val COLOR_BLACK_ARGB = -0x1000000
    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_PURPLE_ARGB = -0x7e387c
    private val COLOR_ORANGE_ARGB = -0xa80e9
    private val COLOR_BLUE_ARGB = -0x657db

    private val POLYGON_STROKE_WIDTH_PX: Float = 8.toFloat()
    private val PATTERN_DASH_LENGTH_PX = 20
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT = Dot()
    private val DASH = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH)

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private val PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP)

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveCanceled() {

    }

    override fun onCameraIdle() {

    }

    override fun onPolylineClick(p0: Polyline?) {

    }

    override fun onPolygonClick(p0: Polygon?) {

    }

    private lateinit var mMap: GoogleMap

    var arregloMarcadores = ArrayList<Marker>()

    val epnLatLang = LatLng(-0.210268, -78.488316)
    val zoom = 17f

    val casaCulturaLatLng = LatLng(-0.206297, -78.486695)

    val casaCultura: CameraPosition = CameraPosition().Builder().target(casaCulturaLatLng).zoom(zoom).build()

    val supermaziLatLng =LatLng(-0.206297, -78.486695)
    val supermaxi: CameraPosition = CameraPosition().Builder().target(supermaziLatLng.zoom(zoom).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)
        solicitarPermisoLocalizacion()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    fun solicitarPermisoLocalizacion(){

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            usuariosTienenPermisosLocalizacion = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        with(googleMap) {
            var polyline1 = googleMap.addPolyline(PolylineOptions()
                    .clickable(true)
                    .add(
                            LatLng(-0.209069, -78.491789),
                            LatLng(-0.208083, -78.490083),
                            LatLng(-0.209961, -78.486972),
                            LatLng(-0.212568, -78.489064)
                    )
            )

            var polygon1 = googleMap.addPolygon(PolygonOptions()
                    .clickable(true)
                    .add(
                            LatLng(-0.209376, -78.490234),
                            LatLng(-0.208556, -78.488936),
                            LatLng(-0.207880, -78.489928),
                            LatLng(-0.208556, -78.488936)

                    )

            )

            polygon1.tag = "alpha"

            formatearEstiloPoligono(polygon1)

        }
    }
    private fun formatearEstiloPoligono(polygon: Polygon) {
        var type = ""
        // Get the data object stored with the polygon.
        if (polygon.tag != null) {
            type = polygon.tag.toString()
        }

        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB

        when (type) {
        // If no type is given, allow the API to use the default.
            "alpha" -> {
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA
                strokeColor = COLOR_GREEN_ARGB
                fillColor = COLOR_PURPLE_ARGB
            }
            "beta" -> {
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA
                strokeColor = COLOR_ORANGE_ARGB
                fillColor = COLOR_BLUE_ARGB
            }
        }

        polygon.strokePattern = pattern
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX)
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }

    private fun anadirMarcador(latitudLongitud: LatLng, titulo: String) {
        arregloMarcadores.forEach { marker: Marker ->
            marker.remove()
        }
        arregloMarcadores = ArrayList<Marker>()
        val marker = mMap.addMarker(MarkerOptions().position(latitudLongitud).title(titulo))
        arregloMarcadores.add(marker)
        Log.i("map-adrian", "$arregloMarcadores")
    }

    private fun moverCamaraPorPosicion(posicionCamara: CameraPosition) {
        changeCamera(CameraUpdateFactory.newCameraPosition(posicionCamara))
    }

    private fun moverCamaraPorLatLongZoom(latitudLongitud: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud, zoom))
    }

    private fun establecerListeners(googleMap: GoogleMap) {
        with(googleMap) {
            setOnCameraIdleListener(this@GoogleMapsActivity)
            setOnCameraMoveStartedListener(this@GoogleMapsActivity)
            setOnCameraMoveListener(this@GoogleMapsActivity)
            setOnCameraMoveCanceledListener(this@GoogleMapsActivity)
            setOnPolylineClickListener(this@GoogleMapsActivity)
            setOnPolygonClickListener(this@GoogleMapsActivity)
        }
    }
}
