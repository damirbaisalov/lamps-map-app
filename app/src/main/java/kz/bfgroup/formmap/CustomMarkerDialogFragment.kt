package kz.bfgroup.formmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kz.bfgroup.formmap.data.ApiRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomMarkerDialogFragment: DialogFragment() {

    private lateinit var rootView: View

    private lateinit var turnOnRequestButton: Button
    private lateinit var turnOffRequestButton: Button
    private lateinit var brightnessRequestSeekBar: SeekBar

    private lateinit var fields: Map<String, String>

    private var lampIdFromActivity : String?=null
    private var userIdFromActivity : String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.dialog_fragment_custom_marker,container,false)

        initViews()

        turnOnRequestButton.setOnClickListener {
            fields = mutableMapOf(
                "type" to "1",
                "user_id" to userIdFromActivity.toString(),
                "bright" to brightnessRequestSeekBar.progress.toString(),
                "lamp_id" to lampIdFromActivity.toString(),
            )
            addRequest()
        }

        turnOffRequestButton.setOnClickListener {
            fields = mutableMapOf(
                "type" to "0",
                "user_id" to userIdFromActivity.toString(),
                "bright" to brightnessRequestSeekBar.progress.toString(),
                "lamp_id" to lampIdFromActivity.toString()
            )
            addRequest()
        }

        Toast.makeText(rootView.context,lampIdFromActivity,Toast.LENGTH_LONG).show()
        Toast.makeText(rootView.context,userIdFromActivity,Toast.LENGTH_LONG).show()

        return rootView
    }

    private fun initViews() {
        turnOnRequestButton = rootView.findViewById(R.id.lamp_turn_on_request)
        turnOffRequestButton = rootView.findViewById(R.id.lamp_turn_off_request)
        brightnessRequestSeekBar = rootView.findViewById(R.id.lamp_brightness)

        val lampData = arguments
        lampIdFromActivity = lampData?.getString("lamp_id").toString()
        userIdFromActivity = lampData?.getString("user_id").toString()
    }

    private fun addRequest() {
        ApiRetrofit.getApiClient().requestAdd(fields).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    Toast.makeText(rootView.context,response.body()!!.string(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(rootView.context,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }
}