package com.poltavets.mobox.secondscreen;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.poltavets.mobox.R;
import com.poltavets.mobox.databinding.SecondActivityBinding;
import com.poltavets.mobox.model.ImageData;
import com.poltavets.mobox.model.TimeDialogService;
import com.poltavets.mobox.model.TimeReceiver;
import com.squareup.picasso.Picasso;

/**
 * Created by Alex Poltavets on 10.09.2016.
 */
public class SecondActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecondActivityBinding binding= DataBindingUtil.setContentView(this, R.layout.second_activity);
        if(getIntent().hasExtra("imagedata"))
            binding.setImage((ImageData) (getIntent().getExtras().getParcelable("imagedata")));
        initReceiver();
    }

    private void initReceiver(){
        broadcastReceiver=new TimeReceiver(getSupportFragmentManager());
        registerReceiver(broadcastReceiver,new IntentFilter(TimeDialogService.DIALOG_TIME));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
