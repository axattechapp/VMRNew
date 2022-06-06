package com.developers.vmrapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.databinding.LayoutItemHistoryBinding;
import com.developers.vmrapp.models.VehicleHistoryModel2;
import com.developers.vmrapp.models.VehicleHistoryapiModel;
import com.developers.vmrapp.service.Api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final Context context;

    private final VehicleHistoryModel2 historyModelList;

    OnHistoryClickListener listener;

    public HistoryAdapter(Context context, VehicleHistoryModel2 historyModelList, OnHistoryClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.historyModelList = historyModelList;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HistoryViewHolder(LayoutItemHistoryBinding
                .inflate(LayoutInflater.from(context), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {


//        DummyHistory dummyHistory = historyModelList.get(position);

        if (historyModelList.getData().get(position).getFlagg().equals("servicelogbookrecord")) {
//            holder.binding.historyName.setText(historyModelList.data.get(position).servicetitle);
//            holder.binding.historyDate.setText(historyModelList.data.get(position).date);


            //This commented
            if (historyModelList.getData().get(position).getDate()!=null) {
                Log.e("input date ", "" + historyModelList.getData().get(position).getDate());
                String dateStr = historyModelList.getData().get(position).getDate();
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf3.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf3 = new SimpleDateFormat("dd-MM-yyyy");

                dateStr = sdf3.format(date);
                Log.e("output date ", "" + dateStr);
//      - binding.insuranceDateText.setText(data.getInsurance_date());
                holder.binding.historyDate.setText(dateStr);
            }else
                holder.binding.historyDate.setText("");


//            holder.binding.historyShop.setText(historyModelList.data.get(position).servicecompany);
            Log.e("histyUrl",""+Api.Image_URL + historyModelList.getData().get(position).getPhotoofreceipt());

            if (historyModelList.getData().get(position).getPhotoofreceipt()!=null)
            {
                Glide.with(context)
                        .load(Api.Image_URL + historyModelList.getData().get(position).getPhotoofreceipt())
                        .into(holder.binding.itemImageView);

            }

        } else {
             holder.binding.historyName.setText(historyModelList.getData().get(position).getMaintenancetitle());
//            holder.binding.historyDate.setText(historyModelList.data.get(position).date);
//            holder.binding.historyShop.setText(historyModelList.data.get(position).maintenancetitle);

             Glide.with(context)
                    .load(Api.Image_URL + historyModelList.getData().get(position).getMaintenanceimage())
                    .into(holder.binding.itemImageView);
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            listener.onHistoryClick(historyModelList.data.get(position));
        });

        Log.e("imageurl", Api.Image_URL + historyModelList.data.get(position) + " " + historyModelList.data.size());


    }

    @Override
    public int getItemCount() {
        if (historyModelList.data.size() != 0)
            return historyModelList.data.size();
        else
            return 0;


    }


    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final LayoutItemHistoryBinding binding;

        public HistoryViewHolder(@NonNull LayoutItemHistoryBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public interface OnHistoryClickListener {

        void onHistoryClick(VehicleHistoryModel2.Datum position);

    }
}