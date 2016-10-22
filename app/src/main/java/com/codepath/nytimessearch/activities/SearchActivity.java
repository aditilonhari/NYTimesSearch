package com.codepath.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ComplexArticleAdapter;
import com.codepath.nytimessearch.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.adapters.FilterDialogFragment;
import com.codepath.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.EditFilterDialogListener{

    @BindView(R.id.rvArticles) RecyclerView rvArticles;

    static final int FILTER_ACTIVITY = 1;
    List<Article> articles;
    ComplexArticleAdapter rvAdapter;
    static final String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    RequestParams params;
    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initialize();
        hideKeyboard();

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        rvArticles.setLayoutManager(gridLayoutManager);
        // Add the scroll listener
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Send an API request to retrieve appropriate data using the offset value as a parameter.
                getDataFromUrl(page);
            }
        });

        mOnClickListener = view -> {
            articles.clear();
            getDataFromUrl(0);
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                onArticleSearch(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_filter:
                showFilterDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        articles = new ArrayList<>();
        rvAdapter = new ComplexArticleAdapter(articles);
        rvArticles.setAdapter(rvAdapter);
    }

    public void onArticleSearch(String query) {
        params = new RequestParams();
        params.put("api-key","b43201480eca4d3a9ee2ef603736b139");
        articles.clear();
        params.put("q", query);
        getDataFromUrl(0);
    }

    private void getDataFromUrl(int page){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20 * 1000); // Increase default timeout
        params.put("page", page);
        Log.d("---------> DEBUG Params", params.toString());
        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray articleJsonResults;
                try {
                    hideKeyboard();
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    if (articleJsonResults.length() == 0) {
                        articles.clear();
                        Snackbar.make(findViewById(R.id.content_search), "No data available", Snackbar.LENGTH_LONG)
                                .show();
                    }else
                        articles.addAll(Article.fromJSONArray(articleJsonResults));

                    rvAdapter.notifyDataSetChanged();
                    Log.d("DEBUG RESPNSE ---> ", response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (!isNetworkAvailable())
                    Log.d("DEBUG: ", "Network not connected");
                else
                    Snackbar.make(findViewById(R.id.content_search), "Network connectivity lost!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY",mOnClickListener)
                            .setActionTextColor(Color.RED)
                            .show();

                if (!isOnline())
                    Log.d("DEBUG: ", "Device not online. Check Internet connection!");
                else
                    Snackbar.make(findViewById(R.id.content_search), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY",mOnClickListener)
                            .setActionTextColor(Color.RED)
                            .show();
            }
        });
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();

        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {e.printStackTrace();}
        return false;
    }

    private void setFilters(View v){
        articles.clear();
        Intent getFilters = new Intent(SearchActivity.this, FilterActivity.class);
        startActivityForResult(getFilters, FILTER_ACTIVITY);
    }

    private void showFilterDialog(){
        articles.clear();
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.newInstance();
        filterDialogFragment.setTargetFragment(filterDialogFragment, FILTER_ACTIVITY);
        /*getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, filterDialogFragment).commit();*/
        filterDialogFragment.show(fm, "activity_filters");
    }

    private String formatString(String news) {
        String[] array = news.split(" ");

        StringBuilder sb = new StringBuilder();
        sb.append("news_desk:(");
        for (String anArray : array) {
            sb.append("\"");
            sb.append(anArray);
            sb.append("\"");
            sb.append("%20");
        }
        sb.append(")");
        return sb.toString();
    }

    private void hideKeyboard(){
        View v = this.getCurrentFocus();
        if(v != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onFinishEditDialog(Bundle inputBundle) {

        String date =  inputBundle.getString("date");
        String spin = inputBundle.getString("spinner_value");
        String news =  inputBundle.getString("news_values");
        String formattedNews = "";
        if(!news.isEmpty()) {
            formattedNews = formatString(news);
            formattedNews = formattedNews.replaceAll(" ", "%20");
            formattedNews = formattedNews.replaceAll("\\+", "%20");
        }

        params.put("begin_date", date);
        params.put("sort", spin);
        if(!formattedNews.isEmpty())
            params.put("fq", formattedNews);

        getDataFromUrl(0);
    }

}
    /*private void getDataFromUrl(String url, RequestParams params){
        adapter.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("DEBUG", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type collectionType = new TypeToken<List<Article>>() {
                }.getType();
                Gson gson = new Gson();
                articles = gson.fromJson(responseString, collectionType);
                adapter.notifyDataSetChanged();
            }
        });

    }*/
