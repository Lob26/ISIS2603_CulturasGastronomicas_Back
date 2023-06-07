package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Mejor no especificar
 * @version 1.0.0
 */
@Slf4j @Service public class RestaurantService {
    @Autowired RestaurantRepository repository;

    /** Create Restaurant into DB */
    @Transactional public RestaurantEntity create(RestaurantEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.RESTAURANT);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCountry() == null) throw new BusinessLogicException(STR.COUNTRY.nil);
        return entity.getId() != null
                ? repository.findById(entity.getId()).orElseGet(() -> repository.save(entity))
                : repository.save(entity);
    }

    /** Get Restaurant from DB (Unused controller) */
    @Transactional public List<RestaurantEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.RESTAURANT);
        return repository.findAll();
    }

    /** Get Restaurant from DB by ID */
    @Transactional public RestaurantEntity findOne(Long id)
            throws EntityNotFoundException {
        log.info("{}: {} [id={}]", CRUD.R_O, STR.RESTAURANT, id);
        Optional<RestaurantEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update Restaurant's instance's data (Unused controller) */
    @Transactional public RestaurantEntity update(Long id,
                                                  RestaurantEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.RESTAURANT, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCountry() == null) throw new BusinessLogicException(STR.COUNTRY.nil);
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete Restaurant's instance (Unused controller) */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.RESTAURANT, id);
        Optional<RestaurantEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);

        RestaurantEntity entity = byId.get();
        if (!entity.getStars().isEmpty())
            throw new BusinessLogicException(STR.RESTAURANT + " has associations with michelin stars");
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}
