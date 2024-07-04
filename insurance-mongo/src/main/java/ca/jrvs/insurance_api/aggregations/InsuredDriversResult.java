package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "people")
public class InsuredDriversResult {
    private long insuredDrivers;
    private List<String> peopleNames;

    public long getInsuredDrivers() {
        return insuredDrivers;
    }
    public void setInsuredDrivers(long insuredDrivers) {
        this.insuredDrivers = insuredDrivers;
    }

    public List<String> getPeopleNames() {
        return peopleNames;
    }
    public void setPeopleNames(List<String> peopleNames) {
        this.peopleNames = peopleNames;
    }
}