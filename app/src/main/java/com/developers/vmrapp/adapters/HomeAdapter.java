package com.developers.vmrapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.developers.vmrapp.ExifUtils;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.LayoutItemHomeBinding;
import com.developers.vmrapp.helper.Kilometer;
import com.developers.vmrapp.models.HomeVehicleDetailModel;
import com.developers.vmrapp.service.Api;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VehicleViewHolder> {

    private final Context context;
    private final OnVehicleClickListener listener;
    HomeVehicleDetailModel vehicleModelList;

    public HomeAdapter(Context context, HomeVehicleDetailModel vehicleModelList, OnVehicleClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.vehicleModelList = vehicleModelList;
    }


    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VehicleViewHolder(LayoutItemHomeBinding
                .inflate(LayoutInflater.from(context), parent, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {

        holder.binding.vehicleImage.setBackgroundResource(R.drawable.white_img);

        if (vehicleModelList.data.get(position).getCurrentKm() != null && !vehicleModelList.data.get(position).getCurrentKm().isEmpty()) {
            Log.e("km","km"+vehicleModelList.data.get(position).getCurrentKm());
            Log.e("km","km"+vehicleModelList.data.get(position).getCurrentKm());
//            holder.binding.vehicleKilometer.setText(Kilometer
//                    .getKilometer(Integer.parseInt(vehicleModelList.data.get(position).currentKm.trim())));
            holder.binding.vehicleKilometer.setText(
                    vehicleModelList.data.get(position).getCurrentKm().toString().trim());

        }

        holder.binding.progressBar2.setVisibility(View.VISIBLE);

        Log.e("id",""+vehicleModelList.data.get(position).getId());
        Log.e("Model",""+vehicleModelList.data.get(position).getVehicleModel());
        Log.e("vehicleName",""+vehicleModelList.data.get(position).getVehicleMake());
        Log.e("getVehicleRegnumber",""+vehicleModelList.data.get(position).getVehicleRegnumber());
        Log.e("img",""+Api.Image_URL+vehicleModelList.data.get(position).getVehicleimage());

//Call your UI related method
//        Picasso.get().load(Api.Image_URL + vehicleModelList.data.get(position).getVehicleimage()).into(holder.binding.vehicleImage);

//        Picasso.get()
//                .load(Api.Image_URL + vehicleModelList.data.get(position).getVehicleimage())
//                .into(holder.binding.vehicleImage, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        //do something when picture is loaded successfully
//                        holder.binding.progressBar2.setVisibility(View.GONE);
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//
//
//                });


        //display thumnil then dwnl
        Glide.with(context)
                .load(Api.Image_URL+vehicleModelList.data.get(position).getVehicleimage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.binding.progressBar2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.progressBar2.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.vehicleImage);




        //first store in disk then show first time it willl take time later no
//        Glide.with(context)
//                .load(Api.Image_URL+vehicleModelList.data.get(position).getVehicleimage())
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .into(holder.binding.vehicleImage);


        holder.binding.vehicleName.setText(vehicleModelList.data.get(position).getVehicleMake());


        holder.binding.vehicleModel.setText(vehicleModelList.data.get(position).getVehicleModel());


        holder.binding.vehicleNumber.setText(vehicleModelList.data.get(position).getVehicleRegnumber());

//        Glide.with(context)
//                .load(Api.Image_URL + vehicleModelList.data.get(position).getVehicleimage())
//                .into(holder.binding.vehicleImage);




        Log.e("servicedate", vehicleModelList.data.get(position).getNextService() + " ");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        try {
            if (vehicleModelList.data.get(position).getNextService() != null) {

                String datestart = vehicleModelList.data.get(position).getNextService();
                cal.setTime(Objects.requireNonNull(sdf.parse(datestart)));// all done
                Calendar cal1 = Calendar.getInstance();
                String formatted = sdf.format(cal1.getTime());//formatted date as i want
                cal1.setTime(Objects.requireNonNull(sdf.parse(formatted)));// all done

                long msDiff = cal.getTimeInMillis() - cal1.getTimeInMillis();
                long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
//                String date=vehicleModelList.data.get(position).getNextService();
//                String formateDate1 = new SimpleDateFormat("dd-MM-yyyy").format(date);

//                holder.binding.vehicleService.setText(vehicleModelList.data.get(position).getNextService());


                String dateStr = vehicleModelList.data.get(position).getNextService().toString();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf1.parse(dateStr);
                sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                dateStr = sdf1.format(date1);
                Log.e("output date ",""+dateStr);
                holder.binding.vehicleService.setText(dateStr);
                Log.e("ServicedaysDifffff", daysDiff + " ");
                int serviceProgresss;
                if (daysDiff<100)
                {
                    if (daysDiff==0){
                        serviceProgresss=100;

                    }else if (daysDiff<0)
                    {
                        serviceProgresss=100;
                    }else
                    {
                        serviceProgresss= (int) (100-daysDiff);
                    }


                }
                else  {
                    serviceProgresss=5;
                }


                if (daysDiff < 0) {
                    holder.binding.serviceProgress.setIndicatorColor(Color.RED);
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                }
                else if (daysDiff == 0) {
                    holder.binding.serviceProgress.setIndicatorColor(Color.RED);
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                } else if (daysDiff > 0 && daysDiff < 33) {
                    holder.binding.serviceProgress.setIndicatorColor(Color.RED);
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                } else if (daysDiff >= 33 && daysDiff < 66) {
                    holder.binding.serviceProgress.setIndicatorColor(context.getResources().getColor(R.color.orange));
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                } else if (daysDiff >= 66 && daysDiff < 100) {
                    holder.binding.serviceProgress.setIndicatorColor(Color.GREEN);
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                } else {
//                    holder.binding.serviceProgress.setIndicatorColor(context.getResources().getColor(R.color.gainsboro));
                    holder.binding.serviceProgress.setIndicatorColor(Color.GREEN);
                    holder.binding.serviceProgress.setProgress((int) serviceProgresss);
                }

//                Toast.makeText(context, "days=" + daysDiff, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(context, "error=" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public int getItemCount() {

        return vehicleModelList.data.size();

    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {

        private final LayoutItemHomeBinding binding;

        public VehicleViewHolder(@NonNull LayoutItemHomeBinding binding,
                                 OnVehicleClickListener listener) {

            super(binding.getRoot());

            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v ->
                    listener.onVehicleClick(vehicleModelList.data.get(getBindingAdapterPosition())));
        }
    }

    public interface OnVehicleClickListener {

        void onVehicleClick(HomeVehicleDetailModel.Datum position);

    }

}