package com.developers.vmrapp.service;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://63.32.172.136:7007/";

    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    public static String token;


    private RetrofitClient() {


        Log.i("Milk Calendar token", "token = "+token);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Log.i("AccessToken", "Token " + token);
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Token " + token)//"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiMGJjMDYwNTcxYzU3Y2JiYzMxYmM1Y2VlMDJiMjJhYTk5NWEwYjk3NzRjZmE1ODg1NzI5NmUwNzQ0MTdkMWVkMWQzYmViYTI3ZjhhNzJiNTEiLCJpYXQiOjE2MTg1NzQwMzcuMzE4ODcyLCJuYmYiOjE2MTg1NzQwMzcuMzE4ODc1LCJleHAiOjE2NTAxMTAwMzcuMzE3MzIsInN1YiI6IjU5Iiwic2NvcGVzIjpbXX0.ak9CkF_tPbr4ggY-DP9cmOERZPi1GvenERbssaNjz9V-t16PkZEdXmVtBOBy2lZH9ybqUOU5GBHsfmRCo1yT4CiQ--GZX26d9DLjhqKtIZqiaylLbPNjyWnSbE35OsavEQAUywUleAXmskYMJPAJOh7PIEMpUtuuH2kAlcOcSWtGiwxHq5CsMkxzrrcJkW50POW8mvlHF7NZ7oYSiShn7zpZ9QspaF23r1SyaCTGqi-Sh3VciYIjWbKUH8WcF5ob_3u9KUIvvJZetIXrTqBvM3Jn6Zfl_uZB3i7uLzD88AKfwazTdFurHO6x2rFvhoPnXn2W4-D5c5GZC3Lea1XMIhIaO5985TJUfhAZ2IpZT-7V1w_wyqdYCVVxLECSeryP1OhQUMCdKVaSW6VHO8n04-RldNC-L8asmfybOAb2EDWNio_ihfhHrhSPMtoxdhjBXPB52Wy-5QPubyywj6z4E_4XF63pQmumJq4yPeJwdB90wzeFUOSYEQwbKkndoXShi6ZFGsXB4kwLG3CgADq1MIPRndeDfB1kvGQRDYr3WycCBrizFXSa3JHLlwjHhUBlcwefJNVd1EXiQn0KGUpPOj4gufVg9hrYb74knWrH3EfUeRzfccJ8hEPXTHnePVjHjpOULE28fpxlovcQt_K5H98Mm1jnkUnYW5RJdckXfW4")
                                .build();
                        Log.i("Request", newRequest.toString());
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }

}