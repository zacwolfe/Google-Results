package zacwolfe.thunderhead.googleresults.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.util.ArrayList;
import java.util.List;

public class GoogleSearchTask extends AsyncTask<Void, Integer, GoogleSearchResults> {
    private final static String TAG = "ThunderheadSearchTask";
    private final static String GOOGLE_API_KEY = "AIzaSyDsAn3q2vTIN_1UdiAUJepKo9oK3ndXDOk";
    private final static String SEARCH_ENGINE_ID = "003466369684162506734:2l0ykpzw188";
    private final static int READ_TIMEOUT_MILLIS = 10000;
    private final static int CONNECT_TIMEOUT_MILLIS = 20000;


    private final OnResultsListener resultsListener;
    private int numResults;
    private String searchQuery;

    public GoogleSearchTask(OnResultsListener resultsListener, int numResults, String searchQuery) {
        this.resultsListener = resultsListener;
        this.numResults = numResults;
        this.searchQuery = searchQuery;
    }


    @Override
    protected GoogleSearchResults doInBackground(Void... voids) {
        try {
            Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) {
                    try {
                        // set connect and read timeouts
                        httpRequest.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
                        httpRequest.setReadTimeout(READ_TIMEOUT_MILLIS);

                    } catch (Exception ex) {
                        Log.e(TAG, "Couldn't initialize request", ex);
                    }
                }
            });

            Customsearch.Cse.List list = customsearch.cse().list(searchQuery);
            list.setKey(GOOGLE_API_KEY);
            list.setCx(SEARCH_ENGINE_ID);

            List<Result> resultList = new ArrayList<>();
            long currentStart = 1;

            while (resultList.size() < numResults) {
                list.setNum(Math.min(10L, numResults - resultList.size()));
                list.setStart(currentStart);
                Search results = list.execute();
                List<Result> items = results.getItems();
                if (items != null) {
                    resultList.addAll(items);
                    currentStart += items.size();
                }

                if (items == null || items.size() < 10 || currentStart >= 100) {
                    break;
                }

            }
            return new GoogleSearchResults(resultList);
        } catch (Exception e) {
            return new GoogleSearchResults(e);
        }

    }

    @Override
    protected void onPostExecute(GoogleSearchResults searchResults) {
        if (resultsListener != null) {
            resultsListener.onResults(searchResults);
        }
    }

    public interface OnResultsListener {
        void onResults(GoogleSearchResults results);
    }

}

