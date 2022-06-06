package com.developers.vmrapp.fragments.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.developers.vmrapp.BuildConfig;
import com.developers.vmrapp.activities.MainActivity;
import com.developers.vmrapp.adapters.MarketAdapter;
import com.developers.vmrapp.models.DummyMarket;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.developers.vmrapp.R;
import com.developers.vmrapp.adapters.ShopAdapter;
import com.developers.vmrapp.databinding.FragmentShopBinding;
import com.developers.vmrapp.helper.ToastUtility;
import com.developers.vmrapp.models.DummyShop;
import com.developers.vmrapp.models.StoresSHopApiModel;
import com.developers.vmrapp.service.Api;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ShopFragment extends Fragment implements ShopAdapter.OnShopClickListener {

    private ShopAdapter shopAdapter;

    private NavController navController;

    private FragmentShopBinding binding;

    private final List<DummyShop> shopModelList = new ArrayList<>();

    Api api;

    Call<StoresSHopApiModel> call;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


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

        SharedPreferences pref = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        api = retrofit.create(Api.class);
        call = api.storeshopapi("Token " + pref.getString("access_token", ""));

        binding = FragmentShopBinding
                .inflate(inflater, container, false);

        return binding.getRoot();

    }

    StoresSHopApiModel model;

    String[] filterdata = {"1km", "5km", "10km", "name (A-Z)"};
    SharedPreferences editor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        editor = requireActivity().getSharedPreferences("my_token", MODE_PRIVATE);

        navController = NavHostFragment
                .findNavController(this);

        initShopAdapter();

        setDummyData();

        init();

        Dexter.withActivity(requireActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(
                requireContext(),
                R.layout.dropdown_menu,
                filterdata
        );


        binding.shopCategoryEdit.setAdapter(adapterType);


        binding.shopCategoryEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = adapterType.getItem(position);
                Log.d("selected123", selectedValue + "121");

                if (selectedValue.equals("1km")) {
                    if (mCurrentLocation != null) {
                        loadlocationdata("1");
                    } else {
                        /*if (mRequestingLocationUpdates && checkPermissions()) {
                            startLocationUpdates();
                        }*/
                    }

                } else if (selectedValue.equals("5km")) {
                    if (mCurrentLocation != null) {
                        loadlocationdata("5");
                    } else {
                       /* if (mRequestingLocationUpdates && checkPermissions()) {
                            startLocationUpdates();
                        }*/
                    }
                } else if (selectedValue.equals("10km")) {
                    if (mCurrentLocation != null) {
                        loadlocationdata("10");
                    } else {
                       /* if (mRequestingLocationUpdates && checkPermissions()) {
                            startLocationUpdates();
                        }*/
                    }
                } else {
                    loadatozfilter();
                }
            }
        });


    }

    private void initShopAdapter() {


        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();


        call.enqueue(new Callback<StoresSHopApiModel>() {
            @Override
            public void onResponse(Call<StoresSHopApiModel> call, Response<StoresSHopApiModel> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    assert response.body() != null;
                    model = response.body();
                    shopAdapter = new ShopAdapter(getContext(), model, ShopFragment.this);

                    Log.d("datasize", model.data.size() + "" + response.body().data.size());

//                    shopAdapter.notifyDataSetChanged();
                    binding.shopRecyclerView.setAdapter(shopAdapter);


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<StoresSHopApiModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());
            }
        });


    }

    private void loadatozfilter() {

        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();


        call = api.Search_a_to_z("Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<StoresSHopApiModel>() {
            @Override
            public void onResponse(@NonNull Call<StoresSHopApiModel> call, Response<StoresSHopApiModel> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    assert response.body() != null;
                    model = response.body();
                    shopAdapter = new ShopAdapter(getContext(), model, ShopFragment.this);

                    Log.d("datasize", model.data.size() + "" + response.body().data.size());

                    binding.shopRecyclerView.setAdapter(shopAdapter);
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        ToastUtility.errorToast(getActivity(), "" + response.code());
//                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoresSHopApiModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());

            }
        });

    }

    private void loadlocationdata(String s) {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading ...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        JsonObject object = new JsonObject();
        object.addProperty("lat", mCurrentLocation.getLatitude());
        object.addProperty("long", mCurrentLocation.getLatitude());
        object.addProperty("range", Integer.parseInt(s));

        call = api.location_filter(object, "Token " + editor.getString("access_token", ""));

        call.enqueue(new Callback<StoresSHopApiModel>() {
            @Override
            public void onResponse(@NonNull Call<StoresSHopApiModel> call, Response<StoresSHopApiModel> response) {
                pd.dismiss();
                if (response.code() == 200) {

                    assert response.body() != null;
                    model = response.body();
                    shopAdapter = new ShopAdapter(getContext(), model, ShopFragment.this);

                    Log.d("datasize", model.data.size() + "" + response.body().data.size());

//                    shopAdapter.notifyDataSetChanged();
                    binding.shopRecyclerView.setAdapter(shopAdapter);
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        ToastUtility.errorToast(getActivity(), "Error : " + jObjError.getString("msg") + " " + response.code());

                    } catch (Exception e) {
                        ToastUtility.errorToast(getActivity(), e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoresSHopApiModel> call, Throwable t) {
                pd.dismiss();
                ToastUtility.errorToast(getActivity(), t.getMessage());

            }
        });
    }


    private void setDummyData() {

        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));
        shopModelList.add(new DummyShop(R.drawable.placeholder_001, "Burleigh Tyre Pro", "27 Burleigh Road, Burleigh", "12 Km Away"));


    }

    @Override
    public void onShopClick(StoresSHopApiModel.Datum position) {

        // Todo

        gotoShopViewScreen(position);

    }

    private void gotoShopViewScreen(StoresSHopApiModel.Datum position) {

        // Todo

        Bundle bundle = new Bundle();
        bundle.putSerializable("StoresSHopApiModel", position);

        navController.navigate(R.id.action_navigation_shop_to_navigation_view_shop, bundle);

    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mSettingsClient = LocationServices.getSettingsClient(requireActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    Double latitude, longitude;

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Log.d("location123",mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude());
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();

        }


    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                        Log.i(TAG, "All location settings are satisfied.");

//                        Toast.makeText(requireContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                                  Log.e("Location","Started location updates!");
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
//                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
//                                Log.e(TAG, errorMessage);

                                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(binding.getRoot().getContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        Log.e("Location","Location updates stopped!");

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
//                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

}