package com.developers.vmrapp.activities.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.vmrapp.databinding.ActivityStartBinding;
import com.developers.vmrapp.fragments.main.HomeFragment;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initClickListeners();

    }

    private void initClickListeners() {

        binding.loginButton.setOnClickListener(v -> gotoLoginScreen());

        binding.registerButton.setOnClickListener(v -> gotoRegisterScreen());

    }

    private void gotoLoginScreen() {


        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }

    private void gotoRegisterScreen() {

        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}