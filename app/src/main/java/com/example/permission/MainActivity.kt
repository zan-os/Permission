package com.example.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.permission.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var getLongitude: Double = 0.0
    private var getLatitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
            val imageUrl = "https://img.icons8.com/plasticine/2x/flower.png"
            loadImage(imageUrl)
        }

        binding.requestPermissionButton.setOnClickListener {
            requestPermissionLocation()
        }

        binding.openGmapButton.setOnClickListener {
            val url = "https://www.google.com/maps/search/?api=1&query=$getLatitude%2C$getLongitude"
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(intent)
        }

        binding.snackbarButton.setOnClickListener {
            showSnackbar()
        }
    }


    private fun showSnackbar() {
        val snackbar =
            Snackbar.make(binding.parentLayout, "ini snackbar", Snackbar.LENGTH_INDEFINITE)

        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }

        snackbar.show()
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .into(binding.imageView)
    }

    private fun requestPermissionLocation() {
        val permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(
                binding.parentLayout,
                "Permission location diizinkan",
                Snackbar.LENGTH_SHORT
            ).show()
            getLongLat()
        } else {
            Snackbar.make(
                binding.parentLayout,
                "Permission location ditolak",
                Snackbar.LENGTH_SHORT
            ).show()
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLongLat() {
        val locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (location != null) {
            getLongitude = location.longitude
            getLatitude = location.latitude
            binding.openGmapButton.visibility = View.VISIBLE
        }

        Snackbar.make(
            binding.parentLayout,
            "Lat: ${location?.latitude} Long: ${location?.longitude}",
            Snackbar.LENGTH_LONG
        )
            .show()

    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 201)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            201 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        binding.parentLayout,
                        "Allow telah dipilih",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    getLongLat()
                } else {
                    Snackbar.make(binding.parentLayout, "Deny telah dipilih", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {
                Snackbar.make(
                    binding.parentLayout,
                    "Bukan request yang dikirim",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}