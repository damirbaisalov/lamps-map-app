package kz.bfgroup.formmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kz.bfgroup.formmap.data.ApiRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewLampActivity : AppCompatActivity() {

    private lateinit var idLampEditText: EditText
    private lateinit var yCordEditText: EditText
    private lateinit var xCordEditText: EditText
    private lateinit var idGatewayEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var districtEditText: EditText
    private lateinit var addNewLampButton: Button

    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_lamp)

        initViews()
        addNewLampButton.setOnClickListener {
            fields = mutableMapOf(
                "position_x" to xCordEditText.text.toString(),
                "position_y" to yCordEditText.text.toString(),
                "lamp_id" to idLampEditText.text.toString(),
                "gateway_id" to idGatewayEditText.text.toString(),
                "street" to streetEditText.text.toString(),
                "district" to districtEditText.text.toString()
            )
            addNewLampRequest()
        }
    }

    private fun initViews() {
        idLampEditText = findViewById(R.id.new_lamp_id)
        yCordEditText = findViewById(R.id.new_lamp_y)
        xCordEditText = findViewById(R.id.new_lamp_x)
        idGatewayEditText = findViewById(R.id.new_lamp_gateway_id)
        streetEditText = findViewById(R.id.new_lamp_street)
        districtEditText = findViewById(R.id.new_lamp_district)
        addNewLampButton = findViewById(R.id.new_lamp_submit_button)
    }

    private fun addNewLampRequest() {
        ApiRetrofit.getApiClient().addLapm(fields).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    Toast.makeText(this@NewLampActivity,response.body()!!.string(), Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@NewLampActivity,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }
}