package zac.wolfe.thunderhead.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import zacwolfe.thunderhead.googleresults.SearchResults;
import zacwolfe.thunderhead.googleresults.utils.SearchResultListener;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private final static String GOOGLE_API_KEY = "AIzaSyDsAn3q2vTIN_1UdiAUJepKo9oK3ndXDOk";
    private final static String SEARCH_ENGINE_ID = "003466369684162506734:2l0ykpzw188";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchResults searchResults = new SearchResults.Builder(this, R.id.searchResultsView)
            .configure()
                .end()
            .setGoogleApiKey(GOOGLE_API_KEY)
            .setSearchEngineId(SEARCH_ENGINE_ID)
            .build();


        searchResults.setResultListener(new SearchResultListener() {

            @Override
            public void onResultSelected(String url, int position) {

                Log.i(TAG, "Selected " + url);
                launch(url);
            }

            @Override
            public void onResultLongPress(String url, int position) {
                Log.i(TAG, "Long pressed " + url);
            }


        });

    }

    private void launch(String urlString) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        startActivity(browserIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
