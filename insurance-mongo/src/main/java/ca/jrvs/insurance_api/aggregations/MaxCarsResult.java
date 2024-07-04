package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class MaxCarsResult {
    private int maxCars;

    public int getMaxCars() {
        return maxCars;
    }
    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }
}
