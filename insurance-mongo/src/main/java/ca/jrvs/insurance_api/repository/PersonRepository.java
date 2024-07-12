package ca.jrvs.insurance_api.repository;

import ca.jrvs.insurance_api.aggregations.*;
import ca.jrvs.insurance_api.model.Person;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, ObjectId> {
    @Aggregation(pipeline = {
            "{ $count: 'count' }"
    })
    List<CountResult> getCount();

    @Aggregation(pipeline = {
            "{ $group: { _id: null, averageAge: { $avg: '$age' } } }"
    })
    List<AverageAgeResult> getAverageAge();

    @Aggregation(pipeline = {
            "{ $project: { carCount: { $size: '$carEntities' } } }",
            "{ $group: { _id: null, maxCars: { $max: '$carCount' } } }"
    })
    List<MaxCarsResult> getMaxCars();

    @Aggregation(pipeline = {
            "{$match: { 'addressEntity.city': ?0 }}",
            "{$project: { firstName: 1, lastName: 1 }}",
            "{$group: { _id: null, peopleInCity: { $sum: 1 }, peopleNames: { $push: { $concat: ['$firstName', ' ', '$lastName', ' ', '(ID : ', {$toString: $_id  }, ')'] } } }}"
    })
    List<PeopleInCityResult> countPeopleInCity(String city);

    @Aggregation(pipeline = {
            "{$match: { 'addressEntity.country': ?0 }}",
            "{$project: { firstName: 1, lastName: 1 }}",
            "{$group: { _id: null, peopleInCountry: { $sum: 1 }, peopleNames: { $push: { $concat: ['$firstName', ' ', '$lastName', ' ', '(ID : ', {$toString: $_id  }, ')'] } } }}"
    })
    List<PeopleInCountryResult> countPeopleInCountry(String country);

    @Aggregation(pipeline = {
            "{$match: { insurance:  false }}",
            "{$project: { firstName: 1, lastName: 1 }}",
            "{$group: { _id: null, uninsuredDrivers: { $sum: 1 }, peopleNames: { $push: { $concat: ['$firstName', ' ', '$lastName', ' ', '(ID : ', {$toString: $_id  }, ')'] } } }}"
    })
    List<UninsuredDriversResult> getUninsuredDrivers();

    @Aggregation(pipeline = {
            "{$match: { insurance:  true }}",
            "{$project: { firstName: 1, lastName: 1 }}",
            "{$group: { _id: null, insuredDrivers: { $sum: 1 }, peopleNames: { $push: { $concat: ['$firstName', ' ', '$lastName', ' ', '(ID : ', {$toString: $_id  }, ')'] } } }}"
    })
    List<InsuredDriversResult> getInsuredDrivers();

    @Aggregation(pipeline = {
            "{$unwind: '$carEntities'}",
            "{$match: {'carEntities.make': ?0}}",
            "{$project: { firstName: 1, lastName: 1 }}",
            "{$group: { _id: null, carMakeCount: { $sum: 1 }, peopleNames: { $push: { $concat: ['$firstName', ' ', '$lastName', ' ', {$toString: $_id  }] } } }}"
    })
    List<CarMakeCountResult> countCarMake(String make);
}
