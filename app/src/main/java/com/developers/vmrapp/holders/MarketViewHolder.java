package com.developers.vmrapp.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.vmrapp.databinding.LayoutItemMarketBinding;

public class MarketViewHolder extends RecyclerView.ViewHolder {

    public final LayoutItemMarketBinding binding;

    public MarketViewHolder(@NonNull LayoutItemMarketBinding binding) {

        super(binding.getRoot());

        this.binding = binding;

    }
}
