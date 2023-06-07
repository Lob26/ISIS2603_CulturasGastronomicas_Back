package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Slf4j @Service public class GastronomicCultureService {
    @Autowired GastronomicCultureRepository repository;

    /** Create culture into DB */
    @Transactional public GastronomicCultureEntity create(GastronomicCultureEntity entity)
            throws
            BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.CULTURE);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        return repository.findByName(entity.getName()).orElseGet(() -> repository.save(entity));
    }

    /** Get culture from DB */
    @Transactional public List<GastronomicCultureEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.CULTURE);
        return repository.findAll();
    }

    /** Get culture from DB by ID */
    @Transactional public GastronomicCultureEntity findOne(Long id)
            throws EntityNotFoundException {
        log.info("{}: {} [id={}]", CRUD.R_O, STR.CULTURE, id);
        Optional<GastronomicCultureEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update culture's instance's data */
    @Transactional public GastronomicCultureEntity update(Long id,
                                                          GastronomicCultureEntity entity)
            throws
            EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.CULTURE, id);
        Optional<GastronomicCultureEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete culture's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.CULTURE, id);
        Optional<GastronomicCultureEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);

        GastronomicCultureEntity entity = byId.get();
        List<String> associations = new ArrayList<>();
        if (!entity.getCountries().isEmpty()) associations.add("countries");
        if (!entity.getCategories().isEmpty()) associations.add("categories");
        if (!entity.getRecipes().isEmpty()) associations.add("recipes");

        if (!associations.isEmpty())
            throw new BusinessLogicException(STR.CULTURE + " has associations with " + String.join(", ", associations));

        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}
