package com.developers.vmrapp.fragments.vehicle;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.developers.vmrapp.databinding.FragmentTransferVehicleBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.models.TransferVehicleapiModel;
import com.developers.vmrapp.service.Api;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class TransferVehicleFragment extends Fragment {

    private NavController navController;

    private FragmentTransferVehicleBinding binding;

    Api api;
    Call<TransferVehicleapiModel> call;

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

        binding = FragmentTransferVehicleBinding
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
        Log.e("vehicle1",data.id.toString());
        Log.e("user1",data.user.toString());

    }

    private void initClickListeners() {
        if (data.vehicleModel!=null){

            binding.transferTitleEdit.setText(data.vehicleModel);
            binding.transferTitleEdit.setTextColor(getResources().getColor(android.R.color.black));
            binding.transferTitleEdit.setHint("");
        }
        else {
            binding.transferTitleEdit.setText(data.vehicleVinnumber);
        }


        binding.backButton.setOnClickListener(v -> gotoBackScreen());
        binding.postButton.setOnClickListener(v -> performpostoperation());

    }

    private void performpostoperation() {

        SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        if (!binding.transferTitleEdit.getText().toString().isEmpty()) {
            binding.selectVehicleInput.setError(null);
            if (!binding.transferNewOwnerEdit.getText().toString().isEmpty()) {
                binding.transferNewOwnerInput.setError(null);
                if (!binding.mobileNumberEdit.getText().toString().isEmpty()) {
                    binding.mobileNumberInput.setError(null);

                    ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage("Loading ...");
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.show();

                    JsonObject object = new JsonObject();
                    object.addProperty("vehicle", data.id.toString());
                    object.addProperty("newownername", binding.transferNewOwnerEdit.getText().toString());
                    Log.e("vehicle",data.id.toString());
                    Log.e("user",data.user.toString());
//                    object.addProperty("newownername", binding.transferNewOwnerEdit.getText().toString().trim());
                    object.addProperty("mobile", binding.mobileNumberEdit.getText().toString().trim());
                    object.addProperty("user", data.user.toString());
//                    object.addProperty("user", pref.getString("userId", ""));

                    call = api.transfervehicleapi(object, "Token " + pref.getString("access_token", ""));

                    call.enqueue(new Callback<TransferVehicleapiModel>() {
                        @Override
                        public void onResponse(Call<TransferVehicleapiModel> call, Response<TransferVehicleapiModel> response) {
                            pd.dismiss();
                            if (response.code() == 200) {

                                TransferVehicleapiModel model = response.body();

                                navController.navigateUp();

                                ToastUtility.successToast(requireContext(), "Success");

                            } else {
                                try {

                                    assert response.errorBody() != null;
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());

                                    ToastUtility.errorToast(getActivity(), jObjError.getString("data") );

                                } catch (Exception e) {
                                    ToastUtility.errorToast(getActivity(), e.getMessage());

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TransferVehicleapiModel> call, Throwable t) {
                            pd.dismiss();
                            ToastUtility.errorToast(getActivity(), "fail"+t.getMessage());
                        }
                    });


                } else {
                    binding.mobileNumberInput.setError("Pls enter mobile number");
                }

            } else {
                binding.transferNewOwnerInput.setError("Pls enter new owner name");
            }

        } else {
            binding.selectVehicleInput.setError("Pls enter title");
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