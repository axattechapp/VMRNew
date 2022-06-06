package com.developers.vmrapp.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.vmrapp.databinding.LayoutItemHomeBinding;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    public final LayoutItemHomeBinding binding;

    public VehicleViewHolder(@NonNull LayoutItemHomeBinding binding) {

        super(binding.getRoot());

        this.binding = binding;
    }
}
