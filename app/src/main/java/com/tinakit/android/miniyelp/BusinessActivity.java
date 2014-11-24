package com.tinakit.android.miniyelp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by Tina on 11/21/2014.
 */
public class BusinessActivity extends SingleFragmentActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            BusinessFragment businessFragment = new BusinessFragment();
            businessFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, businessFragment).commit();
        }

    }

    @Override
    protected Fragment createFragment(){
        String businessId = getIntent().getStringExtra(BusinessFragment.EXTRA_BUSINESS_ID);
        return BusinessFragment.newInstance(businessId);
    }


}
