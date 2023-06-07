package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCategoryDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCategoryDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.GastronomicCultureToCategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and GastronomicCategory entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/culture/{culture_id}/category")
public class GastronomicCultureToCategoryController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCultureToCategoryService service;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<GastronomicCategoryDetailDTO> findAll(@PathVariable("culture_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<GastronomicCategoryDetailDTO>>() {}.getType());
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public GastronomicCategoryDTO create(@PathVariable("culture_id") Long id,
                                         @RequestBody GastronomicCategoryDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, mapper.map(dto, GastronomicCategoryEntity.class)),
                          GastronomicCategoryDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("culture_id") Long id,
                       @RequestParam("category_name") String name)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(id, name);
    }
}
