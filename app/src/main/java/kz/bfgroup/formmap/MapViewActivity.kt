package kz.bfgroup.formmap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.models.GatewayApiData
import kz.bfgroup.formmap.models.LampApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    private lateinit var addLampButton: Button

    private lateinit var allLampTurnOnButton: Button
    private lateinit var allLampTurnOffButton: Button
    private lateinit var allLampBrightnessEditText: EditText

    private lateinit var lampList: List<LampApiData>
    private lateinit var lampIdList: ArrayList<String>
    private lateinit var userIdFromMainActivity: String

    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("5082c38f-5a2f-4d28-8ecb-3371165769a0")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_map_view)
        mapView = findViewById(R.id.map_view)
        mapView.map.move(
            CameraPosition(Point(52.27401,77.00438),11.0f,0.0f,0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        initViews()

        addLampButton.setOnClickListener {
            val intent = Intent(this,NewLampActivity::class.java)
            startActivity(intent)
        }

        loadApiData()
        loadGatewayMarkers()

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

        mapView.map.mapObjects.addTapListener { mapObject, point ->
            Toast.makeText(this,"worked after change window", Toast.LENGTH_LONG).show()
            val lampData = Bundle()
            lampData.putString("user_id", userIdFromMainActivity)

            if (mapObject is PlacemarkMapObject){
                val placemark: PlacemarkMapObject = mapObject

                for (i in lampList){
                    if (
                        placemark.geometry.latitude.toString() == i.positionX.toString()
                        &&
                        placemark.geometry.longitude.toString() == i.positionY.toString()
                    ) {
                        lampData.putString("lamp_id", i.lampId)
                        val dialogFragment = CustomMarkerDialogFragment()
                        dialogFragment.arguments = lampData
                        dialogFragment.show(supportFragmentManager, "customMarker")
                    }
                }
            }

            return@addTapListener true
        }

    }

    private fun initViews(){
        lampList = arrayListOf()
        lampIdList = arrayListOf()
        addLampButton = findViewById(R.id.add_lamp_button)
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
                            drawLampsMarkerOff(p)
                        } else {
                            drawLampsMarker(p)
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

    private fun drawLampsMarker(p: Point) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_marker)
        }
        mapView.map.mapObjects.addPlacemark(
            p,
            ViewProvider(view)
        )

    }

    private fun drawLampsMarkerOff(p: Point) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_marker_off)
        }
        mapView.map.mapObjects.addPlacemark(
            p,
            ViewProvider(view)
        )

    }

    private fun drawGatewaysMarker(p: Point) {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_gateway_marker)
        }
        mapView.map.mapObjects.addPlacemark(
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
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

//    override fun onResume() {
//        super.onResume()
//        loadApiData()
//    }

}