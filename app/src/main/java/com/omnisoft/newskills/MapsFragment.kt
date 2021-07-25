package com.omnisoft.newskills

import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.omnisoft.newskills.Others.LiveLocation
import com.omnisoft.newskills.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    lateinit var bd : FragmentMapsBinding
    var location: Location? = null
    var maps : GoogleMap? =null
    var marker : Marker? = null
    var toBeAnimated = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = FragmentMapsBinding.inflate(inflater,container,false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    fun refreshLocation(){
        LiveLocation(requireContext()).observe(this, Observer {
            location= it
            refreshMarker()
        })
    }

    @SuppressLint("MissingPermission")
    private fun refreshMarker() {
        if(toBeAnimated){
            maps?.clear()
            maps?.mapType = GoogleMap.MAP_TYPE_HYBRID
            maps?.isBuildingsEnabled = true
            maps?.isIndoorEnabled = true
            maps?.isTrafficEnabled = true
            maps?.isMyLocationEnabled = true
            maps?.uiSettings!!.isCompassEnabled = true
            maps?.uiSettings!!.isIndoorLevelPickerEnabled = true
            maps?.uiSettings!!.isRotateGesturesEnabled = true
            maps?.uiSettings!!.isZoomControlsEnabled = true
            maps?.uiSettings!!.isMapToolbarEnabled = true
            marker = maps?.addMarker(MarkerOptions().position(LatLng(location!!.latitude,location!!.longitude)).title("To be set with places API"))
            maps?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,location!!.longitude),18.0f))
            toBeAnimated = false
        }
        marker!!.position= LatLng(location!!.latitude,location!!.longitude)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        val sydney = LatLng(-34.0, 151.0)
        marker = maps?.addMarker(MarkerOptions().position(sydney).title("Sydney"))
        maps?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.0f))
    }
}