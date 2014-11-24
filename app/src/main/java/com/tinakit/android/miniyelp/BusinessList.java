package com.tinakit.android.miniyelp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Tina on 11/21/2014.
 */
public class BusinessList {
    private ArrayList<Business> mBusinesses;
    private static BusinessList sBusinessList;
    private Context mAppContext;

    private BusinessList(Context appContext){
        mAppContext = appContext;
        mBusinesses = new ArrayList<Business>();
    }

    public static BusinessList get(Context c){
        if (sBusinessList == null){
            sBusinessList = new BusinessList(c.getApplicationContext());
        }
        return sBusinessList;
    }

    public ArrayList<Business> getBusinesses(){
        return mBusinesses;
    }

    public Business getBusiness(String businessId){
        for(Business b : mBusinesses){
            if(b.getBusinessId().equals(businessId))
                return b;
        }
        return null;
    }
}
