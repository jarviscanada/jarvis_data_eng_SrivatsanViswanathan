package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "people")
public class PeopleInCountryResult {
    private long peopleInCountry;
    private List<String> peopleNames;

    public long getPeopleInCountry() {
        return peopleInCountry;
    }
    public void setPeopleCount(long peopleInCountry) {
        this.peopleInCountry = peopleInCountry;
    }

    public List<String> getPeopleNames() {
        return peopleNames;
    }
    public void setPeopleNames(List<String> peopleNames) {
        this.peopleNames = peopleNames;
    }
}