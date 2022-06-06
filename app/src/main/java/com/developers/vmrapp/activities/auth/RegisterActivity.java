package com.developers.vmrapp.activities.auth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.activities.vehicle.AddVehicleActivity;
import com.developers.vmrapp.databinding.ActivityRegisterBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.RegistrationModel;
import com.developers.vmrapp.service.Api;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding
                .inflate(getLayoutInflater());

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

        getToken();

        String[] array = {"NSW","QLD","NT","WA","ACT","SA","VIC","TAS"};


        ArrayAdapter<String> adapter;


        adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, array);

        binding.addStateEdit.setAdapter(adapter);

        String[] array1 = {"Female","Male"};


        ArrayAdapter<String> adapter1;


        adapter1 = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, array1);

        binding.addGenderEdit.setAdapter(adapter1);

        initClickListeners();
    }

    private void initClickListeners() {

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.nextButton.setOnClickListener(v -> gotoAddVehicleScreen());

    }

    private void gotoAddVehicleScreen() {
        Log.e("gende","g"+binding.addGenderEdit.getText().toString());
        Log.e("state","g"+binding.addStateEdit.getText().toString());


        if (!binding.registerNameEdit.getText().toString().trim().isEmpty()) {
            binding.registerNameInput.setError(null);
            if (!binding.registerEmailEdit.getText().toString().trim().isEmpty()) {
                binding.registerEmailInput.setError(null);
                if (!binding.registerEmailAgainEdit.getText().toString().trim().isEmpty()) {
                    binding.registerEmailAgainInput.setError(null);
                    if (binding.registerEmailAgainEdit.getText().toString().trim().equalsIgnoreCase(binding.registerEmailEdit.getText().toString().trim())) {
                        if (!binding.registerMobileEdit.getText().toString().trim().isEmpty()) {
                            binding.registerMobileInput.setError(null);
                            if (!binding.registerPasswordEdit.getText().toString().trim().isEmpty()) {
                                binding.registerPasswordInput.setError(null);
                                if (!binding.registerAgeEdit.getText().toString().trim().isEmpty()) {
                                    binding.registerAgeInput.setError(null);
                                    if (!binding.registerLocationEdit.getText().toString().trim().isEmpty()) {
                                        binding.registerLocationInput.setError(null);
                                        ProgressDialog pd = new ProgressDialog(this);
                                        pd.setMessage("Loading ...");
                                        pd.setIndeterminate(true);
                                        pd.setCancelable(false);
                                        pd.show();

                                        JsonObject object = new JsonObject();
                                        object.addProperty("email", binding.registerEmailEdit.getText().toString().trim());
                                        object.addProperty("password", binding.registerPasswordEdit.getText().toString().trim());
                                        object.addProperty("name", binding.registerNameEdit.getText().toString().trim());
                                        object.addProperty("login_type", "Email");
                                        object.addProperty("mobile", binding.registerMobileEdit.getText().toString().trim());
                                        object.addProperty("age", binding.registerAgeEdit.getText().toString().trim());
                                        object.addProperty("location", binding.registerLocationEdit.getText().toString().trim());
                                        object.addProperty("state", binding.addStateEdit.getText().toString().trim());
                                        object.addProperty("gender", binding.addGenderEdit.getText().toString().trim());
                                        object.addProperty("device_id", token);

                                        Call<RegistrationModel> call = api.registration(object);

                                        call.enqueue(new Callback<RegistrationModel>() {
                                            @Override
                                            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                                                pd.dismiss();
                                                if (response.isSuccessful()) {

                                                    RegistrationModel model = response.body();
                                                    if (model.getStatus() == 400)
                                                    {
                                                        ToastUtility.errorToast(RegisterActivity.this, "User already exists");
//                                                        Toast.makeText(RegisterActivity.this, "User Already Exist,Try with other email", Toast.LENGTH_SHORT).show();
                                                    }else {

                                                        SharedPreferences.Editor editor = getSharedPreferences("my_token", MODE_PRIVATE).edit();
                                                        assert model != null;
                                                        editor.putString("access_token", model.token);
                                                        editor.putString("userId", String.valueOf(model.user.id));
                                                        editor.putString("username", model.user.name);
                                                        editor.putString("email", model.user.email);
                                                        editor.apply();

                                                        Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                                                        intent.putExtra("registertype", "register");
                                                        startActivity(intent);
                                                        finish();
                                                        ToastUtility.successToast(RegisterActivity.this, "Success!");
                                                    }

                                                } else {

                                                    try {
                                                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                                                        ToastUtility.errorToast(RegisterActivity.this, "Error : " + jObjError.getString("msg") + " " + response.code());

                                                    } catch (Exception e) {
                                                        ToastUtility.errorToast(RegisterActivity.this,"error"+e.getMessage());

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RegistrationModel> call, Throwable t) {
                                                pd.dismiss();
                                                ToastUtility.errorToast(RegisterActivity.this, "failure" + t.getMessage());
                                            }
                                        });


                                    } else {
                                        binding.registerLocationInput.setError("Enter Location");
                                    }
                                } else {
                                    binding.registerAgeInput.setError("Enter Age");
                                }
                            } else {
                                binding.registerPasswordInput.setError("Enter password");
                            }

                        } else {
                            binding.registerMobileInput.setError("Enter mobile number");
                        }
                    } else {
                        binding.registerEmailAgainInput.setError("Enter same email");
                    }

                } else {
                    binding.registerEmailAgainInput.setError("Enter email again");
                }

            } else {
                binding.registerEmailInput.setError("Enter email");
            }
        } else {
            binding.registerNameInput.setError("enter name");
        }

}

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG123", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                         token = task.getResult();

                        // Log and toast
                        String msg =  token;
                        Log.e("TAG123", token);
//                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}