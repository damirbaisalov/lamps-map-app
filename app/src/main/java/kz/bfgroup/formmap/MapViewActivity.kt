package kz.bfgroup.formmap

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.ui_view.ViewProvider
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.models.GatewayApiData
import kz.bfgroup.formmap.models.GroupApiData
import kz.bfgroup.formmap.models.LampApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat.checkSelfPermission
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.PointF
import android.media.Image
import com.yandex.runtime.image.ImageProvider.fromResource
import androidx.core.app.ActivityCompat.requestPermissions
import com.yandex.mapkit.user_location.UserLocationLayer


class MapViewActivity : AppCompatActivity(), MapObjectTapListener, UserLocationObjectListener, CameraListener {

    private lateinit var mapView: MapView
    private lateinit var pointCollection: MapObjectCollection
    private val requestPermissionLocation = 1
    private var permissionLocation = false
    private var followUserLocation = false
    private lateinit var userLocationLayer: UserLocationLayer
    private var routeStartLocation = Point(52.27401,77.00438)

    private lateinit var addLampButton: Button
    private lateinit var addGroupButton: Button

    private lateinit var allLampTurnOnButton: Button
    private lateinit var allLampTurnOffButton: Button
    private lateinit var allLampBrightnessEditText: EditText

    private lateinit var lampList: List<LampApiData>
    private lateinit var lampIdList: ArrayList<String>
    private lateinit var userIdFromMainActivity: String

    private lateinit var groupsList: ArrayList<String>
    private lateinit var spinner: Spinner
    private lateinit var groupsTextView: TextView

    private lateinit var refreshMapImageView: ImageView
    private lateinit var userLocationImageView: ImageView

    private lateinit var fields: Map<String, String>


    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map_view)
        super.onCreate(savedInstanceState)


        mapView = findViewById(R.id.map_view)
        checkPermission()
        userInterface()

//        mapView.map.move(
//            CameraPosition(routeStartLocation,11.0f,0.0f,0.0f),
//            Animation(Animation.Type.SMOOTH, 0F),
//            null
//        )
        pointCollection = mapView.map.mapObjects.addCollection()
        pointCollection.addTapListener(this)

        initViews()

        addLampButton.setOnClickListener {
            val intent = Intent(this,NewLampActivity::class.java)
            startActivity(intent)
        }

        addGroupButton.setOnClickListener {
            val intent = Intent(this,NewGroupActivity::class.java)
            startActivity(intent)
        }

        loadApiData()
        loadGatewayMarkers()
        loadGroupsApiData()

        refreshMapImageView.setOnClickListener {
            pointCollection.clear()
            loadApiData()
            loadGatewayMarkers()
        }

        groupsTextView = findViewById(R.id.groups_text_view)
        groupsTextView.setOnClickListener {
            pointCollection.addPlacemark(Point(52.27401,77.00438))
        }

        allLampTurnOnButton.setOnClickListener {
            fields = mutableMapOf(
                "type" to "1",
                "user_id" to userIdFromMainActivity,
                "bright" to allLampBrightnessEditText.text.toString(),
                "lamp_id" to lampIdList.toString()
            )
            Toast.makeText(this, fields.toString(), Toast.LENGTH_LONG).show()
            addRequest()
        }

        allLampTurnOffButton.setOnClickListener {
            fields = mutableMapOf(
                "type" to "0",
                "user_id" to userIdFromMainActivity,
                "bright" to allLampBrightnessEditText.text.toString(),
                "lamp_id" to lampIdList.toString()
            )
            Toast.makeText(this, fields.toString(), Toast.LENGTH_LONG).show()
            addRequest()
        }

    }

    private fun initViews(){
        lampList = arrayListOf()
        lampIdList = arrayListOf()
        groupsList = arrayListOf()
        spinner = findViewById(R.id.spinner)
        addLampButton = findViewById(R.id.add_lamp_button)
        addGroupButton = findViewById(R.id.add_group_button)
        refreshMapImageView = findViewById(R.id.refresh_map_image_view)
        allLampTurnOnButton = findViewById(R.id.all_turn_on_request)
        allLampTurnOffButton = findViewById(R.id.all_turn_off_request)
        allLampBrightnessEditText = findViewById(R.id.all_lamp_brightness_edit_text)
        userIdFromMainActivity = intent.getStringExtra("user_id").toString()

    }

    private fun addRequest() {
        ApiRetrofit.getApiClient().requestAdd(fields).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MapViewActivity,response.body()!!.string(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MapViewActivity,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadGroupsApiData() {
        ApiRetrofit.getApiClient().getGroups().enqueue(object :Callback<List<GroupApiData>> {
            override fun onResponse(call: Call<List<GroupApiData>>, response: Response<List<GroupApiData>>) {
                if (response.isSuccessful){
                    val list = response.body()!!
                    groupsList.add("Все")
                    for (index in list.indices){
                        groupsList.add(list[index].name!!)
                    }

                    val myGroupAdapter: ArrayAdapter<String> = ArrayAdapter(
                        this@MapViewActivity,
                        android.R.layout.simple_list_item_1,
                        groupsList
                    )
                    myGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = myGroupAdapter
                }
            }

            override fun onFailure(call: Call<List<GroupApiData>>, t: Throwable) {
                Toast.makeText(this@MapViewActivity,t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getLamps().enqueue(object :Callback<List<LampApiData>> {
            override fun onResponse(call: Call<List<LampApiData>>, response: Response<List<LampApiData>>) {
                if (response.isSuccessful){
                    val list = response.body()!!
                    lampList = list

                    for (index in list.indices) {
                        lampIdList.add(list[index].id!!)
                        val p = Point(
                            list[index].positionX!!.toDouble(),
                            list[index].positionY!!.toDouble()
                        )
                        if (list[index].status == "0") {
                            drawLampsMarkerOff(p, list[index].lampId)
                        } else {
                            drawLampsMarker(p, list[index].lampId)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<LampApiData>>, t: Throwable) {
                Toast.makeText(this@MapViewActivity,t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadGatewayMarkers() {
        ApiRetrofit.getApiClient().getGateways().enqueue(object :Callback<List<GatewayApiData>> {
            override fun onResponse(call: Call<List<GatewayApiData>>, response: Response<List<GatewayApiData>>) {
                if (response.isSuccessful){
                    val list = response.body()!!

                    for (index in list.indices) {
                        val p = Point(
                            list[index].positionX!!.toDouble(),
                            list[index].positionY!!.toDouble()
                        )
                        drawGatewaysMarker(p)
                    }
                }
            }

            override fun onFailure(call: Call<List<GatewayApiData>>, t: Throwable) {
                Toast.makeText(this@MapViewActivity,t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun drawLampsMarker(p: Point, lampId: String?) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_marker)
        }
        val placemark = pointCollection.addPlacemark(
            p,
            ViewProvider(view)
        )
        placemark.userData = lampId
    }

    private fun drawLampsMarkerOff(p: Point, lampId: String?) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_marker_off)
        }
        val placemark = pointCollection.addPlacemark(
            p,
            ViewProvider(view)
        )
        placemark.userData = lampId
    }

    private fun drawGatewaysMarker(p: Point) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_gateway_marker)
        }
        pointCollection.addPlacemark(
            p,
            ViewProvider(view)
        )
    }

    private fun getSavedToken(): String {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            MY_APP_USER_ACTIVITY,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(USER_TOKEN, "default") ?: "default"
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
        super.onStop()
    }

    override fun onStart() {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        super.onStart()
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        Log.d("TAG", p0.toString())
        Toast.makeText(this,"worked after change window", Toast.LENGTH_LONG).show()
        val lampData = Bundle()
        lampData.putString("user_id", userIdFromMainActivity)

        if (p0 is PlacemarkMapObject){
            val placemark: PlacemarkMapObject = p0
            if (placemark.userData!=null){
                lampData.putString("lamp_id", placemark.userData.toString())
                val dialogFragment = CustomMarkerDialogFragment()
                dialogFragment.arguments = lampData
                dialogFragment.show(supportFragmentManager, "customMarker")
            }
        }

        return true
    }

    ////////////////////////////////////

    private fun checkPermission(){
        val permissionLocation = checkSelfPermission(this, ACCESS_FINE_LOCATION)
        if (permissionLocation != PERMISSION_GRANTED) {
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), requestPermissionLocation)
        } else {
            onMapReady()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestPermissionLocation -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    onMapReady()
                }

                return
            }
        }
    }

    private fun userInterface() {
        userLocationImageView = findViewById(R.id.user_location_map_image_view)
        userLocationImageView.setOnClickListener {
            if (permissionLocation) {

                cameraUserPosition()
                followUserLocation = true

            } else {
                checkPermission()
            }
        }

    }

    private fun onMapReady(){
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        mapView.map.addCameraListener(this)

        cameraUserPosition()

        permissionLocation = true
    }

    private fun cameraUserPosition() {
        if (userLocationLayer.cameraPosition() != null){
            routeStartLocation = userLocationLayer.cameraPosition()!!.target
            mapView.map.move(
                CameraPosition(routeStartLocation,16f,0f,0f),
                Animation(Animation.Type.SMOOTH,1F),
                null
            )
        } else {
            mapView.map.move(CameraPosition(routeStartLocation,11f, 0f,0f))
        }
    }

    private fun setAnchor() {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        followUserLocation = false
    }

    private fun noAnchor() {
        userLocationLayer.resetAnchor()

    }

    override fun onObjectAdded(p0: UserLocationView) {
        setAnchor()

        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_user_location)
        }

        p0.pin.setView(ViewProvider(view))
        p0.arrow.setView(ViewProvider(view))
//        p0.pin.setIcon(fromResource(this,R.drawable.marker_logo))
//        p0.arrow.setIcon(fromResource(this,R.drawable.marker_logo))
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onCameraPositionChanged(
        p0: com.yandex.mapkit.map.Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        p3: Boolean
    ) {
        if (p3){
            if (followUserLocation){
                setAnchor()
            }
        } else {
            if (!followUserLocation) {
                noAnchor()
            }
        }
    }
}
