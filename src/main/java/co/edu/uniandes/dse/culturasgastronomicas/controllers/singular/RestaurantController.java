package co.edu.uniandes.dse.culturasgastronomicas.controllers.singular;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RestaurantDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.RestaurantDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RestaurantService;
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
@RestController @RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired private RestaurantService service;
    @Autowired private ModelMapper mapper;

    @GetMapping("/all") @ResponseStatus(code = HttpStatus.OK)
    public List<RestaurantDetailDTO> findAll() {
        List<RestaurantEntity> cultures = service.findAll();
        return mapper.map(cultures, new TypeToken<List<RestaurantDetailDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(code = HttpStatus.OK)
    public RestaurantDetailDTO findOne(@RequestParam Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findOne(id), RestaurantDetailDTO.class);
    }

    @PostMapping @ResponseStatus(code = HttpStatus.CREATED)
    public RestaurantDTO create(@RequestBody RestaurantDTO dto)
            throws BusinessLogicException {
        RestaurantEntity entity = service.create(mapper.map(dto, RestaurantEntity.class));
        return mapper.map(entity, RestaurantDTO.class);
    }

    @PutMapping @ResponseStatus(code = HttpStatus.OK)
    public RestaurantDTO update(@RequestParam Long id,
                                @RequestBody RestaurantDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        RestaurantEntity entity = service.update(id, mapper.map(dto, RestaurantEntity.class));
        return mapper.map(entity, RestaurantDTO.class);
    }

    @DeleteMapping @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(id);
    }
}
