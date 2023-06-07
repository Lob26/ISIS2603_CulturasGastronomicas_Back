package co.edu.uniandes.dse.culturasgastronomicas.controllers.singular;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RecipeDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.RecipeDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RecipeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/recipe")
public class RecipeController {
    @Autowired private RecipeService service;
    @Autowired private ModelMapper mapper;

    @GetMapping("/all") @ResponseStatus(code = HttpStatus.OK)
    public List<RecipeDetailDTO> findAll() {
        return mapper.map(service.findAll(), new TypeToken<List<RecipeDetailDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(code = HttpStatus.OK)
    public RecipeDetailDTO findOne(@RequestParam Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findOne(id), RecipeDetailDTO.class);
    }

    @PostMapping @ResponseStatus(code = HttpStatus.CREATED)
    public RecipeDTO create(@RequestBody RecipeDTO dto)
            throws BusinessLogicException {
        RecipeEntity entity = service.create(mapper.map(dto, RecipeEntity.class));
        return mapper.map(entity, RecipeDTO.class);
    }

    @PutMapping @ResponseStatus(code = HttpStatus.OK)
    public RecipeDTO update(@RequestParam Long id,
                            @RequestBody RecipeDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        RecipeEntity entity = service.update(id, mapper.map(dto, RecipeEntity.class));
        return mapper.map(entity, RecipeDTO.class);
    }

    @DeleteMapping @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(id);
    }
}
