package com.developers.vmrapp.fragments.main;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.R;
import com.developers.vmrapp.activities.auth.StartActivity;
import com.developers.vmrapp.databinding.FragmentSettingsBinding;
import com.developers.vmrapp.models.UserProfileForNotification;
import com.developers.vmrapp.service.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SettingsFragment extends Fragment {

    private NavController navController;

    private FragmentSettingsBinding binding;

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

        binding = FragmentSettingsBinding
                .inflate(inflater, container, false);

        SharedPreferences editor=getActivity().getSharedPreferences("my_token",MODE_PRIVATE);
        Call<UserProfileForNotification> call=api.getRemainderValue("Token " + editor.getString("access_token", ""));
        call.enqueue(new Callback<UserProfileForNotification>() {
            @Override
            public void onResponse(Call<UserProfileForNotification> call, Response<UserProfileForNotification> response) {
                if (response.code()==200)
                {
                    UserProfileForNotification UserProfileForNotification=response.body();
                    Log.e("push_notification",""+UserProfileForNotification.data.push_notification);
                    if (UserProfileForNotification.data.push_notification==true)
                    {
                        binding.notificationSwitch1.setChecked(true);
                    }
                    else
                        binding.notificationSwitch1.setChecked(false);
                }else
                {
                    Log.e("error",""+response.code());
                    Log.e("error",""+response.message());
                }
            }

            @Override
            public void onFailure(Call<UserProfileForNotification> call, Throwable t) {
                Log.e("fail",""+t.getMessage());

            }
        });
        binding.notificationSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("checkValue",""+isChecked);
                SharedPreferences editor=getActivity().getSharedPreferences("my_token",MODE_PRIVATE);
                Call<UserProfileForNotification> call=api.setRemainderValue("Token " + editor.getString("access_token", ""),isChecked);
                call.enqueue(new Callback<UserProfileForNotification>() {
                    @Override
                    public void onResponse(Call<UserProfileForNotification> call, Response<UserProfileForNotification> response) {
                        if (response.code()==200)
                        {
                            UserProfileForNotification UserProfileForNotification=response.body();
                            Log.e("push_notification",""+UserProfileForNotification.data.push_notification);
                        }else
                        {
                            Log.e("error",""+response.code());
                            Log.e("error",""+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileForNotification> call, Throwable t) {
                        Log.e("fail",""+t.getMessage());

                    }
                });
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        initNavController();

        initClickListener();


        binding.logoutButton.setOnClickListener(v -> logout());

    }

    private void logout() {

        SharedPreferences editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);


        ProgressDialog pd = new ProgressDialog(requireActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        Call<Void> call = api.logoutapi("Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    Intent intent = new Intent(requireActivity(), StartActivity.class);
                    startActivity(intent);
                    requireActivity().finish();

                    SharedPreferences.Editor editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE).edit();
                    editor.clear().apply();

                } else {
                    try {
                        Toast.makeText(requireActivity(), new Gson().toJson(response.errorBody().string().replace("\"", "")) + " " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(requireActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    private void initClickListener() {

        binding.privacyView.setOnClickListener(v -> gotoPrivacyScreen());

        binding.termsView.setOnClickListener(v -> gotoTermsScreen());

    }

    private void gotoPrivacyScreen() {

        navController.navigate(R.id.action_navigation_setting_to_navigation_privacy);

    }

    private void gotoTermsScreen() {

        navController.navigate(R.id.action_navigation_setting_to_navigation_terms);

    }


}