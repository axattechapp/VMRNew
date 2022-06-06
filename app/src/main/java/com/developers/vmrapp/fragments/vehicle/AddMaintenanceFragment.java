package com.developers.vmrapp.fragments.vehicle;

import android.Manifest;
import android.app.Activity;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.DialogImageChooser;
import com.developers.vmrapp.ExifUtils;
import com.developers.vmrapp.RealPathUtil;
import com.developers.vmrapp.activities.vehicle.AddVehicleActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentAddMaintenanceBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.models.MainTainRecordApiModel;
import com.developers.vmrapp.service.Api;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class AddMaintenanceFragment extends Fragment {

    private static final Integer REQUEST_SHERGIL = 1234;
    private static final Integer REQUEST_PHOTOOFRECEIPT_SHERGIL = 12345;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_WRITE_PERMISSION = 11;
    private NavController navController;

    private FragmentAddMaintenanceBinding binding;
    Api api;
    Call<MainTainRecordApiModel> call;
    public static final int RequestPermissionCode = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_write_EXTERNAL_STORAGE = 123;
    String imageurl = "";
    String photoofrecieptimageurl = "";
    String currentPhotoPath;
    Uri photoURI;
    private static final Integer Maintainance_Camera=7;
    private static final Integer PhotoReceipt_Camera=77;
    private static final Integer Maintainance_gallery=10;
    private static final Integer PhotoReceipt_gallery=100;
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

        binding = FragmentAddMaintenanceBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    HomeVehicleDetailModel.Datum data;


    SharedPreferences pref;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        data = (HomeVehicleDetailModel.Datum) getArguments().getSerializable("HomeVehicleDetailModel");

        Log.e("dataid", data.id + "");

        initNavController();

        initClickListeners();
        requestPermission();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // do what you want;
                    }
                }
                break;
            default:
                break;
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
//            openFilePicker();
        }
    }
    private void initClickListeners() {

        binding.backButton.setOnClickListener(v ->
                gotoBackScreen());
        binding.postButton.setOnClickListener(v ->

                performpostoperation());
        binding.maintenanceItemImageView.setOnClickListener(v ->
                selectvehicleimage());
        binding.maintenancePhotoofrecieptImageView.setOnClickListener(v ->
                selectphotoofrecieptimage());

    }

    private void performpostoperation() {

        if (!imageurl.isEmpty()) {
            if (!binding.maintenanceTitleEdit.getText().toString().isEmpty()) {
                binding.maintenanceTitleInput.setError(null);
                            if (!binding.maintenanceDescriptionEdit.getText().toString().isEmpty()) {
                                binding.maintenanceDescriptionInput.setError(null);


                                RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), data.id.toString());
                                RequestBody maintenanceTitleEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceTitleEdit.getText().toString().trim());
                                RequestBody maintenanceQuantityEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceQuantityEdit.getText().toString().trim());
                                RequestBody maintenanceSupplierEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceSupplierEdit.getText().toString().trim());
                                RequestBody maintenanceConditionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceConditionEdit.getText().toString().trim());
                                RequestBody maintenanceDescriptionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.maintenanceDescriptionEdit.getText().toString().trim());
                                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));


                                File file = new File(imageurl);
                                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("maintenanceimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


                                ProgressDialog pd = new ProgressDialog(getActivity());
                                pd.setMessage("Loading ...");
                                pd.setIndeterminate(true);
                                pd.setCancelable(false);
                                pd.show();

                                call = api.maintenancerecordapi(vehicleid, maintenanceTitleEdit, maintenanceQuantityEdit, maintenanceSupplierEdit, maintenanceConditionEdit, maintenanceDescriptionEdit, userId, imagePart,photoofreciept,
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
                                            Toast.makeText(requireContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MainTainRecordApiModel> call, Throwable t) {
                                        pd.dismiss();
                                        ToastUtility.errorToast(getActivity(), t.getMessage());

                                    }
                                });


                            } else {
                                binding.maintenanceDescriptionInput.setError("Pls enter description");
                            }


            } else {
                binding.maintenanceTitleInput.setError("Pls enter title");
            }

        } else {
            ToastUtility.warningToast(requireContext(), "Pls select service image");
        }

    }

    private void selectvehicleimage() {
//        Shergil.Companion.create(this)
//                .mimeTypes(MimeType.Companion.getIMAGES())
//                .showDisallowedMimeTypes(false)
//                .numOfColumns(2)
//                .maxSelectable(1)
//                .theme(R.style.Shergil)
//                .allowPreview(true)
//                .allowCamera(true)
//                .showDeviceCamera(false)
//                .showCameraFirst(false)
//                .orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .withRequestCode(REQUEST_SHERGIL);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {

            new DialogImageChooser(binding.getRoot().getContext(), selectedOption -> {
                if (selectedOption.equalsIgnoreCase("camera")) {
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            try {
//                                startActivityForResult(takePictureIntent, 7);
//                            } catch (ActivityNotFoundException e) {
//                                // display error state to the user
//                                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
//                            }

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        photoFile = createImageFile();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(binding.getRoot().getContext(),
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, Maintainance_Camera);
                        }
                    }
//                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, 7);
                } else if (selectedOption.equalsIgnoreCase("gallery")) {
                    if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(intent, Maintainance_gallery);
                    }
                }
            }).show(getActivity().getSupportFragmentManager(), "add_photo_dialog");
        }


    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.e("current",""+currentPhotoPath);
        return image;
    }

    private void selectphotoofrecieptimage() {
//        Shergil.Companion.create(this)
//                .mimeTypes(MimeType.Companion.getIMAGES())
//                .showDisallowedMimeTypes(false)
//                .numOfColumns(2)
//                .maxSelectable(1)
//                .theme(R.style.Shergil)
//                .allowPreview(true)
//                .allowCamera(true)
//                .showDeviceCamera(false)
//                .showCameraFirst(false)
//                .orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .withRequestCode(REQUEST_PHOTOOFRECEIPT_SHERGIL);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {

            new DialogImageChooser(binding.getRoot().getContext(), selectedOption -> {
                if (selectedOption.equalsIgnoreCase("camera")) {
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            try {
//                                startActivityForResult(takePictureIntent, 7);
//                            } catch (ActivityNotFoundException e) {
//                                // display error state to the user
//                                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
//                            }

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        photoFile = createImageFile();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(binding.getRoot().getContext(),
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, PhotoReceipt_Camera);
                        }
                    }
//                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, 7);
                } else if (selectedOption.equalsIgnoreCase("gallery")) {
                    if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(intent, PhotoReceipt_gallery);
                    }
                }
            }).show(getActivity().getSupportFragmentManager(), "add_photo_dialog");
        }


    }

    MultipartBody.Part photoofreciept;

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
//            List<Uri> image = Shergil.Companion.getMediaUris(data);
//
//            Log.d("imageurl", image.get(0).toString());
//
//            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));
//
//
//            Log.d("imageurl", filepath + " ");
//
////            binding.maintenanceItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
//
//            imageurl = filepath;
//            decodeFile(imageurl,"Maintainence");
//
//        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PHOTOOFRECEIPT_SHERGIL) {
//            List<Uri> image = Shergil.Companion.getMediaUris(data);
//
//            Log.d("imageurl", image.get(0).toString());
//
//            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));
//
//
//            Log.d("imageurl", filepath + " ");
//
////            binding.maintenancePhotoofrecieptImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
//
//            photoofrecieptimageurl = filepath;
//            decodeFile(imageurl,"photoreceipt");
//            File file1 = new File(photoofrecieptimageurl);
//            photoofreciept = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

//
//        }
//    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Maintainance_Camera && resultCode == getActivity().RESULT_OK) {
//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        Bitmap bitmap = (Bitmap) data.getExtras();
//                        binding.vehicleImageView.setImageBitmap(bitmap);

            Log.e("uri1",""+photoURI);
            imageurl=currentPhotoPath;
            Log.e("imageurl",""+imageurl);
            try {
                Picasso.get().load(photoURI).into(binding.maintenanceItemImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                        Log.e("uri",""+imageUri);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                        Uri tempUri = getImageUri1(getApplicationContext(), bitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
//                        File finalFile = new File(getRealPathFromURI1(tempUri));
//                        imageurl=getRealPathFromURI1(tempUri);
//                        decodeFile(imageurl);
//                        Log.e("path", "File Path: " + imageurl);

        }else if (requestCode == Maintainance_gallery && resultCode == getActivity().RESULT_OK){

                Uri uri=data.getData();
            try {
                Picasso.get().load(uri).into(binding.maintenanceItemImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
                if (Build.VERSION.SDK_INT < 11)
                {
                    imageurl = RealPathUtil.getRealPathFromURI_BelowAPI11(binding.getRoot().getContext(), uri);
                }
                // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                {
                    imageurl = RealPathUtil.getRealPathFromURI_API11to18(binding.getRoot().getContext(), uri);
                }
                // SDK > 19 (Android 4.4)
                else
                {
                    imageurl = RealPathUtil.getRealPathFromURI_API19(binding.getRoot().getContext(), uri);
                }
                Log.e("path", "File Path: " + imageurl);
//                Bitmap bitmap= null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                binding.maintenanceItemImageView.setImageBitmap(bitmap);

        }else if (requestCode == PhotoReceipt_Camera && resultCode == getActivity().RESULT_OK)
        {
            Log.e("uri1",""+photoURI);
            photoofrecieptimageurl=currentPhotoPath;
            File file1 = new File(photoofrecieptimageurl);
            photoofreciept = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

            Log.e("imageurl",""+imageurl);
            try {
                Picasso.get().load(photoURI).into(binding.maintenancePhotoofrecieptImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PhotoReceipt_gallery && resultCode == getActivity().RESULT_OK){

            Uri uri=data.getData();
            try {
                Picasso.get().load(uri).into(binding.maintenancePhotoofrecieptImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT < 11)
            {
                photoofrecieptimageurl = RealPathUtil.getRealPathFromURI_BelowAPI11(binding.getRoot().getContext(), uri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
            {
                photoofrecieptimageurl = RealPathUtil.getRealPathFromURI_API11to18(binding.getRoot().getContext(), uri);
            }
            // SDK > 19 (Android 4.4)
            else
            {
                photoofrecieptimageurl = RealPathUtil.getRealPathFromURI_API19(binding.getRoot().getContext(), uri);
            }
            Log.e("path", "File Path: " + imageurl);
//            Bitmap bitmap= null;
//            try {
//                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            File file1 = new File(photoofrecieptimageurl);
            photoofreciept = MultipartBody.Part.createFormData("photoofreceipt", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

//            binding.maintenancePhotoofrecieptImageView.setImageBitmap(bitmap);

        }
    }
//    String imageurl = "";
//    String photoofrecieptimageurl = "";


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
    public void decodeFile(String filePath,String value) {

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
if (value=="Maintainence")
{
    binding.maintenanceItemImageView.setImageBitmap(b);

}else
{
    binding.maintenancePhotoofrecieptImageView.setImageBitmap(b);
}


        // image.setImageBitmap(bitmap);
    }
}