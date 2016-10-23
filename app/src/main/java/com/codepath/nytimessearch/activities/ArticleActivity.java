package com.codepath.nytimessearch.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    Article article;
    final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));
        String url = article.getWebUrl();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));

        // add share action to menu list
        builder.addDefaultShareMenuItem();

        // Map the bitmap, text, and pending intent to this icon
        // Set tint to be true so it matches the toolbar color
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.action_shareLink);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }
}
