package com.poltavets.mobox.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by Alex Poltavets on 11.09.2016.
 */
public class TimeReceiver extends BroadcastReceiver {

    private FragmentManager fm;

    public TimeReceiver(FragmentManager fragmentManager) {
        super();
        fm=fragmentManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showDialog(intent.getExtras());
    }
    private void showDialog(Bundle bundle){
        TimeDialogFragment.showTimeFragment(fm,bundle.getString("hours"),bundle.getString("minutes"),(Bitmap) bundle.getParcelable("image"));
    }
}
