package com.smart.weatherapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.weatherapp.adapter.RecyclerAdapter
import com.smart.weatherapp.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smart.weatherapp.ViewModel.MainViewWeatherListModel
import com.smart.weatherapp.ViewModel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    //View binding object
    private lateinit  var viewBinding : ActivityMainBinding

    //View model object
    private lateinit var viewModel : MainViewWeatherListModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    // ProgressDialog object
    private lateinit var  progressDialog : ProgressDialog


    private var lastLocation : Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.pBar.visibility =  View.VISIBLE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.show()


        layoutManager = LinearLayoutManager(this)
        viewBinding.weatherRecyclerViewList.layoutManager = layoutManager

        viewBinding.button2.setOnClickListener {
            progressDialog.show()
            viewModel.fetchData()
        }

    }

    private fun convertDateAndDayTime( milliseconds : Long) : String
    {
        var dy = TimeUnit.MILLISECONDS.toDays(milliseconds);
        var  hr = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
        var minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        var  second= TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        var ms = TimeUnit.MILLISECONDS.toMillis(milliseconds)
        - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(milliseconds));
        return "$dy : $hr : $minutes : $second"
    }

    public override fun onStart() {
        super.onStart()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null){
                lastLocation = task.result
                Log.d("MainActivity", "muru latitude : ${lastLocation?.latitude.toString()}")
                Log.d("MainActivity", "muru longitude : ${lastLocation?.longitude.toString()}")
                viewModel = ViewModelProvider(this,
                    WeatherViewModelFactory(lastLocation?.latitude.toString(), lastLocation?.longitude.toString())
                ).get(MainViewWeatherListModel::class.java)

                viewModel.weatherLiveData.observe(this){
                    Log.d("MainActivity", "****** muru ${it.speed}")
                    viewBinding.currentWeatherState.text = /*it.toString()*/
                                "Location : "+it.name+ "\n"+
                                "Country : "+it.country+"\n"+
                                "Main  : " + it.main+ "\n"+
                                "Description : "+ it.description+"\n"+
                                "Wind Speed :"+it.speed+"\n"+
                                "Sunrise : ${convertDateAndDayTime(it.sunrise)}\n" +
                                "Sunset :  ${convertDateAndDayTime(it.sunset)}\n"

                }
                viewModel.weatherLiveDailyData.observe(this){
                    var list = mutableListOf<String>()
                    for (value in it){
                        for (weatherData in value){
                            var itemList = "Day Type :  ${weatherData.main}\n" +
                                    "Description : ${weatherData.description}\n" +
                                    "Day : ${convertDateAndDayTime(weatherData.dt)}\n" +
                                    "Sunrise : ${ convertDateAndDayTime(weatherData.sunrise)}  " +
                                    "Sunset :  ${convertDateAndDayTime(weatherData.sunset)}\n" +
                                    "Day temp : ${weatherData.day}\n" +
                                    "Min:  ${weatherData.min}  " +
                                    "Min: ${weatherData.max}\n"+
                                    "Night :  ${weatherData.night}   " +
                                    "Evening : ${weatherData.eve}   " +
                                    "Morning : ${weatherData.morn}\n" +
                                    "Wind Spreed :  ${weatherData.wind_speed}\n"+
                                    "Pressure : ${weatherData.pressure}  " +
                                    "Humidity : ${weatherData.humidity}\n"
                            list.add(itemList)
                        }
                    }
                    adapter = RecyclerAdapter(this, list)
                    viewBinding.weatherRecyclerViewList.adapter = adapter
                    progressDialog.dismiss()
                }

                viewModel.fetchData()
            }else
            {
                Toast.makeText(this@MainActivity, "No location detected. Make sure location is enabled on the device.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }


}