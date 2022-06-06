package com.developers.vmrapp.fragments.vehicle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.developers.vmrapp.activities.MainActivity;
import com.developers.vmrapp.activities.auth.LoginActivity;
import com.developers.vmrapp.fragments.main.HomeFragment;
import com.developers.vmrapp.models.DummyMarket;
import com.developers.vmrapp.models.LoginModel;
import com.developers.vmrapp.models.VehicleEdit;
import com.developers.vmrapp.models.VehicleHistoryModel2;
import com.developers.vmrapp.service.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.R;
import com.developers.vmrapp.adapters.HistoryAdapter;
import com.developers.vmrapp.databinding.FragmentViewVehicleBinding;
import com.developers.vmrapp.helper.Kilometer;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.DummyHistory;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.models.VehicleHistoryapiModel;
import com.developers.vmrapp.service.Api;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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

import static android.content.Context.MODE_PRIVATE;


public class ViewVehicleFragment extends Fragment implements HistoryAdapter.OnHistoryClickListener {

    private Context context;
    private NavController navController;

    private FragmentViewVehicleBinding binding;

    private HistoryAdapter historyAdapter;

    private final List<DummyHistory> historyModelList = new ArrayList<>();

    Call<VehicleHistoryModel2> call;
    Call<VehicleEdit> call1;
    SharedPreferences editor;
    HomeVehicleDetailModel.Datum data;
    Api api;
    int vehicleSeviceProgress, RegistrainProgress, InsuranceProgress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
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

        api = retrofit.create(Api.class);


        data = (HomeVehicleDetailModel.Datum) getArguments().getSerializable("HomeVehicleDetailModel");
        int id = data.getId();
        Log.e("VehicleNumber2", "v" + data.getVehicleRegnumber());
        JsonObject object = new JsonObject();
        object.addProperty("vehicleid", id);
        call = api.vehiclehistoryapi2(String.valueOf(id), "Token " + editor.getString("access_token", ""));
        Log.e("idid", "" + id);

        binding = FragmentViewVehicleBinding
                .inflate(inflater, container, false);


        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.vehicleNumber.setEnabled(true);
                binding.vehicleNumber.setFocusable(true);
                binding.vehicleNumber.requestFocus();
                binding.vehicleNumber.setBackgroundResource(R.drawable.edit_text_design);


                binding.registrationDateText.setEnabled(true);
                binding.registrationDateText.setBackgroundResource(R.drawable.edit_text_design_white);

                binding.vehicleKilometer.setEnabled(true);
                binding.vehicleKilometer.setBackgroundResource(R.drawable.edit_text_design);

                binding.insuranceDateText.setEnabled(true);
                binding.insuranceDateText.setBackgroundResource(R.drawable.edit_text_design_white);

                binding.addNewRecordButton.setText("Save");
//                Toast.makeText(binding.addNewRecordButton.getContext(), "Please click on Edit Record button", Toast.LENGTH_SHORT).show();
            }
        });
        binding.vehicleImage.setBackgroundResource(R.drawable.white_img);
        return binding.getRoot();
    }

    VehicleHistoryModel2 model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        ProgressDialog pd = new ProgressDialog(requireContext());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();


        call.enqueue(new Callback<VehicleHistoryModel2>() {
            @Override
            public void onResponse(Call<VehicleHistoryModel2> call, Response<VehicleHistoryModel2> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    model = response.body();
                    historyAdapter = new HistoryAdapter(getContext(), model, ViewVehicleFragment.this);

                    Log.d("modelsize", String.valueOf(model.getData().size()));

                    binding.historyRecyclerView.setAdapter(historyAdapter);

                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//                        ToastUtility.errorToast(requireContext(), "Error : " + jObjError.getString("msg") + " " + response.code());
//
//                    } catch (Exception e) {
//                        ToastUtility.errorToast(requireContext(), e.getMessage());
//
//                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleHistoryModel2> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(requireContext(), t.getMessage());
            }
        });

        initNavController();

        initClickListeners();

        context = getContext();

        setHistoryAdapter();

        setDummyData();

        setDummyHistory();

    }

    private void setHistoryAdapter() {


    }

    private void initClickListeners() {

        binding.addNewRecordButton
                .setOnClickListener(v -> gotoAddNewRecordScreen(data));

        binding.backButton.setOnClickListener(v -> gotoBackScreen());

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    private void gotoBackScreen() {

        navController.navigateUp();

    }

    private void setDummyData() {

        int progress = 70;
        String model = "2018";
        Integer kilometer = 15000;
        String serviceDate = "27/01/2020";
        String carName = "Mitsubishi Lancer";
        Integer nextServiceKilometer = 45000;
        String insuranceDate = "September 2021";
        String registrationDate = "December 2021";
        String identification = "V9829879891296";

        binding.vehicleModel.setText(data.getVehicleModel());
        binding.vehicleName.setText(data.getVehicleMake());


        if (data.getInsurance_date() != null) {
            Log.e("input date ", "" + data.getInsurance_date());
            String dateStr = data.getInsurance_date();
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf3.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf3 = new SimpleDateFormat("dd-MM-yyyy");

            dateStr = sdf3.format(date);
            Log.e("output date ", "" + dateStr);
//        binding.insuranceDateText.setText(data.getInsurance_date());
            binding.insuranceDateText.setText(dateStr);
        } else
            binding.insuranceDateText.setText("");


        if (data.getRegDueDate() != null) {
            Log.e("getRegDueDate ", "" + data.getRegDueDate());
            String dateStr1 = data.getRegDueDate();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date1 = sdf1.parse(dateStr1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            dateStr1 = sdf1.format(date1);
            Log.e("output date ", "" + dateStr1);

//        binding.registrationDateText.setText(data.getRegDueDate());
            binding.registrationDateText.setText(dateStr1);
        }
//        binding.vehicleNumber.setText(data.getVehicleRegnumber());

        Log.e("vehicleNumber", "v" + data.getVehicleRegnumber());
        Log.e("registrationDateText", "v" + data.getRegDueDate());


        binding.serviceProgress.setProgress(progress);
        binding.vehicleNumber.setText(data.getVehicleRegnumber());

        Calendar cal = Calendar.getInstance();
        Calendar calender = Calendar.getInstance();
        Calendar regDueDatecal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        try {
            if (data.getNextService() != null) {

                String datestart = data.getNextService();

                cal.setTime(sdf.parse(datestart));// all done

                Log.e("cal", "c" + cal);
                Calendar cal1 = Calendar.getInstance();
                String formatted = sdf.format(cal1.getTime());//formatted date as i want
                cal1.setTime(sdf.parse(formatted));// all done

                Log.e("cal1", "c" + cal1);

                long msDiff = cal.getTimeInMillis() - cal1.getTimeInMillis();
                long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                Log.e("ServicedaysDiff", "c" + daysDiff);
//                binding.vehicleService.setText(data.getNextService());
                if (data.getNextService() != null) {
                    Log.e("dataNextService", "" + data.getNextService());
                    String dateStr = data.getNextService();
                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf3.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf3 = new SimpleDateFormat("dd-MM-yyyy");

                    dateStr = sdf3.format(date);
                    Log.e("output date ", "" + dateStr);
//        binding.insuranceDateText.setText(data.getInsurance_date());
                    binding.vehicleService.setText(dateStr);
                }


                if (daysDiff < 100) {
                    if (daysDiff == 0) {
                        vehicleSeviceProgress = 100;

                    } else if (daysDiff < 0) {
                        vehicleSeviceProgress = 100;
                    } else {
                        vehicleSeviceProgress = (int) (100 - daysDiff);
                    }


                } else {
                    vehicleSeviceProgress = 5;
                }


//                binding.nextServiceHard.setText(data.nextService);

                if (daysDiff < 0) {
                    binding.serviceProgress.setIndicatorColor(Color.RED);
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                } else if (daysDiff == 0) {
                    binding.serviceProgress.setIndicatorColor(Color.RED);
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                } else if (daysDiff > 0 && daysDiff < 33) {
                    binding.serviceProgress.setIndicatorColor(Color.RED);
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                } else if (daysDiff >= 33 && daysDiff < 66) {
                    binding.serviceProgress.setIndicatorColor(context.getResources().getColor(R.color.orange));
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                } else if (daysDiff >= 66 && daysDiff < 100) {
                    binding.serviceProgress.setIndicatorColor(Color.GREEN);
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                } else {
//                    holder.binding.serviceProgress.setIndicatorColor(context.getResources().getColor(R.color.gainsboro));
                    binding.serviceProgress.setIndicatorColor(Color.GREEN);
                    binding.serviceProgress.setProgress((int) vehicleSeviceProgress);
                }

//                Toast.makeText(context, "days=" + daysDiff, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e("registrationDate", "r" + data.getRegDueDate());
        Log.e("Insurance_date", "r" + data.getInsurance_date());
//        binding.insuranceDateText.setText(insuranceDate);


        try {
            if (data.getRegDueDate() != null) {

                String regDueDate_datestart = data.getRegDueDate();

                cal.setTime(sdf.parse(regDueDate_datestart));// all done

                Log.e("cal", "c" + cal);
                Calendar regDueDate_cal1 = Calendar.getInstance();
                String formatted = sdf.format(regDueDate_cal1.getTime());//formatted date as i want
                regDueDate_cal1.setTime(sdf.parse(formatted));// all done

                Log.e("cal1", "c" + regDueDate_cal1);

                long regDueDate_msDiff = cal.getTimeInMillis() - regDueDate_cal1.getTimeInMillis();
                long regDueDate_daysDiff = TimeUnit.MILLISECONDS.toDays(regDueDate_msDiff);

                Log.e("RegisdaysDiff", "c" + regDueDate_daysDiff);
//                binding.registrationDateText.setText(data.getRegDueDate());
//                binding.nextServiceHard.setText(data.nextService);
                if (regDueDate_daysDiff < 100) {
                    if (regDueDate_daysDiff == 0) {
                        RegistrainProgress = 100;

                    } else if (regDueDate_daysDiff < 0) {
                        RegistrainProgress = 100;
                    } else {
                        RegistrainProgress = (int) (100 - regDueDate_daysDiff);
                    }

                } else {
                    RegistrainProgress = 5;
                }

                if (regDueDate_daysDiff < 0) {
                    binding.registrationProgress.setIndicatorColor(Color.RED);
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                } else if (regDueDate_daysDiff == 0) {
                    binding.registrationProgress.setIndicatorColor(Color.RED);
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                } else if (regDueDate_daysDiff > 0 && regDueDate_daysDiff < 33) {
                    binding.registrationProgress.setIndicatorColor(Color.RED);
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                } else if (regDueDate_daysDiff >= 33 && regDueDate_daysDiff < 66) {
                    binding.registrationProgress.setIndicatorColor(context.getResources().getColor(R.color.orange));
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                } else if (regDueDate_daysDiff >= 66 && regDueDate_daysDiff < 100) {
                    binding.registrationProgress.setIndicatorColor(Color.GREEN);
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                } else {
                    binding.registrationProgress.setIndicatorColor(Color.GREEN);
                    binding.registrationProgress.setProgress((int) RegistrainProgress);
                }

//                Toast.makeText(context, "days=" + daysDiff, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if (data.getInsurance_date() != null) {

                String Insurance_date_datestart = data.getInsurance_date();

                calender.setTime(sdf.parse(Insurance_date_datestart));// all done

                Log.e("cal", "c" + calender);
                Calendar regDueDate_cal1 = Calendar.getInstance();
                String formatted = sdf.format(regDueDate_cal1.getTime());//formatted date as i want
                regDueDate_cal1.setTime(sdf.parse(formatted));// all done

                Log.e("cal1", "c" + regDueDate_cal1);

                long regDueDate_msDiff1 = calender.getTimeInMillis() - regDueDate_cal1.getTimeInMillis();
                long regDueDate_daysDiff1 = TimeUnit.MILLISECONDS.toDays(regDueDate_msDiff1);

                Log.e("InsudaysDiff", "c" + regDueDate_daysDiff1);
//                binding.insuranceDateText.setText(data.getInsurance_date());
//                binding.nextServiceHard.setText(data.nextService);


                if (regDueDate_daysDiff1 < 100) {
                    if (regDueDate_daysDiff1 == 0) {
                        InsuranceProgress = 100;

                    } else if (regDueDate_daysDiff1 < 0) {
                        InsuranceProgress = 100;
                    } else {
                        InsuranceProgress = (int) (100 - regDueDate_daysDiff1);
                    }

                } else {
                    InsuranceProgress = 5;
                }

                if (regDueDate_daysDiff1 < 0) {
                    binding.insuranceProgress.setIndicatorColor(Color.RED);
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                } else if (regDueDate_daysDiff1 == 0) {
                    binding.insuranceProgress.setIndicatorColor(Color.RED);
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                } else if (regDueDate_daysDiff1 > 0 && regDueDate_daysDiff1 < 33) {
                    binding.insuranceProgress.setIndicatorColor(Color.RED);
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                } else if (regDueDate_daysDiff1 >= 33 && regDueDate_daysDiff1 < 66) {
                    binding.insuranceProgress.setIndicatorColor(context.getResources().getColor(R.color.orange));
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                } else if (regDueDate_daysDiff1 >= 66 && regDueDate_daysDiff1 < 100) {
                    binding.insuranceProgress.setIndicatorColor(Color.GREEN);
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                } else {
                    binding.insuranceProgress.setIndicatorColor(Color.GREEN);
                    binding.insuranceProgress.setProgress((int) InsuranceProgress);
                }

//                Toast.makeText(context, "days=" + daysDiff, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        binding.registrationProgress.setProgress(progress);

//        binding.vehicleKilometer
//                .setText(Kilometer.getKilometer(Integer.valueOf(data.currentKm)));

        binding.vehicleKilometer
                .setText(data.currentKm.toString().trim());

        binding.nextServiceKilometers
                .setText(Kilometer.getKilometer(nextServiceKilometer));

//        Glide.with(context).load(Api.Image_URL + data.vehicleimage).into(binding.vehicleImage);
//        Picasso.get().load(Api.Image_URL + data.vehicleimage)
//                .into(binding.vehicleImage);

        //display thumnil then dwnl
        Glide.with(context)
                .load(Api.Image_URL + data.vehicleimage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar3.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar3.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.vehicleImage);

    }

    private void setDummyHistory() {

        historyModelList.add(new DummyHistory(R.drawable.placeholder_005, "Rear Tail Light Replacement", "10/05/2020", "Gold Coast Mechanics"));
        historyModelList.add(new DummyHistory(R.drawable.placeholder_006, "Rear Tail Light Replacement", "10/05/2020", "Gold Coast Mechanics"));
        historyModelList.add(new DummyHistory(R.drawable.placeholder_007, "Rear Tail Light Replacement", "10/05/2020", "Gold Coast Mechanics"));

//        historyAdapter.notifyDataSetChanged();

    }

    private void gotoAddNewRecordScreen(HomeVehicleDetailModel.Datum data) {
        if (binding.addNewRecordButton.getText().equals("Add New Record")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("HomeVehicleDetailModel", data);

            navController.navigate(R.id.action_navigation_view_vehicle_to_navigation_add_record, bundle);
        } else {
            Edit();
//        Edit1();
        }

    }

    @Override
    public void onHistoryClick(VehicleHistoryModel2.Datum position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VehicleHistoryDetailModel", position);

        if (position.getFlagg().equals("servicelogbookrecord")) {

            navController.navigate(R.id.action_navigation_view_vehicle_to_viewLogBookServiceRecordFragment2, bundle);

        } else {

            navController.navigate(R.id.action_navigation_view_vehicle_to_viewMaintenanceRecordFragment2, bundle);

        }
    }


    private void Edit() {
        binding.vehicleNumber.setEnabled(true);
        binding.registrationDateText.setEnabled(true);
        binding.vehicleKilometer.setEnabled(true);
        binding.insuranceDateText.setEnabled(true);


        if (!binding.registrationDateText.getText().toString().trim().equals("") || !binding.insuranceDateText.getText().toString().trim().equals("") || !binding.vehicleKilometer.getText().toString().trim().equals("")) {
            SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

            SharedPreferences editor;

            editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading ...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            String km = binding.vehicleKilometer.getText().toString().trim().replace(" KM", "");

            Log.e("edit_values", "" + km + binding.vehicleNumber.getText().toString().trim() + binding.registrationDateText.getText().toString().trim() + data.id.toString() + binding.insuranceDateText.getText().toString().trim());


            String registrationDateText = dateParser(binding.registrationDateText.getText().toString().trim());
            String insuranceDateText = dateParser(binding.insuranceDateText.getText().toString().trim());


            RequestBody vehicle_regnumber = RequestBody.create(MediaType.parse("text/plain"), binding.vehicleNumber.getText().toString().trim());
            RequestBody reg_due_date = RequestBody.create(MediaType.parse("text/plain"), registrationDateText);
            RequestBody vehicleKilometer = RequestBody.create(MediaType.parse("text/plain"), km);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), data.id.toString());
            RequestBody insurance_date = RequestBody.create(MediaType.parse("text/plain"), insuranceDateText);
            try {
                call1 = api.edit(id, vehicle_regnumber, reg_due_date, vehicleKilometer, insurance_date, "Token " + editor.getString("access_token", ""));
            } catch (Exception e) {
                Log.e("", e.toString());
            }
            call1.enqueue(new Callback<VehicleEdit>() {
                @Override
                public void onResponse(Call<VehicleEdit> call, Response<VehicleEdit> response) {
                    pd.dismiss();
                    if (response.code() == 200) {

                        binding.vehicleModel.setText(binding.vehicleModel.getText().toString());
                        binding.vehicleService.setText(binding.vehicleService.getText().toString());
                        binding.vehicleKilometer.setText(binding.vehicleKilometer.getText().toString());
                        binding.insuranceDateText.setText(binding.insuranceDateText.getText().toString());
                        binding.registrationDateText.setText(binding.registrationDateText.getText().toString());
                        ToastUtility.successToast(getActivity(), "Successfully updated");

                        navController.navigateUp();

                    } else {
                        Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
//                        try {
//                            assert response.errorBody() != null;
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//                            ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());
//
//                        } catch (Exception e) {
//                            ToastUtility.errorToast(getActivity(), e.getMessage());
//
//                        }
                    }
                }

                @Override
                public void onFailure(Call<VehicleEdit> call, Throwable t) {
                    pd.dismiss();
                    ToastUtility.errorToast(getActivity(), t.getMessage());

                }
            });

        } else {
            ToastUtility.errorToast(getActivity(), "Error : Please fill all Information");
        }

    }


    private void Edit1() {

        SharedPreferences editor;

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        RetrofitClient.token = editor.getString("access_token", "");

        String id = data.id.toString();


        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        Call<VehicleEdit> call = RetrofitClient
                .getInstance()
                .getApi()
                .edit1("EqwdW", "2022-07-02", "202", id);
        //calling the api
        call.enqueue(new Callback<VehicleEdit>() {
            @Override
            public void onResponse(Call<VehicleEdit> call, Response<VehicleEdit> response) {
                if (response.code() == 403) {
                    progressDialog.dismiss();
                    // Handle this error gracefully and return some meaningful text which I can show it to the user on screen


                } else {
                    if (response.code() == 201) {
                        Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    } else {
                        progressDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleEdit> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String dateParser(String date) {
        String parsedDate = "";
        if (!date.isEmpty()) {
//            Log.e("old date", "" + date);
//            Date initDate = null;
//            try {
//                initDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date + Calendar.getInstance());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            parsedDate = formatter.format(initDate);
//            Log.e("new date", "" + parsedDate);
//            return parsedDate;

            String startDateString = date;
            DateTimeFormatter formatter = null;
            DateTimeFormatter formatter2 = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


                formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");


                System.out.println(LocalDate.parse(startDateString, formatter).format(formatter2));
                parsedDate = LocalDate.parse(startDateString, formatter).format(formatter2);
                return parsedDate;


            }else
            {
                Toast.makeText(context, "msg"+"Your version is lower.", Toast.LENGTH_SHORT).show();
                String dateStr = date;
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date1 = null;
                try {
                    date1 = sdf1.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                dateStr = sdf1.format(date1);
                Log.e("dateParser",""+dateStr);
                return dateStr;
            }


        }
        return parsedDate;
    }

}