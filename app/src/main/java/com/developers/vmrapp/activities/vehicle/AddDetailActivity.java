package com.developers.vmrapp.activities.vehicle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.ActivityAddDetailBinding;
import com.developers.vmrapp.helper.ToastUtility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDetailActivity extends AppCompatActivity {

    private ActivityAddDetailBinding binding;
    Intent i;
    String registrationdate = "";
    String InsuranceDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddDetailBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        i = getIntent();


        initClickListeners();

        binding.vehicleDueDateEdit.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
//                    .mustBeOnFuture()
                    .curved()
                    .minutesStep(15)
                    .backgroundColor(Color.WHITE)
                    .mainColor(getResources().getColor(R.color.colorPrimary))
                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
                    .displayHours(false)
                    .displayMinutes(false)
                    .todayText("Today")
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {

                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            Log.d("datetime", picker.getDate().toString());
                        }
                    })
                    .title("Date")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {

                            String formateDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            Log.v("output date ", formateDate);
                            binding.vehicleDueDateEdit.setText(formateDate);
                            binding.vehicleDueDateEdit.setTextColor(Color.BLACK);
                            registrationdate = formateDate;
                            Log.d("datetime", String.valueOf(formateDate) + " " + date.toString());
                        }
                    }).display();
        });
        binding.vehicleInsuranceDueDateEdit.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
//                    .mustBeOnFuture()
                    .curved()
                    .minutesStep(15)
                    .backgroundColor(Color.WHITE)
                    .mainColor(getResources().getColor(R.color.colorPrimary))
                    .titleTextColor(getResources().getColor(R.color.colorPrimary))
                    .displayHours(false)
                    .displayMinutes(false)
                    .todayText("Today")
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {

                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            Log.d("datetime", picker.getDate().toString());
                        }
                    })
                    .title("Date")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {

                            String formateDate1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            Log.v("output date ", formateDate1);
                            binding.vehicleInsuranceDueDateEdit.setText(formateDate1);
                            binding.vehicleInsuranceDueDateEdit.setTextColor(Color.BLACK);
                            InsuranceDate = formateDate1;
                            Log.d("datetime", String.valueOf(formateDate1) + " " + date.toString());
                        }
                    }).display();
        });

    }

    private void initClickListeners() {

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.nextButton.setOnClickListener(v -> gotoAddSupplierScreen());

    }


    private void gotoAddSupplierScreen() {
          Log.e("Insurance_date","date"+InsuranceDate);
        if (!binding.vehicleKilometerEdit.getText().toString().isEmpty()) {
            binding.vehicleKilometerInput.setError(null);
            if (!binding.vehicleMakeEdit.getText().toString().isEmpty()) {
                binding.vehicleMakeInput.setError(null);
                if (!binding.vehicleModelEdit.getText().toString().isEmpty()) {
                    binding.vehicleModelInput.setError(null);
                    if (!binding.vehicleYearEdit.getText().toString().isEmpty()) {
                        binding.vehicleYearInput.setError(null);
                        if (!registrationdate.isEmpty()) {
                            if (!InsuranceDate.isEmpty()){
                            Intent intent = new Intent(getApplicationContext(), AddSuppliersActivity.class);
                            intent.putExtra("vehiclename", i.getStringExtra("vehiclename"));
                            intent.putExtra("imageurl", i.getStringExtra("imageurl"));
                            intent.putExtra("vehiclenumber", i.getStringExtra("vehiclenumber"));
                            intent.putExtra("vehiclekelometer", binding.vehicleKilometerEdit.getText().toString().trim());
                            intent.putExtra("vehiclemake", binding.vehicleMakeEdit.getText().toString().trim());
                            intent.putExtra("vehiclemodel", binding.vehicleModelEdit.getText().toString().trim());
                            intent.putExtra("vehicleyear", binding.vehicleYearEdit.getText().toString().trim());
                            intent.putExtra("vehicleregduedate", registrationdate);
                            intent.putExtra("registertype", getIntent().getStringExtra("registertype"));
                            intent.putExtra("insurance_date", InsuranceDate);
                            startActivity(intent);
                        } else {
                            ToastUtility.warningToast(this, "Pls select Insurance date");
                        }
                    }else{
                            ToastUtility.warningToast(this, "Pls select Registration date");
                        }}else {
                        binding.vehicleYearInput.setError("Enter vehicle year");
                    }
                } else {

                }
            } else {
                binding.vehicleMakeInput.setError("Enter vehicle make");
            }

        } else {
            binding.vehicleKilometerInput.setError("Enter km ");
        }


    }

}