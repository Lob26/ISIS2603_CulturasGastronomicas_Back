package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and Recipe entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(RecipeService.class) @Slf4j @Service
public class GastronomicCultureToRecipeService {
    @Autowired private RecipeRepository repository;
    @Autowired private GastronomicCultureRepository checker;
    @Autowired private RecipeService service;

    /**
     * Retrieves all recipes associated with a given GastronomicCulture.
     *
     * @param cultureID the ID of the GastronomicCulture to retrieve recipes for.
     * @return a list of RecipeEntity objects associated with the given GastronomicCulture.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     */
    @Transactional public List<RecipeEntity> findAll(Long cultureID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.RECIPE, STR.CULTURE, cultureID);
        if (checker.findById(cultureID).isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        return repository.findAllByCulture(cultureID);
    }

    /**
     * Creates a new {@link RecipeEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID the ID of the GastronomicCulture to create the RecipeEntity for.
     * @param entity    the RecipeEntity object to create.
     * @return the newly created RecipeEntity object.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the RecipeEntity cannot be created.
     */
    @Transactional public RecipeEntity create(Long cultureID,
                                              RecipeEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}]", CRUD.C, CRUD.A, STR.RECIPE, STR.CULTURE, cultureID);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        var response = service.create(entity);
        if (culture.get().getRecipes().contains(response)) culture.get().getRecipes().add(response);
        return response;
    }

    /**
     * Deletes a {@link RecipeEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID the ID of the GastronomicCulture to delete the RecipeEntity from.
     * @param recipeId  the ID of the RecipeEntity to delete.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the RecipeEntity cannot be deleted.
     */
    @Transactional public void delete(Long cultureID,
                                      Long recipeId)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={}]", CRUD.D, STR.RECIPE, STR.CULTURE, cultureID);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        var recipe = service.findOne(recipeId);
        if (!culture.get().getRecipes().remove(recipe)) throw new BusinessLogicException(STR.CULTURE_RECIPE.det);
        service.delete(recipeId);
    }
}