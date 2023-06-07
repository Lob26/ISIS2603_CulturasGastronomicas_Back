package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RepresentativeProductDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.GastronomicCultureToCategoryToProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture -> GastronomicCategory and RepresentativeProduct entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/culture/{culture_id}/category")
public class GastronomicCultureToCategoryToProductController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCultureToCategoryToProductService service;

    @GetMapping("/products") @ResponseStatus(HttpStatus.OK)
    public List<RepresentativeProductDTO> findAll(@PathVariable("culture_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<RepresentativeProductDTO>>() {}.getType());
    }

    @PutMapping @ResponseStatus(HttpStatus.CREATED)
    public RepresentativeProductDTO create(@PathVariable("culture_id") Long id,
                                           @RequestParam("category_name") String name,
                                           @RequestBody RepresentativeProductDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, name, mapper.map(dto, RepresentativeProductEntity.class)),
                          RepresentativeProductDTO.class);
    }
}
