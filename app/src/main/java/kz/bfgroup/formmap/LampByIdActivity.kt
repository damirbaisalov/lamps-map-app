package kz.bfgroup.formmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class LampByIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lamp_by_id)

        val backButton = findViewById<ImageButton>(R.id.activity_lamp_by_id_back_image_button)
        backButton.setOnClickListener {
            finish()
        }
    }
}