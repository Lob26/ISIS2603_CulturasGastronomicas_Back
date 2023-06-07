package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.CountryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles CRUD operations for {@link CountryEntity} objects in the repository.
 * <p/>
 * Uses {@link CountryRepository} to interact with the database.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Slf4j @Service public class CountryService {
    @Autowired CountryRepository repository;

    /**
     * Creates a new {@link CountryEntity} in the repository. If a {@link CountryEntity} with the same name already exists,
     * that entity is returned instead.
     *
     * @param entity the {@link CountryEntity} to create
     * @return the created {@link CountryEntity}, or an existing one with the same name
     * @throws BusinessLogicException if the name of the country entity is blank or null
     * @throws RuntimeException       if an error occurs while interacting with the repository
     */
    @Transactional public CountryEntity create(CountryEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.COUNTRY);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        return repository.findByName(entity.getName()).orElseGet(() -> repository.save(entity));
    }

    /**
     * Retrieves a list of all {@link CountryEntity} objects from the repository.
     *
     * @return a list of all {@link CountryEntity} objects
     * @throws RuntimeException if an error occurs while interacting with the repository
     */
    @Transactional public List<CountryEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.COUNTRY);
        return repository.findAll();
    }

    /**
     * Retrieves a single {@link CountryEntity} object from the repository based on its name.
     *
     * @param name the name of the country to retrieve
     * @return the {@link CountryEntity} object with the specified name
     * @throws EntityNotFoundException if no {@link CountryEntity} object with the specified name is found
     * @throws RuntimeException        if an error occurs while interacting with the repository
     */
    @Transactional public CountryEntity findOne(String name)
            throws EntityNotFoundException {
        log.info("{}: {} [name={}]", CRUD.R_O, STR.COUNTRY, name);
        Optional<CountryEntity> byId = repository.findByName(name);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /**
     * Updates an existing {@link CountryEntity} object in the repository with the specified ID.
     *
     * @param id     the ID of the {@link CountryEntity} object to update
     * @param entity the updated {@link CountryEntity} object to save
     * @return the updated {@link CountryEntity} object
     * @throws EntityNotFoundException if no {@link CountryEntity} object with the specified ID is found
     * @throws BusinessLogicException  if the updated {@link CountryEntity} object has a blank name
     * @throws RuntimeException        if there is an error while interacting with the repository
     */
    @Transactional public CountryEntity update(Long id,
                                               CountryEntity entity)
            throws EntityNotFoundException,
            BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.COUNTRY, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /**
     * Deletes a {@link CountryEntity} with the given ID.
     *
     * @param id the ID of the {@link CountryEntity} to be deleted
     * @throws EntityNotFoundException if no {@link CountryEntity} with the given ID is found
     * @throws BusinessLogicException  if the {@link CountryEntity} has associations with restaurants
     * @throws TransactionException    if an error occurs during the transaction
     */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.COUNTRY, id);
        Optional<CountryEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);

        CountryEntity entity = byId.get();
        if (!entity.getRestaurants().isEmpty())
            throw new BusinessLogicException(STR.COUNTRY + " has associations with restaurants");
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}
