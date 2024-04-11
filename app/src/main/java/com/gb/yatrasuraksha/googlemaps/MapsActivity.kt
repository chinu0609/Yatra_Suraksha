package com.gb.yatrasuraksha.googlemaps

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.gb.yatrasuraksha.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gb.yatrasuraksha.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CircleOptions
import java.io.BufferedReader
import java.io.InputStreamReader

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val target = LatLng(12.9788, 77.5997)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 10f))

        // Draw circle overlay
        val center = LatLng(12.9788, 77.5997)
        val radius = 5000.0 // in meters
        val circleOptions = CircleOptions()
            .center(center)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(Color.RED)
            .fillColor(Color.argb(70, 255, 0, 0)) // transparent red
        googleMap.addCircle(circleOptions)

        binding.cardShowAccident.setOnClickListener { showPoints(googleMap) }

    }

    private fun showPoints(googleMap: GoogleMap) {
        mMap = googleMap

        val filePath = "final.csv"

        val progressBar = ProgressDialog(this).apply {
            setMessage("Please wait...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
        progressBar.show()

        Thread {
            val allPoints = readCsvAndConvertToList(this, filePath)
            val pointsWithinRadius = arialProbab(12.9788, 77.5997, 0.01, allPoints)

            runOnUiThread {
                for (dataPoint in pointsWithinRadius) {
                    mMap.addMarker(MarkerOptions().position(dataPoint))
                }

                progressBar.dismiss()
            }
        }.start()
    }


    fun readCsvAndConvertToList(context: Context, fileName: String): List<LatLng> {
        val latLngList = mutableListOf<LatLng>()
        try {
            context.assets.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    // Skip the header if exists
                    reader.readLine()
                    while (reader.readLine().also { line = it } != null) {
                        val parts = line!!.split(",").map { it.trim().toDouble() }
                        val lat = parts[1]
                        val lng = parts[2]
                        val latLng = LatLng(lat, lng)
                        latLngList.add(latLng)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return latLngList
    }

    fun arialProbab(lat: Double, lon: Double, radius: Double, data: List<LatLng>): List<LatLng> {
        val points = mutableListOf<LatLng>()

        for (point in data) {
            if ((point.latitude <= lat + radius && point.latitude >= lat - radius) &&
                (point.longitude <= lon + radius && point.longitude >= lon - radius)) {
                points.add(LatLng(point.latitude, point.longitude))
            }
        }

        return points
    }

}