package zacwolfe.thunderhead.googleresults.utils;

import com.google.api.services.customsearch.model.Result;

import java.util.Collections;
import java.util.List;

public class GoogleSearchResults {
    private List<Result> results;
    private Exception error;

    public List<Result> getResults() {
        return results == null ? Collections.emptyList() : results;
    }

    public Exception getError() {
        return error;
    }

    GoogleSearchResults(List<Result> results) {
        super();
        this.results = results;
    }

    GoogleSearchResults(Exception error) {
        super();
        this.error = error;
    }
}
