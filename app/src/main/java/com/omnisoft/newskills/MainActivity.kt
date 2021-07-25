package com.omnisoft.newskills

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.*
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.omnisoft.newskills.Others.APIs
import com.omnisoft.newskills.Retrofit.RetrofitCallback
import com.omnisoft.newskills.Retrofit.RetrofitClient
import com.omnisoft.newskills.Retrofit.RetrofitListener
import com.omnisoft.newskills.Worker.WorkerClass
import com.omnisoft.newskills.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), View.OnClickListener, RetrofitCallback {
    lateinit var bd: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setListeners()
        createMapFragment()
    }

    private fun createMapFragment() {
        supportFragmentManager.beginTransaction().replace(bd.mapFragment.id,MapsFragment(),"mapsFragment").addToBackStack("mapsFragment").commit()
    }

    private fun setListeners() {
        bd.btnExec.setOnClickListener(this)
        bd.btnPermission.setOnClickListener(this)
        bd.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(bd.btnPermission.id==v.id){
            setUpPermissions()
        } else if(v.id==bd.btnLogin.id){
            startActivity(Intent(this@MainActivity,FirebaseLogin::class.java))
        }else{
            createWorkRequest()
        }
    }

    private fun setUpPermissions() {

        val permissionDeniedMultiplePermissionsListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
            .with(bd.root,"Please allow location permissions to proceed")
            .withOpenSettingsButton("SETTINGS")
            .withDuration(Snackbar.LENGTH_INDEFINITE)
            .build()
        val permissionsListener = object :MultiplePermissionsListener{
            override fun onPermissionsChecked(permission: MultiplePermissionsReport?) {
                when {
                    permission!!.areAllPermissionsGranted() -> {
                        Toast.makeText(this@MainActivity, "PERMISSIONS GRATED", Toast.LENGTH_SHORT).show()
                        if(supportFragmentManager.findFragmentByTag("mapsFragment") is MapsFragment){
                            (supportFragmentManager.findFragmentByTag("mapsFragment") as MapsFragment).refreshLocation()
                        }
                    }
                    permission.isAnyPermissionPermanentlyDenied -> {
                        Toast.makeText(this@MainActivity, "PERMISSIONS PERMANENTLY DENIED", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "PERMISSIONS DENIED", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(requestList: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }
        }
        val compositeMultiplePermissionsListener = CompositeMultiplePermissionsListener(permissionsListener,permissionDeniedMultiplePermissionsListener)
        Dexter.withContext(this)
            .withPermissions(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
            .withListener(compositeMultiplePermissionsListener)
            .check()
    }

    fun createWorkRequest() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workManager = WorkManager.getInstance(this)
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest
            .Builder(WorkerClass::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(workRequest)

    }

    fun createLoginApiCall() {
        val headers: HashMap<String, String> = HashMap()
        headers["User-agent"] = System.getProperty("http.agent")
        var jsonObject = JsonObject()
        jsonObject.addProperty("userId", "4230184127328")
        jsonObject.addProperty("password", "Naeeem12")
        jsonObject.addProperty("captchaCount", 0)
        val loginAPICall: Call<JsonElement> =
            RetrofitClient.getInstance().retrofitInterface.postRequest(
                headers,
                APIs.getAPI(APIs.EndPoints.LOGIN),
                jsonObject
            )
        callApi(loginAPICall, "login")
    }

    fun createTimeApiCall() {
        val timeApiCall: Call<JsonElement> =
            RetrofitClient.getInstance().retrofitInterface.getRequest(
                APIs.getAPI(APIs.EndPoints.TIME)
            )
        callApi(timeApiCall, "time")
    }

    fun callApi(apiCall: Call<JsonElement>, tag: String) {
        RetrofitListener().enqueue(apiCall, tag, this)
    }

    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>, tag: String) {
        if (response.isSuccessful) {

        }
    }

    override fun onFailure(call: Call<JsonElement>, t: Throwable, tag: String) {

    }
}