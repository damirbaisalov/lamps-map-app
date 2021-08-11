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

class NewGroupActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var addNewGroupButton: Button

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        initViews()

        addNewGroupButton.setOnClickListener {
            name = nameEditText.text.toString()
            addNewGroupRequest(name)
        }
    }

    private fun initViews() {
        nameEditText = findViewById(R.id.new_group_name)
        addNewGroupButton = findViewById(R.id.new_group_submit_button)
    }

    private fun addNewGroupRequest(name: String) {
        ApiRetrofit.getApiClient().addGroup(name).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    Toast.makeText(this@NewGroupActivity,response.body()!!.string(), Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@NewGroupActivity,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }
}