package zacwolfe.thunderhead.googleresults;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.api.services.customsearch.model.Result;

import java.io.IOException;

import zacwolfe.thunderhead.googleresults.adapter.SearchResultsAdapter;
import zacwolfe.thunderhead.googleresults.model.SearchResultsConfig;
import zacwolfe.thunderhead.googleresults.utils.GoogleSearchResults;
import zacwolfe.thunderhead.googleresults.utils.GoogleSearchTask;
import zacwolfe.thunderhead.googleresults.utils.SearchResultListener;


public final class SearchResults {

    private final static String TAG = "SearchResults";

    private SearchResultListener resultListener;
    private SearchResultsView resultsView;
    private SearchResultsAdapter searchResultsAdapter;

    private final int viewId;
    private final SearchResultsConfig config;
    private GoogleSearchResults googleSearchResults;


    /**
     * Package Private Constructor to insure SearchResults can't be initiated the default way
     */
    private SearchResults(Builder builder, SearchResultsConfig config) {
        this.viewId = builder.viewId;
        this.config = config;
    }

    /* Init Results View */
    private void init(View rootView, String googleApiKey, String searchEngineId) {
        resultsView = rootView.findViewById(viewId);
        resultsView.setHasFixedSize(true);
        resultsView.setHorizontalScrollBarEnabled(false);
        resultsView.applyConfigFromLayout(this);
        resultsView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsView.addOnScrollListener(new ResultsScrollListener());

        GoogleSearchTask task = new GoogleSearchTask(results -> {
            setGoogleSearchResults(results);
            searchResultsAdapter = new SearchResultsAdapter(this, getContext());
            resultsView.setAdapter(searchResultsAdapter);
        }, googleApiKey, searchEngineId, config.getMaxResults(), config.getSearchQuery());

        task.execute();
    }

    public SearchResultListener getResultListener() {
        return resultListener;
    }

    public void setResultListener(SearchResultListener resultListener) {
        this.resultListener = resultListener;
    }

    private void setGoogleSearchResults(GoogleSearchResults results) {
        this.googleSearchResults = results;
    }

    public GoogleSearchResults getGoogleSearchResults() {
        return this.googleSearchResults;
    }


    public SearchResultsView getSearchResultsView() {
        return resultsView;
    }

    public Context getContext() {
        return resultsView.getContext();
    }


    public SearchResultsConfig getConfig() {
        return config;
    }


    public static class Builder {

        final int viewId;
        final View rootView;
        private String googleApiKey;
        private String searchEngineId;

        private SearchResultsConfigBuilder configBuilder;

        /**
         * @param rootView pass the rootView for the Fragment where SearchResults is attached
         * @param viewId   the id specified for SearchResultsView in your layout
         */
        public Builder(View rootView, int viewId) {
            this.rootView = rootView;
            this.viewId = viewId;
        }

        /**
         * @param activity pass the activity where SearchResults is attached
         * @param viewId   the id specified for SearchResultsView in your layout
         */
        public Builder(Activity activity, int viewId) {
            this.rootView = activity.getWindow().getDecorView();
            this.viewId = viewId;
        }

        public SearchResultsConfigBuilder configure() {
            if (configBuilder == null) {
                configBuilder = new SearchResultsConfigBuilder(this);
            }

            return configBuilder;
        }

        public Builder setGoogleApiKey(String googleApiKey) {
            this.googleApiKey = googleApiKey;
            return this;
        }

        public Builder setSearchEngineId(String searchEngineId) {
            this.searchEngineId = searchEngineId;
            return this;
        }


        /**
         * @return Instance of {@link SearchResults} initiated with builder settings
         */
        public SearchResults build() throws IllegalStateException {

            if (configBuilder == null) {
                configBuilder = new SearchResultsConfigBuilder(this);
            }
            SearchResultsConfig config = configBuilder.createConfig();

            SearchResults searchResults = new SearchResults(this, config);
            searchResults.init(rootView, googleApiKey, searchEngineId);
            return searchResults;
        }
    }


    private class ResultsScrollListener extends RecyclerView.OnScrollListener {


        ResultsScrollListener() {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (resultListener != null) {
                resultListener.onResultsScroll(resultsView, dx, dy);
            }
        }

    }
}