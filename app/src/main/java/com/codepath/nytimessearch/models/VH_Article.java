package com.codepath.nytimessearch.models;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by aditi on 10/21/2016.
 */

public class VH_Article extends RecyclerView.ViewHolder {

    private ViewDataBinding v1Binding; //This will be used by onBindViewHolder()

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public VH_Article(ViewDataBinding itemViewBinding) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemViewBinding.getRoot());
        // Since the layout was already inflated within onCreateViewHolder(), we
        // can use this bind() method to assiciate the layout variables with the layout
        v1Binding = itemViewBinding;
        v1Binding.executePendingBindings();
    }

    public ViewDataBinding getViewDataBinding(){
        return v1Binding;
    }
}

