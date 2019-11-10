package zacwolfe.thunderhead.googleresults;

import android.graphics.drawable.Drawable;

import zacwolfe.thunderhead.googleresults.model.SearchResultsConfig;

public class SearchResultsConfigBuilder {

    public static final float DEFAULT_SIZE_TEXT_LINK = 14f;
    public static final float DEFAULT_SIZE_TEXT_TITLE = 16f;
    public static final float DEFAULT_SIZE_TEXT_SNIPPET = 12f;
    public final static int DEFAULT_LINK_COLOR = R.color.font_dark_gray;
    public final static int DEFAULT_TITLE_COLOR = R.color.font_black;
    public final static int DEFAULT_SNIPPET_COLOR = R.color.font_medium_dark_gray;
    public final static String DEFAULT_SEARCH_QUERY = "Thunderhead ONE";
    public final static int DEFAULT_MAX_RESULTS = 10;


    /* Font Size, Color and Background */
    private float sizeLinkText;
    private float sizeTitleText;
    private float sizeSnippetText;

    private int colorLinkText = -1;
    private int colorTitleText = -1;
    private int colorSnippetText = -1;
    private Drawable cardBackground;

    private int maxResults;
    private String searchQuery;

    private SearchResults.Builder builder;

    SearchResultsConfigBuilder(SearchResults.Builder builder) {
        this.builder = builder;
    }

    /**
     * Set the text size of the search result text in scale-independent pixels
     *
     * @param sizeLinkText    the Link text size, in SP
     * @param sizeTitleText the Title text size, in SP
     * @param sizeSnippetText the Snippet text size, in SP
     */
    public SearchResultsConfigBuilder textSize(float sizeLinkText, float sizeTitleText,
                                               float sizeSnippetText) {
        this.sizeLinkText = sizeLinkText;
        this.sizeTitleText = sizeTitleText;
        this.sizeSnippetText = sizeSnippetText;
        return this;
    }

    /**
     * Set the text size of the link text in scale-independent pixels
     *
     * @param size the Link text size, in SP
     */
    public SearchResultsConfigBuilder sizeLinkText(float size) {
        this.sizeLinkText = size;
        return this;
    }

    /**
     * Set the text size of the title text in scale-independent pixels
     *
     * @param size the Title text size, in SP
     */
    public SearchResultsConfigBuilder sizeTitleText(float size) {
        this.sizeTitleText = size;
        return this;
    }

    /**
     * Set the text size of the snippet text in scale-independent pixels
     *
     * @param size the Snippet text size, in SP
     */
    public SearchResultsConfigBuilder sizeSnippetText(float size) {
        this.sizeSnippetText = size;
        return this;
    }

    /**
     * Set the text color of the search result text
     *
     * @param colorLinkText the Link text color
     * @param colorTitleText the Title text color
     * @param colorSnippetText the Snippet text color
     */
    public SearchResultsConfigBuilder textColor(int colorLinkText, int colorTitleText,
                                               int colorSnippetText) {
        this.colorLinkText = colorLinkText;
        this.colorTitleText = colorTitleText;
        this.colorSnippetText = colorSnippetText;
        return this;
    }

    /**
     * Set the text color of the link text
     *
     * @param color the Link text color
     */
    public SearchResultsConfigBuilder colorLinkText(int color) {
        this.colorLinkText = color;
        return this;
    }

    /**
     * Set the text color of the title text
     *
     * @param color the Title text color
     */
    public SearchResultsConfigBuilder colorTitleText(int color) {
        this.colorTitleText = color;
        return this;
    }


    /**
     * Set the text color of the snippet text
     *
     * @param color the Snippet text color
     */
    public SearchResultsConfigBuilder colorSnippetText(int color) {
        this.colorSnippetText = color;
        return this;
    }

    /**
     * Set the background of the search result card
     *
     * @param background the search results background
     */
    public SearchResultsConfigBuilder resultsBackground(Drawable background) {
        this.cardBackground = background;
        return this;
    }

    /**
     * Set the maximum number of search results to return [0,100]
     *
     * @param results the maximum number of search results
     */
    public SearchResultsConfigBuilder maxResults(int results) {
        this.maxResults = results;
        return this;
    }


    /**
     * Set the search query value
     *
     * @param query the search query
     */
    public SearchResultsConfigBuilder searchQuery(String query) {
        this.searchQuery = query;
        return this;
    }

    SearchResultsConfig createConfig() {
        return new SearchResultsConfig(sizeLinkText, sizeTitleText, sizeSnippetText, colorLinkText, colorTitleText, colorSnippetText, maxResults, searchQuery, cardBackground);
    }

    public SearchResults.Builder end() {
        return builder;
    }

}
