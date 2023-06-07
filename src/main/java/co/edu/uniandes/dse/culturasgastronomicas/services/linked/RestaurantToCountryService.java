package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.CountryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j @Service public class RestaurantToCountryService {
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private CountryRepository countryRepository;

    @Transactional public void removeGastronomicCulture(Long recipeId)
            throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la GastronomicCulture del recipe con id = {}", recipeId);
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(recipeId);
        if (restaurantEntity.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);

        Optional<CountryEntity> countryEntity = countryRepository.findById(restaurantEntity.get().getCountry().getId());
        countryEntity.ifPresent(countryCulture -> countryCulture.getRestaurants().remove(restaurantEntity.get()));

        restaurantEntity.get().setCountry(null);
        log.info("Termina proceso de borrar la gastronomicCulture de la recipe con id = {}", recipeId);
    }
}