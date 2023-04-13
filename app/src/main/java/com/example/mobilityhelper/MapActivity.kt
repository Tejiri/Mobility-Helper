package com.example.mobilityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var latitude: Double? = null
    var longitude: Double? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val extras = intent.extras
        latitude = extras?.getDouble("latitude")
        longitude = extras?.getDouble("longitude")
        title = extras?.getString("title")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val location = LatLng(latitude!!, longitude!!)
        mMap.addMarker(
            MarkerOptions().position(location).title(title)
        )
        var zoomLevel = 16.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }
}