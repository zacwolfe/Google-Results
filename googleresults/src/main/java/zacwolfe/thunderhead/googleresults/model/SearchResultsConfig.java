package zacwolfe.thunderhead.googleresults.model;

import android.graphics.drawable.Drawable;

public class SearchResultsConfig {

    /* Font Sizes & Colors */
    private float sizeLinkText;
    private float sizeTitleText;
    private float sizeSnippetText;
    private int colorLinkText;
    private int colorTitleText;
    private int colorSnippetText;

    private Drawable background;

    /* Search settings */
    private int maxResults;
    private String searchQuery;


    public SearchResultsConfig(float sizeLinkText, float sizeTitleText, float sizeSnippetText, int colorLinkText, int colorTitleText, int colorSnippetText, int maxResults, String searchQuery, Drawable background) {
        this.sizeLinkText = sizeLinkText;
        this.sizeTitleText = sizeTitleText;
        this.sizeSnippetText = sizeSnippetText;
        this.colorTitleText = colorTitleText;
        this.colorLinkText = colorLinkText;
        this.colorSnippetText = colorSnippetText;
        this.background = background;
        this.searchQuery = searchQuery;
        this.maxResults = maxResults;
    }


    public SearchResultsConfig setSizeLinkText(float sizeLinkText) {
        this.sizeLinkText = sizeLinkText;
        return this;
    }

    public SearchResultsConfig setSizeTitleText(float sizeTitleText) {
        this.sizeTitleText = sizeTitleText;
        return this;
    }

    public SearchResultsConfig setSizeSnippetText(float sizeSnippetText) {
        this.sizeSnippetText = sizeSnippetText;
        return this;
    }

    public SearchResultsConfig setColorLinkText(int colorLinkText) {
        this.colorLinkText = colorLinkText;
        return this;
    }

    public SearchResultsConfig setColorTitleText(int colorTitleText) {
        this.colorTitleText = colorTitleText;
        return this;
    }

    public SearchResultsConfig setColorSnippetText(int colorSnippetText) {
        this.colorSnippetText = colorSnippetText;
        return this;
    }

    public SearchResultsConfig setBackground(Drawable background) {
        this.background = background;
        return this;
    }

    public SearchResultsConfig setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public SearchResultsConfig setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        return this;
    }


    public int getColorTitleText() {
        return colorTitleText;
    }

    public int getColorSnippetText() {
        return colorSnippetText;
    }

    public float getSizeLinkText() {
        return sizeLinkText;
    }

    public float getSizeTitleText() {
        return sizeTitleText;
    }

    public float getSizeSnippetText() {
        return sizeSnippetText;
    }

    public int getColorLinkText() {
        return colorLinkText;
    }

    public Drawable getBackground() {
        return background;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setupDefaultValues(SearchResultsConfig defaultConfig) {
        if (defaultConfig == null) {
            return;
        }
        if (sizeLinkText <= 0) {
            sizeLinkText = defaultConfig.sizeLinkText;
        }
        if (sizeTitleText <= 0) {
            sizeTitleText = defaultConfig.sizeTitleText;
        }
        if (sizeSnippetText <= 0) {
            sizeSnippetText = defaultConfig.sizeSnippetText;
        }
        if (colorLinkText == -1) {
            colorLinkText = defaultConfig.colorLinkText;
        }
        if (colorTitleText == -1) {
            colorTitleText = defaultConfig.colorTitleText;
        }
        if (colorSnippetText == -1) {
            colorSnippetText = defaultConfig.colorSnippetText;
        }

        if (background == null) {
            background = defaultConfig.background;
        }

        if (searchQuery == null) {
            searchQuery = defaultConfig.searchQuery;
        }

        if (maxResults <= 0) {
            maxResults = defaultConfig.maxResults;
        }


    }
}
