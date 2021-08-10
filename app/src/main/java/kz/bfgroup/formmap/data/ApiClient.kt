package kz.bfgroup.formmap.data

import kz.bfgroup.formmap.models.GatewayApiData
import kz.bfgroup.formmap.models.GroupApiData
import kz.bfgroup.formmap.models.LampApiData
import kz.bfgroup.formmap.models.UserApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {

    @GET("lamps/get.php")
    fun getLamps(): Call<List<LampApiData>>

    @GET("groups/get.php")
    fun getGroups(): Call<List<GroupApiData>>

    @GET("gateways/get.php")
    fun getGateways(): Call<List<GatewayApiData>>

    @FormUrlEncoded
    @POST("auth/Auth.php")
    fun authUser(@FieldMap fields: Map<String, String>) : Call<UserApiData>

    @FormUrlEncoded
    @POST("request/add.php")
    fun requestAdd(@FieldMap fields: Map<String, String>) : Call<ResponseBody>
}