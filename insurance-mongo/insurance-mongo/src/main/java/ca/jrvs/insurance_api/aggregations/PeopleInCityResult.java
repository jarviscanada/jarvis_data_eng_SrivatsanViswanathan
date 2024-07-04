package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "people")
public class PeopleInCityResult {
    private long peopleInCity;
    private List<String> peopleNames;

    public long getPeopleInCity() {
        return peopleInCity;
    }
    public void setPeopleCount(long peopleInCity) {
        this.peopleInCity = peopleInCity;
    }

    public List<String> getPeopleNames() {
        return peopleNames;
    }
    public void setPeopleNames(List<String> peopleNames) {
        this.peopleNames = peopleNames;
    }
}
