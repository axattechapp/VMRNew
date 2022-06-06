package com.developers.vmrapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.R;
import com.developers.vmrapp.databinding.LayoutItemShopBinding;
import com.developers.vmrapp.models.StoresSHopApiModel;
import com.developers.vmrapp.service.Api;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private final Context context;
    private final OnShopClickListener listener;
    private final StoresSHopApiModel shopModelList;

    public ShopAdapter(Context context, StoresSHopApiModel shopModelList, OnShopClickListener listener) {

        this.context = context;
        this.listener = listener;
        this.shopModelList = shopModelList;

    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ShopViewHolder(LayoutItemShopBinding
                .inflate(LayoutInflater.from(context), parent, false), listener);


    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {

//        DummyShop dummyShop = shopModelList.get(position);
        if (shopModelList.data.get(position).logo != null) {
            Glide.with(context)
                    .load(Api.Image_URL + shopModelList.data.get(position).logo)
                    .into(holder.binding.itemImageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.placeholder_001)
                    .into(holder.binding.itemImageView);
        }


        holder.binding.shopName.setText(shopModelList.data.get(position).name);

        holder.binding.shopAddress.setText(shopModelList.data.get(position).Location);

        holder.binding.shopDistance.setText(shopModelList.data.get(position).openhours);

    }

    @Override
    public int getItemCount() {

        if (shopModelList.data != null) {
            return shopModelList.data.size();
        }
        else {
            return 0;
        }

    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        public final LayoutItemShopBinding binding;

        public ShopViewHolder(@NonNull LayoutItemShopBinding binding, OnShopClickListener listener) {

            super(binding.getRoot());

            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v ->
                    listener.onShopClick(shopModelList.data.get(getAdapterPosition())));

        }
    }

    public interface OnShopClickListener {

        void onShopClick(StoresSHopApiModel.Datum  position);

    }

}
