package com.developers.vmrapp.activities.vehicle;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.activities.MainActivity;
import com.developers.vmrapp.activities.PaymentActivity;
import com.developers.vmrapp.databinding.ActivityAddSuppliersBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.GetVehicleDetailModel;
import com.developers.vmrapp.service.Api;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddSuppliersActivity extends AppCompatActivity {

    private ActivityAddSuppliersBinding binding;
    String serviceschedule = "";
    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;
    Call<JsonObject> call;
    Intent i;
    ArrayList<String> serviceKmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddSuppliersBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());




        i = getIntent();
        serviceKmList=new ArrayList<>();
        serviceKmList.add("Service Kms");
        serviceKmList.add("10,000kms");
        serviceKmList.add("15,000kms");
        serviceKmList.add("20,000kms");
        serviceKmList.add("25,000kms");
        serviceKmList.add("30,000kms");
        serviceKmList.add("35,000kms");
        serviceKmList.add("40,000kms");
        serviceKmList.add("45,000kms");
        serviceKmList.add("50,000kms");
        serviceKmList.add("55,000kms");
        serviceKmList.add("60,000kms");
        serviceKmList.add("70,000kms");
        serviceKmList.add("100,000kms");
        serviceKmList.add("150,000kms");




//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                AddSuppliersActivity.this,R.layout.srvice_km_dropdown_layout,serviceKmList){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.BLACK);
//                    tv.setAlpha(0.5F);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//
//                }
//                return view;
//            }
//        };
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.srvice_km_dropdown_layout);
//        binding.spinner.setAdapter(spinnerArrayAdapter);


//        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItemText = (String) parent.getItemAtPosition(position);
//                // If user change the default selection
//                // First item is disable and it is used for hint
//                if(position > 1){
//                    // Notify the selected item text
//////                    Toast.makeText
////                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
////                            .show();
//                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
////                    area_id = studenId.get(position-2);
////                  Toast.makeText(AddSuppliersActivity.this, "ID Province : " +binding.spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                }
//                else if (position==1)
//                {
//                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
//                    Toast.makeText(AddSuppliersActivity.this, "None Selected", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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

        initClickListeners();

        binding.vehicleServiceEdit.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
                    .mustBeOnFuture()
                    .curved()
                    .minutesStep(15)
                    .backgroundColor(Color.WHITE)
                    .mainColor(getResources().getColor(R.color.colorPrimary))
                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
                    //.displayHours(false)
                    //.displayMinutes(false)
                    .todayText("Today")
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {

                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            Log.d("datetime", picker.getDate().toString());
                        }
                    })
                    .title("Date")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {

                            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            String formateDate1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            Log.v("output date ", formateDate);
                            binding.vehicleServiceEdit.setText(formateDate1);
                            binding.vehicleServiceEdit.setTextColor(Color.BLACK);
                            serviceschedule = formateDate;
                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
                        }
                    }).display();
        });

    }

    private void initClickListeners() {

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.finishButton.setOnClickListener(v -> gotoMainActivity());

    }

    private void gotoMainActivity() {
        Log.e("vehicleSupplierEdit",""+binding.vehicleSupplierEdit.getText());
        Log.e("vehicleContactEdit",""+binding.vehicleContactEdit.getText());
        Log.e("vehicleServiceEdit",""+binding.vehicleServiceEdit.getText());
        Log.e("spinner",""+binding.vehicleServicekmEdit.getText().toString().trim());
        if (!binding.vehicleSupplierEdit.getText().toString().isEmpty()) {
            binding.vehicleSupplierInput.setError(null);
            if (!binding.vehicleContactEdit.getText().toString().isEmpty()) {
                binding.vehicleContactInput.setError(null);
                if (!binding.vehicleServiceEdit.getText().toString().isEmpty()) {
                    Log.e("vehicleSupplierEdit",""+binding.vehicleSupplierEdit.getText());
//
                    if (!serviceschedule.isEmpty()) {

                        String d="";
                        String d1="";

                        SharedPreferences pref = getSharedPreferences("my_token", MODE_PRIVATE);

                        String str =i.getStringExtra("vehicleregduedate");
                        try {
                            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(str);
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                            d=formattedDate.toString();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String str1 =i.getStringExtra("insurance_date");
                        try {
                            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(str1);
                            String formattedDate1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);

                            d1=formattedDate1.toString();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.e("insuranceDate","date"+d1);


                        RequestBody vehiclename = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehiclename"));
                        RequestBody vehiclenumber = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehiclenumber"));
                        RequestBody vehiclekelometer = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehiclekelometer"));
                        RequestBody vehiclemake = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehiclemake"));
                        RequestBody vehiclemodel = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehiclemodel"));
                        RequestBody vehicleyear = RequestBody.create(MediaType.parse("text/plain"), i.getStringExtra("vehicleyear"));
                        RequestBody vehicleregduedate = RequestBody.create(MediaType.parse("text/plain"), d);
                        RequestBody vehiclesupplier = RequestBody.create(MediaType.parse("text/plain"), binding.vehicleSupplierEdit.getText().toString().trim());
                        RequestBody vehiclecontact = RequestBody.create(MediaType.parse("text/plain"), binding.vehicleContactEdit.getText().toString().trim());
                        RequestBody servicekm = RequestBody.create(MediaType.parse("text/plain"), binding.vehicleServicekmEdit.getText().toString().trim());
                        RequestBody vehicleservice = RequestBody.create(MediaType.parse("text/plain"), serviceschedule);
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));
                        RequestBody insuranceDate = RequestBody.create(MediaType.parse("text/plain"), d1);


                        String imageurl = i.getStringExtra("imageurl");
                        Log.e("insuranceDate","date"+imageurl+""+i.getStringExtra("vehiclename")+""+i.getStringExtra("vehiclenumber")+""+i.getStringExtra("vehiclekelometer"));
                        File file = new File(imageurl);
                        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("vehicleimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


                        call = api.sendvehicledetail(vehiclenumber, vehiclename, vehiclekelometer, vehiclemake, vehiclemodel,
                                vehicleyear, vehicleregduedate, vehiclesupplier, vehiclecontact, vehicleservice, servicekm, userId,
                                imagePart,insuranceDate, "Token " + pref.getString("access_token", ""));

                        ProgressDialog pd = new ProgressDialog(this);
                        pd.setMessage("Loading ...");
                        pd.setIndeterminate(true);
                        pd.setCancelable(false);
                        pd.show();

                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(@NonNull Call<JsonObject> call, Response<JsonObject> response) {
                                pd.dismiss();
                                int unpaid_count;
                                if (response.code() == 200) {

                                    Log.e("fullresponse", new Gson().toJson(response.body()));
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        unpaid_count = jsonObject.getInt("unpaid_count");

                                        if (getIntent().getStringExtra("registertype") != null) {

                                            final Dialog dialog = new Dialog(AddSuppliersActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.addvehicledialog);

                                            TextView text = (TextView) dialog.findViewById(R.id.btn_yes);
                                            text.setOnClickListener(v -> {
                                                Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            });

                                            TextView dialogButton = (TextView) dialog.findViewById(R.id.btn_no);
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                                                    intent.putExtra("unpaid_count", "" + unpaid_count);
                                                    intent.putExtra("flow_type", "register");
                                                    startActivity(intent);
                                                    finishAffinity();
                                                    dialog.dismiss();
                                                }
                                            });

                                            dialog.show();

                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

//                                ToastUtility.successToast(AddSuppliersActivity.this, new Gson().toJson(response.body())+model.data.created);


                                } else {

                                    ToastUtility.errorToast(AddSuppliersActivity.this, "Error : " + " " + response.code());

                                    Log.e("unSuccess", new Gson().toJson(response.errorBody()));

                                    assert response.errorBody() != null;
                                    JSONObject jObjError = null;
                                    try {
                                        jObjError = new JSONObject(response.errorBody().string());
                                        ToastUtility.errorToast(AddSuppliersActivity.this, "Error : " + jObjError.getString("msg") + " " + response.code());

                                    } catch (JSONException | IOException e) {
                                        ToastUtility.errorToast(AddSuppliersActivity.this, "Exception " + e.getMessage());

                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<JsonObject> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(AddSuppliersActivity.this, t.getMessage());
                            }
                        });

                    } else {
                        ToastUtility.warningToast(this, "Select Date for service");
                    }
                } else {
                    binding.vehicleContactInput.setError("Enter Service Kms");
                }
            } else {
                binding.vehicleContactInput.setError("Enter Contact number");
            }
        } else {
            binding.vehicleSupplierInput.setError("Enter Supplier");
        }


    }
}