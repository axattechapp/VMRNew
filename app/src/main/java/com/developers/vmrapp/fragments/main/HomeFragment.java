package com.developers.vmrapp.fragments.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.activities.PaymentActivity;
import com.developers.vmrapp.activities.auth.LoginActivity;
import com.developers.vmrapp.activities.auth.StartActivity;
import com.developers.vmrapp.activities.vehicle.AddSuppliersActivity;
import com.developers.vmrapp.models.CheckUserExistsModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.activities.vehicle.AddVehicleActivity;
import com.developers.vmrapp.adapters.HomeAdapter;
import com.developers.vmrapp.databinding.FragmentHomeBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.DummyVehicle;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.service.Api;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements HomeAdapter.OnVehicleClickListener {

    private HomeAdapter homeAdapter;

    private FragmentHomeBinding binding;

    private NavController navController;

    private final List<DummyVehicle> vehicleModelList = new ArrayList<>();

    OkHttpClient client;
    Gson gson;
    Retrofit retrofit;
    Api api;
    Call<HomeVehicleDetailModel> call;
    Call<CheckUserExistsModel> call1;

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {
        binding = FragmentHomeBinding
                .inflate(inflater, container, false);


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





        return binding.getRoot();

    }

    SharedPreferences editor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);




        Log.e("apptoken", editor.getString("access_token", "") + " " + editor.getString("userId", ""));

        checkUserExistsOrNot();


        initHomeAdapter();

        initNavController();

        setDummyData();


        binding.nestedscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY && binding.addNewRecordButton.isShown()) {
//                    binding.addNewRecordButton.setVisibility(View.GONE);
                    binding.addNewRecordButton.setVisibility(View.VISIBLE);
                    Log.i("scroll", "Scroll DOWN....");
                }
                if (scrollY < oldScrollY) {
                    binding.addNewRecordButton.setVisibility(View.VISIBLE);
                    //you can write here
                    Log.i("scroll", "Scroll UP.....");
                }

                if (scrollY == 0 && binding.addNewRecordButton.isShown()) {
                    binding.addNewRecordButton.setVisibility(View.VISIBLE);
                    Log.i("scroll", "TOP SCROLL.....");
                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight()) && binding.addNewRecordButton.isShown()) {
                    Log.i("scroll", "BOTTOM SCROLL...");
//                    binding.addNewRecordButton.setVisibility(View.GONE);
                    binding.addNewRecordButton.setVisibility(View.VISIBLE);
                }
            }
        });


        binding.addNewRecordButton.setOnClickListener(v -> {
            Log.e("redirection",""+model.plan_redirection);
            if (model.plan_redirection) {
                Intent intent = new Intent(requireContext(), PaymentActivity.class);
                intent.putExtra("unpaid_count", "0");
                intent.putExtra("flow_type", "inapp");
                startActivity(intent);

            } else {
                Intent i = new Intent(getActivity(), AddVehicleActivity.class);
                startActivity(i);
            }

        });

    }

    private void checkUserExistsOrNot() {
        call1 = api.checkUserExists(editor.getString("access_token", ""));
        call1.enqueue(new Callback<CheckUserExistsModel>() {
            @Override
            public void onResponse(Call<CheckUserExistsModel> call, Response<CheckUserExistsModel> response) {
                if (response.code()==200)
                {
                    CheckUserExistsModel checkUserExistsModel=response.body();
                    if (checkUserExistsModel.data==true)
                    {

                        Log.e("continue","continue");

                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Sorry,your account is deleted",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE).edit();
                        editor.clear().apply();
                        Intent intent=new Intent(getActivity().getApplicationContext(), StartActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckUserExistsModel> call, Throwable t) {
                Log.e("API error","error"+t.getMessage());

            }
        });
    }

    HomeVehicleDetailModel model;

    private void initHomeAdapter() {

        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();


        call = api.getvehicledetail("Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<HomeVehicleDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<HomeVehicleDetailModel> call, Response<HomeVehicleDetailModel> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    model = response.body();

                    navController.navigateUp();

                    homeAdapter = new HomeAdapter(getContext(), model, HomeFragment.this);

                    binding.homeRecyclerView.setAdapter(homeAdapter);
                    pd.dismiss();


                } else {
                    pd.dismiss();
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        ToastUtility.errorToast(getActivity(), "Error1 : " + jObjError.getString("msg") + " " + response.code());

                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), "GetVehicle"+e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeVehicleDetailModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(),"GetVehicleFail"+t.getMessage());

            }
        });


    }

    private void setDummyData() {


        vehicleModelList.add(new DummyVehicle("Mitsubishi Lancer", "2018", R.drawable.placeholder_010, 31564, "V9829876891296", 50.0));
        vehicleModelList.add(new DummyVehicle("Mitsubishi Lancer", "2018", R.drawable.placeholder_010, 31564, "V9829876891296", 50.0));
        vehicleModelList.add(new DummyVehicle("Mitsubishi Lancer", "2018", R.drawable.placeholder_010, 31564, "V9829876891296", 50.0));
        vehicleModelList.add(new DummyVehicle("Mitsubishi Lancer", "2018", R.drawable.placeholder_010, 31564, "V9829876891296", 50.0));

//        homeAdapter.notifyDataSetChanged();


    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    @Override
    public void onVehicleClick(HomeVehicleDetailModel.Datum position) {

        gotoViewVehicleScreen(position);

    }

    private void gotoViewVehicleScreen(HomeVehicleDetailModel.Datum position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("HomeVehicleDetailModel", position);

        navController.navigate(R.id.action_navigation_home_to_navigation_view_vehicle, bundle);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        call.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
//        call.cancel();
    }

    @Override
    public void onDestroy() {

        try {
            call.cancel();
        }catch (Exception e)
        {
            Log.e("distroy","d"+e.getMessage());
        }
       super.onDestroy();
    }

}