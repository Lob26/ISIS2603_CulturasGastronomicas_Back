package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RestaurantDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.RecipeToRestaurantService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between Recipe and Restaurant entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/recipe/{recipe_id}/restaurant")
public class RecipeToRestaurantController {
    @Autowired private ModelMapper mapper;
    @Autowired private RecipeToRestaurantService service;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDTO> findAll(@PathVariable("recipe_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<RestaurantDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO findOne(@PathVariable("recipe_id") Long recipeID,
                                 @RequestParam("restaurant_id") Long restaurantID)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.findOne(recipeID, restaurantID), RestaurantDTO.class);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDTO create(@PathVariable("recipe_id") Long id,
                                @RequestBody RestaurantDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, mapper.map(dto, RestaurantEntity.class)), RestaurantDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("recipe_id") Long recipeID,
                       @RequestParam("restaurant_id") Long restaurantID)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(recipeID, restaurantID);
    }
}
