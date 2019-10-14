package com.vjmanlapaz.weatherapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.vjmanlapaz.weatherapp.Constants
import com.vjmanlapaz.weatherapp.R
import com.vjmanlapaz.weatherapp.apis.ApiClient
import com.vjmanlapaz.weatherapp.models.Base
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    private val api by lazy { ApiClient.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if(!checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    Constants.REQUEST_GPS
                )
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    Constants.REQUEST_GPS
                )
            }
        } else {
            getWeather()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.e(TAG, "=== onRequestPermissionsResult ===")
        Log.e(TAG, "Permissions => ${permissions.map { it }}")
        Log.e(TAG, "Granted => ${grantResults.map { it == PackageManager.PERMISSION_GRANTED }}")
        if(requestCode == Constants.REQUEST_GPS) {
            var granted = false
            permissions.forEachIndexed { index, perm ->
                if(perm ==  Manifest.permission.ACCESS_FINE_LOCATION) {
                    granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
                }
                if(perm ==  Manifest.permission.ACCESS_COARSE_LOCATION) {
                    granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
                }
                Log.e(TAG, "Index => $index => Permission => $perm | Granted => $granted")
            }

            if(granted) {
                Log.e(TAG, "GET WEATHER DATA")
                getWeather()
            } else {
                finish()
            }
        }
    }

    private fun getWeather() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            api.getWeather(it.latitude.toString(), it!!.longitude.toString()).enqueue(object : Callback<Base> {
                override fun onFailure(call: Call<Base>, t: Throwable) {}

                override fun onResponse(call: Call<Base>, response: Response<Base>) {
                    updateUI(response.body())
                }
            })
        }.addOnFailureListener {
            Log.e(TAG, "FUSED FAILURE => ${it.message}")
        }.addOnCanceledListener {
            Log.e(TAG, "FUSED CANCELLED => CANCELLED")
        }
    }

    private fun checkPermission() : Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateUI(base: Base?) {
        if(base != null) {
            name.text = String.format(Locale.US, "%s %5s", "Name:", base.name ?: "N/A")
            Picasso.get().load("http://openweathermap.org/img/wn/${base.weather!![0]?.icon}@2x.png").fit().into(description_img)
            description.text = String.format(Locale.US, "%s %5s", "Description:", base.weather[0]?.description ?: "N/A")
            temperature.text = String.format(Locale.US, "%s %5s ℃", "Temperature:", base.main?.temp ?: "N/A")
            pressure.text = String.format(Locale.US, "%s %5s", "Pressure:", base.main?.pressure ?: "N/A")
            humidity.text = String.format(Locale.US, "%s %5s %%", "Humidity:",  base.main?.humidity ?: "N/A")
            sea_level.text = String.format(Locale.US, "%s %5s", "Sea Level:", base.main?.seaLevel ?: "N/A")
            ground_level.text = String.format(Locale.US, "%s %5s", "Ground Level:", base.main?.grndLevel ?: "N/A")
            wind.text = String.format(Locale.US, "%s %5s km\u002Fh", "Wind:", base.wind?.speed ?: "N/A")
        }
    }

     /*else if(!Utils.isDeviceLocationEnabled(v.context)) {
        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.REQUEST_LOCATION_SETTINGS)
    }*/
}
