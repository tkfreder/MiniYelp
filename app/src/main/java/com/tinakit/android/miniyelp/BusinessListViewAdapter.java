package com.tinakit.android.miniyelp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tina on 11/21/2014.
 */
public class BusinessListViewAdapter extends ArrayAdapter<Business> {
    Context context;
    //display the first n characters of the last review
    static final int LAST_REVIEW_SUBSTRING = 40;
    ViewHolder holder = null;

    public BusinessListViewAdapter(Context context,
                                   int resourceId,
                                   List<Business> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView titleTextView;
        TextView averageRatingTextView;
        TextView totalReviewsTextView;
        TextView lastReviewIntroTextView;
        ImageView starRatingImageView;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        Business business = getItem(position);

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //check for convertView.getTag()
        // some versions will return null for getTag() even after listview has been populated
        if(convertView == null || convertView.getTag() == null){
            convertView = mInflater.inflate(R.layout.business_list_item, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView)convertView.findViewById(R.id.title_text_view);
            holder.averageRatingTextView = (TextView)convertView.findViewById(R.id.averageRating_text_view);
            holder.totalReviewsTextView = (TextView)convertView.findViewById(R.id.totalReviews_text_view);
            holder.lastReviewIntroTextView = (TextView)convertView.findViewById(R.id.lastReviewIntro_text_view);
            holder.starRatingImageView = (ImageView)convertView.findViewById(R.id.starRating_image_view);
        }else
            holder = (ViewHolder)convertView.getTag();

        holder.titleTextView.setText(business.getTitle());
        if (String.valueOf(business.getAverageRating()) != null) {
            holder.averageRatingTextView.setText(String.valueOf(business.getAverageRating()));
            holder.starRatingImageView.setImageDrawable(
                    getContext().getResources().getDrawable(
                            getStarRatingResourceId(
                                    business.getAverageRating()
                            )));
        }

        if(String.valueOf(business.getTotalReviews()) != null && !String.valueOf(business.getTotalReviews()).isEmpty())
            holder.totalReviewsTextView.setText(String.valueOf(business.getTotalReviews()));
        else
            holder.totalReviewsTextView.setText("0");
        if(business.getLastReviewIntro() != null && !business.getLastReviewIntro().trim().isEmpty())
            if (business.getLastReviewIntro().length()<LAST_REVIEW_SUBSTRING)
                holder.lastReviewIntroTextView.setText(business.getLastReviewIntro() + " ...");
            else
                holder.lastReviewIntroTextView.setText(business.getLastReviewIntro().substring(0,LAST_REVIEW_SUBSTRING) + " ...");
        return convertView;
    }
    protected static int getStarRatingResourceId(double rating){

        if (rating == 5.0)
            return R.drawable.gold_star_5;
        else if (rating == 4.5)
            return R.drawable.gold_star_4_5;
        else if (rating == 4.0)
            return R.drawable.gold_star_4;
        else if (rating == 3.5)
            return R.drawable.gold_star_3_5;
        else if (rating == 3.0)
            return R.drawable.gold_star_3;
        else if (rating == 2.5)
            return R.drawable.gold_star_2_5;
        else if (rating == 2.0)
            return R.drawable.gold_star_2;
        else if (rating == 1.5)
            return R.drawable.gold_star_1_5;
        else if (rating == 1.0)
            return R.drawable.gold_star_1;
        else if (rating == 0.5)
            return R.drawable.gold_star_0_5;
        else
            return R.drawable.gold_star_0;

    }
}
