package com.developers.vmrapp.activities;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.developers.vmrapp.R;
import com.developers.vmrapp.activities.auth.LoginActivity;
import com.developers.vmrapp.databinding.ActivityPaymentBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.PlanModel;
import com.developers.vmrapp.service.Api;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;

    ProgressDialog pd;

    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;

    String unpaid_count;
    int count = 0;

    String year;
    String monthNumber;
    SharedPreferences pref;

    int plan = 0;

    Call<PlanModel> planModelCall;

    PlanModel model;

    String flow_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = getSharedPreferences("my_token", MODE_PRIVATE);


        unpaid_count = getIntent().getStringExtra("unpaid_count");
        flow_type = getIntent().getStringExtra("flow_type");

        Log.e("unpsid", unpaid_count);

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


        ProgressDialog pd_ = new ProgressDialog(this);
        pd_.setMessage("Loading ...");
        pd_.setIndeterminate(true);
        pd_.setCancelable(false);
        pd_.show();


        planModelCall = api.plansdata("Token " + pref.getString("access_token", ""));


        planModelCall.enqueue(new Callback<PlanModel>() {
            @Override
            public void onResponse(Call<PlanModel> call, Response<PlanModel> response) {

                pd_.dismiss();
                if (response.code() == 200) {

                    model = response.body();

                    if (unpaid_count.equals("3")) {
                        plan = model.data.get(2).id;
                        binding.singleCarPrice.setVisibility(View.GONE);
                        binding.twoCarPrice.setVisibility(View.GONE);
                    } else if (unpaid_count.equals("2")) {
                        plan = model.data.get(1).id;
                        binding.singleCarPrice.setVisibility(View.GONE);
                    } else {
                        plan = model.data.get(0).id;
                    }

                    binding.singleCarPrice.setTag(model.data.get(0).id);
                    binding.singleCarPrice.setText(model.data.get(0).cars + " " + "Car - $" + model.data.get(0).price + "/" + model.data.get(0).plan_duration);

                    binding.twoCarPrice.setTag(model.data.get(1).id);
                    binding.twoCarPrice.setText(model.data.get(1).cars + " " + "Car - $" + model.data.get(1).price + "/" + model.data.get(1).plan_duration);

                    binding.multiCarPrice.setTag(model.data.get(2).id);
                    binding.multiCarPrice.setText("3+ " + "Car - $" + model.data.get(2).price + "/" + model.data.get(2).plan_duration);


                } else {
                    try {
                        ToastUtility.errorToast(PaymentActivity.this, new Gson().toJson(response.errorBody().string().replace("\"","")) + " error code -" + response.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PlanModel> call, Throwable t) {
                pd_.dismiss();
                ToastUtility.errorToast(PaymentActivity.this, t.getMessage());
            }
        });


        binding.cardNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputlength = binding.cardNumberEdit.getText().toString().length();

                if (count <= inputlength && (inputlength == 4 ||
                        inputlength == 9 || inputlength == 14)) {

                    binding.cardNumberEdit.setText(binding.cardNumberEdit.getText().toString() + " ");

                    int pos = binding.cardNumberEdit.getText().length();
                    binding.cardNumberEdit.setSelection(pos);

                } else if (count >= inputlength && (inputlength == 4 ||
                        inputlength == 9 || inputlength == 14)) {
                    binding.cardNumberEdit.setText(binding.cardNumberEdit.getText().toString()
                            .substring(0, binding.cardNumberEdit.getText()
                                    .toString().length() - 1));

                    int pos = binding.cardNumberEdit.getText().length();
                    binding.cardNumberEdit.setSelection(pos);
                }
                count = binding.cardNumberEdit.getText().toString().length();
            }
        });


        binding.backButton.setOnClickListener(v -> {
            if (flow_type.equals("register")) {
                startActivity(new Intent(PaymentActivity.this, LoginActivity.class));
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            finishAffinity();
        });

        binding.expirydate.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
                    .curved()
                    .mustBeOnFuture()
                    .displayMinutes(false)
                    .displayHours(false)
                    .displayDays(false)
                    .displayMonth(true)
                    .displayYears(true)
                    .displayDaysOfMonth(false)
                    .titleTextColor(getResources().getColor(R.color.colorAccent))
                    .mainColor(getResources().getColor(R.color.colorAccent))
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            Log.d("apptoken", picker.getDate() + "");

                        }
                    })
                    .title("month/year")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {


                            Log.d("apptoken", date + " " + date.getTime());


                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                            String temp = "Thu Dec 17 15:37:43 GMT+05:30 2015";
                            try {
                                date = formatter.parse(String.valueOf(date));
                                monthNumber = (String) DateFormat.format("MM", date);
                                year = (String) DateFormat.format("yyyy", date);
                                binding.expirydate.setText(String.format("%s/%s", monthNumber, year));
                                binding.expirydate.setTextColor(getResources().getColor(R.color.colorBlack));

                                Log.e("formated date ", date + "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .display();
        });

        binding.nextButton.setOnClickListener(v -> {


            Log.d("stripe_token", binding.cardNumberEdit.getText().toString().replace(" ", "").trim());

            if (!binding.cardNumberEdit.getText().toString().isEmpty() && !binding.nameEdit.getText().toString().isEmpty() && !monthNumber.isEmpty() && !binding.carcvv.getText().toString().isEmpty()) {

                pd = new ProgressDialog(this);
                pd.setMessage("Loading ...");
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();

                Card cardModel = new Card(binding.cardNumberEdit.getText().toString().replace(" ", "").trim(), parseInt(monthNumber), parseInt(year), binding.carcvv.getText().toString().trim());


                new Stripe(this).createToken(cardModel, Api.Public_key, new TokenCallback() {
                    @Override
                    public void onSuccess(@NonNull Token result) {
                        Log.e("stripe_token", result.getId());

                        paymentdone(result.getId());
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(PaymentActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                Toast.makeText(PaymentActivity.this, "Pls Fill All the Fields", Toast.LENGTH_SHORT).show();

            }

        });

        binding.singleCarPrice.setTextColor(getResources().getColor(R.color.colorWhite));
        binding.singleCarPrice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.singleCarPrice.setOnClickListener(v -> {

            plan = Integer.parseInt(String.valueOf(model.data.get(0).id));

            binding.singleCarPrice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.twoCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));
            binding.multiCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));

            binding.singleCarPrice.setTextColor(getResources().getColor(R.color.colorWhite));
            binding.twoCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.multiCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
        });

        binding.twoCarPrice.setOnClickListener(v -> {
            plan = Integer.parseInt(String.valueOf(model.data.get(0).id));
            binding.singleCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));
            binding.twoCarPrice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.multiCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));

            binding.singleCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.twoCarPrice.setTextColor(getResources().getColor(R.color.colorWhite));
            binding.multiCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
        });

        binding.multiCarPrice.setOnClickListener(v -> {

            plan = Integer.parseInt(String.valueOf(model.data.get(0).id));
            binding.singleCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));
            binding.twoCarPrice.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timedatebg, null));
            binding.multiCarPrice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            binding.singleCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.twoCarPrice.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.multiCarPrice.setTextColor(getResources().getColor(R.color.colorWhite));
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flow_type.equals("register")) {
            startActivity(new Intent(PaymentActivity.this, LoginActivity.class));
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        finishAffinity();
    }

    private void paymentdone(String id) {

        JsonObject object = new JsonObject();
        object.addProperty("tokenId", id);
        object.addProperty("email", pref.getString("email", ""));
        object.addProperty("plan", plan);


        Call<JsonElement> call = api.paymentapi(object, "Token " + pref.getString("access_token", ""));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                    finishAffinity();

                    ToastUtility.successToast(PaymentActivity.this, response.body().toString());

                } else {
                    ToastUtility.errorToast(PaymentActivity.this, response.message() + response.code());

//                    try {
//                        assert response.errorBody() != null;
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//                        ToastUtility.errorToast(PaymentActivity.this, "Error : " + jObjError.getString("msg") + " " + response.code());
//
//                    } catch (Exception e) {
//                        ToastUtility.errorToast(PaymentActivity.this, e.getMessage() + response.code());
//
//                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(PaymentActivity.this, t.getMessage());
            }
        });


    }
}