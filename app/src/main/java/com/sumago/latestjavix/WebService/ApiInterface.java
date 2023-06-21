package com.sumago.latestjavix.WebService;


import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.sumago.latestjavix.CitizenListPostResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;


public interface ApiInterface {
    @GET("crops_list.php")
    Call<ResponseBody> getLastCropTaken();

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> getLogin(@Field("email") String emailid,
                                @Field("password") String password);


    //    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("citizen/citizenReferList")
    Call<ResponseBody> ReferBySevika(@Field("isUnrefer") int isUnrefer,
                                     @Field("pstatus") String pstatus,
                                     @Field("isInstant") String isInstant,
                                     @Field("citizenId") String citizenId,
                                     @Field("status") String status,
                                     @Field("token") String token);





    @FormUrlEncoded
    @POST("citizen/citizenrefer")
    Call<ResponseBody> ReferBySevikaToDr(@Field("token") String token,
                                         @Field("isUnrefer")String isUnrefer,
                                         @Field("ngoId") String ngoId);


//    @POST("citizen/citizenReferList")
//    Call<ResponseBody> Refe
//    rBySevika(@Body JSONObject jsonObject);

//    @Headers("Content-Type: application/json")
    @POST("/api/citizen/citizenListPagination")
    Call<ResponseBody> getCitizenList(@Body ResponseBody params);


    @FormUrlEncoded
    @POST("fetch_district_on_state_id.php")
    Call<ResponseBody> getDistrict(@Field("state_id") String state_id);

    @POST("test/addbreasttest")
    Call<ResponseBody> addbreasttest(@Body RequestBody  params);

}


