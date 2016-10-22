package com.codepath.nytimessearch.models;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.nytimessearch.activities.ArticleActivity;
import com.codepath.nytimessearch.databinding.ItemVhNoImageArticleBinding;

import org.parceler.Parcels;

/**
 * Created by aditi on 10/21/2016.
 */

public class VH_NoImageArticle extends RecyclerView.ViewHolder implements View.OnClickListener {

    final public ItemVhNoImageArticleBinding binding; //This will be used by onBindViewHolder()

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public VH_NoImageArticle(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);
        // Since the layout was already inflated within onCreateViewHolder(), we
        // can use this bind() method to assiciate the layout variables with the layout
        binding = DataBindingUtil.bind(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context viewContext = v.getContext();
        Intent intent =  new Intent(viewContext, ArticleActivity.class);
        intent.putExtra("article", Parcels.wrap(this));
        viewContext.startActivity(intent);
    }
}
