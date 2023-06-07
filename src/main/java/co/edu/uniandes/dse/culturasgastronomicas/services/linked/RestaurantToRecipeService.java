package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RestaurantRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between Restaurant and Recipe entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(RecipeService.class) @Slf4j @Service
public class RestaurantToRecipeService {
    @Autowired private RecipeRepository repository;
    @Autowired private RestaurantRepository checker;
    @Autowired private RecipeService service;

    /**
     * Retrieves all recipes associated with a given Restaurant.
     *
     * @param restaurantID the ID of the Restaurant to retrieve recipes for.
     * @return a list of RecipeEntity objects associated with the given Restaurant.
     * @throws EntityNotFoundException if the given Restaurant does not exist.
     */
    @Transactional public List<RecipeEntity> findAll(Long restaurantID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.RECIPE, STR.RESTAURANT, restaurantID);
        if (checker.findById(restaurantID).isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        return repository.findAllByRestaurant(restaurantID);
    }

    /**
     * This method finds a recipe by its ID, checking first if it belongs to a specific restaurant.
     *
     * @param restaurantID the ID of the restaurant where the recipe is located
     * @param recipeID     the ID of the recipe to be found
     * @return the RecipeEntity object corresponding to the specified ID
     * @throws EntityNotFoundException if either the restaurant or the recipe cannot be found in the database
     * @throws BusinessLogicException  if the recipe is not associated with the specified restaurant
     */
    @Transactional public RecipeEntity findOne(Long restaurantID,
                                               Long recipeID)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={};{}.id={}]",
                 CRUD.R_O, STR.RECIPE, STR.RESTAURANT, restaurantID, STR.RECIPE, recipeID);
        var restaurant = checker.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        var recipe = repository.findById(recipeID);
        if (recipe.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        if (!restaurant.get().getRecipes().contains(recipe.get()))
            throw new BusinessLogicException(STR.RESTAURANT_RECIPE.det);
        return recipe.get();
    }

    /**
     * Creates a new {@link RecipeEntity} associated with a given {@link RestaurantEntity}.
     *
     * @param restaurantID the ID of the Restaurant to create the RecipeEntity for.
     * @param entity       the RecipeEntity object to create.
     * @return the newly created RecipeEntity object.
     * @throws EntityNotFoundException if the given Restaurant does not exist.
     * @throws BusinessLogicException  if the RecipeEntity cannot be created.
     */
    @Transactional public RecipeEntity create(Long restaurantID,
                                              RecipeEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}]", CRUD.C, CRUD.A, STR.RECIPE, STR.RESTAURANT, restaurantID);
        var restaurant = checker.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        var response = service.create(entity);
        if (!repository.findAllByRestaurant(restaurantID).contains(entity)) restaurant.get().getRecipes().add(response);
        return response;
    }

    /**
     * Deletes a {@link RecipeEntity} associated with a given {@link RestaurantEntity}.
     *
     * @param restaurantID the ID of the Restaurant to delete the RecipeEntity from.
     * @param recipeID     the ID of the RecipeEntity to delete.
     * @throws EntityNotFoundException if the given Restaurant does not exist.
     * @throws BusinessLogicException  if the RecipeEntity cannot be deleted.
     */
    @Transactional public void delete(Long restaurantID,
                                      Long recipeID)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={}; {}.name={}]",
                 CRUD.D, STR.RECIPE, STR.RESTAURANT, restaurantID, STR.RECIPE, recipeID);
        var restaurant = checker.findById(restaurantID);
        if (restaurant.isEmpty()) throw new EntityNotFoundException(STR.RESTAURANT);
        var recipe = repository.findById(recipeID);
        if (recipe.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);
        if (!restaurant.get().getRecipes().remove(recipe.get()))
            throw new BusinessLogicException(STR.RESTAURANT_RECIPE.det);
    }
}
