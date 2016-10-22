package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aditi on 10/17/2016.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

//    @BindDrawable(R.drawable.notfound) Drawable notFound;
//    @BindDrawable(R.drawable.nothumbnail) Drawable nothumbnail;

    public static class ViewHolder{
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.tvTitle) TextView tvTitle;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for the position
        Article article = this.getItem(position);
        ViewHolder viewHolder;
        // check to see if existing view is reused
        // if not using a recycled view -> inflate the layout
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_simple_article, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //clear out the recycled image from the convertView from last time
        viewHolder.imageView.setImageResource(0);
        viewHolder.tvTitle.setText(article.getHeadline());

         String thumbnail = article.getThumbNail();
         if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext())
                    .load(thumbnail)
                    .into(viewHolder.imageView);
         }
        else{
             Picasso.with(getContext())
                     .load(R.drawable.nothumbnail)
                     .fit().centerCrop()
                     .into(viewHolder.imageView);
         }

        return convertView;
    }
}
