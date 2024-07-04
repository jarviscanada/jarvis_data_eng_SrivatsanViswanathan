package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection =  "people")
public class CarMakeCountResult {
    private long carMakeCount;
    private List<String> peopleNames;

    public long getCarMakeCount() {
        return carMakeCount;
    }
    public void setCarMakeCount(long carMakeCount) {
        this.carMakeCount = carMakeCount;
    }

    public List<String> getPeopleNames() {
        return peopleNames;
    }
    public void setPeopleNames(List<String> peopleNames) {
        this.peopleNames = peopleNames;
    }
}
