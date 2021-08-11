package kz.bfgroup.formmap.data

import kz.bfgroup.formmap.models.GatewayApiData
import kz.bfgroup.formmap.models.GroupApiData
import kz.bfgroup.formmap.models.LampApiData
import kz.bfgroup.formmap.models.UserApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    //ok
    @GET("lamps/get.php")
    fun getLamps(): Call<List<LampApiData>>

    //
    @GET("groups/get.php")
    fun getGroups(): Call<List<GroupApiData>>

    //ok
    @GET("gateways/get.php")
    fun getGateways(): Call<List<GatewayApiData>>

    //ok
    @FormUrlEncoded
    @POST("auth/Auth.php")
    fun authUser(@FieldMap fields: Map<String, String>) : Call<UserApiData>

    //ok?
    @FormUrlEncoded
    @POST("request/add.php")
    fun requestAdd(@FieldMap fields: Map<String, String>) : Call<ResponseBody>

    //ok
    @FormUrlEncoded
    @POST("lamps/add.php")
    fun addLapm(@FieldMap fields: Map<String, String>) : Call<ResponseBody>

    //ok
    @FormUrlEncoded
    @POST("groups/add.php")
    fun addGroup(@Field("name") name: String) : Call<ResponseBody>
}