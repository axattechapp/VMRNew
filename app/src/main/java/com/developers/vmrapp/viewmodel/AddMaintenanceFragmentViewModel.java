package com.developers.vmrapp.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.service.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMaintenanceFragmentViewModel extends ViewModel {

    Context context;
    public AddMaintenanceFragmentViewModel(Context context) {
        this.context=context;
    }
    SharedPreferences pref = context.getSharedPreferences("my_token", MODE_PRIVATE);

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.MINUTES)
            .writeTimeout(8, TimeUnit.MINUTES)
            .readTimeout(8, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    Api api = retrofit.create(Api.class);



}
