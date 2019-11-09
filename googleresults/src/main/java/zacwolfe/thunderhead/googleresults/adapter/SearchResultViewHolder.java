package zacwolfe.thunderhead.googleresults.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zacwolfe.thunderhead.googleresults.R;

class SearchResultViewHolder extends RecyclerView.ViewHolder {

    TextView link;
    TextView title;
    TextView snippet;
    ImageView image;
    ImageView background;
    View layoutContent;

    SearchResultViewHolder(View rootView) {
        super(rootView);
        link = rootView.findViewById(R.id.search_result_link);
        title = rootView.findViewById(R.id.search_result_title);
        snippet = rootView.findViewById(R.id.search_result_snippet);
        image = rootView.findViewById(R.id.search_result_image);
        layoutContent = rootView.findViewById(R.id.search_result_card);
        background = rootView.findViewById(R.id.search_result_background);
    }
}
