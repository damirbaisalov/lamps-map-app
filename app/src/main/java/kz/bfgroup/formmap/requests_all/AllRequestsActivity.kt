package kz.bfgroup.formmap.requests_all

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.bfgroup.formmap.MY_APP_USER_ACTIVITY
import kz.bfgroup.formmap.R
import kz.bfgroup.formmap.USER_ID
import kz.bfgroup.formmap.data.ApiRetrofit
import kz.bfgroup.formmap.requests_all.models.RequestApiData
import kz.bfgroup.formmap.requests_all.view.RequestAdapter
import kz.bfgroup.formmap.requests_all.view.RequestClickListener
import kz.bfgroup.formmap.requests_all.view.SuccessSubmitRequestDialogFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllRequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var requestAdapter = RequestAdapter(getRequestClickListener())
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_requests)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()

        swipeRefreshLayout.setOnRefreshListener {
            requestAdapter.clearAll()
            loadApiData()
        }
    }

    private fun initViews() {
        backButton = findViewById(R.id.activity_all_request_back_image_button)
        recyclerView = findViewById(R.id.activity_requests_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = requestAdapter
        swipeRefreshLayout = findViewById(R.id.activity_requests_swipe_refresh)
    }

    private fun loadApiData() {
        swipeRefreshLayout.isRefreshing = true
        ApiRetrofit.getApiClient().getRequests(getSavedUserID()).enqueue(object:
            Callback<List<RequestApiData>> {
            override fun onResponse(
                call: Call<List<RequestApiData>>,
                response: Response<List<RequestApiData>>
            ) {
                swipeRefreshLayout.isRefreshing = false
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val requestsApiDataResponseList: MutableList<RequestApiData> = mutableListOf()
                    val list = response.body()!!
                    requestsApiDataResponseList.addAll(list)
                    requestAdapter.setList(requestsApiDataResponseList)
//                    Toast.makeText(this@AllRequestsActivity, getSavedUserID(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<RequestApiData>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
//                progressBar.visibility = View.GONE
                Toast.makeText(this@AllRequestsActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun okRequest(requestId: String) {
        ApiRetrofit.getApiClient().submitRequest(requestId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
//                    Toast.makeText(this@AllRequestsActivity, response.body()?.string(), Toast.LENGTH_LONG).show()
                    val dialogFragment = SuccessSubmitRequestDialogFragment()
                    val fragmentManager = supportFragmentManager

                    val transaction = fragmentManager.beginTransaction()
                    dialogFragment.show(transaction, "dialog")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AllRequestsActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getRequestClickListener(): RequestClickListener {
        return object: RequestClickListener{
            override fun onClick(id: String?) {
//                Toast.makeText(this@AllRequestsActivity, id, Toast.LENGTH_SHORT).show()
                okRequest(id!!)
            }
        }
    }

    private fun getSavedUserID(): String {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            MY_APP_USER_ACTIVITY,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(USER_ID, "default") ?: "default"
    }

    override fun onResume() {
        super.onResume()
        loadApiData()
    }
}