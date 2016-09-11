package com.poltavets.mobox.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Source {
    private static Source singletonObj=null;
    private Context context;
    private Source() {
    }
    private Source(Context context) {
        this.context=context;
    }

    public static Source getInstance(Context context){
        if(singletonObj!=null)
            return singletonObj;
        else
            return new Source(context);
    }

    private String jsonURL="http://mediaportal.esy.es/mobox.json";


    public List<ImageData> getDatafromJSON(){
        ArrayList<ImageData> itemlist=new ArrayList<ImageData>();
        JSONObject jsonObject=null;

        try {
            jsonObject=new JSONObject(getJSONFromHttp());
            JSONArray dataArray=jsonObject.getJSONObject("data").getJSONArray("item");

            for(int i=0;i<dataArray.length();i++){
            JSONObject item=dataArray.getJSONObject(i);
            ImageData sourceItem=new ImageData(loadImage(item.getString("imgsrc")),item.getString("text"));
            itemlist.add(sourceItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemlist;
    }

    private String getJSONFromHttp(){
        String resultJSON=null;
        try {

            //GET JSON FROM HTTP
            URL url=new URL(jsonURL);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //GET INPUT STREAM
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            String jsonLine;
            StringBuffer stringBuffer=new StringBuffer();

            while ((jsonLine=bufferedReader.readLine())!=null){
                stringBuffer.append(jsonLine);
            }
            resultJSON=stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJSON;
    }
    private Bitmap loadImage(String imageUrl) throws IOException {
        return Picasso.with(context)
                .load(imageUrl)
                .get();
    }

}
