package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "people")
public class UninsuredDriversResult {
    private long uninsuredDrivers;
    private List<String> peopleNames;

    public long getUninsuredDrivers() {
        return uninsuredDrivers;
    }
    public void setUninsuredDrivers(long uninsuredDrivers) {
        this.uninsuredDrivers = uninsuredDrivers;
    }

    public List<String> getPeopleNames() {
        return peopleNames;
    }
    public void setPeopleNames(List<String> peopleNames) {
        this.peopleNames = peopleNames;
    }
}