package com.developers.vmrapp.fragments.privacy;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.databinding.FragmentPrivacyBinding;
import com.developers.vmrapp.models.PrivacyModel;
import com.developers.vmrapp.models.TermsPrivacyModel;
import com.developers.vmrapp.service.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivacyFragment extends Fragment {

    private NavController navController;

    private FragmentPrivacyBinding binding;

    Api api;

    SharedPreferences pref;

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

        pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        api = retrofit.create(Api.class);

        binding = FragmentPrivacyBinding
                .inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        initNavController();

        initClickListener();

        loaddata();

    }

    private void loaddata() {
        Call<TermsPrivacyModel> call= api.GetprivacyTerms("Token "+pref.getString("access_token", ""));
        call.enqueue(new Callback<TermsPrivacyModel>() {
            @Override
            public void onResponse(Call<TermsPrivacyModel> call, Response<TermsPrivacyModel> response) {
                if (response.code() == 200){
                    assert response.body() != null;
                    TermsPrivacyModel termsPrivacyModel=response.body();
                    for (int i=0;i<termsPrivacyModel.data.size();i++)
                    {
                        if (termsPrivacyModel.data.get(i).title.equals("Privacy"))
                        {
                            binding.privacyPolicyText.setText(termsPrivacyModel.data.get(i).description);
                            Log.e("text",""+termsPrivacyModel.data.get(i).description);
                        }

//                        Log.e("text1",""+termsPrivacyModel.data.get(i).title);

                    }


                }
                else {
                    try {
                        Toast.makeText(requireContext(), new Gson().toJson(response.errorBody().string().replace("\"","")+" "+response.code()), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TermsPrivacyModel> call, Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initClickListener() {

        binding.backButton.setOnClickListener(v -> gotoBackScreen());

    }

    private void gotoBackScreen() {

        navController.navigateUp();

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }
}