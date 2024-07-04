package ca.jrvs.insurance_api.controller;

import ca.jrvs.insurance_api.aggregations.*;
import ca.jrvs.insurance_api.model.Car;
import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.service.PersonService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/insurance_api")
public class PersonController {

    @Autowired
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping("person")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Person> postPerson(@RequestBody Person person) {
        service.save(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @GetMapping("people")
    public List<Person> getPeople() {
        return service.findAll();
    }

    @GetMapping("person/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable ObjectId id) {
        Optional<Person> o = service.findOne(id);
        if (o.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(o.get());
    }

    @DeleteMapping("person/{id}")
    public ResponseEntity<Optional<Person>> deletePerson(@PathVariable String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Person> deletedPerson = service.findOne(objectId);
        service.delete(objectId);
        return new ResponseEntity<>(deletedPerson, HttpStatus.CREATED);
    }

    @PutMapping("person")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        ObjectId id = person.getId();
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Person> existingPerson = service.findOne(id);
        if (existingPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.update(person);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("people/count")
    public ResponseEntity<Long> countPeople() {
        long count = service.getCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("people/average-age")
    public ResponseEntity<Double> getAverageAge() {
        double averageAge = service.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("people/max-cars")
    public ResponseEntity<Integer> getMaxCars() {
        int maxCars = service.getMaxCars();
        return ResponseEntity.ok(maxCars);
    }

    @GetMapping("people/by-city/{city}")
    public ResponseEntity<PeopleInCityResult> getPeopleInCity(@PathVariable String city) {
        List<PeopleInCityResult> results = service.getPeopleInCity(city);

        PeopleInCityResult response = new PeopleInCityResult();
        response.setPeopleCount(results.get(0).getPeopleInCity());; // Assuming results size is count of people
        response.setPeopleNames(results.get(0).getPeopleNames()); // Assuming names are in first result

        return ResponseEntity.ok(response);
    }

    @GetMapping("people/by-country/{country}")
    public ResponseEntity<PeopleInCountryResult> getPeopleInCountry(@PathVariable String country) {
        List<PeopleInCountryResult> results = service.getPeopleInCountry(country);

        PeopleInCountryResult response = new PeopleInCountryResult();
        response.setPeopleCount(results.get(0).getPeopleInCountry());
        response.setPeopleNames(results.get(0).getPeopleNames());

        return ResponseEntity.ok(response);
    }

    @GetMapping("people/uninsured")
    public ResponseEntity<UninsuredDriversResult> getUninsuredDrivers() {
        List<UninsuredDriversResult> results = service.getUninsuredDrivers();

        UninsuredDriversResult response = new UninsuredDriversResult();
        response.setUninsuredDrivers((results.get(0).getUninsuredDrivers()));
        response.setPeopleNames(results.get(0).getPeopleNames());

        return ResponseEntity.ok(response);
    }
    @GetMapping("people/insured")
    public ResponseEntity<InsuredDriversResult> getInsuredDrivers() {
        List<InsuredDriversResult> results = service.getInsuredDrivers();

        InsuredDriversResult response = new InsuredDriversResult();
        response.setInsuredDrivers((results.get(0).getInsuredDrivers()));
        response.setPeopleNames(results.get(0).getPeopleNames());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/people/car-make/{make}")
    public ResponseEntity<CarMakeCountResult> getCarMakeCount(@PathVariable String make) {
        List<CarMakeCountResult> results = service.getCarMakeCount(make);

        CarMakeCountResult response = new CarMakeCountResult();
        response.setCarMakeCount(results.get(0).getCarMakeCount());
        response.setPeopleNames(results.get(0).getPeopleNames());

        return ResponseEntity.ok(response);
    }

    //FINISH THE REST ACCORDING TO THE FEATURES IN PERSONSERVICE

}
