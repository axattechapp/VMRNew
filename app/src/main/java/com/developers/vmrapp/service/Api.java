package com.developers.vmrapp.service;

import com.developers.vmrapp.models.AddreceiptModel;
import com.developers.vmrapp.models.CheckUserExistsModel;
import com.developers.vmrapp.models.HistroyMdel;
import com.developers.vmrapp.models.LogbookServiceViewdapiModel2;
import com.developers.vmrapp.models.LogbookServiceViewdapiModel3;
import com.developers.vmrapp.models.MarketSearch;
import com.developers.vmrapp.models.PrivacyModel;
import com.developers.vmrapp.models.Product_sold_Model;
import com.developers.vmrapp.models.TermsModel;
import com.developers.vmrapp.models.TermsPrivacyModel;
import com.developers.vmrapp.models.UserProfileForNotification;
import com.developers.vmrapp.models.VehicleEdit;
import com.developers.vmrapp.models.VehicleHistoryModel2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.developers.vmrapp.models.DummyMarket;
import com.developers.vmrapp.models.GetVehicleDetailModel;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.models.LogBookServiceModel;
import com.developers.vmrapp.models.LogbookServiceViewdapiModel;
import com.developers.vmrapp.models.LoginModel;
import com.developers.vmrapp.models.MainTainRecordApiModel;
import com.developers.vmrapp.models.PlanModel;
import com.developers.vmrapp.models.RegistrationModel;
import com.developers.vmrapp.models.ServiceRecordApiModel;
import com.developers.vmrapp.models.StoresSHopApiModel;
import com.developers.vmrapp.models.TransferVehicleapiModel;
import com.developers.vmrapp.models.UpdateLogBookModel;
import com.developers.vmrapp.models.UserProfileModel;
import com.developers.vmrapp.models.VehicleHistoryapiModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://99.81.90.131:7007/";
//String BASE_URL = "http://99.81.90.131:7020/";
    String Image_URL = "http://99.81.90.131:7007";

    String Public_key = "pk_test_51JNYa7CObxkC6QaC4KtKQlZNshiBzB1mTSqHLlvsUQTcVqQdle38cRNgfYFfA6Z20DV0AOUaz6ntJT59VbEuprDy00FqFn6Hcs";

    @POST("register/")
    Call<RegistrationModel> registration(@Body JsonObject model);

    @POST("login/")
    Call<LoginModel> login(@Body JsonObject model);

    @POST("location_filter/")
    Call<StoresSHopApiModel> location_filter(@Body JsonObject model,@Header("Authorization") String token);

    @GET("Search_a_to_z/")
    Call<StoresSHopApiModel> Search_a_to_z(@Header("Authorization") String token);

    @GET("token_checkapi/")
    Call<CheckUserExistsModel> checkUserExists(@Query("token") String token);

    @FormUrlEncoded
    @POST("userprofileapi/")
    Call<UserProfileForNotification> setRemainderValue(@Header("Authorization") String token,@Field("push_notification") boolean push_notification);


    @GET("userprofileapi/")
    Call<UserProfileForNotification> getRemainderValue(@Header("Authorization") String token);


    @GET("logoutapi/")
    Call<Void> logoutapi(@Header("Authorization") String token);

    @GET("Market_a_to_z/")
    Call<DummyMarket> Market_search(@Header("Authorization") String token);


   @FormUrlEncoded
    @POST("search_market/")
    Call<DummyMarket> search_market(@Header("Authorization") String token,
                                     @Field("search") String search);

    @GET("privacyvapi/")
    Call<PrivacyModel> privacyvapi(@Header("Authorization") String token);

    @GET("privacyvapi/")
    Call<TermsPrivacyModel> GetprivacyTerms(@Header("Authorization") String token);

    @GET("privacyvapi/")
    Call<TermsPrivacyModel> GetTerms(@Header("Authorization") String token);

    @GET("termsapi/")
    Call<TermsModel> termsapi(@Header("Authorization") String token);

    @Multipart
    @POST("vehicle_details/")
    Call<JsonObject> sendvehicledetail(@Part("vehicle_vinnumber") RequestBody vehicle_vinnumber,
                                                  @Part("vehicle_regnumber") RequestBody vehicle_regnumber,
                                                  @Part("current_km") RequestBody current_km,
                                                  @Part("vehicle_make") RequestBody vehicle_make,
                                                  @Part("vehicle_model") RequestBody vehicle_model,
                                                  @Part("vehicle_year") RequestBody vehicle_year,
                                                  @Part("reg_due_date") RequestBody reg_due_date,
                                                  @Part("prefferedsupp") RequestBody prefferedsupp,
                                                  @Part("contact_number") RequestBody contact_number,
                                                  @Part("next_service") RequestBody next_service,
                                                  @Part("servicekms") RequestBody servicekms,
                                                  @Part("user") RequestBody userId,
                                                 @Part MultipartBody.Part vehicleimage,
                                                 @Part("insurance_date") RequestBody Insurance_date,
                                                 @Header("Authorization") String token);

    @GET("vehicle_details/")
    Call<HomeVehicleDetailModel> getvehicledetail(@Header("Authorization") String token);

    @GET("vehiclehistoryapi/")
    Call<VehicleHistoryapiModel> vehiclehistoryapi(@Query("vehicleid") String vehicleid, @Header("Authorization") String token);

    @GET("vehiclehistoryapi/")
    Call<VehicleHistoryModel2> vehiclehistoryapi2(@Query("vehicle") String vehicleid, @Header("Authorization") String token);

    @GET("vehiclehistoryapi/")
    Call<HistroyMdel> vehiclehistoryapi3(@Query("vehicle") String vehicleid, @Header("Authorization") String token);

    @GET("storeshopapi/")
    Call<StoresSHopApiModel> storeshopapi(@Header("Authorization") String token);

    @GET("marketplaceviewapi/")
    Call<DummyMarket> getmarketplaceviewdata(@Header("Authorization") String token);

    @POST("api_forgot/")
    Call<JsonObject> forgotpassword(@Body JsonObject object);

    @GET("plansdata/")
    Call<PlanModel> plansdata(@Header("Authorization") String token);

    @POST("paymentapi/")
    Call<JsonElement> paymentapi(@Body JsonObject object, @Header("Authorization") String token);

    @POST("vehicletransferapi/")
    Call<TransferVehicleapiModel> transfervehicleapi(@Body JsonObject object, @Header("Authorization") String token);

    @GET("userprofileapi/")
    Call<UserProfileModel> userprofileapi(@Header("Authorization") String token);

    @GET("logbookserviceseditapi/")
    Call<LogbookServiceViewdapiModel> getlogbookservicesviewdapi(@Query("logbookservicesid") String logbookservicesid, @Header("Authorization") String token);


    @GET("logbookserviceseditapi/")
    Call<LogbookServiceViewdapiModel3> getlogbookservicesviewdapi2(@Query("logbookservicesid") String logbookservicesid, @Header("Authorization") String token);

    @GET("maintenancerecordapi/")
    Call<List<MainTainRecordApiModel.Data>> getmaintenancerecordapi(@Query("maintenancerecordid") String id, @Header("Authorization") String token);

    @Multipart
    @POST("userprofileapi/")
    Call<JsonObject> postuserprofileapi(@Part("email") RequestBody email,
                                        @Part("name") RequestBody name,
                                        @Part("age") RequestBody age,
                                        @Part("location") RequestBody location,
                                        @Part MultipartBody.Part profileimage, @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("userprofileapi/")
    Call<UserProfileModel> postuserprofileapi1(@Field("email") String email,
                                        @Field("name") String name,
                                        @Field("age") String age,
                                        @Field("location") String location,
                                       @Header("Authorization") String token);

    @Multipart
    @POST("updatelogbookapi/")
    Call<UpdateLogBookModel> updatelogbookapi(@Part("vehicle") RequestBody vehicle,
                                              @Part("current_km") RequestBody current_km,
                                              @Part("date") RequestBody date,
                                              @Part("next_service") RequestBody next_service,
                                              @Part("servicecompany") RequestBody servicecompany,
                                              @Part("notes") RequestBody notes,
                                              @Part("user") RequestBody user,
                                              @Part MultipartBody.Part odometerimage, @Header("Authorization") String token);

    @Multipart
    @POST("servicerecordapi/")
    Call<ServiceRecordApiModel> servicerecordapi(@Part("vehicle") RequestBody vehicle,
                                                 @Part("servicetitle") RequestBody servicetitle,
                                                 @Part("servicecompany") RequestBody servicecompany,
                                                 @Part("minorormajorservice") RequestBody minorormajorservice,
                                                 @Part("date") RequestBody date,
                                                 @Part("completed") RequestBody completed,
                                                 @Part("replace") RequestBody replace,
                                                 @Part("user") RequestBody user,
                                                 @Part MultipartBody.Part serviceimage, @Header("Authorization") String token);


    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<LogBookServiceModel> logbookservicesviewdapi(
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part("current_km") RequestBody current_km,
            @Part("next_service") RequestBody next_service,
            @Part("notes") RequestBody notes,
            @Part("servicetitle") RequestBody servicetitle,
            @Part("date") RequestBody date,
            @Part("servicecompany") RequestBody servicecompany,
            @Part("minorormajorservice") RequestBody minorormajorservice,
            @Part("completed") RequestBody completed,
            @Part("replace") RequestBody replace,
            @Part MultipartBody.Part serviceimage, @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);


    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<AddreceiptModel> logbookservicesviewdapi(
            @Part("notes") RequestBody notes,
            @Part("date") RequestBody date,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
         @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);

    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<AddreceiptModel> logbookservicesviewdapi1(
            @Part("notes") RequestBody notes,
            @Part("date") RequestBody date,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part MultipartBody.Part photoofreceipt,@Part MultipartBody.Part photoofreceipt1, @Header("Authorization") String token);


    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<AddreceiptModel> logbookservicesviewdapi2(
            @Part("notes") RequestBody notes,
            @Part("date") RequestBody date,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part MultipartBody.Part photoofreceipt,
            @Part MultipartBody.Part photoofreceipt1,
            @Part MultipartBody.Part photoofreceipt2,
            @Header("Authorization") String token);


    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<AddreceiptModel> logbookservicesviewdapi3(
            @Part("notes") RequestBody notes,
            @Part("date") RequestBody date,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part MultipartBody.Part photoofreceipt,
            @Part MultipartBody.Part photoofreceipt1,
            @Part MultipartBody.Part photoofreceipt2,
            @Part MultipartBody.Part photoofreceipt3,
            @Header("Authorization") String token);

    @Multipart
    @POST("logbookservicesviewdapi/")
    Call<AddreceiptModel> logbookservicesviewdapi4(
            @Part("notes") RequestBody notes,
            @Part("date") RequestBody date,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part MultipartBody.Part photoofreceipt,
            @Part MultipartBody.Part photoofreceipt1,
            @Part MultipartBody.Part photoofreceipt2,
            @Part MultipartBody.Part photoofreceipt3,
            @Part MultipartBody.Part photoofreceipt4,
            @Header("Authorization") String token);



    @Multipart
    @POST("logbookserviceseditapi/")
    Call<LogBookServiceModel> logbookserviceseditapi(
            @Part("logbookservicesid") RequestBody logbookservicesid,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,
            @Part("current_km") RequestBody current_km,
            @Part("next_service") RequestBody next_service,
            @Part("notes") RequestBody notes,
            @Part("servicetitle") RequestBody servicetitle,
            @Part("date") RequestBody date,
            @Part("servicecompany") RequestBody servicecompany,
            @Part("minorormajorservice") RequestBody minorormajorservice,
            @Part("completed") RequestBody completed,
            @Part("replace") RequestBody replace,
            @Part MultipartBody.Part serviceimage, @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);


    @Multipart
    @POST("logbookserviceseditapi/")
    Call<AddreceiptModel> logbookserviceseditapi2(
            @Part("logbookservicesid") RequestBody logbookservicesid,
            @Part("user") RequestBody user,
            @Part("vehicle") RequestBody vehicle,

            @Part("notes") RequestBody notes,

            @Part("date") RequestBody date,

            @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);



    @Multipart
    @POST("maintenancerecordapi/")
    Call<MainTainRecordApiModel> maintenancerecordapi(@Part("vehicle") RequestBody vehicle,
                                                      @Part("maintenancetitle") RequestBody maintenancetitle,
                                                      @Part("quantity") RequestBody quantity,
                                                      @Part("supplier") RequestBody supplier,
                                                      @Part("condition") RequestBody condition,
                                                      @Part("description") RequestBody description,
                                                      @Part("user") RequestBody user,
                                                      @Part MultipartBody.Part maintenanceimage,
                                                      @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);

    @Multipart
    @POST("maintenancerecordeditapi/")
    Call<MainTainRecordApiModel> maintenancerecordeditapi(
            @Part("maintenancerecordid") RequestBody maintenancerecordid,
            @Part("vehicle") RequestBody vehicle,
            @Part("maintenancetitle") RequestBody maintenancetitle,
            @Part("quantity") RequestBody quantity,
            @Part("supplier") RequestBody supplier,
            @Part("condition") RequestBody condition,
            @Part("description") RequestBody description,
            @Part("user") RequestBody user,
            @Part MultipartBody.Part maintenanceimage,
            @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);


    @Multipart
    @POST("maintenancerecordeditapi/")
    Call<MainTainRecordApiModel> maintenancerecordeditapi2(
            @Part("maintenancerecordid") RequestBody maintenancerecordid,
            @Part("vehicle") RequestBody vehicle,
            @Part("maintenancetitle") RequestBody maintenancetitle,
            @Part("description") RequestBody description,
            @Part("user") RequestBody user,
            @Part MultipartBody.Part maintenanceimage,
            @Part MultipartBody.Part photoofreceipt, @Header("Authorization") String token);

    @Multipart
    @POST("marketplaceviewapi/")
    Call<DummyMarket.Datum> marketplaceviewapi(
            @Part("partname") RequestBody partname,
            @Part("partdescription") RequestBody partdescription,
            @Part("quantity") RequestBody quantity,
            @Part("makemodel") RequestBody makemodel,
            @Part("condition") RequestBody condition,
            @Part("price") RequestBody price,
            @Part("pemissiontocontact") Boolean pemissiontocontact,
            @Part("user") RequestBody user,
            @Part MultipartBody.Part partimage, @Header("Authorization") String token);

    @Multipart
    @POST("vehicleeditapi/")
    Call<VehicleEdit> edit(
            @Part("id") RequestBody id,
            @Part("vehicle_regnumber") RequestBody vehicle_regnumber,
            @Part("reg_due_date") RequestBody reg_due_date,
            @Part("current_km") RequestBody current_km,
            @Part("insurance_date") RequestBody insurance_date,
             @Header("Authorization") String token);


    @FormUrlEncoded
    @POST("vehicleeditapi/")
    Call<VehicleEdit> edit1(@Field("vehicle_regnumber") String vehicle_regnumber,
                               @Field("reg_due_date") String reg_due_date,
                               @Field("current_km") String current_km,
                                @Field("id") String id
                            );

    @FormUrlEncoded
    @POST("product_sold/")
    Call<Product_sold_Model> soldItem(@Field("product_id") String product_id,
                                      @Field("user_id") String user_id,
                                      @Header("Authorization") String token);
}
