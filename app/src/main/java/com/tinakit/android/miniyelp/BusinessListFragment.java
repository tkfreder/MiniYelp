package com.tinakit.android.miniyelp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class BusinessListFragment extends ListFragment{
    private ArrayList<Business> mBusinesses;
    private EditText searchEditText;
    private TextView keywordTextView;
    private TextView cityTextView;
    private String keyword;
    private String city;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.mini_yelp_title);

        //do not enable OptionsMenu
        setHasOptionsMenu(true);

        //hide keyboard on launch
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_businesslist,container, false);

        //wire up widgets
        keywordTextView = (TextView)view.findViewById(R.id.keyword_text_view);
        cityTextView = (TextView)view.findViewById(R.id.city_text_view);
/*
        //populate city spinner
        Spinner spinner = (Spinner)view.findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchEditText = (EditText)view.findViewById(R.id.search_edit_text);
*/
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_business_list, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(BusinessListActivity.LAST_SEARCH_KEYWORD, null)
                        .commit();
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(BusinessListActivity.LAST_SEARCH_CITY, null)
                        .commit();
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void updateItems(){
        new getBusinessesAsyncTask().execute();
    }

    public void saveQueryWords(){
        keywordTextView.setText("\"" + keyword + "\"");
        cityTextView.setText(" , \"" + city + "\"");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBusinesses = BusinessList.get(getActivity()).getBusinesses();
        updateItems();

        setupAdapter();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Business business = (Business) getListAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), BusinessActivity.class);
                intent.putExtra(BusinessFragment.EXTRA_BUSINESS_ID, business.getBusinessId());
                startActivity(intent);
            }
        });
    }

    void setupAdapter(){
        if(getActivity() == null || getListView() == null) return;

        if (mBusinesses != null){
            BusinessListViewAdapter adapter =
                    new BusinessListViewAdapter(getActivity(),
                            R.layout.business_list_item,
                            mBusinesses);
            setListAdapter(adapter);
        }else
            setListAdapter(null);


    }



    private String buildYahooBizUrl(){
        keyword = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(BusinessListActivity.LAST_SEARCH_KEYWORD, null);
        city = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(BusinessListActivity.LAST_SEARCH_CITY, null);

        String url;
        if (keyword != null && city != null)
            url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20query%3D%22" + keyword +"%22%20and%20location%3D%22" + city + "%2C%20ca%22&format=json&diagnostics=true&callback=";
        else if (keyword != null)
            url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20query%3D%22" + keyword +"%22%20and%20location%3D%22pasadena%2C%20ca%22&format=json&diagnostics=true&callback=";
        else
            url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20query%3D%22sushi%22%20and%20location%3D%22pasadena%2C%20ca%22&format=json&diagnostics=true&callback=";
        url = url.replace(" ", "%20");
        return url;
    }


    private class getBusinessesAsyncTask extends AsyncTask<String,String,ArrayList<Business>> {
        @Override
        protected ArrayList<Business> doInBackground(String... strings) {

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            //access web service data tables
            HttpPost httppost = new HttpPost(buildYahooBizUrl());
            httppost.setHeader("Content-type","application/json");
            InputStream inputStream = null;

            String result = null;

            try{
                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;

                while((line = reader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                try{
                    if (inputStream != null)
                        inputStream.close();
                }catch(Exception e){}
            }
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                JSONObject queryJSONObject = jsonObject.getJSONObject("query");
                JSONObject resultsJSONObject = queryJSONObject.getJSONObject("results");
                JSONArray resultJSONArray = resultsJSONObject.getJSONArray("Result");

                //clear the business list before repopulating
                mBusinesses.clear();
                for (int i = 0; i < resultJSONArray.length(); i++) {
                    JSONObject idJSONObject = (JSONObject)resultJSONArray.getJSONObject(i);

                    if (idJSONObject.has("id") && idJSONObject.has("Title")) {
                        {
                            Business business = new Business();
                            business.setBusinessId(idJSONObject.getString("id").replace("null", ""));
                            business.setTitle(idJSONObject.getString("Title").replace("null", ""));
                            if (idJSONObject.has("Address"))
                                business.setAddress(idJSONObject.getString("Address").replace("null", ""));
                            if (idJSONObject.has("City"))
                                business.setCity(idJSONObject.getString("City").replace("null", ""));
                            if (idJSONObject.has("State"))
                                business.setState(idJSONObject.getString("State").replace("null", ""));
                            if (idJSONObject.has("Phone"))
                                business.setPhone(idJSONObject.getString("Phone").replace("null", ""));
                            if (idJSONObject.has("Latitude"))
                                business.setLatitude(Double.parseDouble(idJSONObject.getString("Latitude")));
                            if (idJSONObject.has("Longitude"))
                                business.setLongitude(Double.parseDouble(idJSONObject.getString("Longitude")));
                            if (idJSONObject.has("BusinessUrl"))
                                business.setBusinessUrl(idJSONObject.getString("BusinessUrl").replace("null", ""));
                            if (idJSONObject.has("MapUrl"))
                                business.setMapUrl(idJSONObject.getString("MapUrl").replace("null", ""));
                            if (idJSONObject.has("Rating")) {
                                JSONObject ratingJSONObject = idJSONObject.getJSONObject("Rating");
                                if (ratingJSONObject.has("AverageRating"))
                                    business.setAverageRating(Double.parseDouble(ratingJSONObject.getString("AverageRating")));
                                if (ratingJSONObject.has("TotalRatings"))
                                    business.setTotalRatings(Double.parseDouble(ratingJSONObject.getString("TotalRatings")));
                                if (ratingJSONObject.has("TotalReviews"))
                                    business.setTotalReviews(Integer.parseInt(ratingJSONObject.getString("TotalReviews")));
                                if (ratingJSONObject.has("LastReviewIntro"))
                                    business.setLastReviewIntro(ratingJSONObject.getString("LastReviewIntro").replace("null",""));
                            }
                            mBusinesses.add(business);
                        }
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

            return mBusinesses;
        }


        @Override
        protected void onPostExecute(ArrayList<Business> items) {

            mBusinesses = items;
            setupAdapter();
            saveQueryWords();
        }

    }






}
