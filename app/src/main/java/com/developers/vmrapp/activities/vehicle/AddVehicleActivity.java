package com.developers.vmrapp.activities.vehicle;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.developers.vmrapp.DialogImageChooser;
import com.developers.vmrapp.ExifUtils;
import com.developers.vmrapp.R;
import com.developers.vmrapp.RealPathUtil;
import com.developers.vmrapp.RealPathUtils;
import com.developers.vmrapp.databinding.ActivityAddVehicleBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kinnerapriyap.sugar.Shergil;
import com.kinnerapriyap.sugar.choice.MimeType;
import com.lassi.common.utils.KeyUtils;
import com.lassi.data.media.MiMedia;
import com.lassi.domain.media.LassiOption;
import com.lassi.domain.media.MediaType;
import com.lassi.presentation.builder.Lassi;
import com.lassi.presentation.cropper.CropImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddVehicleActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_WRITE_PERMISSION = 200;
    private ActivityAddVehicleBinding binding;
    private static final Integer REQUEST_SHERGIL = 1234;
    public static final int RequestPermissionCode = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_write_EXTERNAL_STORAGE = 123;
    String imageurl = "";
    String currentPhotoPath;
    Uri photoURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddVehicleBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initClickListeners();

        EnableRuntimePermission();
        requestPermission();


        binding.vehicleImageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }else {

                    new DialogImageChooser(AddVehicleActivity.this, selectedOption -> {
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
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                photoFile = createImageFile();
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                     photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, 7);
                                }
                            }
//                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, 7);
                        } else if (selectedOption.equalsIgnoreCase("gallery")) {
                            if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {


                                ActivityCompat.requestPermissions(AddVehicleActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            } else {
                                Intent intent = new Intent();
                                intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setAction(Intent.ACTION_PICK);

                                startActivityForResult(intent, 10);
                            }
                        }
                    }).show(getSupportFragmentManager(), "add_photo_dialog");
                }



//                pickprofileimage();
//                Shergil.Companion.create(this)
//                        .mimeTypes(MimeType.Companion.getIMAGES())
//                        .showDisallowedMimeTypes(false)
//                        .numOfColumns(2)
//                        .maxSelectable(1)
//                        .theme(R.style.Shergil)
//                        .allowPreview(true)
//                        .allowCamera(true)
//                        .showDeviceCamera(false)
//                        .showCameraFirst(false)
//                        .orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                        .withRequestCode(REQUEST_SHERGIL);



        });

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
//            openFilePicker();
        }
    }
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
//            List<Uri> image = Shergil.Companion.getMediaUris(data);
//
//            Log.d("imageurl", image.get(0).toString());
//
//            String filepath = getRealPathFromUri(this, Uri.parse(image.get(0).toString()));
//
//
//            Log.d("imageurl", filepath + " ");
//
//            binding.vehicleImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
//
//
//            imageurl = filepath;
//
//        }
//    }

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

    private void initClickListeners() {

        binding.backButton.setOnClickListener(v ->
                onBackPressed());

        binding.nextButton.setOnClickListener(v ->
                gotoAddDetailScreen());

    }

    private void gotoAddDetailScreen() {
        if (!imageurl.isEmpty()) {
            if (!binding.vehicleIdentificationEdit.getText().toString().isEmpty()) {
                binding.vehicleIdentificationInput.setError(null);
                if (!binding.vehicleRegistrationEdit.getText().toString().isEmpty()) {
                    binding.vehicleRegistrationInput.setError(null);

                    Intent intent = new Intent(getApplicationContext(), AddDetailActivity.class);
                    intent.putExtra("vehiclename", binding.vehicleIdentificationEdit.getText().toString().trim());
                    intent.putExtra("imageurl", imageurl);
                    intent.putExtra("vehiclenumber", binding.vehicleRegistrationEdit.getText().toString().trim());
                    intent.putExtra("registertype", getIntent().getStringExtra("registertype"));
                    startActivity(intent);

                } else {
                    binding.vehicleRegistrationInput.setError("Enter vehicle Registration number");
                }
            } else {
                binding.vehicleIdentificationInput.setError("Enter vehicle type");
            }

        } else {
            ToastUtility.warningToast(this, "Pls Select image");
        }


    }
    private void pickprofileimage() {

        Intent intent = new Lassi(AddVehicleActivity.this)
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
//                        imageurl=selectedMedia.get(0).getPath();
//
//                        binding.vehicleImageView.setImageBitmap(BitmapFactory.decodeFile(selectedMedia.get(0).getPath()));
//
//                        File file = new File(selectedMedia.get(0).getPath());
//
////                        imagePart = MultipartBody.Part.createFormData("profileimage", file.getName(), RequestBody.create(okhttp3.MediaType.parse("image/*"), file));
//
//                    }
//
//                }
                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                    if (requestCode == 7 && resultCode == RESULT_OK) {
//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        Bitmap bitmap = (Bitmap) data.getExtras();
//                        binding.vehicleImageView.setImageBitmap(bitmap);

                        Log.e("uri1",""+photoURI);
                        imageurl=currentPhotoPath;
                        Log.e("imageurl",""+imageurl);
                        try {
                            Picasso.get().load(photoURI).into(binding.vehicleImageView);
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

                    }else {
                        if (requestCode == 10 && resultCode == RESULT_OK)
                        {
                            Uri uri=data.getData();
                            try {
                                Picasso.get().load(uri).into(binding.vehicleImageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT < 11)
                            {
                                imageurl = RealPathUtil.getRealPathFromURI_BelowAPI11(AddVehicleActivity.this, uri);
                            }
                            // SDK >= 11 && SDK < 19
                            else if (Build.VERSION.SDK_INT < 19)
                            {
                                imageurl = RealPathUtil.getRealPathFromURI_API11to18(AddVehicleActivity.this, uri);
                            }
                            // SDK > 19 (Android 4.4)
                            else
                            {
                                imageurl = RealPathUtil.getRealPathFromURI_API19(AddVehicleActivity.this, uri);
                            }
                            Log.e("path", "File Path: " + imageurl);
//                            Bitmap bitmap= null;
//                            try {
//                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            binding.vehicleImageView.setImageBitmap(bitmap);
                        }
                    }
                }
                public void EnableRuntimePermission(){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddVehicleActivity.this,
                            Manifest.permission.CAMERA)) {
                        Toast.makeText(AddVehicleActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(AddVehicleActivity.this,new String[]{
                                Manifest.permission.CAMERA}, RequestPermissionCode);
                    }
                }
//                @Override
//                public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
//                    super.onRequestPermissionsResult(requestCode, permissions, result);
//                    switch (requestCode) {
//                        case RequestPermissionCode:
//                            if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
//                                Toast.makeText(AddVehicleActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(AddVehicleActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//                            }
//                            break;
//                    }
//                }
    public Uri getImageUri1(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI1(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
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

        binding.vehicleImageView.setImageBitmap(b);

        // image.setImageBitmap(bitmap);
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_write_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_write_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_write_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(AddVehicleActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // do what you want;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }

    }
            }

