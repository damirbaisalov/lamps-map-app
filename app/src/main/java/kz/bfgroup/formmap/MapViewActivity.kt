package kz.bfgroup.formmap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.models.LampApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewActivity : AppCompatActivity() , GeoObjectTapListener, InputListener {

    private lateinit var mapView: MapView

    private lateinit var button1: Button

    private lateinit var lampList: List<LampApiData>
    private lateinit var userIdFromMainActivity: String

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

        button1.setOnClickListener {
            val intent = Intent(this,LampByIdActivity::class.java)
            startActivity(intent)
        }

        loadApiData()

        mapView.map.mapObjects.addTapListener { mapObject, point ->

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
                    }
                }
                val dialogFragment = CustomMarkerDialogFragment()
                dialogFragment.arguments = lampData
//                dialogFragment.arguments = userIdData
                dialogFragment.show(supportFragmentManager, "customMarker")
            }

            return@addTapListener true
        }

        mapView.map.addTapListener(this)
        mapView.map.addInputListener(this)

    }

    private fun initViews(){
        lampList = arrayListOf()
        button1 = findViewById(R.id.button1)
        userIdFromMainActivity = intent.getStringExtra("user_id").toString()
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getLamps().enqueue(object :Callback<List<LampApiData>> {
            override fun onResponse(call: Call<List<LampApiData>>, response: Response<List<LampApiData>>) {
                if (response.isSuccessful){
                    val list = response.body()!!
                    lampList = list

                    for (index in list.indices) {
                        val p = Point(
                            list[index].positionX!!.toDouble(),
                            list[index].positionY!!.toDouble()
                        )
                        drawLampsMarker(p)
                    }
                }
            }

            override fun onFailure(call: Call<List<LampApiData>>, t: Throwable) {
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

    override fun onObjectTap(p0: GeoObjectTapEvent): Boolean {
        val selectionMetaData:GeoObjectSelectionMetadata = p0
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)

        mapView.map.selectGeoObject(selectionMetaData.id, selectionMetaData.layerId)

        return true
    }

    override fun onMapTap(p0: Map, p1: Point) {
        mapView.map.deselectGeoObject()
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }
}