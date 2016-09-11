package com.poltavets.mobox.mainscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poltavets.mobox.R;
import com.poltavets.mobox.model.ImageData;
import com.poltavets.mobox.model.Source;
import com.poltavets.mobox.model.TimeDialogService;
import com.poltavets.mobox.model.TimeReceiver;
import com.poltavets.mobox.secondscreen.SecondActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Timer;


public class MainAcitivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        startService(new Intent(getBaseContext(), TimeDialogService.class));
        initViews();
        loadData();
        initReceiver();
    }
    private void initViews(){
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_refreshlayout);
        refreshLayout.setOnRefreshListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        recyclerView=(RecyclerView)findViewById(R.id.main_recycler);
        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ((OnHolderSwipeToLeft)viewHolder).holderSwipeAction();
            }
        };
        ItemTouchHelper onItemSwipe=new ItemTouchHelper(simpleCallback);
        onItemSwipe.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void loadData() {
        MainWebLoader webLoader = new MainWebLoader(recyclerView);
        webLoader.execute();
    }
    private void initReceiver(){
        broadcastReceiver=new TimeReceiver(getSupportFragmentManager());
        registerReceiver(broadcastReceiver,new IntentFilter(TimeDialogService.DIALOG_TIME));
    }
    @Override
    public void onRefresh() {
        loadData();
    }

    private class MainWebLoader extends AsyncTask<Void, Void, List<ImageData>> {
        private RecyclerView recyclerView;

        private MainWebLoader(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<ImageData> doInBackground(Void... voids) {
            return Source.getInstance(getBaseContext()).getDatafromJSON();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(List<ImageData> imageDatas) {
            super.onPostExecute(imageDatas);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(new RecycleCustomAdapter(imageDatas));
            if(refreshLayout.isRefreshing())refreshLayout.setRefreshing(false);
        }
    }

    private class CustomHolder extends RecyclerView.ViewHolder implements OnHolderSwipeToLeft,View.OnClickListener{

        private TextView textView;
        private ImageView imageView;
        private CustomRecyclerAdapterCallback adapterCallback;

        public CustomHolder(View itemView,CustomRecyclerAdapterCallback action) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.main_holder_textview);
            imageView= (ImageView) itemView.findViewById(R.id.main_holder_imageview);
            imageView.setOnClickListener(this);
            this.adapterCallback =action;
        }

        @Override
        public void holderSwipeAction() {
            adapterCallback.deleteItembyPosition(getAdapterPosition());
        }

        @Override
        public void onClick(View view) {
            final Intent intent=new Intent(getBaseContext(), SecondActivity.class);
            intent.putExtra("imagedata",adapterCallback.getImageDatabyPosition(getAdapterPosition()));
            Animation animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.imagemoving);
            imageView.startAnimation(animation);
            Animation animation1=AnimationUtils.loadAnimation(getBaseContext(),R.anim.textalpha);
            textView.startAnimation(animation1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    private class RecycleCustomAdapter extends RecyclerView.Adapter<CustomHolder> implements CustomRecyclerAdapterCallback {

        List<ImageData> itemslist;

        public RecycleCustomAdapter(List<ImageData> data) {
            super();
            itemslist = data;
        }

        @Override
        public void deleteItembyPosition(int position) {
            itemslist.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public ImageData getImageDatabyPosition(int position) {
            return itemslist.get(position);
        }

        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_holder, parent, false),this);
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position) {
            holder.getImageView().setImageBitmap(itemslist.get(position).getImageBitmap());
            holder.getTextView().setText(itemslist.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return itemslist.size();
        }
    }

    public interface CustomRecyclerAdapterCallback {
        void deleteItembyPosition(int position);
        ImageData getImageDatabyPosition(int position);
    }
    public interface OnHolderSwipeToLeft{
        void holderSwipeAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), TimeDialogService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){e.printStackTrace();}
    }
}
