package com.developers.vmrapp.fragments.vehicle.ViewHistory;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.models.VehicleHistoryModel2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentViewMaintenanceRecordBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.MainTainRecordApiModel;
import com.developers.vmrapp.models.VehicleHistoryapiModel;
import com.developers.vmrapp.service.Api;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;

import org.json.JSONObject;

import java.io.File;
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


public class ViewMaintenanceRecordFragment extends Fragment {

    FragmentViewMaintenanceRecordBinding binding;
    Api api;

    private static final Integer REQUEST_SHERGIL = 1234;
    private static final Integer REQUEST_PHOTOOFRECIEPT_SHERGIL = 12345;
    private NavController navController;

    Call<MainTainRecordApiModel> call;
    boolean setClick=false;
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

        binding = FragmentViewMaintenanceRecordBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    SharedPreferences pref;
//    VehicleHistoryapiModel.Datum data;
    VehicleHistoryModel2.Datum data;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


//        data = (VehicleHistoryapiModel.Datum) getArguments().getSerializable("VehicleHistoryDetailModel");
        data = (VehicleHistoryModel2.Datum) getArguments().getSerializable("VehicleHistoryDetailModel");
        setmaintenancedata();

        initNavController();

        initClickListeners();

        binding.editButton.setOnClickListener(v -> {
//            binding.maintenanceConditionEdit.setEnabled(true);
//            binding.maintenanceSupplierEdit.setEnabled(true);
//            binding.maintenanceQuantityEdit.setEnabled(true);
            setClick=true;
            binding.maintenanceDescriptionEdit.setEnabled(true);
            binding.maintenanceTitleEdit.setEnabled(true);
            binding.maintenanceItemImageView.setEnabled(true);
            binding.maintenancePhotoofImageView.setEnabled(true);
            binding.editButton.setVisibility(View.GONE);
            binding.postButton.setVisibility(View.VISIBLE);
        });

    }

    private void setmaintenancedata() {


//        binding.maintenanceConditionEdit.setEnabled(false);
//        binding.maintenanceSupplierEdit.setEnabled(false);
//        binding.maintenanceQuantityEdit.setEnabled(false);
        binding.maintenanceDescriptionEdit.setEnabled(false);
        binding.maintenanceTitleEdit.setEnabled(false);
        binding.maintenanceItemImageView.setEnabled(true);
        binding.maintenancePhotoofImageView.setEnabled(true);



        if (data.maintenanceimage != null) {
            Glide.with(requireActivity()).load(Api.Image_URL + data.maintenanceimage)
                    .into(binding.maintenanceItemImageView);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
                    .into(binding.maintenanceItemImageView);
        }
        if (data.photoofreceipt != null) {
            Glide.with(requireActivity()).load(Api.Image_URL + data.photoofreceipt)
                    .into(binding.maintenancePhotoofImageView);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_clear_black_24dp)
                    .into(binding.maintenancePhotoofImageView);
        }
//        assert data.condition != null;
//        binding.maintenanceConditionEdit.setText(data.condition);

        assert data.description != null;
        binding.maintenanceDescriptionEdit.setText(data.description);

//        assert data.quantity != null;
//        binding.maintenanceQuantityEdit.setText(data.quantity);

        assert data.maintenancetitle != null;
        binding.maintenanceTitleEdit.setText(data.maintenancetitle);

//        assert data.supplier != null;
//        binding.maintenanceSupplierEdit.setText(data.supplier);


    }

    private void initClickListeners() {

        binding.backButton.setOnClickListener(v -> gotoBackScreen());
        binding.postButton.setOnClickListener(v -> performpostoperation());
        binding.maintenanceItemImageView.setOnClickListener(v -> selectvehicleimage());
        binding.maintenancePhotoofImageView.setOnClickListener(v -> selectphotoofimage());

    }

    MultipartBody.Part imagePart;
    MultipartBody.Part photoofreciept;


    private void performpostoperation() {


        RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.vehicle));
        RequestBody maintenanceTitleEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceTitleEdit.getText().toString().trim());
//        RequestBody maintenanceQuantityEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceQuantityEdit.getText().toString().trim());
//        RequestBody maintenanceSupplierEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceSupplierEdit.getText().toString().trim());
//        RequestBody maintenanceConditionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceConditionEdit.getText().toString().trim());
        RequestBody maintenanceDescriptionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceDescriptionEdit.getText().toString().trim());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));
        RequestBody maintenancerecordid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.id));


        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        call = api.maintenancerecordeditapi2(maintenancerecordid, vehicleid, maintenanceTitleEdit, maintenanceDescriptionEdit, userId, imagePart,photoofreciept,
                "Token " + pref.getString("access_token", ""));

        call.enqueue(new Callback<MainTainRecordApiModel>() {
            @Override
            public void onResponse(Call<MainTainRecordApiModel> call, Response<MainTainRecordApiModel> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    MainTainRecordApiModel model = response.body();
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
            public void onFailure(Call<MainTainRecordApiModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());

            }
        });


    }

    private void selectvehicleimage() {
        if (setClick==true) {
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
        }else
        {
            loadPhoto3();
        }

    }

    private void selectphotoofimage() {

        if (setClick==true) {
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
                    .withRequestCode(REQUEST_PHOTOOFRECIEPT_SHERGIL);

        }else
        {
            loadPhoto2();
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

            binding.maintenanceItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            imageurl = filepath;

            File file = new File(imageurl);
            imagePart = MultipartBody.Part.createFormData("maintenanceimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        }
        else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PHOTOOFRECIEPT_SHERGIL){
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.d("imageurl", filepath + " ");

            binding.maintenancePhotoofImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

            imageurl = filepath;

            File file = new File(filepath);
            photoofreciept = MultipartBody.Part.createFormData("photoofreceipt", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        }
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
    private void loadPhoto2() {
        View alertCustomdialog = LayoutInflater.from(getActivity()).inflate(R.layout.image_popup, null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        ImageView imageView2 = (ImageView) alertCustomdialog.findViewById(R.id.img);
        ImageView imageView3 = (ImageView) alertCustomdialog.findViewById(R.id.close_dialog);
        Log.e("imgurl12", "" + Api.Image_URL+data.getPhotoofreceipt());
        Glide.with(requireActivity()).load(Api.Image_URL+data.getPhotoofreceipt())
                .into(imageView2);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//finally show the dialog box in android all
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void loadPhoto3() {
        View alertCustomdialog = LayoutInflater.from(getActivity()).inflate(R.layout.image_popup, null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        ImageView imageView2 = (ImageView) alertCustomdialog.findViewById(R.id.img);
        ImageView imageView3 = (ImageView) alertCustomdialog.findViewById(R.id.close_dialog);
        Log.e("imgurl12", "" + Api.Image_URL+data.getMaintenanceimage());
        Glide.with(requireActivity()).load(Api.Image_URL+data.getMaintenanceimage())
                .into(imageView2);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//finally show the dialog box in android all
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}