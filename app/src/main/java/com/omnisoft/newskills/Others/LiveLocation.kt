package com.omnisoft.newskills.Others

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import com.omnisoft.newskills.App

class LiveLocation constructor(val context: Context) : LiveData<Location>() {
    var locationListener : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var  locationCallback: LocationCallback
    var locationRequest: LocationRequest
    init {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                this@LiveLocation.postValue(result.lastLocation)
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                super.onLocationAvailability(availability)
                if(!availability.isLocationAvailable){
                    Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000)
    }
    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationListener.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    override fun onInactive() {
        super.onInactive()
        locationListener.removeLocationUpdates(locationCallback)
    }

    override fun postValue(value: Location?) {
        super.postValue(value)
    }

}