package bc.bfi.google_places.scrapers;

import java.util.ArrayList;
import java.util.List;

public class Queries {

    private final List<String> queries = new ArrayList<>();

    public void loadQueries(String query) {
        this.queries.clear();

        for (String location : query.trim().split("\n")) {
            this.queries.add(location.trim());
        }
    }

    public List<String> getQueries() {
        return queries;
    }

}
