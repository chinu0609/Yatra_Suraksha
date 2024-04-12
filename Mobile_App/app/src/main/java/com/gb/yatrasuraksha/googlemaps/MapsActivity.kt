package com.gb.yatrasuraksha.googlemaps

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var pointsWithinRadius: List<LatLng>

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
        val lat : Double = 15.6
        val long : Double = 75.8
        val target = LatLng(lat,long)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 10f))

        val filePath = "final.csv"

        val allPoints = readCsvAndConvertToList(this, filePath)
        pointsWithinRadius = arialProbab(lat, long, 0.01, allPoints)

        val color = getTheColor(pointsWithinRadius.size)
        val t = hexToRgb(color)

        val center = LatLng(lat, long)
        val radius = 5000.0 // in meters
        val circleOptions = CircleOptions()
            .center(center)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(Color.parseColor(color))
            .fillColor(Color.argb(70, t!!.first, t.second, t.third))// transparent
        googleMap.addCircle(circleOptions)

        binding.cardShowAccident.setOnClickListener { showPoints(googleMap, pointsWithinRadius) }
    }

    private fun showPoints(googleMap: GoogleMap, pointsWithinRadius: List<LatLng>) {
        mMap = googleMap

        val progressBar = ProgressDialog(this).apply {
            setMessage("Please wait...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
        progressBar.show()

        Thread {

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

    fun getTheColor(value: Int): String {
        if (value <= 3) {
            val intensity = Integer.toHexString((255 * value / 3)).padStart(2, '0')
            return "#$intensity" + "0000"
        } else {
            return "#ff0000"
        }
    }


    fun hexToRgb(hex: String): Triple<Int, Int, Int>? {
        var colorHex = hex.trim()
        // Remove the '#' prefix if present
        if (colorHex.startsWith("#")) {
            colorHex = colorHex.substring(1)
        }

        // Ensure the hex code has a valid length
        if (colorHex.length != 6) {
            return null
        }

        try {
            // Parse the hex string to RGB values
            val red = Integer.parseInt(colorHex.substring(0, 2), 16)
            val green = Integer.parseInt(colorHex.substring(2, 4), 16)
            val blue = Integer.parseInt(colorHex.substring(4, 6), 16)
            return Triple(red, green, blue)
        } catch (e: NumberFormatException) {
            // Handle invalid hex color format
            return null
        }
    }



}