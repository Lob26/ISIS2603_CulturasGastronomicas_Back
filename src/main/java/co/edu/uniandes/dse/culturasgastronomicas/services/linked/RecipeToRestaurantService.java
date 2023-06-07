package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between Recipe and Restaurant entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(RestaurantService.class) @Slf4j @Service
public class RecipeToRestaurantService {
    @Autowired private RestaurantRepository repository;
    @Autowired private RecipeRepository checker;
    @Autowired private RestaurantService service;

    /**
     * Retrieves all restaurants associated with a given Recipe.
     *
     * @param recipeID the ID of the Recipe to retrieve countries for.
     * @return a list of RestaurantEntity objects associated with the given Recipe.
     * @throws EntityNotFoundException if the given Recipe does not exist.
     */
    @Transactional public List<RestaurantEntity> findAll(Long recipeID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.RESTAURANT, STR.RECIPE, recipeID);
        if (checker.findById(recipeID).isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        return repository.findAllByRecipe(recipeID);
    }

    /**
     * This method finds a restaurant by its ID, checking first if it contains a specific recipe.
     *
     * @param recipeID     the ID of the recipe that belongs to the restaurant
     * @param restaurantID the ID of the restaurant to be found
     * @return the RestaurantEntity object corresponding to the specified ID
     * @throws EntityNotFoundException if either the restaurant or the recipe cannot be found in the database
     * @throws BusinessLogicException  if the recipe is not associated with the specified restaurant
     */
    @Transactional
    public RestaurantEntity findOne(Long recipeID,
                                    Long restaurantID)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={};{}.id={}]",
                 CRUD.R_O, STR.RESTAURANT, STR.RECIPE, recipeID, STR.RESTAURANT, restaurantID);
        var recipe = checker.findById(recipeID);
        if (recipe.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        var restaurant = repository.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        if (!restaurant.get().getRecipes().contains(recipe.get()))
            throw new BusinessLogicException(STR.RESTAURANT_RECIPE.det);
        return restaurant.get();
    }

    /**
     * Creates a new {@link RestaurantEntity} associated with a given {@link RecipeEntity}.
     *
     * @param recipeID the ID of the Recipe to create the RestaurantEntity for.
     * @param entity   the RestaurantEntity object to create.
     * @return the newly created RestaurantEntity object.
     * @throws EntityNotFoundException if the given Recipe does not exist.
     * @throws BusinessLogicException  if the RestaurantEntity cannot be created.
     */
    @Transactional public RestaurantEntity create(Long recipeID,
                                   RestaurantEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}]", CRUD.C, CRUD.A, STR.RESTAURANT, STR.RECIPE, recipeID);
        var recipe = checker.findById(recipeID);
        if (recipe.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        var response = service.create(entity);
        if (!repository.findAllByRecipe(recipeID).contains(entity))
            recipe.get().getRestaurants().add(response);
        return response;
    }

    /**
     * Deletes a {@link RestaurantEntity} associated with a given {@link RecipeEntity}.
     *
     * @param recipeID     the ID of the Recipe to delete the RestaurantEntity from.
     * @param restaurantID the name of the RestaurantEntity to delete.
     * @throws EntityNotFoundException if the given Recipe does not exist.
     * @throws BusinessLogicException  if the RestaurantEntity cannot be deleted.
     */
    @Transactional public void delete(Long recipeID,
                       Long restaurantID)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={}; {}.name={}]",
                 CRUD.D, STR.RESTAURANT, STR.RECIPE, recipeID, STR.RESTAURANT, restaurantID);
        var recipe = checker.findById(recipeID);
        if (recipe.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        var restaurant = repository.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        if (!recipe.get().getRestaurants().remove(restaurant.get()))
            throw new BusinessLogicException(STR.RECIPE_RESTAURANT.det);
    }
}
