package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.CountryDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.GastronomicCultureToCountryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and Country entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@RestController @RequestMapping("/culture/{culture_id}/country")
public class GastronomicCultureToCountryController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCultureToCountryService service;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<CountryDTO> findAll(@PathVariable("culture_id") Long id)
            throws EntityNotFoundException {
        return mapper.map(service.findAll(id), new TypeToken<List<CountryDTO>>() {}.getType());
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CountryDTO create(@PathVariable("culture_id") Long id,
                             @RequestBody CountryDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        return mapper.map(service.create(id, mapper.map(dto, CountryEntity.class)), CountryDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("culture_id") Long id,
                       @RequestParam("country_name") String name)
            throws EntityNotFoundException, BusinessLogicException {
        service.delete(id, name);
    }
}
