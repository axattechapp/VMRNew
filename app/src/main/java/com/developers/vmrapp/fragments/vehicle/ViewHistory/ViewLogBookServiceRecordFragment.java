package com.developers.vmrapp.fragments.vehicle.ViewHistory;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.adapters.SliderAdapterExample;
import com.developers.vmrapp.adapters.SliderItem;
import com.developers.vmrapp.models.AddreceiptModel;
import com.developers.vmrapp.models.LogbookServiceViewdapiModel3;
import com.developers.vmrapp.models.VehicleHistoryModel2;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentViewLogBookServiceRecordBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.service.Api;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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



public class ViewLogBookServiceRecordFragment extends Fragment {
    FragmentViewLogBookServiceRecordBinding binding;
    private NavController navController;
    private static final Integer REQUEST_SHERGIL = 1234;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final Integer REQUEST_SHERGIL_PHOTOOFRECIEPT = 12345;
    //    PhotoViewAttacher pAttacher;
    Call<AddreceiptModel> call;
    Api api;
    boolean setClick=false;
    String getImageurl="";
    String getImageurl2="";
    List<String> image;
    int position=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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



        binding = FragmentViewLogBookServiceRecordBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        requestRead();
        image=new ArrayList<String>();
//        binding.addPhotoofrecieptItemImageView.setEnabled(true);
//        pAttacher = new PhotoViewAttacher(binding.addPhotoofrecieptItemImageView);
//
//        pAttacher.update();
        return binding.getRoot();
    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    Call<LogbookServiceViewdapiModel3> call1;
    Call<LogbookServiceViewdapiModel3> call2;
    SharedPreferences pref;
    //    VehicleHistoryapiModel.Datum data;
    VehicleHistoryModel2.Datum data;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


//        data = (VehicleHistoryapiModel.Datum) getArguments().getSerializable("VehicleHistoryDetailModel");
        data = (VehicleHistoryModel2.Datum) getArguments().getSerializable("VehicleHistoryDetailModel");


        call1 = api.getlogbookservicesviewdapi2(String.valueOf(data.id), "Token " + pref.getString("access_token", ""));
        call2 = api.getlogbookservicesviewdapi2(String.valueOf(data.id), "Token " + pref.getString("access_token", ""));

        Log.e("data_id",String.valueOf(data.id));

        initNavController();
        initClickListeners();

        binding.editButton.setOnClickListener(v -> {
            setClick=true;
//            binding.logbookVehicleKmEdit.setEnabled(true);
            binding.imageSlider.setEnabled(true);
//            binding.logbookItemImageView.setEnabled(true);
//            binding.logbookServiceEdit.setEnabled(true);
            binding.logbookDateEdit.setEnabled(true);
            binding.logbookNotesEdit.setEnabled(true);
//            binding.addServiceTitleEdit.setEnabled(true);
//            binding.addServiceCompanyEdit.setEnabled(true);
//            binding.addServiceChoiceEdit.setEnabled(true);
//            binding.addServiceReplaceEdit.setEnabled(true);
//            binding.addServiceCompleteEdit.setEnabled(true);
            binding.editButton.setVisibility(View.GONE);
            binding.postButton.setVisibility(View.VISIBLE);

        });




        call1.enqueue(new Callback<LogbookServiceViewdapiModel3>() {
            @Override
            public void onResponse(Call<LogbookServiceViewdapiModel3> call, Response<LogbookServiceViewdapiModel3> response) {
                if (response.code() == 200) {

//                    binding.logbookVehicleKmEdit.setEnabled(false);
//                    binding.addPhotoofrecieptItemImageView.setEnabled(false);
//                    binding.logbookItemImageView.setEnabled(false);
//                    binding.logbookServiceEdit.setEnabled(false);
                    binding.logbookDateEdit.setEnabled(false);
                    binding.logbookNotesEdit.setEnabled(false);
//                    binding.addServiceTitleEdit.setEnabled(false);
//                    binding.addServiceCompanyEdit.setEnabled(false);
//                    binding.addServiceChoiceEdit.setEnabled(false);
//                    binding.addServiceCompleteEdit.setEnabled(false);
//                    binding.addServiceReplaceEdit.setEnabled(false);
                    binding.editButton.setVisibility(View.VISIBLE);

                    LogbookServiceViewdapiModel3 model = response.body();

                    Log.e("fullresponse", new Gson().toJson(response.body()));

                    List<SliderItem> sliderItems = new ArrayList<>();




                    if (model.data.photoofreceipt != null) {
                        SliderItem item = new SliderItem();
                        item.photoofreceipt = model.data.photoofreceipt;
                        sliderItems.add(item);
                    }
                    if (model.data.photoofreceipt2 != null) {
                        SliderItem item = new SliderItem();
                        item.photoofreceipt = (String) model.data.photoofreceipt2;
                        sliderItems.add(item);
                    }
                    if (model.data.photoofreceipt3 != null) {
                        SliderItem item = new SliderItem();
                        item.photoofreceipt = (String) model.data.photoofreceipt3;
                        sliderItems.add(item);
                    }
                    if (model.data.photoofreceipt4 != null) {
                        SliderItem item = new SliderItem();
                        item.photoofreceipt = (String) model.data.photoofreceipt4;
                        sliderItems.add(item);
                    }
                    if (model.data.photoofreceipt5 != null) {
                        SliderItem item = new SliderItem();
                        item.photoofreceipt = (String) model.data.photoofreceipt5;
                        sliderItems.add(item);
                    }
                    binding.imageSlider.setSliderAdapter(new SliderAdapterExample(binding.getRoot().getContext(), sliderItems));

                    binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    binding.imageSlider.setIndicatorSelectedColor(Color.GREEN);
                    binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    binding.imageSlider.setScrollTimeInSec(2); //set scroll delay in seconds :
                    binding.imageSlider.startAutoCycle();
                    ////this commented
//                    if (model.data.photoofreceipt!=null)
//                    {
//                    Glide.with(requireActivity()).load(Api.Image_URL + model.data.photoofreceipt).placeholder(R.drawable.placeholder_011)
//                                .into(binding.addPhotoofrecieptItemImageView);
//                        getImageurl=Api.Image_URL + model.data.photoofreceipt;
//                    } else {
//                        Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
//                                .into(binding.addPhotoofrecieptItemImageView);
//                    }
//                    if (model.data.serviceimage != null) {
//
//                        Glide.with(requireActivity()).load(Api.Image_URL + model.data.serviceimage).placeholder(R.drawable.placeholder_011)
//                                .into(binding.logbookItemImageView);
//
//                        getImageurl2=Api.Image_URL + model.data.serviceimage;
//                    } else {
//                        Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
//                                .into(binding.logbookItemImageView);
//                    }
//
//                    assert model.data.current_km != null;
//                    binding.logbookVehicleKmEdit.setText(model.data.current_km);
//                    assert model.data.date != null;
                    if (model.data.date!=null) {
                        String dateStr = model.data.date;
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = null;
                        try {
                            date1 = sdf1.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                        dateStr = sdf1.format(date1);
                        Log.e("output date ", "" + dateStr);
                        binding.logbookDateEdit.setText(dateStr);
                    }
//                    registrationdate = model.data.date;
//                    assert model.data.next_service != null;
//                    String dateStr2 =model.data.next_service;
//                    Log.e("service_next",""+model.data.next_service);
//                    SimpleDateFormat sdf12 = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date12 = null;
//                    try {
//                        date12 = sdf12.parse(dateStr2);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    sdf12 = new SimpleDateFormat("dd-MM-yyyy");
//
//                    assert date12 != null;
//                    dateStr2 = sdf12.format(date12);
//                    Log.e("output date ",""+dateStr2);
//
//                    binding.logbookServiceEdit.setText(dateStr2);
//                    nextservicedate = model.data.next_service;
//                    assert model.data.notes != null;
                    if (model.data.notes!=null)
                    {
                        binding.logbookNotesEdit.setText(model.data.notes);
                    }

//                    assert model.data.servicetitle != null;
//                    binding.addServiceTitleEdit.setText(model.data.servicetitle);
//                    assert model.data.servicecompany != null;
//                    binding.addServiceCompanyEdit.setText(model.data.servicecompany);
//                    assert model.data.minorormajorservice != null;
//                    binding.addServiceChoiceEdit.setText(model.data.minorormajorservice);

//                    if (model.data.minorormajorservice.equals("Minor Service")) {
//                        String[] array = {"Minor Service", "Major Service"};
//
//
//                        ArrayAdapter<String> adapter;
//
//
//                        adapter = new ArrayAdapter<String>(requireContext(),
//                                android.R.layout.simple_list_item_1, array);
//
//                        binding.addServiceChoiceEdit.setAdapter(adapter);
//                    } else {
//                        String[] array = {"Major Service", "Minor Service"};
//
//
//                        ArrayAdapter<String> adapter;
//
//
//                        adapter = new ArrayAdapter<String>(requireContext(),
//                                android.R.layout.simple_list_item_1, array);
//
//                        binding.addServiceChoiceEdit.setAdapter(adapter);
//                    }
//
//
//                    assert model.data.completed != null;
//                    binding.addServiceCompleteEdit.setText(model.data.completed);
//                    assert model.data.replace != null;
//                    binding.addServiceReplaceEdit.setText(model.data.replace);


                } else {

                    ToastUtility.errorToast(requireActivity(), new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LogbookServiceViewdapiModel3> call, Throwable t) {
                ToastUtility.errorToast(requireActivity(), t.getMessage());
            }
        });


        binding.backButton.setOnClickListener(v -> gotoBackScreen());
//        binding.logbookItemImageView.setOnClickListener(v -> selectvehicleimage());
        binding.postButton.setOnClickListener(v -> performpostoperation());
//        binding.addPhotoofrecieptItemImageView.setOnClickListener(v -> selectphotoofrecieptimage());
        binding.imageSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setClick==true)
                {
                    selectphotoofrecieptimage();
                }else
                    loadPhoto();
            }
        });

//        binding.logbookItemImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (setClick==true)
//                {
//                    selectvehicleimage();
//                }else
//                    loadPhoto2();
//            }
//        });


    }

    private void loadPhoto2() {
        View alertCustomdialog = LayoutInflater.from(getActivity()).inflate(R.layout.image_popup,null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        ImageView imageView2 = (ImageView)alertCustomdialog.findViewById(R.id.img);
        ImageView imageView3 = (ImageView)alertCustomdialog.findViewById(R.id.close_dialog);
        Log.e("imgurl12",""+getImageurl);
        Glide.with(requireActivity()).load(getImageurl2)
                .into(imageView2);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        call2.enqueue(new Callback<LogbookServiceViewdapiModel>() {
//            @Override
//            public void onResponse(Call<LogbookServiceViewdapiModel> call, Response<LogbookServiceViewdapiModel> response) {
//                if (response.code()==200)
//                {
//                    LogbookServiceViewdapiModel model = response.body();
//
//                    Log.e("fullresponse", new Gson().toJson(response.body()));
//
//
//                    if (model.data.photoofreceipt != null) {
////                        setClick=true;
//
//                        Glide.with(requireActivity()).load(Api.Image_URL + model.data.photoofreceipt).placeholder(R.drawable.placeholder_011)
//                                .into(imageView2);
//                    } else {
//                        Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
//                                .into(imageView2);
//                    }
//                }else
//                    Toast.makeText(getContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<LogbookServiceViewdapiModel> call, Throwable t) {
//                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });




        //finally show the dialog box in android all
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    String registrationdate = "";
    String nextservicedate = "";
    String addServiceDateEdit = "";
    String Photoofreciepturl = "";

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
//                        }
//                    }).display();
//        });
//

        binding.logbookDateEdit.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(requireActivity())
                    .bottomSheet()
//                    .mustBeOnFuture()
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
                    .title("Date and Time")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {

                            String formateDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            Log.v("output date ", formateDate);
                            binding.logbookDateEdit.setText(formateDate);
                            binding.logbookDateEdit.setTextColor(Color.BLACK);
                            registrationdate = formateDate;
                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
                        }
                    }).display();
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
        binding.postButton.setOnClickListener(v -> performpostoperation());
        binding.imageSlider.setOnClickListener(v -> selectphotoofrecieptimage());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.d("imageurl", filepath + " ");

//            binding.logbookItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            imageurl = filepath;


            File file = new File(imageurl);
            imagePart = MultipartBody.Part.createFormData("serviceimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL_PHOTOOFRECIEPT) {
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.e("imageurl", filepath + " ");

            //this

           // binding.addPhotoofrecieptItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            Photoofreciepturl = filepath;

            File file1 = new File(Photoofreciepturl);

            imagePart1 = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));


        }
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
                .withRequestCode(REQUEST_SHERGIL_PHOTOOFRECIEPT);

    }


    MultipartBody.Part imagePart1;
    MultipartBody.Part imagePart;


    private void performpostoperation() {


        SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
        RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.vehicle));
//        RequestBody addServiceTitleEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceTitleEdit.getText().toString().trim());
//        RequestBody addServiceCompanyEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompanyEdit.getText().toString().trim());
//        RequestBody addServiceChoiceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceChoiceEdit.getText().toString().trim());
//        RequestBody addServiceCompleteEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompleteEdit.getText().toString().trim());
//        RequestBody addServiceReplaceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceReplaceEdit.getText().toString().trim());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));
        RequestBody logbookservicesid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.id));
//        RequestBody vehiclecurrentkm = RequestBody.create(MediaType.parse("text/plain"), binding.logbookVehicleKmEdit.getText().toString().trim());
        RequestBody registerdate = RequestBody.create(MediaType.parse("text/plain"), registrationdate);
        RequestBody vehicleservice = RequestBody.create(MediaType.parse("text/plain"), nextservicedate);
        RequestBody note = RequestBody.create(MediaType.parse("text/plain"), binding.logbookNotesEdit.getText().toString().trim());


        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();


        call = api.logbookserviceseditapi2(logbookservicesid, userId, vehicleid, note, registerdate, imagePart1, "Token " + pref.getString("access_token", ""));

        call.enqueue(new Callback<AddreceiptModel>() {
            @Override
            public void onResponse(Call<AddreceiptModel> call, Response<AddreceiptModel> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    Log.d("fullresponse", new Gson().toJson(response.body()));
//                    ToastUtility.successToast(requireContext(), new Gson().toJson(response.body()));

                    AddreceiptModel model= response.body();
                    navController.navigateUp();
                    ToastUtility.successToast(getActivity(), ""+model.getMsg());

                } else {
                    ToastUtility.errorToast(getActivity(), "Error : " + " " + response.message());

//                    try {
//                        assert response.errorBody() != null;
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());
//
//                    } catch (Exception e) {
//                        ToastUtility.errorToast(getActivity(), e.getMessage());
//
//                    }
                }
            }

            @Override
            public void onFailure(Call<AddreceiptModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());
            }
        });


    }
    private void loadPhoto() {

//        final Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        //dialog.setContentView(R.layout.custom_fullimage_dialog);
//        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.image_popup, null);
//        ImageView imageView1=(ImageView) view.findViewById(R.id.img);
//        getActivity().setContentView(view);
//
//        imageView1.setImageDrawable(imag.getDrawableeView());
//
//
//
//        dialog.setContentView(inflater);
//        dialog.show();

        View alertCustomdialog = LayoutInflater.from(getActivity()).inflate(R.layout.image_popup,null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        ImageView imageView2 = (ImageView)alertCustomdialog.findViewById(R.id.img);
        ImageView imageView3 = (ImageView)alertCustomdialog.findViewById(R.id.close_dialog);
        Log.e("imgurl12",""+getImageurl);
        Glide.with(requireActivity()).load(getImageurl)
                .into(imageView2);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        call2.enqueue(new Callback<LogbookServiceViewdapiModel>() {
//            @Override
//            public void onResponse(Call<LogbookServiceViewdapiModel> call, Response<LogbookServiceViewdapiModel> response) {
//                if (response.code()==200)
//                {
//                    LogbookServiceViewdapiModel model = response.body();
//
//                    Log.e("fullresponse", new Gson().toJson(response.body()));
//
//
//                    if (model.data.photoofreceipt != null) {
////                        setClick=true;
//
//                        Glide.with(requireActivity()).load(Api.Image_URL + model.data.photoofreceipt).placeholder(R.drawable.placeholder_011)
//                                .into(imageView2);
//                    } else {
//                        Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
//                                .into(imageView2);
//                    }
//                }else
//                    Toast.makeText(getContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<LogbookServiceViewdapiModel> call, Throwable t) {
//                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //finally show the dialog box in android all

        dialog.show();



    }
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
//            readFile();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                readFile();
            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}