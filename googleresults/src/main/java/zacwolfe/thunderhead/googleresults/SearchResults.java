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

    SearchResultListener resultListener;
    private SearchResultsView resultsView;
    private SearchResultsAdapter searchResultsAdapter;

    private final int viewId;
    private final SearchResultsConfig config;
    private GoogleSearchResults googleSearchResults;


    /**
     * Package Private Constructor to insure SearchResults can't be initiated the default way
     */
    SearchResults(Builder builder, SearchResultsConfig config) {
        this.viewId = builder.viewId;
        this.config = config;
    }

    /* Init Calendar View */
    void init(View rootView) {
        resultsView = rootView.findViewById(viewId);
        resultsView.setHasFixedSize(true);
        resultsView.setHorizontalScrollBarEnabled(false);
        resultsView.applyConfigFromLayout(this);
        resultsView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsView.addOnScrollListener(new ResultsScrollListener());

//        post(() -> centerToPositionWithNoAnimation(positionOfDate(defaultSelectedDate)));

        GoogleSearchTask task = new GoogleSearchTask(results -> {
            if (results.getError() != null) {
                Log.i(TAG, "Dude we got an error!", results.getError());
            } else {
                Log.i(TAG, "Dude we got "+ results.getResults().size() + " results!");
            }

            for (Result r : results.getResults()) {
                try {
                    Log.i(TAG, r.toPrettyString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setGoogleSearchResults(results);
            searchResultsAdapter = new SearchResultsAdapter(this, getContext());
            resultsView.setAdapter(searchResultsAdapter);
        }, config.getMaxResults(), config.getSearchQuery());

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


    public void post(Runnable runnable) {
        resultsView.post(runnable);
    }

    @TargetApi(21)
    public void setElevation(float elevation) {
        resultsView.setElevation(elevation);
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




        /**
         * @return Instance of {@link SearchResults} initiated with builder settings
         */
        public SearchResults build() throws IllegalStateException {

            if (configBuilder == null) {
                configBuilder = new SearchResultsConfigBuilder(this);
            }
            SearchResultsConfig config = configBuilder.createConfig();

            SearchResults searchResults = new SearchResults(this, config);
            searchResults.init(rootView);
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