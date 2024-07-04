package ca.jrvs.insurance_api.service;

import ca.jrvs.insurance_api.aggregations.*;
import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.repository.PersonRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; // Import for List
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private final PersonRepository repo;

    @Autowired
    public PersonService(PersonRepository repo) {
        this.repo = repo;
    }

    public void save(Person person) {
        repo.save(person);
    }

    public Optional<Person> findOne(ObjectId id) {
        return repo.findById(id);
    }

    public void saveAll(List<Person> persons) {
        repo.saveAll(persons);
    }

    public List<Person> findAll(List<ObjectId> ids) {
        return repo.findAllById(ids);
    }

    public List<Person> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace(); // or use a logging framework like SLF4J/Logback
            // Rethrow or handle the exception as needed
            throw e;
        }
    }

    public void delete(ObjectId id) {
        repo.deleteById(id);
    }

    public void update(Person person) {
        repo.save(person);
    }

    public void update(List<Person> people) {
        repo.saveAll(people);
    }

    public long getCount() {
       return repo.getCount().get(0).getCount();
    }

    public double getAverageAge() {
        return repo.getAverageAge().get(0).getAverageAge();
    }

    public int getMaxCars() {
        return repo.getMaxCars().get(0).getMaxCars();
    }

    public List<PeopleInCityResult> getPeopleInCity(String city) {
        return repo.countPeopleInCity(city);
    }

    public List<PeopleInCountryResult>getPeopleInCountry(String country) {
        return repo.countPeopleInCountry(country);
    }

    public List<UninsuredDriversResult> getUninsuredDrivers() {
        return repo.getUninsuredDrivers();
    }

    public List<InsuredDriversResult> getInsuredDrivers() {
        return repo.getInsuredDrivers();
    }

    public List<CarMakeCountResult> getCarMakeCount(String make) {
        return repo.countCarMake(make);
    }
}
