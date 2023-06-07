package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RecipeDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.GastronomicCultureToRecipeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and Recipe entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/culture/{culture_id}/recipe")
public class GastronomicCultureToRecipeController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCultureToRecipeService service;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<RecipeDTO> findAll(@PathVariable("culture_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<RecipeDTO>>() {}.getType());
    }

    @PutMapping @ResponseStatus(HttpStatus.OK)
    public RecipeDTO create(@PathVariable("culture_id") Long id,
                            @RequestBody RecipeDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, mapper.map(dto, RecipeEntity.class)), RecipeDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("culture_id") Long id,
                       @RequestParam("recipe_id") Long recipeId)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(id, recipeId);
    }
}

