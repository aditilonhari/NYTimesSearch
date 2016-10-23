package com.codepath.nytimessearch.models;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.codepath.nytimessearch.BR;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.activities.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by aditi on 10/17/2016.
 */

@Parcel
public class Article extends BaseObservable{
    //fields must be public for Parceler
    @Bindable
    String webUrl;
    @Bindable
    String headline;
    @Bindable
    String thumbNail;

    //empty Constructor needed for Parceler
    public Article(){

    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        notifyPropertyChanged(BR.webUrl);
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
        notifyPropertyChanged(BR.thumbNail);
    }

    public void setHeadline(String headline) {
        this.headline = headline;
        notifyPropertyChanged(BR.headline);
    }

    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");

            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            }
            else{
                this.thumbNail = "";
            }
        }
        catch (JSONException e){
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();

        for(int i=0; i<array.length(); i++){
            try{
                results.add(new Article(array.getJSONObject(i)));
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
    public void onSaveClick(View view, Article article){
        String url = article.getWebUrl();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        final int REQUEST_CODE = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity((SearchActivity)view.getContext(),
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));

        // add share action to menu list
        builder.addDefaultShareMenuItem();

        // Map the bitmap, text, and pending intent to this icon
        // Set tint to be true so it matches the toolbar color
        //builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl((SearchActivity)view.getContext(), Uri.parse(url));
    }

}
