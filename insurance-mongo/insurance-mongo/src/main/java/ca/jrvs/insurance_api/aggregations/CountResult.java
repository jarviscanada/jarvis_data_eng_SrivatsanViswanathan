package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection =  "people")
public class CountResult {
    private long count;

    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
}
