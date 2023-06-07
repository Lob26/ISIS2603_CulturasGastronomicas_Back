package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.MichelinStarRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad MichelinStar y Stare
 *
 * @author ISIS2603
 */
@Slf4j @Service

public class RestaurantToMichelinStarService {
    private static final String UNRELATED = "The restaurant and the star are not associated";

    @Autowired private RestaurantRepository restaurantRep;
    @Autowired private MichelinStarRepository starRep;

    /** Associate MichelinStar's instance to a certain Restaurant into DB */
    @Transactional public MichelinStarEntity addStar(Long restaurantID, Long starID) throws EntityNotFoundException {
        log.info("{}: {}", CRUD.A, STR.M_STAR);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        Optional<MichelinStarEntity> star = starRep.findById(starID);
        if (star.isEmpty()) throw new EntityNotFoundException(STR.M_STAR);
        restaurant.get().getStars().add(star.get());
        return star.get();
    }


    /** Get MichelinStar's instance associated to a certain Restaurant */
    @Transactional public MichelinStarEntity getStar(Long restaurantID, Long starID) throws
            EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [id={}; id={}]", CRUD.R_O, STR.M_STAR, restaurantID, starID);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);
        Optional<MichelinStarEntity> star = starRep.findById(starID);

        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        if (star.isEmpty()) throw new EntityNotFoundException(STR.M_STAR);

        if (!restaurant.get().getStars().contains(star.get())) throw new BusinessLogicException(UNRELATED);
        return star.get();
    }

    /** Get MichelinStar's instance associated to a certain Restaurant */
    @Transactional public List<MichelinStarEntity> getStars(Long restaurantID) throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.M_STAR, STR.RESTAURANT, restaurantID);
        Optional<RestaurantEntity> restaurant = restaurantRep.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        return restaurant.get().getStars();
    }
}