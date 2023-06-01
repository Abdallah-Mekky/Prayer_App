package com.abdallah.alalamiyaalhuratask.ui.qibla

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Half.EPSILON
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.abdallah.alalamiyaalhuratask.Constants
import com.abdallah.alalamiyaalhuratask.R
import com.abdallah.alalamiyaalhuratask.databinding.ActivityQiblaBinding
import com.abdallah.alalamiyaalhuratask.model.LatLang
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin


@AndroidEntryPoint
class QiblaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityQiblaBinding
    private var timestamp: Long = 0
    private val NS2S = 1.0f / 1000000000.0f
    private lateinit var qiblaDirection: Marker
    private lateinit var currentLocationMarker: Marker
    private lateinit var kaabaMarker: Marker

    private val qiblaViewModel: QiblaViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackClick()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                // Handle gyroscope sensor event

                val xRotationRate = event?.values?.get(0) ?: return
                val yRotationRate = event.values?.get(1) ?: return
                val zRotationRate = event.values?.get(2) ?: return

                val deltaRotationVector = FloatArray(4)
                val rotationMatrix = FloatArray(9)

                val omegaMagnitude = kotlin.math.sqrt(
                    xRotationRate * xRotationRate +
                            yRotationRate * yRotationRate +
                            zRotationRate * zRotationRate
                )

                if (omegaMagnitude > EPSILON) {
                    val deltaTime = (event.timestamp - timestamp) * NS2S
                    timestamp = event.timestamp

                    val dTheta = omegaMagnitude * deltaTime
                    val sinThetaOverTwo = sin(dTheta / 2.0f)
                    val cosThetaOverTwo = cos(dTheta / 2.0f)

                    deltaRotationVector[0] =
                        (sinThetaOverTwo * xRotationRate / omegaMagnitude).toFloat()
                    deltaRotationVector[1] =
                        (sinThetaOverTwo * yRotationRate / omegaMagnitude).toFloat()
                    deltaRotationVector[2] =
                        (sinThetaOverTwo * zRotationRate / omegaMagnitude).toFloat()
                    deltaRotationVector[3] = cosThetaOverTwo.toFloat()
                }

                val deltaRotationMatrix = FloatArray(9)

                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector)

                val currentRotationMatrix = FloatArray(9)
                System.arraycopy(rotationMatrix, 0, currentRotationMatrix, 0, 9)

                SensorManager.getRotationMatrix(
                    rotationMatrix,
                    null,
                    deltaRotationMatrix,
                    currentRotationMatrix
                )

                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)

                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()


                val arrowRotation = -azimuth // Invert azimuth to match image rotation direction
                Log.e("TAG", "onSensorChanged: $arrowRotation")
                qiblaDirection.rotation = arrowRotation
                //currenttDirection = arrowRotation
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle gyroscope sensor accuracy change
            }
        }, gyroscope,
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val currentLocationOnMap = LatLng(Constants.latitude!!, Constants.longitude!!)
        val kaabaLocationOnMap = LatLng(Constants.KAABA_LATITUDE, Constants.KAABA_LONGITUDE)

        createMyLocationMarker(currentLocationOnMap)

        createKaabaLocationMarker(kaabaLocationOnMap)


        val qiblaDirectionMarkerLatLag = qiblaViewModel.halfDistance(
            Constants.latitude!!, Constants.longitude!!, Constants.KAABA_LATITUDE,
            Constants.KAABA_LONGITUDE
        )

        createQiblaDirectionMarker(qiblaDirectionMarkerLatLag)


        setQiblaDirectionMarker()


        cameraFocusOnMarkers(
            currentLocationMarker,
            kaabaMarker,
            qiblaDirection,
            currentLocationOnMap = currentLocationOnMap,
            kaabaLocationOnMap = kaabaLocationOnMap
        )

    }

    private fun setQiblaDirectionMarker() {
        lifecycleScope.launch {

            qiblaViewModel.getQiblaDirections(Constants.latitude!!, Constants.longitude!!)
                ?.toFloat()
                ?.let {
                    qiblaDirection.rotation = it
                }
        }
    }

    private fun createQiblaDirectionMarker(lineImageLatLag: LatLang) {

        qiblaDirection = mMap.addMarker(MarkerOptions()
            .apply {
                position(
                    LatLng(lineImageLatLag.latitude, lineImageLatLag.longitude)
                )
                icon(bitmapDescriptorFromVector(this@QiblaActivity, R.drawable.up_arrow))
                title("Line")

            })!!
    }

    private fun createKaabaLocationMarker(kaabaLocationOnMap: LatLng) {
        kaabaMarker = mMap.addMarker(MarkerOptions()
            .apply {
                position(kaabaLocationOnMap)
                title("Kaaba")
                icon(bitmapDescriptorFromVector(this@QiblaActivity, R.drawable.ic_kaaba))

            })!!

    }

    private fun createMyLocationMarker(currentLocationOnMap: LatLng) {
        currentLocationMarker = mMap.addMarker(MarkerOptions().apply {
            position(currentLocationOnMap)
            title("My Location")
        })!!

    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int):
            BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun onBackClick() {
        binding.icArrowBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun makeRedLineBetweenTwoLocation(
        currentLocationOnMap: LatLng,
        kaabaLocationOnMap: LatLng
    ): PolylineOptions {

        return PolylineOptions().add(currentLocationOnMap, kaabaLocationOnMap)
            .width(5f)
            .color(Color.RED)
    }

    private fun cameraFocusOnMarkers(
        vararg marker: Marker, currentLocationOnMap: LatLng,
        kaabaLocationOnMap: LatLng
    ) {

        val markersList: MutableList<Marker?> = ArrayList()
        marker.forEach {
            markersList.add(it)
        }

        val builder = LatLngBounds.Builder()
        for (m in markersList) {
            builder.include(m!!.position)
        }

        val padding = 50

        val bounds: LatLngBounds = builder.build()

        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.addPolyline(makeRedLineBetweenTwoLocation(currentLocationOnMap, kaabaLocationOnMap))

        mMap.setOnMapLoadedCallback {
            mMap.animateCamera(cu)
        }
    }
}