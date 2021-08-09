package kz.bfgroup.formmap

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.ModelStyle
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import org.w3c.dom.Text

class MapViewActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection


    private lateinit var button1: Button

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
        mapObjects = mapView.map.mapObjects.addCollection()

        button1 = findViewById(R.id.button1)
        button1.setOnClickListener {
            val intent = Intent(this,LampByIdActivity::class.java)
            startActivity(intent)
        }

        val viewPlacemark = mapObjects.addPlacemark(Point(52.27401,77.00438), ImageProvider.fromResource(this,R.drawable.marker_logo))
//        mapView.map.mapObjects.addPlacemark(Point(52.27401,77.00438), ImageProvider.fromBitmap(drawSimpleBitmap("lkdsajkldjaslkdas")))
    }


//    private fun createMapObjects() {
//        val textView : TextView = TextView(this)
//        val colors : List<Int> = listOf(
//            Color.BLUE,
//            Color.GREEN,
//            Color.BLACK
//        )
//
//        val params : ViewGroup.LayoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        textView.setTextColor(Color.RED)
//        textView.text = "Hello, world"
//
//        val viewProvider: ViewProvider = ViewProvider(textView)
//        val viewPlacemark: PlacemarkMapObject = mapObjects.addPlacemark(Point(52.27401,77.00438), ImageProvider.fromBitmap(drawSimpleBitmap("10")))
//
//    }

    private fun drawSimpleBitmap(number: String): Bitmap {
        val picSize = 30
        val bitmap = Bitmap.createBitmap(picSize,picSize,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas.drawCircle((picSize/2).toFloat(), (picSize/2).toFloat(), (picSize/2).toFloat(), paint)

        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.textSize = 10F
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(number, (picSize/2).toFloat(),
        picSize/2-((paint.descent()+paint.ascent())/2),paint)

        return bitmap
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}