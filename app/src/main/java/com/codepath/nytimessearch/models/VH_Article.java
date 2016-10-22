package com.codepath.nytimessearch.models;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.activities.ArticleActivity;
import com.codepath.nytimessearch.databinding.ItemVhArticleBinding;

import org.parceler.Parcels;

/**
 * Created by aditi on 10/21/2016.
 */

public class VH_Article extends RecyclerView.ViewHolder implements View.OnClickListener {

    final public ItemVhArticleBinding binding; //This will be used by onBindViewHolder()

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public VH_Article(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);
        // Since the layout was already inflated within onCreateViewHolder(), we
        // can use this bind() method to assiciate the layout variables with the layout
        binding = DataBindingUtil.inflate(LayoutInflater.from(itemView.getContext()), R.layout.item_vh_article, (ViewGroup) itemView, false);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
            Context viewContext = v.getContext();
            Intent intent =  new Intent(viewContext, ArticleActivity.class);
            intent.putExtra("article", Parcels.wrap(this));
            viewContext.startActivity(intent);
    }

    /*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //Bundle bundle = new Bundle()
        //bundle.putBundle("article", (Bundle) VH_Article.this);
        //out.writeBundle();
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private VH_Article(Parcel in) {
        //titleTextView = in.readValue();
        //mInfo = in.readParcelable(MySubParcelable.class.getClassLoader());
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<VH_Article> CREATOR
            = new Parcelable.Creator<VH_Article>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public VH_Article createFromParcel(Parcel in) {
            return new VH_Article(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public VH_Article[] newArray(int size) {
            return new VH_Article[][size];
        }
    };*/
}

