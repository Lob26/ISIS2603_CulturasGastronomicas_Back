package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.CountryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** @author Pedro Lobato */
@Slf4j @Service public class CountryToRestaurantService {
    @Autowired private CountryRepository countryRep;
    @Autowired private RestaurantRepository restaurantRep;

    /** Associate Restaurant's instance to a certain Country into DB */
    @Transactional public RestaurantEntity addRestaurant(Long countryID, Long restaurantID) throws
            EntityNotFoundException {
        log.info("{}: {}", CRUD.A, STR.COUNTRY_RESTAURANT);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        country.get().getRestaurants().add(restaurant.get());
        return restaurant.get();
    }

    /** Get Restaurant's instance associated to a certain Country */
    @Transactional public List<RestaurantEntity> getRestaurants(Long countryID) throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.COUNTRY_RESTAURANT, STR.COUNTRY, countryID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        if (country.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        return country.get().getRestaurants();
    }

    /** Get Restaurant's instance associated to a certain Country */
    @Transactional public RestaurantEntity getRestaurant(Long countryID, Long restaurantID) throws
            EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [id={}; id={}]", CRUD.R_O, STR.COUNTRY_RESTAURANT, countryID, restaurantID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);

        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);

        if (!country.get().getRestaurants().contains(restaurant.get()))
            throw new BusinessLogicException(STR.COUNTRY_RESTAURANT.det);
        return restaurant.get();
    }

    /** Disassociate a Restaurant's instance from an association to a certain Country */
    @Transactional public void removeRestaurant(Long countryID, Long restaurantID) throws EntityNotFoundException {
        log.info("{}:{} [id={}; id={}]", CRUD.D, STR.COUNTRY_RESTAURANT, countryID, restaurantID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);

        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);

        country.get().getRestaurants().remove(restaurant.get());
    }
}
