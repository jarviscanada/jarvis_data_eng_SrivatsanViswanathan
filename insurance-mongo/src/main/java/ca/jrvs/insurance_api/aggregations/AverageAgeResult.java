package ca.jrvs.insurance_api.aggregations;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class AverageAgeResult {
    private double averageAge;

    public double getAverageAge() {
        return Math.round(averageAge * 100.0) / 100.0;
    }
    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }
}
