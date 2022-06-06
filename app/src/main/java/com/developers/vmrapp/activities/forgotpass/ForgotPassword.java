package com.developers.vmrapp.activities.forgotpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.activities.auth.LoginActivity;
import com.developers.vmrapp.databinding.ActivityForgotPasswordBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.service.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;
    Call<JsonObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.MINUTES)
                .writeTimeout(8, TimeUnit.MINUTES)
                .readTimeout(8, TimeUnit.MINUTES)
                .build();

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(Api.class);

        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.sendResetlink.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.loginEmailEdit.getText().toString())) {
                binding.loginEmailInput.setError(null);

                JsonObject object = new JsonObject();
                object.addProperty("email", binding.loginEmailEdit.getText().toString().trim());

                ProgressDialog pd = new ProgressDialog(ForgotPassword.this);
                pd.setMessage("Loading ...");
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();

                call = api.forgotpassword(object);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        pd.dismiss();
                        if (response.code() == 200) {
                            try {
                                JSONObject object1 = new JSONObject(response.body().toString());

                                if (!object1.optBoolean("error")) {
                                    Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                                    startActivity(i);
                                    ForgotPassword.this.finish();
                                    ToastUtility.successToast(ForgotPassword.this, object1.optString("result"));
                                }
                                else {
                                    ToastUtility.errorToast(ForgotPassword.this, object1.optString("result"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            ToastUtility.errorToast(ForgotPassword.this, "Unable to connect : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        pd.dismiss();
                        ToastUtility.errorToast(ForgotPassword.this, t.getMessage());
                    }
                });

            } else {
                binding.loginEmailInput.setError("Email can not be empty.");
            }

        });

    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }

    }

}