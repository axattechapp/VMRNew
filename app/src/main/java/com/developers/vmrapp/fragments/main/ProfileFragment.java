package com.developers.vmrapp.fragments.main;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.RealPathUtil;
import com.developers.vmrapp.RealPathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentProfileBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.UserProfileModel;
import com.developers.vmrapp.service.Api;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;
import com.lassi.common.utils.KeyUtils;
import com.lassi.data.media.MiMedia;
import com.lassi.domain.media.LassiOption;
import com.lassi.domain.media.MediaType;
import com.lassi.presentation.builder.Lassi;
import com.lassi.presentation.cropper.CropImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private static final String WRITE_EXTERNAL_STORAGE = "11";
    private static final String READ_EXTERNAL_STORAGE = "222";
    private Context context;
    private FragmentProfileBinding binding;

    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;
    String imageurl = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final Integer REQUEST_SHERGIL = 1234;
    KProgressHUD progressDialog;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        binding = FragmentProfileBinding
                .inflate(inflater, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},111);
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
        binding.profileImageView.setEnabled(false);
        binding.addImage.setEnabled(false);
        return binding.getRoot();

    }

    Call<JsonObject> postuserprofilecall;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

//        binding.profileImageView.setOnClickListener(v -> {
//            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//            pickprofileimage();
//        });

        context = getContext();
        progressDialog = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait");
        progressDialog.setCancellable(false);
        progressDialog.show();
        binding.ScrollLayout.setVisibility(View.INVISIBLE);
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(intent, REQUEST_SHERGIL);


                }
            }

        });



        binding.editButton.setOnClickListener(v -> {

            if (binding.editButton.getText().toString().equals("Edit Profile")) {
                binding.editButton.setText("SAVE");

                binding.profileImageView.setEnabled(true);
                binding.addImage.setEnabled(true);
                binding.profileNameText.setEnabled(true);
                binding.locationTextView.setEnabled(true);
                binding.emailTextView.setEnabled(true);
                binding.ageTextView.setEnabled(true);
//                binding.mobileTextView.setEnabled(true);

                binding.profileNameText.setEnabled(true);
                binding.profileNameText.requestFocus();
                binding.profileNameText.setBackgroundResource(R.drawable.edit_text_design_white);

                binding.locationTextView.setEnabled(true);
                binding.locationTextView.setBackgroundResource(R.drawable.edit_text_design_white);

                binding.emailTextView.setEnabled(true);
                binding.emailTextView.setBackgroundResource(R.drawable.edit_text_design_white);

                binding.ageTextView.setEnabled(true);
                binding.ageTextView.setBackgroundResource(R.drawable.edit_text_design_white);

//                binding.mobileTextView.setEnabled(true);
//                binding.mobileTextView.setBackgroundResource(R.drawable.edit_text_design_white);



            } else {

                ProgressDialog pd = new ProgressDialog(requireActivity());
                pd.setMessage("Loading ...");
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                Log.e("saveImg", "s" + imageurl);

                RequestBody age = RequestBody.create(okhttp3.MediaType.parse("text/plain"), binding.ageTextView.getText().toString().trim());
                RequestBody name = RequestBody.create(okhttp3.MediaType.parse("text/plain"), binding.profileNameText.getText().toString().trim());
                RequestBody location = RequestBody.create(okhttp3.MediaType.parse("text/plain"), binding.locationTextView.getText().toString().trim());
                RequestBody email = RequestBody.create(okhttp3.MediaType.parse("text/plain"), binding.emailTextView.getText().toString().trim());

                if (!imageurl.equals("")) {
                    File file1 = new File(imageurl);
                    MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("profileimage", file1.getName(), RequestBody.create(okhttp3.MediaType.parse("image/*"), file1));

                    postuserprofilecall = api.postuserprofileapi(email, name, age, location, imagePart1, "Token " + editor.getString("access_token", ""));

                    postuserprofilecall.clone().enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            pd.dismiss();
                            if (response.code() == 200) {
                                setDummy1();
//                            JSONObject jObjError = null;
                                Log.e("success", "success");
                                binding.profileNameText.setBackgroundResource(0);
                                binding.locationTextView.setBackgroundResource(0);
                                binding.emailTextView.setBackgroundResource(0);
                                binding.ageTextView.setBackgroundResource(0);


                                binding.editButton.setText("Edit Profile");
//                                jObjError = new JSONObject(response.body().toString());

//                                ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());
                                ToastUtility.successToast(getActivity(), "successfully Data Updated ");

//                            Log.e("jObjError", jObjError.toString());


                            }
//                            if (imageurl==null){
//
//                                ToastUtility.errorToast(getActivity(), "Error : " + " " + response.message() + +response.code());
//
//
//                                try {
//                                    assert response.errorBody() != null;
//                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
//
//
//                                } catch (Exception e) {
//                                    ToastUtility.errorToast(getActivity(), e.getMessage());
//
//                                }
//                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            pd.dismiss();
                            ToastUtility.errorToast(getActivity(),"errorr"+ t.getMessage());

                        }
                    });

                }
                if (imageurl.equals("")){

                    String email1= binding.emailTextView.getText().toString().trim();
                    String name1= binding.profileNameText.getText().toString().trim();
                    String age1= binding.ageTextView.getText().toString().trim();
                    String location1= binding.locationTextView.getText().toString().trim();

                    Log.e("values",""+email1+""+name1+""+age1+""+location1);
                    Call<UserProfileModel> call = api.postuserprofileapi1(email1, name1, age1, location1, "Token " + editor.getString("access_token", ""));
                    call.enqueue(new Callback<UserProfileModel>() {
                        @Override
                        public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                            if (response.code()==200)
                            {
                                pd.dismiss();
                                setDummy1();
//                            JSONObject jObjError = null;
                                Log.e("success", "success");
                                binding.profileNameText.setBackgroundResource(0);
                                binding.locationTextView.setBackgroundResource(0);
                                binding.emailTextView.setBackgroundResource(0);
                                binding.ageTextView.setBackgroundResource(0);


                                binding.editButton.setText("Edit Profile");
//                                jObjError = new JSONObject(response.body().toString());

//                                ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());
                                ToastUtility.successToast(getActivity(), "successfully Data Updated ");

                            }else
                                Toast.makeText(getContext(), "error"+response.message(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UserProfileModel> call, Throwable t) {
                            Toast.makeText(getContext(), "fail"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        });



        initClickListeners();

        setDummyData();

    }

    private void pickprofileimage() {

        Intent intent = new Lassi(requireActivity())
                .with(LassiOption.CAMERA_AND_GALLERY) // choose Option CAMERA, GALLERY or CAMERA_AND_GALLERY
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE) // MediaType : VIDEO IMAGE, AUDIO OR DOC
                .setCompressionRation(10) // compress image for single item selection (can be 0 to 100)
//                .setMinTime(15) // for MediaType.VIDEO only
//                .setMaxTime(30) // for MediaType.VIDEO only
                .setSupportedFileTypes("jpg", "jpeg", "png") // Filter by limited media format (Optional)
//                .setMinFileSize(100) // Restrict by minimum file size
//                .setMaxFileSize(1024) //  Restrict by maximum file size
                .disableCrop() // to remove crop from the single image selection (crop is enabled by default for single image)
                /*
                 * Configuration for  UI
                 */
                .setStatusBarColor(R.color.colorPrimaryDark)
                .setToolbarResourceColor(R.color.colorPrimary)
                .setProgressBarColor(R.color.colorAccent)
                .setPlaceHolder(R.drawable.ic_image_placeholder)
                .setErrorDrawable(R.drawable.ic_image_placeholder)
                .setCropType(CropImageView.CropShape.RECTANGLE) // choose shape for cropping after capturing an image from camera (for MediaType.IMAGE only)
                .setCropAspectRatio(1, 1) // define crop aspect ratio for cropping after capturing an image from camera (for MediaType.IMAGE only)
                .enableFlip() // Enable flip image option while image cropping (for MediaType.IMAGE only)
                .enableRotate() // Enable rotate image option while image cropping (for MediaType.IMAGE only)
                .enableActualCircleCrop() // Enable actual circular crop (only for MediaType.Image and CropImageView.CropShape.OVAL)
                .build();

//        someActivityResultLauncher.launch(intent);

    }

    MultipartBody.Part imagePart;

//
//    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//
//                        assert data != null;
//                        ArrayList<MiMedia> selectedMedia = (ArrayList<MiMedia>) data.getSerializableExtra(KeyUtils.SELECTED_MEDIA);
//                        // Do needful with your selectedMedia
//                        Log.d("Imagepicker", selectedMedia.get(0).getPath());
//
//                        binding.profileImageView.setImageBitmap(BitmapFactory.decodeFile(selectedMedia.get(0).getPath()));
//
//                        File file = new File(selectedMedia.get(0).getPath());
//
//                        imagePart = MultipartBody.Part.createFormData("profileimage", file.getName(), RequestBody.create(okhttp3.MediaType.parse("image/*"), file));
//
//                    }
//                }
//            });

    private void initClickListeners() {

    }

    Call<UserProfileModel> call;

    SharedPreferences editor;

    private void setDummyData() {

        binding.profileImageView.setEnabled(false);
        binding.profileNameText.setEnabled(false);
        binding.locationTextView.setEnabled(false);
        binding.emailTextView.setEnabled(false);
        binding.ageTextView.setEnabled(false);
        binding.mobileTextView.setEnabled(false);

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


//        ProgressDialog pd = new ProgressDialog(requireActivity());
//        pd.setMessage("Loading ...");
//        pd.setIndeterminate(true);
//        pd.setCancelable(false);
//        pd.show();

        call = api.userprofileapi("Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
//                pd.dismiss();
                if (response.code() == 200) {

                    UserProfileModel model = response.body();

//                    Glide.with(context).load(Api.Image_URL + model.data.profileimage).into(binding.profileImageView);






                    binding.ageTextView.setText(model.data.age);
                    binding.emailTextView.setText(model.data.email);
                    binding.profileNameText.setText(model.data.name);
                    binding.mobileTextView.setText(model.data.mobile);
//                    binding.locationTextView.setText(model.data.location);
                    Log.d("location","locatopn"+model.data.location);

                    if (model.data.location != null)
                        binding.locationTextView.setText(model.data.location);
                    else
                        binding.locationTextView.setText("No location");

                    if (model.data.profileimage != null) {
                        Picasso.get().load(Api.Image_URL + model.data.profileimage).into(binding.profileImageView);
                    }
                    else {
                        Glide.with(context).load(R.drawable.ic_profile)
                                .into(binding.profileImageView);
                    }
                    progressDialog.dismiss();
                    binding.ScrollLayout.setVisibility(View.VISIBLE);

//                    if (model.data.profileimage != null) {
//                        Glide.with(context).load(Api.Image_URL + model.data.profileimage).placeholder(R.drawable.placeholder_011)
//                                .into(binding.profileImageView);
//                    }
//                    else {
//                        Glide.with(context).load(R.drawable.placeholder_011)
//                                .into(binding.profileImageView);
//                    }

                } else {

                    ToastUtility.errorToast(getActivity(), "Error : "  + " " + response.code());

                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string().replace("\"", ""));


                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), "error1"+e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
//                pd.dismiss();
                progressDialog.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());
            }
        });


        String name = "John Smith";

        String age = "27 Year Old";

        String location = "Australia";

        String email = "johnsmith@gmail.com";


    }
    public void setDummy1(){
        binding.profileImageView.setEnabled(false);
        binding.profileNameText.setEnabled(false);
        binding.locationTextView.setEnabled(false);
        binding.emailTextView.setEnabled(false);
        binding.ageTextView.setEnabled(false);
        binding.mobileTextView.setEnabled(false);

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


        ProgressDialog pd = new ProgressDialog(requireActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        call = api.userprofileapi("Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    UserProfileModel model = response.body();

//                    Glide.with(context).load(Api.Image_URL + model.data.profileimage).into(binding.profileImageView);




                    binding.ageTextView.setText(model.data.age);
                    binding.emailTextView.setText(model.data.email);
                    binding.profileNameText.setText(model.data.name);
                    binding.mobileTextView.setText(model.data.mobile);
//                    binding.locationTextView.setText(model.data.location);
                    Log.d("location","locatopn"+model.data.location);

                    if (model.data.location != null)
                        binding.locationTextView.setText(model.data.location);
                    else
                        binding.locationTextView.setText("No location");




//                    if (model.data.profileimage != null) {
//                        Glide.with(context).load(Api.Image_URL + model.data.profileimage).placeholder(R.drawable.placeholder_011)
//                                .into(binding.profileImageView);
//                    }
//                    else {
//                        Glide.with(context).load(R.drawable.placeholder_011)
//                                .into(binding.profileImageView);
//                    }

                } else {

                    ToastUtility.errorToast(getActivity(), "Error : "  + " " + response.code());

                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string().replace("\"", ""));


                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), "file"+e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), "error"+t.getMessage());
            }
        });


        String name = "John Smith";

        String age = "27 Year Old";

        String location = "Australia";

        String email = "johnsmith@gmail.com";

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_SHERGIL && resultCode == Activity.RESULT_OK && data != null)
        {


            Uri uri=data.getData();
//             imageFile = new File(path.getPath());
//             imageurl=path.getPath().toString();
//               imageurl= new File(getRealPathFromURI(path));
//            imageurl = getRealPathFromUri(requireActivity(),path);

//            imageurl= RealPathUtil.getRealPath(binding.getRoot().getContext(),uri);
//            Log.e("imgUrlResult","i"+imageurl);
//            String path = getRealPathFromURI(getActivity(), uri);
//            if (imageurl==null)
//            {
//
//            }
//            imageurl=getImagePathFromInputStreamUri(uri);
            String path = null;
            if (Build.VERSION.SDK_INT < 11)
            {
                imageurl = RealPathUtil.getRealPathFromURI_BelowAPI11(getContext(), uri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
            {
                imageurl = RealPathUtil.getRealPathFromURI_API11to18(getContext(), uri);
            }
            // SDK > 19 (Android 4.4)
            else
            {
                imageurl = RealPathUtil.getRealPathFromURI_API19(getContext(), uri);
            }

            Log.e("path", "File Path: " + imageurl);
            // Get the file instance
//            File file = new File(path);
            Log.e("imgUrlResult","i"+imageurl);

//            Log.e("Check", "URI Path : " + uri.getPath());
//            Log.e("Check", "Real Path : " + path);


//            File file = new File(path.getPath());
//            RequestBody requestFile=RequestBody.create(MediaType.parse("multipart/form-data"), file);
//             body = try {
////                bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
////
////               UploadImage();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            Bitmap bitmap= null;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            binding.profileImageView.setImageBitmap(bitmap);
//

        }
    }

    public String getImagePathFromInputStreamUri(Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream);

                filePath = photoFile.getPath();

            } catch (FileNotFoundException e) {
                // log
            } catch (IOException e) {
                // log
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private File createTemporalFileFrom(InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile();
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
    Date now = new Date();

    private File createTemporalFile() {
        Log.e("file","tempFile"+formatter.format(now)+".jpg");
        return new File(getActivity().getExternalCacheDir(), "tempFile"+formatter.format(now)+".jpg"); // context needed

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        call.cancel();
    }
}