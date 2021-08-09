package kz.bfgroup.formmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var openMapButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openMapButton = findViewById(R.id.open_map_button1)
        openMapButton.setOnClickListener {
            val intent = Intent(this,MapViewActivity::class.java)
            startActivity(intent)
        }
    }
}