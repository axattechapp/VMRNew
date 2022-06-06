package com.developers.vmrapp.fragments.vehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.FragmentAddRecordBinding;
import com.developers.vmrapp.models.HomeVehicleDetailModel;


public class AddRecordFragment extends Fragment {

    private NavController navController;

    private FragmentAddRecordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        binding = FragmentAddRecordBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }

    HomeVehicleDetailModel.Datum data;
    Bundle bundle1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        assert getArguments() != null;
        data = (HomeVehicleDetailModel.Datum) getArguments().getSerializable("HomeVehicleDetailModel");

        bundle1 = new Bundle();
        bundle1.putSerializable("HomeVehicleDetailModel", data);
        
        initNavController();

        initClickListeners();

    }

    private void initClickListeners() {

        binding.backButton.setOnClickListener(c -> gotoBackScreen());

        binding.addMaintenanceButton.setOnClickListener(v -> gotoAddMaintenance());

        binding.addServiceButton.setOnClickListener(v -> gotoAddServiceScreen());

        binding.transferVehicleButton.setOnClickListener(v -> gotoTransferVehicle());

        binding.updateLogbookButton.setOnClickListener(v -> gotoUpdateLogScreen());

    }

    private void gotoAddMaintenance() {

        navController.navigate(R.id.action_navigation_add_record_to_navigation_add_maintenance, bundle1);

    }

    private void gotoAddServiceScreen() {

        navController.navigate(R.id.action_navigation_add_record_to_navigation_add_service, bundle1);

    }

    private void gotoTransferVehicle() {

        navController.navigate(R.id.action_navigation_add_record_to_navigation_transfer_vehicle, bundle1);

    }

    private void gotoUpdateLogScreen() {

        navController.navigate(R.id.action_navigation_add_record_to_navigation_update_logbook, bundle1);

    }

    private void gotoBackScreen() {

        navController.navigateUp();

    }

    private void initNavController() {

        navController = NavHostFragment
                .findNavController(this);

    }

}