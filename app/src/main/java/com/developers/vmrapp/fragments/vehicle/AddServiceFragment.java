package com.developers.vmrapp.fragments.vehicle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentAddServiceBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.models.ServiceRecordApiModel;
import com.developers.vmrapp.service.Api;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
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

import static android.content.Context.MODE_PRIVATE;


public class AddServiceFragment extends Fragment {

    private NavController navController;
    private static final Integer REQUEST_SHERGIL = 1234;

    private FragmentAddServiceBinding binding;
    Api api;
    Call<ServiceRecordApiModel> call;

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

        binding = FragmentAddServiceBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    HomeVehicleDetailModel.Datum data;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        data = (HomeVehicleDetailModel.Datum) getArguments().getSerializable("HomeVehicleDetailModel");

        initNavController();
        initClickListeners();

        String[] array = {"Minor Service", "Major Service"};


        ArrayAdapter<String> adapter;


        adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, array);

        binding.addServiceChoiceEdit.setAdapter(adapter);



    }

        String addServiceDateEdit;


    private void initClickListeners() {

        binding.addServiceDateEdit.setOnClickListener(v -> {
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

                            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            Log.v("output date ", formateDate);
                            binding.addServiceDateEdit.setText(formateDate);
                            binding.addServiceDateEdit.setTextColor(Color.BLACK);
                            addServiceDateEdit = formateDate;
                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
                        }
                    }).display();
        });


        binding.backButton.setOnClickListener(v -> gotoBackScreen());
        binding.postButton.setOnClickListener(v -> performpostoperation());
        binding.addServiceItemImageView.setOnClickListener(v -> selectvehicleimage());

    }

    private void performpostoperation() {

        if (!binding.addServiceTitleEdit.getText().toString().isEmpty()) {
            binding.addServiceTitleInput.setError(null);
            if (!addServiceDateEdit.isEmpty()) {
                if (!binding.addServiceCompanyEdit.getText().toString().isEmpty()) {
                    binding.addServiceCompanyInput.setError(null);
                    if (!binding.addServiceChoiceEdit.getText().toString().isEmpty()) {
                        binding.addServiceChoiceInput.setError(null);
                        if (!binding.addServiceCompleteEdit.getText().toString().isEmpty()) {
                            binding.addServiceCompleteInput.setError(null);
                            if (!binding.addServiceReplaceEdit.getText().toString().isEmpty()) {
                                binding.addServiceReplaceInput.setError(null);


                                SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
                                RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), data.id.toString());
                                RequestBody addServiceTitleEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceTitleEdit.getText().toString().trim());
                                RequestBody addServiceDateEdit_ = RequestBody.create(MediaType.parse("text/plain"), addServiceDateEdit);
                                RequestBody addServiceCompanyEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompanyEdit.getText().toString().trim());
                                RequestBody addServiceChoiceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceChoiceEdit.getText().toString().trim());
                                RequestBody addServiceCompleteEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceCompleteEdit.getText().toString().trim());
                                RequestBody addServiceReplaceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.addServiceReplaceEdit.getText().toString().trim());
                                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));


                                ProgressDialog pd = new ProgressDialog(getActivity());
                                pd.setMessage("Loading ...");
                                pd.setIndeterminate(true);
                                pd.setCancelable(false);
                                pd.show();

                                File file = new File(imageurl);
                                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("serviceimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                                call = api.servicerecordapi(vehicleid, addServiceTitleEdit,addServiceCompanyEdit,addServiceChoiceEdit, addServiceDateEdit_, addServiceCompleteEdit, addServiceReplaceEdit, userId, imagePart, "Token " + pref.getString("access_token", ""));
                                call.enqueue(new Callback<ServiceRecordApiModel>() {
                                    @Override
                                    public void onResponse(Call<ServiceRecordApiModel> call, Response<ServiceRecordApiModel> response) {
                                        pd.dismiss();
                                        if (response.code() == 200) {
                                            ServiceRecordApiModel model = response.body();

                                            navController.navigateUp();

                                            ToastUtility.successToast(requireContext(), "success");

                                        } else {
                                            try {
                                                assert response.errorBody() != null;
                                                JSONObject jObjError = new JSONObject(response.errorBody().string());

                                                ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                                            } catch (Exception e) {
                                                ToastUtility.errorToast(getActivity(), e.getMessage());

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServiceRecordApiModel> call, Throwable t) {
                                        pd.dismiss();
                                        ToastUtility.errorToast(getActivity(), t.getMessage());

                                    }
                                });


                            } else {
                                binding.addServiceReplaceInput.setError("Pls enter replace service");
                            }
                        } else {
                            binding.addServiceCompleteInput.setError("Pls enter service complete");
                        }

                    } else {
                        binding.addServiceChoiceInput.setError("pLs enter service choice");
                    }
                } else {
                    binding.addServiceCompanyInput.setError("Pls enter company name");
                }

            } else {
                ToastUtility.warningToast(requireContext(), "Pls select date");
            }

        } else {
            binding.addServiceTitleInput.setError("Pls enter title");
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.d("imageurl", filepath + " ");

            binding.addServiceItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            imageurl = filepath;

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

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }


    private void gotoBackScreen() {

        navController.navigateUp();

    }

}