package kz.bfgroup.formmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.models.GroupApiData
import kz.bfgroup.formmap.view.GroupAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomGroupDialogFragment: DialogFragment() {

    private lateinit var rootView: View

    private lateinit var recyclerView: RecyclerView
    private var groupAdapter: GroupAdapter = GroupAdapter()

//    private lateinit var fields: Map<String, String>
//
//    private var lampIdFromActivity : String?=null
//    private var userIdFromActivity : String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.dialog_fragment_group_list,container,false)

        initViews()

        loadGroupData()




        return rootView
    }

    private fun initViews() {
        recyclerView = rootView.findViewById(R.id.recyclerview_group)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = groupAdapter
//        val lampData = arguments
//        lampIdFromActivity = lampData?.getString("lamp_id").toString()
//        userIdFromActivity = lampData?.getString("user_id").toString()
    }

    private fun loadGroupData() {
        ApiRetrofit.getApiClient().getGroups().enqueue(object : Callback<List<GroupApiData>> {
            override fun onResponse(call: Call<List<GroupApiData>>, response: Response<List<GroupApiData>>) {
                if (response.isSuccessful){
                    val groupApiDataResponseList: MutableList<GroupApiData> = mutableListOf()
                    val list = response.body()!!

                    groupApiDataResponseList.addAll(list)

                    groupAdapter?.setList(groupApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<GroupApiData>>, t: Throwable) {
                Toast.makeText(rootView.context,"ERROR OCCURED", Toast.LENGTH_LONG).show()
            }
        })
    }
}