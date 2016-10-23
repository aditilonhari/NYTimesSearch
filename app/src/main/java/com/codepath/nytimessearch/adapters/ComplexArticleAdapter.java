package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.BR;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.models.VH_Article;
import com.codepath.nytimessearch.models.VH_NoImageArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aditi on 10/21/2016.
 */

public class ComplexArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Article> mArticles;
    private Context mContext;
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

    private final int ARTICLE = 0, ARTICLE_NO_IMAGE = 1;

    public ComplexArticleAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position){
        Article currentArticle = mArticles.get(position);
        String thumbnail = currentArticle.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            return ARTICLE;
        } else if (TextUtils.isEmpty(thumbnail)) {
            return ARTICLE_NO_IMAGE;
        }
        return -1;
    }

    /**
     * This method creates different RecyclerView.ViewHolder objects based on the item view type.
     *
     * param viewGroup ViewGroup container for the item
     * param viewType type of view to be inflated
     * return viewHolder to be inflated
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case ARTICLE:
                ViewDataBinding v1Binding = DataBindingUtil.inflate(inflater, R.layout.item_vh_article, parent, false);
                viewHolder = new VH_Article(v1Binding);
                break;
            case ARTICLE_NO_IMAGE:
                ViewDataBinding v2Binding = DataBindingUtil.inflate(inflater, R.layout.item_vh_no_image_article, parent, false);
                viewHolder = new VH_NoImageArticle(v2Binding);
                break;
            default:
                View v = inflater.inflate(R.layout.item_simple_article, parent, false);
                viewHolder = new ArticleAdapter.ViewHolder(v);
                break;
        }

        return viewHolder;
    }


    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * param viewHolder The type of RecyclerView.ViewHolder to populate
     * param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Article article = mArticles.get(position);
        ViewDataBinding viewDataBinding;

        switch (viewHolder.getItemViewType()) {
            case ARTICLE:
                VH_Article vh1 = (VH_Article) viewHolder;
                viewDataBinding = vh1.getViewDataBinding();
                viewDataBinding.setVariable(BR.vh_article, article);
                break;
            case ARTICLE_NO_IMAGE:
                VH_NoImageArticle vh2 = (VH_NoImageArticle) viewHolder;
                viewDataBinding = vh2.getViewDataBinding();
                viewDataBinding.setVariable(BR.vh_noimage_article, article);
                break;
            default:
                ArticleAdapter.ViewHolder vh = (ArticleAdapter.ViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }

    }

    private void configureDefaultViewHolder(ArticleAdapter.ViewHolder vh, int position) {
        Article article = mArticles.get(position);
        if (article != null) {
            vh.titleTextView.setText(article.getHeadline());
            Glide.with(mContext).load(article.getThumbNail())
                    .into(vh.posterImage);
        }
    }
}
