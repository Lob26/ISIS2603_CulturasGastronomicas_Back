package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Diego Rubio
 * @version 1.0.0
 */
@Slf4j @Service public class GastronomicCategoryService {
    @Autowired GastronomicCategoryRepository repository;

    /** Create category into DB */
    @Transactional public GastronomicCategoryEntity create(GastronomicCategoryEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.CATEGORY);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCulture() == null) throw new BusinessLogicException("Culture is null");
        return repository.findByName(entity.getName()).orElseGet(() -> repository.save(entity));
    }

    /** Get Category from DB */
    @Transactional public List<GastronomicCategoryEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.CATEGORY);
        return repository.findAll();
    }

    /** Get Category from DB by ID */
    @Transactional public GastronomicCategoryEntity findOne(String name)
            throws EntityNotFoundException {
        log.info("{}: {} [name={}]", CRUD.R_O, STR.CATEGORY, name);
        Optional<GastronomicCategoryEntity> byName = repository.findByName(name);
        if (byName.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);
        log.info("end::{}", CRUD.R_O);
        return byName.get();
    }

    @Transactional public GastronomicCategoryEntity findOne(Long id)
            throws EntityNotFoundException {
        log.info("{}: {} [id={}]", CRUD.R_O, STR.CATEGORY, id);
        Optional<GastronomicCategoryEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update Category's instance's data */
    @Transactional public GastronomicCategoryEntity update(Long id,
                                                           GastronomicCategoryEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.CATEGORY, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCulture() == null) throw new BusinessLogicException("Culture is null");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete Category's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.CATEGORY, id);
        Optional<GastronomicCategoryEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);

        GastronomicCategoryEntity entity = byId.get();
        if (!entity.getProducts().isEmpty())
            throw new BusinessLogicException(STR.CATEGORY + " has associations with products");
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}
