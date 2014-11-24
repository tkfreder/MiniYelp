package com.tinakit.android.miniyelp;

import java.util.UUID;

/**
 * Created by Tina on 11/21/2014.
 */
public class Business {

    private UUID mId;
    private String mBusinessId;
    private String mTitle;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mPhone;
    private double mLatitude;
    private double mLongitude;
    private String mBusinessUrl;
    private String mMapUrl;
    private double mAverageRating;
    private double mTotalRatings;
    private int mTotalReviews;
    private String mLastReviewIntro;

    public Business(){
        mId = UUID.randomUUID();
    }

    public String toString(){
        return mTitle;
    }

    public String getBusinessId() {
        return mBusinessId;
    }

    public void setBusinessId(String businessId) {
        mBusinessId = businessId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getBusinessUrl() {
        return mBusinessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        mBusinessUrl = businessUrl;
    }

    public String getMapUrl() {
        return mMapUrl;
    }

    public void setMapUrl(String mapUrl) {
        mMapUrl = mapUrl;
    }

    public double getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(double averageRating) {
        mAverageRating = averageRating;
    }

    public double getTotalRatings() {
        return mTotalRatings;
    }

    public void setTotalRatings(double totalRatings) {
        mTotalRatings = totalRatings;
    }

    public int getTotalReviews() {
        return mTotalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        mTotalReviews = totalReviews;
    }

    public String getLastReviewIntro() {
        return mLastReviewIntro;
    }

    public void setLastReviewIntro(String lastReviewIntro) {
        mLastReviewIntro = lastReviewIntro;
    }
}

