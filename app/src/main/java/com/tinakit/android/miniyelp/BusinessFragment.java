package com.tinakit.android.miniyelp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tina on 11/21/2014.
 */
public class BusinessFragment extends Fragment {
    public static final String EXTRA_BUSINESS_ID = "com.tinakit.android.miniyelp.business_id";
    private Business mBusiness;
    private String businessId;
    private TextView titleTextView;
    private TextView averageRatingTextView;
    private ImageView starRatingImageView;
    private TextView totalReviewsTextView;
    private TextView lastReviewIntroTextView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView stateTextView;
    private TextView phoneNumberTextView;
    private TextView businessUrlTextView;

    public static BusinessFragment newInstance(String businessId){
        Bundle args = new Bundle();
        args.putString(EXTRA_BUSINESS_ID, businessId);

        BusinessFragment fragment = new BusinessFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String businessId = (String)getArguments().getString(EXTRA_BUSINESS_ID);
        mBusiness = BusinessList.get(getActivity()).getBusiness(businessId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.business_entry, container, false);

        //wire up the widgets
        titleTextView = (TextView)v.findViewById(R.id.title_text_view);
        averageRatingTextView = (TextView)v.findViewById(R.id.averageRating_text_view);
        starRatingImageView = (ImageView)v.findViewById(R.id.starRating_image_view);
        totalReviewsTextView = (TextView)v.findViewById(R.id.totalReviews_text_view);
        lastReviewIntroTextView = (TextView)v.findViewById(R.id.lastReviewIntro_text_view);
        addressTextView = (TextView)v.findViewById(R.id.address_text_view);
        cityTextView = (TextView)v.findViewById(R.id.city_text_view);
        stateTextView = (TextView)v.findViewById(R.id.state_text_view);
        phoneNumberTextView = (TextView)v.findViewById(R.id.phoneNumber_text_view);
        businessUrlTextView = (TextView)v.findViewById(R.id.businessUrl_text_view);

        //set values of widgets from mBusiness
        titleTextView.setText(mBusiness.getTitle().replace("null",""));
        averageRatingTextView.setText(String.valueOf(mBusiness.getAverageRating()));
        starRatingImageView.setImageDrawable((
                getActivity().getResources().getDrawable(
                        BusinessListViewAdapter.getStarRatingResourceId(
                                mBusiness.getAverageRating()
                        ))));
        totalReviewsTextView.setText(String.valueOf(mBusiness.getTotalReviews()));
        lastReviewIntroTextView.setText(mBusiness.getLastReviewIntro().replace("null",""));
        addressTextView.setText(mBusiness.getAddress().replace("null",""));
        cityTextView.setText(mBusiness.getCity().replace("null",""));
        stateTextView.setText(mBusiness.getState().replace("null",""));
        phoneNumberTextView.setText(mBusiness.getPhone().replace("null",""));
        businessUrlTextView.setText(mBusiness.getBusinessUrl().replace("null",""));

        return v;
    }
}
