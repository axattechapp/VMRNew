package com.developers.vmrapp.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.vmrapp.databinding.LayoutItemShopBinding;

public class ShopViewHolder extends RecyclerView.ViewHolder {

    public final LayoutItemShopBinding binding;

    public ShopViewHolder(@NonNull LayoutItemShopBinding binding) {

        super(binding.getRoot());

        this.binding = binding;

    }
}
