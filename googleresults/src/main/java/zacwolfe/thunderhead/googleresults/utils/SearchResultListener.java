package zacwolfe.thunderhead.googleresults.utils;

import zacwolfe.thunderhead.googleresults.SearchResultsView;

public abstract class SearchResultListener {

    public abstract void onResultSelected(String urlString, int position);
    public abstract void onResultLongPress(String urlString, int position);

    public void onResultsScroll(SearchResultsView resultsView, int dx, int dy) {
    }


}