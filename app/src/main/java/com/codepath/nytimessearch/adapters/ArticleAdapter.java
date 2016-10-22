package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.activities.ArticleActivity;
import com.codepath.nytimessearch.models.Article;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by aditi on 10/20/2016.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private static Context mContext;
    private List<Article> mArticles;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTextView;
        public ImageView posterImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.tvTitle);
            posterImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }


    }

    public ArticleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private static Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_simple_article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ArticleAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Article article = mArticles.get(position);

        // Set item views based on your views and data model
        viewHolder.posterImage.setImageResource(0);
        viewHolder.titleTextView.setText(article.getHeadline());

        String thumbnail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbnail)){
            Glide.with(mContext)
                    .load(thumbnail)
                    .into(viewHolder.posterImage);
        }
        else{
            Glide.with(mContext)
                    .load(R.drawable.nothumbnail)
                    .centerCrop()
                    .into(viewHolder.posterImage);
        }

        viewHolder.posterImage.setOnClickListener(view -> {
            Intent intent =  new Intent(mContext, ArticleActivity.class);
            intent.putExtra("article", Parcels.wrap(article));
            mContext.startActivity(intent);
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
