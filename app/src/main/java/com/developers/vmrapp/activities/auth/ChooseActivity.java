package com.developers.vmrapp.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.vmrapp.databinding.ActivityChooseBinding;

public class ChooseActivity extends AppCompatActivity {

    private ActivityChooseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChooseBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initClickListeners();

    }

    private void initClickListeners() {

        binding.signupEmailButton
                .setOnClickListener(v -> gotoRegisterScreen());

    }

    private void gotoRegisterScreen() {

        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}