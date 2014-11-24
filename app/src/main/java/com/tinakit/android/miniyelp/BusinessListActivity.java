package com.tinakit.android.miniyelp;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class BusinessListActivity extends SingleFragmentActivity {
    private static final String TAG = "BusinessActivity";
    public static final String LAST_SEARCH_KEYWORD = "lastSearchKeyword";
    public static final String LAST_SEARCH_CITY = "lastSearchCity";


    @Override
    protected Fragment createFragment(){
        return new BusinessListFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        BusinessListFragment fragment = (BusinessListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            String[] tokens = query.split("[,]");
            Log.i(TAG, "Received new query search: " + query);
            //save the current query
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(LAST_SEARCH_KEYWORD, tokens[0].trim())
                    .commit();
            if (tokens.length == 2)
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(LAST_SEARCH_CITY, tokens[1].trim())
                    .commit();
        }
        fragment.updateItems();
    }

}
