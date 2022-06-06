package com.developers.vmrapp.fragments.vehicle;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.ExifUtils;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentUpdateLogbookBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.AddreceiptModel;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.service.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

public class UpdateLogbookFragment extends Fragment {

    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 40;
    private NavController navController;
    private static final Integer REQUEST_SHERGIL = 1234;
    private static final Integer REQUEST_SHERGIL_PHOTOOFRECIEPT = 12345;

    private FragmentUpdateLogbookBinding binding;

    Call<AddreceiptModel> call;
    Api api;
    int position=0;
    List<Uri> image;
    List<String> filepath;
    public boolean clicked=false;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

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
        image=new ArrayList<>();
        filepath=new ArrayList<>();
        initDatePicker();

//        binding.addPhotoofrecieptItemImageView1.setImageResource(R.drawable.ic_select_photoofreciept);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }



        binding = FragmentUpdateLogbookBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                binding.logbookDateEdit.setText(date);
            }


        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeDateString(int day, int month, int year)
    {
//        return getMonthFormat(month) + " " + day + " " + year;
        if (day<10 && month<10)
        {
            return getMonthFormat(day)+"-"+getMonthFormat(month)+"-"+year;
        }else if (day<10)
        {
            return getMonthFormat(day)+"-"+month+"-"+year;
        }else if (month<10)
        {
            return day+"-"+getMonthFormat(month)+"-"+year;
        }else
            return day+"-"+month+"-"+year;

    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "01";
        if(month == 2)
            return "02";
        if(month == 3)
            return "03";
        if(month == 4)
            return "04";
        if(month == 5)
            return "05";
        if(month == 6)
            return "06";
        if(month == 7)
            return "07";
        if(month == 8)
            return "08";
        if(month == 9)
            return "09";
        if(month == 10)
            return "10";


        //default should never happen
        return "01";
    }

    public void openDatePicker()
    {
        datePickerDialog.show();
    }


    HomeVehicleDetailModel.Datum data;

    SharedPreferences pref;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


        data = (HomeVehicleDetailModel.Datum) getArguments().getSerializable("HomeVehicleDetailModel");


        Log.e("fullresponse",data.id+"");


        initNavController();
        initClickListeners();

        String[] array = {"Minor Service", "Major Service"};


        ArrayAdapter<String> adapter;


        adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, array);

//        binding.addServiceChoiceEdit.setAdapter(adapter);



    }

    String registrationdate = "";
    String nextservicedate = "";
    String addServiceDateEdit = "";

    private void initClickListeners() {



//        binding.addServiceDateEdit.setOnClickListener(v -> {
//            new SingleDateAndTimePickerDialog.Builder(requireActivity())
//                    .bottomSheet()
////                    .mustBeOnFuture()
//                    .curved()
//                    .minutesStep(15)
//                    .backgroundColor(Color.WHITE)
//                    .mainColor(getResources().getColor(R.color.colorPrimary))
//                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
//                    //.displayHours(false)
//                    //.displayMinutes(false)
//                    .todayText("Today")
//                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//
//                        @Override
//                        public void onDisplayed(SingleDateAndTimePicker picker) {
//                            Log.d("datetime", picker.getDate().toString());
//                        }
//                    })
//                    .title("Date and Time")
//                    .listener(new SingleDateAndTimePickerDialog.Listener() {
//                        @Override
//                        public void onDateSelected(Date date) {
//
//                            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//                            Log.v("output date ", formateDate);
//                            binding.addServiceDateEdit.setText(formateDate);
//                            binding.addServiceDateEdit.setTextColor(Color.BLACK);
//                            addServiceDateEdit = formateDate;
//                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
//                            Log.d("datetime1", addServiceDateEdit + " " + addServiceDateEdit);
//                        }
//                    }).display();
//        });
//

        binding.logbookDateEdit.setOnClickListener(v -> {
//            new SingleDateAndTimePickerDialog.Builder(requireActivity())
//                    .bottomSheet()
////                    .mustBeOnFuture()
//                    .curved()
//                    .minutesStep(15)
//                    .backgroundColor(Color.WHITE)
//                    .mainColor(getResources().getColor(R.color.colorPrimary))
//                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
//                    //.displayHours(false)
//                    //.displayMinutes(false)
//                    .todayText("Today")
//                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//
//                        @Override
//                        public void onDisplayed(SingleDateAndTimePicker picker) {
//                            Log.d("datetime", picker.getDate().toString());
//                        }
//                    })
//                    .title("Date and Time")
//                    .listener(new SingleDateAndTimePickerDialog.Listener() {
//                        @Override
//                        public void onDateSelected(Date date) {
//
//                            String formateDate = new SimpleDateFormat("dd-mm-yyyy").format(date);
//                            Log.v("output date ", formateDate);
//                            binding.logbookDateEdit.setText(formateDate);
//                            binding.logbookDateEdit.setTextColor(Color.BLACK);
//                            registrationdate = formateDate;
//                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
//                        }
//                    }).display();
            openDatePicker();
        });


//        binding.logbookServiceEdit.setOnClickListener(v -> {
//            new SingleDateAndTimePickerDialog.Builder(requireActivity())
//                    .bottomSheet()
////                    .mustBeOnFuture()
//                    .curved()
//                    .minutesStep(15)
//                    .backgroundColor(Color.WHITE)
//                    .mainColor(getResources().getColor(R.color.colorPrimary))
//                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
//                    //.displayHours(false)
//                    //.displayMinutes(false)
//                    .todayText("Today")
//                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//
//                        @Override
//                        public void onDisplayed(SingleDateAndTimePicker picker) {
//                            Log.d("datetime", picker.getDate().toString());
//                        }
//                    })
//                    .title("Date and Time")
//                    .listener(new SingleDateAndTimePickerDialog.Listener() {
//                        @Override
//                        public void onDateSelected(Date date) {
//
//                            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//                            Log.v("output date ", formateDate);
//                            binding.logbookServiceEdit.setText(formateDate);
//                            binding.logbookServiceEdit.setTextColor(Color.BLACK);
//                            nextservicedate = formateDate;
//                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
//                        }
//                    }).display();
//        });

        binding.backButton.setOnClickListener(v -> gotoBackScreen());
//        binding.logbookItemImageView.setOnClickListener(v -> selectvehicleimage());
        binding.postButton.setOnClickListener(v -> performpostoperation1());
        binding.addPhotoofrecieptItemImageView1.setOnClickListener(v -> selectphotoofrecieptimage());


        binding.addPhotoofrecieptItemImageView1.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(binding.getRoot().getContext());
//                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                myView.setLayoutParams(new ImageSwitcher.LayoutParams(
//                        ImageSwitcher.LayoutParams.MATCH_PARENT,
//                        ImageSwitcher.LayoutParams.MATCH_PARENT));
                return myView;
            }
        });

        binding.preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position>0)
                {
                    position--;
                    binding.addPhotoofrecieptItemImageView1.setImageURI(image.get(position));
                }else
                    Toast.makeText(binding.getRoot().getContext(), "No image", Toast.LENGTH_SHORT).show();

            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position<image.size()-1)
                {
                    position++;
                    binding.addPhotoofrecieptItemImageView1.setImageURI(image.get(position));
                }else
                    Toast.makeText(binding.getRoot().getContext(), "No image", Toast.LENGTH_SHORT).show();


            }
        });
    }

    String Photoofreciepturl = "";




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.d("imageurl", filepath + " ");

            binding.logbookItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            imageurl = filepath;

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL_PHOTOOFRECIEPT) {
             image = Shergil.Companion.getMediaUris(data);

            Log.e("images", "size"+image.size());

            for (int i=0;i<image.size();i++)
            {

                filepath.add(getRealPathFromUri(requireActivity(), Uri.parse(image.get(i).toString())));
                Log.e("imageurl_filepath"+i, filepath.get(i) + " ");
            }
            Log.e("imageurl_filepath", filepath.size() + " ");
            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


//            Log.d("imageurl", filepath + " ");
            binding.addPhotoofrecieptItemImageView1.setBackground(null);

            binding.addPhotoofrecieptItemImageView1.setImageURI(image.get(0));
            position=0;

//            binding.addPhotoofrecieptItemImageView1.setImageBitmap(BitmapFactory.decodeFile(filepath));

            Photoofreciepturl = filepath;

        }
    }


    private void selectvehicleimage() {
        Shergil.Companion.create(this)
                .mimeTypes(MimeType.Companion.getIMAGES())
                .showDisallowedMimeTypes(false)
                .numOfColumns(2)
                .maxSelectable(1)
                .theme(R.style.Shergil)
                .allowPreview(true)
                .allowCamera(true)
                .showDeviceCamera(false)
                .showCameraFirst(false)
                .orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .withRequestCode(REQUEST_SHERGIL);

    }

    private void selectphotoofrecieptimage() {
        clicked=true;
        Shergil.Companion.create(this)
                .mimeTypes(MimeType.Companion.getIMAGES())
                .showDisallowedMimeTypes(false)
                .numOfColumns(2)
                .maxSelectable(5)
                .theme(R.style.Shergil)
                .allowPreview(true)
                .allowCamera(true)
                .showDeviceCamera(false)
                .showCameraFirst(false)
                .orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .withRequestCode(REQUEST_SHERGIL_PHOTOOFRECIEPT);

    }
    private void performpostoperation1()
    {
        Log.e("logbookDateEdit", "" + binding.logbookDateEdit.getText());
        Log.e("logbookNotesEdit", "" + binding.logbookNotesEdit.getText());


        if (!binding.logbookDateEdit.getText().toString().equals("Date")){

            if (!binding.logbookNotesEdit.getText().toString().isEmpty()) {
                binding.logbookNotesEdit.setError(null);
                if (image.size()!=0) {


                    Log.e("user_id", "" + pref.getString("userId", ""));
                    Log.e("vehicle_id", "" + data.id.toString());
                    Log.e("filepath_size", "" + filepath.size());
                    Log.e("img_size", "" + image.size());

//                    Toast.makeText(binding.getRoot().getContext(), "" + image.size(), Toast.LENGTH_SHORT).show();


                    SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
                    RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), data.id.toString());
//        RequestBody addServiceTitleEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceTitleEdit.getText().toString().trim());
////                                                        RequestBody addServiceDateEdit_ = RequestBody.create(MediaType.parse("text/plain"), addServiceDateEdit);
//        RequestBody addServiceCompanyEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompanyEdit.getText().toString().trim());
//        RequestBody addServiceChoiceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceChoiceEdit.getText().toString().trim());
//        RequestBody addServiceCompleteEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompleteEdit.getText().toString().trim());
//        RequestBody addServiceReplaceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceReplaceEdit.getText().toString().trim());
//        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));


                    ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage("Loading ...");
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.show();


//                                                            SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
//                                                            RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), data.id.toString());
//        RequestBody vehiclecurrentkm = RequestBody.create(MediaType.parse("text/plain"), binding.logbookVehicleKmEdit.getText().toString().trim());
                    RequestBody registerdate = RequestBody.create(MediaType.parse("text/plain"), binding.logbookDateEdit.getText().toString());
//        RequestBody vehicleservice = RequestBody.create(MediaType.parse("text/plain"), nextservicedate);
//                                                            RequestBody company = RequestBody.create(MediaType.parse("text/plain"), binding.logbookCompanyEdit.getText().toString().trim());
                    RequestBody note = RequestBody.create(MediaType.parse("text/plain"), binding.logbookNotesEdit.getText().toString().trim());
                    RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));

                    Log.e("note",""+binding.logbookNotesEdit.getText().toString().trim());
                    Log.e("date",""+binding.logbookDateEdit.getText().toString().trim());
                    Log.e("userId",""+pref.getString("userId", ""));
                    Log.e("vehicle_id",""+data.id.toString());

//        File file = new File(imageurl);
//        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("serviceimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                    if (image.size() == 1) {
                        File file1 = new File(filepath.get(0).toString());
                        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));


//                                                call = api.logbookservicesviewdapi(userId, vehicleid, vehiclecurrentkm, registerdate, note, addServiceTitleEdit, registerdate, addServiceCompanyEdit, addServiceChoiceEdit,addServiceCompleteEdit, addServiceReplaceEdit, imagePart, imagePart1, "Token " + pref.getString("access_token", ""));
                        call = api.logbookservicesviewdapi(note, registerdate, userId, vehicleid, imagePart1, "Token " + pref.getString("access_token", ""));

                        call.enqueue(new Callback<AddreceiptModel>() {
                            @Override
                            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
//                                                            Log.d("fullresponse", new Gson().toJson(response.body()));
//                                                                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                                    AddreceiptModel model = response.body();

                                    navController.navigateUp();
                                    ToastUtility.successToast(getActivity(), "" + model.getMsg());

                                } else {
                                    Toast.makeText(binding.getRoot().getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(getActivity(), t.getMessage());
                            }
                        });
                    }

                    if (image.size() == 2) {
                        File file1 = new File(filepath.get(0).toString());
                        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

                        File file2 = new File(filepath.get(1).toString());
                        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("photoofreceipt2", file2.getName(), RequestBody.create(MediaType.parse("image/*"), file2));


//                                                call = api.logbookservicesviewdapi(userId, vehicleid, vehiclecurrentkm, registerdate, note, addServiceTitleEdit, registerdate, addServiceCompanyEdit, addServiceChoiceEdit,addServiceCompleteEdit, addServiceReplaceEdit, imagePart, imagePart1, "Token " + pref.getString("access_token", ""));
                        call = api.logbookservicesviewdapi1(note, registerdate, userId, vehicleid, imagePart1, imagePart2, "Token " + pref.getString("access_token", ""));

                        call.enqueue(new Callback<AddreceiptModel>() {
                            @Override
                            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
//                                                            Log.d("fullresponse", new Gson().toJson(response.body()));
//                                                                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                                    AddreceiptModel model = response.body();

                                    navController.navigateUp();
                                    ToastUtility.successToast(getActivity(), "" + model.getMsg());

                                } else {
                                    try {
//                                                                    assert response.errorBody() != null;
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                                    } catch (Exception e) {
                                        ToastUtility.errorToast(getActivity(), e.getMessage());

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(getActivity(), t.getMessage());
                            }
                        });
                    }
                    if (image.size() == 3) {
                        File file1 = new File(filepath.get(0).toString());
                        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

                        File file2 = new File(filepath.get(1).toString());
                        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("photoofreceipt2", file2.getName(), RequestBody.create(MediaType.parse("image/*"), file2));

                        File file3 = new File(filepath.get(2).toString());
                        MultipartBody.Part imagePart3 = MultipartBody.Part.createFormData("photoofreceipt3", file3.getName(), RequestBody.create(MediaType.parse("image/*"), file3));

//                                                call = api.logbookservicesviewdapi(userId, vehicleid, vehiclecurrentkm, registerdate, note, addServiceTitleEdit, registerdate, addServiceCompanyEdit, addServiceChoiceEdit,addServiceCompleteEdit, addServiceReplaceEdit, imagePart, imagePart1, "Token " + pref.getString("access_token", ""));
                        call = api.logbookservicesviewdapi2(note, registerdate, userId, vehicleid, imagePart1, imagePart2, imagePart3, "Token " + pref.getString("access_token", ""));

                        call.enqueue(new Callback<AddreceiptModel>() {
                            @Override
                            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
//                                                            Log.d("fullresponse", new Gson().toJson(response.body()));
//                                                                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                                    AddreceiptModel model = response.body();

                                    navController.navigateUp();
                                    ToastUtility.successToast(getActivity(), "" + model.getMsg());

                                } else {
                                    try {
//                                                                    assert response.errorBody() != null;
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                                    } catch (Exception e) {
                                        ToastUtility.errorToast(getActivity(), e.getMessage());

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(getActivity(), t.getMessage());
                            }
                        });
                    }

                    if (image.size() == 4) {
                        File file1 = new File(filepath.get(0).toString());
                        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

                        File file2 = new File(filepath.get(1).toString());
                        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("photoofreceipt2", file2.getName(), RequestBody.create(MediaType.parse("image/*"), file2));

                        File file3 = new File(filepath.get(2).toString());
                        MultipartBody.Part imagePart3 = MultipartBody.Part.createFormData("photoofreceipt3", file3.getName(), RequestBody.create(MediaType.parse("image/*"), file3));

                        File file4 = new File(filepath.get(3).toString());
                        MultipartBody.Part imagePart4 = MultipartBody.Part.createFormData("photoofreceipt4", file4.getName(), RequestBody.create(MediaType.parse("image/*"), file4));

//                                                call = api.logbookservicesviewdapi(userId, vehicleid, vehiclecurrentkm, registerdate, note, addServiceTitleEdit, registerdate, addServiceCompanyEdit, addServiceChoiceEdit,addServiceCompleteEdit, addServiceReplaceEdit, imagePart, imagePart1, "Token " + pref.getString("access_token", ""));
                        call = api.logbookservicesviewdapi3(note, registerdate, userId, vehicleid, imagePart1, imagePart2, imagePart3, imagePart4, "Token " + pref.getString("access_token", ""));

                        call.enqueue(new Callback<AddreceiptModel>() {
                            @Override
                            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
//                                                            Log.d("fullresponse", new Gson().toJson(response.body()));
//                                                                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                                    AddreceiptModel model = response.body();

                                    navController.navigateUp();
                                    ToastUtility.successToast(getActivity(), "" + model.getMsg());

                                } else {
                                    try {
//                                                                    assert response.errorBody() != null;
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                                    } catch (Exception e) {
                                        ToastUtility.errorToast(getActivity(), e.getMessage());

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(getActivity(), t.getMessage());
                            }
                        });
                    }
                    if (image.size() == 5) {
                        File file1 = new File(filepath.get(0).toString());
                        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

                        File file2 = new File(filepath.get(1).toString());
                        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("photoofreceipt2", file2.getName(), RequestBody.create(MediaType.parse("image/*"), file2));

                        File file3 = new File(filepath.get(2).toString());
                        MultipartBody.Part imagePart3 = MultipartBody.Part.createFormData("photoofreceipt3", file3.getName(), RequestBody.create(MediaType.parse("image/*"), file3));

                        File file4 = new File(filepath.get(3).toString());
                        MultipartBody.Part imagePart4 = MultipartBody.Part.createFormData("photoofreceipt4", file4.getName(), RequestBody.create(MediaType.parse("image/*"), file4));

                        File file5 = new File(filepath.get(4).toString());
                        MultipartBody.Part imagePart5 = MultipartBody.Part.createFormData("photoofreceipt5", file5.getName(), RequestBody.create(MediaType.parse("image/*"), file5));

//                                                call = api.logbookservicesviewdapi(userId, vehicleid, vehiclecurrentkm, registerdate, note, addServiceTitleEdit, registerdate, addServiceCompanyEdit, addServiceChoiceEdit,addServiceCompleteEdit, addServiceReplaceEdit, imagePart, imagePart1, "Token " + pref.getString("access_token", ""));
                        call = api.logbookservicesviewdapi4(note, registerdate, userId, vehicleid, imagePart1, imagePart2, imagePart3, imagePart4, imagePart5, "Token " + pref.getString("access_token", ""));

                        call.enqueue(new Callback<AddreceiptModel>() {
                            @Override
                            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
//                                                            Log.d("fullresponse", new Gson().toJson(response.body()));
//                                                                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                                    AddreceiptModel model = response.body();

                                    navController.navigateUp();
                                    ToastUtility.successToast(getActivity(), "" + model.getMsg());

                                } else {
                                    ToastUtility.errorToast(getActivity(), "Error : " + response.message());
//                                    try {
////                                                                    assert response.errorBody() != null;
//                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//                                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());
//
//                                    } catch (Exception e) {
//                                        ToastUtility.errorToast(getActivity(), e.getMessage());
//
//                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                                pd.dismiss();
                                ToastUtility.errorToast(getActivity(), t.getMessage());
                            }
                        });
                    }
                } else {
                    Toast.makeText(binding.getRoot().getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();


                }
            }else {
                    binding.logbookNotesInput.setError("enter notes");
                }
            } else {
            Toast.makeText(binding.getRoot().getContext(), "Please complete all fields before saving.", Toast.LENGTH_SHORT).show();
            }


    }

//



    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }


    private void gotoBackScreen() {

        navController.navigateUp();

    }

    String imageurl = "";


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b= ExifUtils.rotateBitmap(filePath, b1);
        binding.addPhotoofrecieptItemImageView.setImageBitmap(b);


    }

}