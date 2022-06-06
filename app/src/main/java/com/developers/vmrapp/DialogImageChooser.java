package com.developers.vmrapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogImageChooser extends BottomSheetDialogFragment {


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    View view;
    //    DialogNationalitySelectionBinding mBinding;
    Context mContext;
    private bottomSheetListener mListener;
    TextView tvClickImageText, tvChooseGalleryText;

    public DialogImageChooser() {
    }

    @SuppressLint("ValidFragment")
    public DialogImageChooser(@NonNull Context context, bottomSheetListener mListener) {
        this.mListener = mListener;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        view = inflater.inflate(R.layout.image_choose_bottom_sheet, container, false);

        tvClickImageText = view.findViewById(R.id.tvClickImageText);
        tvChooseGalleryText = view.findViewById(R.id.tvChooseGalleryText);

        tvClickImageText.setOnClickListener(v -> {
            mListener.onCardClicked("camera");
            dismiss();
        });

        tvChooseGalleryText.setOnClickListener(v -> {
            mListener.onCardClicked("gallery");
            dismiss();
        });

        return view;
    }


    public interface bottomSheetListener {
        void onCardClicked(String option);
    }
}
