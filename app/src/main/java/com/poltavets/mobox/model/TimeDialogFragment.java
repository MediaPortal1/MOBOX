package com.poltavets.mobox.model;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poltavets.mobox.R;

/**
 * Created by Alex Poltavets on 11.09.2016.
 */
public class TimeDialogFragment extends DialogFragment {

    private String hours;
    private String minutes;
    private Bitmap image;

    public static void showTimeFragment(FragmentManager fragmentManager, String h, String m, Bitmap bm){
        TimeDialogFragment timeDialogService=new TimeDialogFragment();
        timeDialogService.setHours(h);
        timeDialogService.setMinutes(m);
        timeDialogService.setImage(bm);
        timeDialogService.show(fragmentManager,"time_dialog");
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_time,container);
        ((ImageView)view.findViewById(R.id.dialog_imageview)).setImageBitmap(image);
        getDialog().setTitle(getString(R.string.curent_time)+" "+hours+":"+minutes);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    //stupid bag...
    }
}
