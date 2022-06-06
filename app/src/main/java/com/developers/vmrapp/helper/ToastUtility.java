package com.developers.vmrapp.helper;

import android.content.Context;


import com.shashank.sony.fancytoastlib.FancyToast;



public class ToastUtility {

    public static void errorToast(Context context, String message) {

        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();

    }

    public static void successToast(Context context, String message) {

        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

    }

    public static void warningToast(Context context, String message) {

        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();

    }

    public static void accentToast(Context context, String message) {

    }

    public static void primaryToast(Context context, String message) {

        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.DEFAULT,false).show();

    }

}
