package zacwolfe.thunderhead.googleresults;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;

import zacwolfe.thunderhead.googleresults.adapter.SearchResultsAdapter;
import zacwolfe.thunderhead.googleresults.model.SearchResultsConfig;

public class SearchResultsView extends RecyclerView {

    private final float FLING_SCALE_DOWN_FACTOR = 1.0f;
    private SearchResultsConfig config;


    public SearchResultsView(Context context) {
        super(context);
    }

    public SearchResultsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.GoogleSearchResultView,
            0, 0);

        try {
            int colorLinkText = a.getColor(R.styleable.GoogleSearchResultView_colorLinkText, getColor(SearchResultsConfigBuilder.DEFAULT_LINK_COLOR));
            int colorTitleText = a.getColor(R.styleable.GoogleSearchResultView_colorTitleText, getColor(SearchResultsConfigBuilder.DEFAULT_TITLE_COLOR));
            int colorSnippetText = a.getColor(R.styleable.GoogleSearchResultView_colorSnippetText, getColor(SearchResultsConfigBuilder.DEFAULT_SNIPPET_COLOR));

            Drawable cardBackground = a.getDrawable(R.styleable.GoogleSearchResultView_cardBackground);

            float sizeLinkText = getRawSizeValue(a, R.styleable.GoogleSearchResultView_sizeLinkText,
                SearchResultsConfigBuilder.DEFAULT_SIZE_TEXT_LINK);
            float sizeTitleText = getRawSizeValue(a, R.styleable.GoogleSearchResultView_sizeTitleText,
                SearchResultsConfigBuilder.DEFAULT_SIZE_TEXT_TITLE);
            float sizeSnippetText = getRawSizeValue(a, R.styleable.GoogleSearchResultView_sizeSnippetText,
                SearchResultsConfigBuilder.DEFAULT_SIZE_TEXT_SNIPPET);

            int maxResults = a.getInt(R.styleable.GoogleSearchResultView_maxResults, SearchResultsConfigBuilder.DEFAULT_MAX_RESULTS);
            String searchQuery = a.getString(R.styleable.GoogleSearchResultView_searchQuery);

            config = new SearchResultsConfig(sizeLinkText, sizeTitleText, sizeSnippetText, colorLinkText,
                colorTitleText, colorSnippetText, maxResults,
                searchQuery == null || searchQuery.isEmpty() ? SearchResultsConfigBuilder.DEFAULT_SEARCH_QUERY : searchQuery, cardBackground);

        } finally {
            a.recycle();
        }

    }

    private int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    public SearchResultsConfig getConfig() {
        return config;
    }

    /**
     * get the raw value from a complex value ( Ex: complex = 14sp, returns 14)
     */
    private float getRawSizeValue(TypedArray a, int index, float defValue) {
        TypedValue outValue = new TypedValue();
        boolean result = a.getValue(index, outValue);
        if (!result) {
            return defValue;
        }

        return TypedValue.complexToFloat(outValue.data);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= FLING_SCALE_DOWN_FACTOR; // (between 0 for no fling, and 1 for normal fling, or more for faster fling).

        return super.fling(velocityX, velocityY);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (isInEditMode()) {
            setMeasuredDimension(widthSpec, 150);
        } else {
            super.onMeasure(widthSpec, heightSpec);
        }

    }

    @Override
    public SearchResultsAdapter getAdapter() {
        return (SearchResultsAdapter) super.getAdapter();
    }

    public void applyConfigFromLayout(SearchResults searchResults) {
        searchResults.getConfig().setupDefaultValues(config);
    }

}
