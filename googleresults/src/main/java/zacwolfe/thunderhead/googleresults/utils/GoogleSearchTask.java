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
    private final static String TAG = "GoogleSearchTask";
    private final static int READ_TIMEOUT_MILLIS = 10000;
    private final static int CONNECT_TIMEOUT_MILLIS = 20000;


    private final OnResultsListener resultsListener;
    private int numResults;
    private String searchQuery;
    private String googleApiKey;
    private String searchEngineId;

    public GoogleSearchTask(OnResultsListener resultsListener, String googleApiKey, String searchEngineId, int numResults, String searchQuery) {
        this.resultsListener = resultsListener;
        this.googleApiKey = googleApiKey;
        this.searchEngineId = searchEngineId;
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
            list.setKey(googleApiKey);
            list.setCx(searchEngineId);

            List<Result> resultList = new ArrayList<>();
            long currentStart = 1;

            /* keeping going until desired # of results is reached */
            while (resultList.size() < numResults) {
                list.setNum(Math.min(10L, numResults - resultList.size()));
                list.setStart(currentStart);
                Search results = list.execute();
                List<Result> items = results.getItems();
                if (items != null) {
                    resultList.addAll(items);
                    currentStart += items.size();
                }

                /* google search api won't allow more than 100 results for 1 query */
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

