package com.poltavets.mobox.model;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class ImageData implements Parcelable{

    private String text;
    private Bitmap imageBitmap;

    public ImageData(Bitmap imageBitmap, String text) {
        this.imageBitmap=imageBitmap;
        this.text = text;
    }

    protected ImageData(Parcel in) {
        text = in.readString();
        imageBitmap=in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    public String getText() {
        return text;
    }


    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeParcelable(imageBitmap,PARCELABLE_WRITE_RETURN_VALUE);
    }

    @BindingAdapter("android:src")
    public static void setBitmapToImageView(ImageView view, Bitmap imageBitmap){
        view.setImageBitmap(imageBitmap);
    }
}
