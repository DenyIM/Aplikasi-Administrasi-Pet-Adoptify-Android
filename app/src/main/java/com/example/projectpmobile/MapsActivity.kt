package com.example.projectpmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projectpmobile.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val Toko1 = LatLng(-7.856929453987352, 110.39110572780865)
        val Toko2 = LatLng(-7.81165069357774, 110.32654401589956)
        val Toko3 = LatLng(-7.815071121088537, 110.36969257890873)
        val Toko4 = LatLng(-7.783239552593141, 110.36570036282791)
        val Toko5 = LatLng(-7.763374025377566, 110.33663931633623)

        mMap.addMarker(
            MarkerOptions().position(Toko1)
                .title("Toko Adopsi Hewan 1")
        )

        mMap.addMarker(
            MarkerOptions().position(Toko2)
                .title("Toko Adopsi Hewan 2")
        )

        mMap.addMarker(
            MarkerOptions().position(Toko3)
                .title("Toko Adopsi Hewan 3")
        )

        mMap.addMarker(
            MarkerOptions().position(Toko4)
                .title("Toko Adopsi Hewan 4")
        )

        mMap.addMarker(
            MarkerOptions().position(Toko5)
                .title("Toko Adopsi Hewan 5")
        )

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Toko1))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f))
    }
}