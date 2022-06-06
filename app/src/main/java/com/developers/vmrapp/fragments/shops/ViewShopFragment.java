package com.developers.vmrapp.fragments.shops;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentViewShopBinding;
import com.developers.vmrapp.models.StoresSHopApiModel;
import com.developers.vmrapp.service.Api;

public class ViewShopFragment extends Fragment {

    private Context context;

    private NavController navController;

    private FragmentViewShopBinding binding;

    StoresSHopApiModel.Datum data;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        data = (StoresSHopApiModel.Datum) getArguments().getSerializable("StoresSHopApiModel");

        binding = FragmentViewShopBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        initNavController();

        initClickListeners();

        context = getContext();

        setDummyData();

    }

    private void goBackScreen() {

        navController.navigateUp();

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

    private void initClickListeners() {

        binding.backButton
                .setOnClickListener(v -> goBackScreen());

    }

    private void setDummyData() {

        String shopName = "Burleigh Tyre Pro";
        String shopEmail = "johnsmith@gmail.com";
        String shopHours = "Monday-Friday 8AM - 6PM";
        String shopLocation = "2/64 West Burleigh Road";
        String shopServices = "Tyres, Repairs, Wheel Alignments, Servicing, Bodywork";
        String shopDescription = "Burleigh Tyre Pro has been servicing the gold coast region for over 10 years. We provide expert servicing and reasonable pricing for all our customers.";

        binding.shopNameText.setText(data.name);
        binding.hoursTextView.setText(data.openhours);
        binding.emailTextView.setText(data.email);
        binding.detailText.setText(data.description);
        binding.servicesTextView.setText(data.Services);
        binding.locationTextView.setText(data.Location);

        if (data.logo != null) {
            Glide.with(context).load(Api.Image_URL+data.logo).into(binding.shopImage);

        } else {
            Glide.with(context).load(R.drawable.placeholder_001).into(binding.shopImage);

        }
        binding.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                        // Permission has already been granted
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+data.contact_number));
                        startActivity(callIntent);



                } catch (Exception e){
                    Log.d("ErrorView",e.toString());
                }

            }
        });

    }
}