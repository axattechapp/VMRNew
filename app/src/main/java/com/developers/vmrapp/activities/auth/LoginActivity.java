package com.developers.vmrapp.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginResult;
import com.developers.vmrapp.Util;
import com.developers.vmrapp.activities.PaymentActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.activities.MainActivity;
import com.developers.vmrapp.activities.forgotpass.ForgotPassword;
import com.developers.vmrapp.databinding.ActivityLoginBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.LoginModel;
import com.developers.vmrapp.service.Api;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>{

    private ActivityLoginBinding binding;
    OkHttpClient client;
    Gson gson;
    Api api;
    Retrofit retrofit;
    private CallbackManager callbackManager;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding
                .inflate(getLayoutInflater());
//        binding.loginButton.setReadPermissions("email");
         //If using in a fragment
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
      //   Callback registration
//        binding.loginButton.registerCallback(callbackManager,this);
//        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                Log.e("Logged in","Logged in successfully");
//            }
//            @Override
//            public void onCancel() {
//                // App code
//            }
//            @Override
//            public void onError(FacebookException exception) {
//                Log.e("Logged in","Error");
//                // App code
//            }
//        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //start new activity
                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                Log.e("Logged in","Logged in successfully");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        Util.keyhashes(this);
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

        initClickListeners();
        binding.signupFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

    }


    private void initClickListeners() {

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.loginButton1.setOnClickListener(v -> gotoMainActivity());

        binding.resetText.setOnClickListener(v -> gotoForgotPass());

    }

    private void gotoForgotPass() {
        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivity(intent);
    }

    private void gotoMainActivity() {
        Log.d("textinput", binding.loginPasswordEdit.getText() + "");
        Log.e("FCMToken", token + "");

        if (!binding.loginEmailEdit.getText().toString().trim().isEmpty()) {
            binding.loginEmailInput.setError(null);
            if (!binding.loginPasswordEdit.getText().toString().trim().isEmpty()) {
                binding.loginPasswordInput.setError(null);

                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage("Loading ...");
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();


                SharedPreferences editor = getSharedPreferences("fcm_token", MODE_PRIVATE);


                JsonObject object = new JsonObject();
                object.addProperty("email", Objects.requireNonNull(binding.loginEmailEdit.getText()).toString().trim());
                object.addProperty("password", binding.loginPasswordEdit.getText().toString().trim());
                object.addProperty("device_id",token);
                object.addProperty("device_type","android" );

                Call<LoginModel> call = api.login(object);
                call.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        pd.dismiss();
                        if (response.code() == 200) {

                            LoginModel model = response.body();
                            if (model.isPayment()==false)
                            {
                                ToastUtility.errorToast(LoginActivity.this, "Your payment was not Successful, Please try signing up.");
//                                Toast.makeText(LoginActivity.this, "Your Payment not succeed", Toast.LENGTH_SHORT).show();

                            }else {

                                SharedPreferences.Editor editor = getSharedPreferences("my_token", MODE_PRIVATE).edit();

                                editor.putString("access_token", model.token);
                                editor.putString("userId", model.user.id.toString());
                                editor.putString("username", model.user.name);
                                editor.putString("email", model.user.email);
                                editor.apply();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                                ToastUtility.successToast(LoginActivity.this, "Successfull login");
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());

                                ToastUtility.errorToast(LoginActivity.this, "Error : " + jObjError.getString("msg") + " " + response.code());

                            } catch (Exception e) {
                                ToastUtility.errorToast(LoginActivity.this, e.getMessage());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        pd.dismiss();
                        ToastUtility.errorToast(LoginActivity.this, t.getMessage());

                    }
                });

            } else {
                binding.loginPasswordInput.setError("Enter password");
            }

        } else {
            binding.loginEmailInput.setError("Please enter email");
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(@NonNull FacebookException e) {

    }

    @Override
    public void onSuccess(LoginResult loginResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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