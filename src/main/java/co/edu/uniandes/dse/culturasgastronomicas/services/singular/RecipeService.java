package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Santiago Díaz
 * @version 1.0.0
 */
@Slf4j @Service public class RecipeService {
    @Autowired RecipeRepository repository;

    /** Create Recipe into DB */
    @Transactional public RecipeEntity create(RecipeEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.RECIPE);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getInstructions() == null || entity.getInstructions().isBlank())
            throw new BusinessLogicException("Blank steps");
        if (entity.getCulture() == null) throw new BusinessLogicException(STR.CULTURE.nil);
        return entity.getId() != null
                ? repository.findById(entity.getId()).orElseGet(() -> repository.save(entity))
                : repository.save(entity);
    }

    /** Get Recipe from DB */
    @Transactional public List<RecipeEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.RECIPE);
        return repository.findAll();
    }

    /** Get Recipe from DB by ID */
    @Transactional public RecipeEntity findOne(Long id)
            throws EntityNotFoundException {
        log.info("{}: {} [id={}]", CRUD.R_O, STR.RECIPE, id);
        Optional<RecipeEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update Recipe's instance's data */
    @Transactional public RecipeEntity update(Long id,
                                              RecipeEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.RECIPE, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getInstructions() == null || entity.getInstructions().isBlank())
            throw new BusinessLogicException("Blank steps");
        if (entity.getCulture() == null) throw new BusinessLogicException(STR.RECIPE.nil);
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete Recipe's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.RECIPE, id);
        Optional<RecipeEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);

        RecipeEntity entity = byId.get();
        if (!entity.getUrls().isEmpty())
            throw new BusinessLogicException(STR.RECIPE + " has associations with multimedia");
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}