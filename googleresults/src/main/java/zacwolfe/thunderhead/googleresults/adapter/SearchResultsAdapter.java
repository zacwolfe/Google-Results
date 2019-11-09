package zacwolfe.thunderhead.googleresults.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.customsearch.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import zacwolfe.thunderhead.googleresults.R;
import zacwolfe.thunderhead.googleresults.SearchResults;
import zacwolfe.thunderhead.googleresults.model.SearchResultsConfig;
import zacwolfe.thunderhead.googleresults.utils.GoogleSearchResults;


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    private final static String TAG = "SearchResultsAdapter";

    private final SearchResults search;
    private final Context context;

    public SearchResultsAdapter(final SearchResults search, final Context context) {
        this.search = search;
        this.context = context;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_card, parent, false);

        return new SearchResultViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        GoogleSearchResults results = search.getGoogleSearchResults();
        if (results == null) {
            return 0;
        }

        if (results.getError() != null) {
            return 1;
        }

        return results.getResults().size();
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        GoogleSearchResults results = search.getGoogleSearchResults();
        if (position == 0 && results.getError() != null) {
            holder.link.setVisibility(View.GONE);
            holder.title.setText(context.getString(R.string.google_error));
            holder.title.setTextColor(Color.RED);
            holder.snippet.setText(results.getError().getLocalizedMessage());
        } else {
            List<Result> items = results.getResults();

            Result item = items.get(position);

            SearchResultsConfig config = search.getConfig();

            holder.itemView.setOnClickListener(new ResultClickListener(item, holder));
            holder.itemView.setOnLongClickListener(new ResultLongPressListener(item, holder));

            holder.link.setVisibility(View.VISIBLE);
            applyStyle(holder, config);

            holder.link.setText(formatLink(item.getFormattedUrl()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.title.setText(Html.fromHtml(item.getHtmlTitle(), Html.FROM_HTML_MODE_COMPACT));
                holder.snippet.setText(Html.fromHtml(item.getHtmlSnippet(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.title.setText(Html.fromHtml(item.getHtmlTitle()));
                holder.snippet.setText(Html.fromHtml(item.getHtmlSnippet()));
            }

            String imageLink = getImageLink(item);

            if (imageLink != null) {
                Picasso.with(context).load(imageLink).into(holder.image);
            }
        }

    }

    private String getImageLink(Result item) {
        if (item.getImage() != null) {
            return item.getImage().getThumbnailLink();
        }

        if (item.getPagemap().containsKey("cse_thumbnail")) {
            List<Map<String, Object>> l = item.getPagemap().get("cse_thumbnail");
            if (l != null && !l.isEmpty()) {
                return (String) l.get(0).get("src");
            }
        }

        if (item.getPagemap().containsKey("metatags")) {
            List<Map<String, Object>> l = item.getPagemap().get("metatags");
            if (l != null && !l.isEmpty()) {
                String imageLink = (String) l.get(0).get("og:image");
                if (imageLink != null) {
                    return imageLink;
                }
            }
        }

        if (item.getPagemap().containsKey("cse_image")) {
            List<Map<String, Object>> l = item.getPagemap().get("cse_image");
            if (l != null && !l.isEmpty()) {
                return (String) l.get(0).get("src");
            }
        }

        return null;

    }

    private String formatLink(String formattedUrl) {
        return formattedUrl == null ? "" : formattedUrl.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "").replaceAll("/", " > ");
    }


    private void applyStyle(SearchResultViewHolder viewHolder, SearchResultsConfig config) {

        viewHolder.link.setTextColor(config.getColorLinkText());
        viewHolder.title.setTextColor(config.getColorTitleText());
        viewHolder.snippet.setTextColor(config.getColorSnippetText());

        viewHolder.link.setTextSize(config.getSizeLinkText());
        viewHolder.title.setTextSize(config.getSizeTitleText());
        viewHolder.snippet.setTextSize(config.getSizeSnippetText());

        viewHolder.background.setImageDrawable(config.getBackground());
        viewHolder.background.setVisibility(config.getBackground() == null ? View.GONE : View.VISIBLE);

        /*if (Build.VERSION.SDK_INT >= 16) {

            viewHolder.layoutContent.setBackground(config.getBackground());
        } else {
            viewHolder.layoutContent.setBackgroundDrawable(config.getBackground());
        }*/
    }


    private class ResultClickListener implements View.OnClickListener {
        private final SearchResultViewHolder viewHolder;
        private final Result result;

        ResultClickListener(Result result, SearchResultViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            this.result = result;
        }

        @Override
        public void onClick(View v) {
            if (search.getResultListener() == null) {
                return;
            }

            int position = viewHolder.getAdapterPosition();
            if (position == -1)
                return;

            search.getResultListener().onResultSelected(result.getLink(), position);
        }
    }

    private class ResultLongPressListener implements View.OnLongClickListener {
        private final SearchResultViewHolder viewHolder;
        private final Result result;

        ResultLongPressListener(Result result, SearchResultViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            this.result = result;
        }

        @Override
        public boolean onLongClick(View v) {
            if (search.getResultListener() == null) {
                return false;
            }

            int position = viewHolder.getAdapterPosition();
            if (position == -1)
                return false;

            search.getResultListener().onResultLongPress(result.getLink(), position);
            return true;
        }
    }
}
