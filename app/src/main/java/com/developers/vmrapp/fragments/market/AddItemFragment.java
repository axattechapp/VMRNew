package com.developers.vmrapp.fragments.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentAddItemBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.DummyMarket;
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

import static android.content.Context.MODE_PRIVATE;


public class AddItemFragment extends Fragment {

    private NavController navController;

    private FragmentAddItemBinding binding;
    Api api;

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

        binding = FragmentAddItemBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    SharedPreferences editor;

    Call<DummyMarket.Datum> call;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


        initNavController();
        initClickListener();

        String[] array = {"New", "Used"};


        ArrayAdapter<String> adapter;


        adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, array);

        binding.marketItemConditionEdit.setAdapter(adapter);

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    private void initClickListener() {

        binding.backButton.setOnClickListener(v -> gotoBackScreen());
        binding.postButton.setOnClickListener(v -> performpostoperation());
        binding.marketItemImageView.setOnClickListener(v -> selectvehicleimage());

    }

    private void gotoBackScreen() {

        navController.navigateUp();

    }

    private static final Integer REQUEST_SHERGIL = 1234;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHERGIL) {
            List<Uri> image = Shergil.Companion.getMediaUris(data);

            Log.d("imageurl", image.get(0).toString());

            String filepath = getRealPathFromUri(requireActivity(), Uri.parse(image.get(0).toString()));


            Log.d("imageurl", filepath + " ");

            binding.marketItemImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

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

    private void performpostoperation() {

        if (!imageurl.isEmpty()) {
            if (!binding.marketItemNameEdit.getText().toString().isEmpty()) {
                binding.marketItemNameInput.setError(null);
                if (!binding.marketItemQuantityEdit.getText().toString().isEmpty()) {
                    binding.marketItemQuantityInput.setError(null);
                    if (!binding.marketItemMakeEdit.getText().toString().isEmpty()) {
                        binding.marketItemMakeInput.setError(null);
                        if (!binding.marketItemPriceEdit.getText().toString().isEmpty()) {
                            binding.marketItemPriceInput.setError(null);
                            if (!binding.marketItemConditionEdit.getText().toString().isEmpty()) {
                                binding.marketItemConditionInput.setError(null);
                                if (!binding.marketItemDescriptionEdit.getText().toString().isEmpty()) {
                                    binding.marketItemDescriptionInput.setError(null);
                                    boolean checkedpermission;
                                    Log.d("checkedornot",binding.permissionBox.isChecked()+"");
                                    if (binding.permissionBox.isChecked()) {
                                        checkedpermission = true;
                                    } else {
                                        checkedpermission = false;
                                    }

                                    SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);
                                    RequestBody marketItemPriceEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemPriceEdit.getText().toString().trim());
                                    RequestBody marketItemNameEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemNameEdit.getText().toString().trim());
                                    RequestBody marketItemQuantityEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemQuantityEdit.getText().toString().trim());
                                    RequestBody marketItemMakeEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemMakeEdit.getText().toString().trim());
                                    RequestBody marketItemConditionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemConditionEdit.getText().toString().trim());
                                    RequestBody marketItemDescriptionEdit = RequestBody.create(MediaType.parse("text/plain"), binding.marketItemDescriptionEdit.getText().toString().trim());
                                    RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pref.getString("userId", ""));


                                    ProgressDialog pd = new ProgressDialog(getActivity());
                                    pd.setMessage("Loading ...");
                                    pd.setIndeterminate(true);
                                    pd.setCancelable(false);
                                    pd.show();

                                    File file = new File(imageurl);
                                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("partimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                                    call = api.marketplaceviewapi(marketItemNameEdit, marketItemDescriptionEdit, marketItemQuantityEdit, marketItemMakeEdit, marketItemConditionEdit, marketItemPriceEdit, checkedpermission, userId, imagePart, "Token " + pref.getString("access_token", ""));
                                    call.enqueue(new Callback<DummyMarket.Datum>() {
                                        @Override
                                        public void onResponse(Call<DummyMarket.Datum> call, Response<DummyMarket.Datum> response) {
                                            pd.dismiss();
                                            if (response.code() == 200) {
                                                DummyMarket.Datum model = response.body();

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
                                        public void onFailure(Call<DummyMarket.Datum> call, Throwable t) {
                                            pd.dismiss();
                                            ToastUtility.errorToast(getActivity(), t.getMessage());

                                        }
                                    });


                                } else {
                                    binding.marketItemDescriptionEdit.setError("Pls enter description");
                                }
                            } else {
                                binding.marketItemConditionInput.setError("Pls select condition");
                            }
                        } else {
                            binding.marketItemPriceInput.setError("pLs enter price");
                        }

                    } else {
                        binding.marketItemMakeEdit.setError("pLs enter model");
                    }
                } else {
                    binding.marketItemQuantityInput.setError("Pls enter Quantity");
                }

            } else {
                binding.marketItemNameInput.setError("Pls enter name");
            }
        } else {
            ToastUtility.warningToast(requireActivity(), "Pls Select image.");
        }


    }

}