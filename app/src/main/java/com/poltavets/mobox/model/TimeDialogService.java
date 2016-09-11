package com.poltavets.mobox.model;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Alex Poltavets on 11.09.2016.
 */
public class TimeDialogService extends IntentService {

    public static final String DIALOG_TIME="com.poltavets.mobox.TIME_DIALOG";

    private Calendar calendar;
    public TimeDialogService(){
        super("timedialogservice");

    }
    @Override
    public void onCreate() {
        super.onCreate();
        calendar=Calendar.getInstance();
    }

    private void initService(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                final Intent intent=new Intent(DIALOG_TIME);
                intent.putExtra("hours",String.valueOf(calendar.HOUR));
                intent.putExtra("minutes",String.valueOf(calendar.MINUTE));
                    Picasso.with(getBaseContext())
                            .load("https://www.dreamhost.com/blog/wp-content/uploads/2015/10/DHC_blog-image-01-300x300.jpg")
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    intent.putExtra("image",bitmap);
                                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                    sendBroadcast(intent);
                                }
                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                handler.postDelayed(this, 120000);
            }
        }, 60000);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initService();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
