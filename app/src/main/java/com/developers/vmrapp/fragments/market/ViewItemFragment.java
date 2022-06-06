package com.developers.vmrapp.fragments.market;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentViewItemBinding;
import com.developers.vmrapp.models.DummyMarket;
import com.developers.vmrapp.service.Api;


public class ViewItemFragment extends Fragment {

    private Context context;
    private NavController navController;

    private FragmentViewItemBinding binding;

    DummyMarket.Datum data;
    DummyMarket.Datum data1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        data = (DummyMarket.Datum) getArguments().getSerializable("HomeVehicleDetailModel");

        binding = FragmentViewItemBinding
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

        String quantity = "5";
        String condition = "New";
        String name = "John Smith";
        String marketPrice = "500$";
        String model = "Michelin Sports TT11";
        String marketName = "Michelin Sports Tyres";
        String shopDescription = "We have 4 brand new Michelin Sports Tyres for sale. These are brand new and haven’t been driven before. Contact us directly to arrange pickup at our local store.";

        if (data.user.name!=null)
        {
            binding.userNameText.setText(data.user.name);
        }
        if (data!=null)
        {
            if (data.makemodel!=null)
            binding.makeTextView.setText(data.makemodel);

            if (data.quantity!=null)
                binding.quantityTextView.setText(data.quantity);

            if (data.price!=null)
                binding.priceTextView.setText("$"+data.price);

            if (data.partname!=null)
                binding.marketNameText.setText(data.partname);

            if (data.partdescription!=null)
                binding.detailText1.setText(data.partdescription);

            if (data.condition!=null)
                binding.conditionTextView.setText(data.condition);


        }

        binding.userNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        // Ask for permision
                        ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE}, 1);
                        // Permission has already been granted
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                        startActivity(callIntent);
                    }
                    else {
                        // Permission has already been granted
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                        startActivity(callIntent);
                    }
//                   if (ActivityCompat.checkSelfPermission(context,
//                           Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                       //Creating intents for making a call
//
//
//                   }else{
//                       Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
//                   }

                } catch (Exception e){
                    Log.d("ErrorView",e.toString());
                }

            }
        });

        binding.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                           != PackageManager.PERMISSION_GRANTED) {
                       // Permission is not granted
                       // Ask for permision
                       ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE}, 1);

                       // Permission has already been granted
                       Intent callIntent = new Intent(Intent.ACTION_CALL);
                       callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                       startActivity(callIntent);
                   }
                   else {
                    // Permission has already been granted
                       Intent callIntent = new Intent(Intent.ACTION_CALL);
                       callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                       startActivity(callIntent);
                   }
//                   if (ActivityCompat.checkSelfPermission(context,
//                           Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                       //Creating intents for making a call
//
//
//                   }else{
//                       Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
//                   }

               } catch (Exception e){
                   Log.d("ErrorView",e.toString());
               }

            }
        });

        binding.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        // Ask for permision
                        ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE}, 1);
                        // Permission has already been granted
                        if (Manifest.permission.CALL_PHONE == String.valueOf(PackageManager.PERMISSION_GRANTED))
                        {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                            startActivity(callIntent);

                        }

                    }
                    else {
                        // Permission has already been granted
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+data.user.mobile));
                        startActivity(callIntent);
                    }
//                   if (ActivityCompat.checkSelfPermission(context,
//                           Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                       //Creating intents for making a call
//
//
//                   }else{
//                       Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
//                   }

                } catch (Exception e){
                    Log.d("ErrorView",e.toString());
                }

            }
        });

        Glide.with(context).load(Api.Image_URL+data.partimage).into(binding.marketImage);
        Glide.with(context).load(R.drawable.placeholder_011).into(binding.userImageView);

    }

}