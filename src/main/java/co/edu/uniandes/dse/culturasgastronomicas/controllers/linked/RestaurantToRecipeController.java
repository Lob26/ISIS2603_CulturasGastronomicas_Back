package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RecipeDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.RestaurantToRecipeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between Restaurant and Recipe entities.
 *
 * @author Luis Borbon
 * @version 1.0.0
 */
@RestController @RequestMapping("/restaurant/{restaurant_id}/recipe")
public class RestaurantToRecipeController {
    @Autowired private ModelMapper mapper;
    @Autowired private RestaurantToRecipeService service;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<RecipeDTO> findAll(@PathVariable("restaurant_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<RecipeDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(HttpStatus.OK)
    public RecipeDTO findOne(@PathVariable("restaurant_id") Long restaurantID,
                             @RequestParam("recipe_id") Long recipeID)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.findOne(restaurantID, recipeID), RecipeDTO.class);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO create(@PathVariable("restaurant_id") Long id,
                            @RequestBody RecipeDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, mapper.map(dto, RecipeEntity.class)), RecipeDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurant_id") Long restaurantID,
                       @RequestParam("recipe_id") Long recipeID)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(restaurantID, recipeID);
    }
}
