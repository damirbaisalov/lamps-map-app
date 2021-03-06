package kz.bfgroup.formmap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.yandex.mapkit.MapKitFactory
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.models.UserApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val MY_APP_USER_ACTIVITY = "MY_APP_USER_ACTIVITY"
const val USER_TOKEN = "USER_TOKEN"
const val USER_ID = "USER_ID"
class MainActivity : AppCompatActivity() {

    private lateinit var openMapButton : Button
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getSavedToken()!="default"){
            val intent = Intent(this@MainActivity,MapViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        phoneEditText = findViewById(R.id.phone_number_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        openMapButton = findViewById(R.id.open_map_button1)
        openMapButton.setOnClickListener {
            if (phoneEditText.text.toString().isEmpty() || passwordEditText.toString().isEmpty()){
                Toast.makeText(this, "Заполните необходимые поля", Toast.LENGTH_LONG).show()
            } else {
                fields = mutableMapOf(
                    "phone" to phoneEditText.text.toString(),
                    "password" to passwordEditText.text.toString()
                )
                userAuth(fields)
            }
        }
    }

    private fun userAuth(fields: Map<String,String>) {
        ApiRetrofit.getApiClient().authUser(fields).enqueue(object : Callback<UserApiData> {
            override fun onResponse(call: Call<UserApiData>, response: Response<UserApiData>) {
                if (response.isSuccessful){
                    if(response.body()!!.token==null) {
                        Toast.makeText(this@MainActivity,"Неверный номер телефона или пароль",Toast.LENGTH_LONG).show()
                    } else {
                        saveUserData(response.body()!!.token, response.body()!!.id)

                        val intent = Intent(this@MainActivity,MapViewActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<UserApiData>, t: Throwable) {
                Toast.makeText(this@MainActivity,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveUserData(token: String?, id: String?) {
        val sharedPref = this.getSharedPreferences(MY_APP_USER_ACTIVITY, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_ID, id)
        editor.apply()
    }

    private fun getSavedToken(): String {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            MY_APP_USER_ACTIVITY,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(USER_TOKEN, "default") ?: "default"
    }
}