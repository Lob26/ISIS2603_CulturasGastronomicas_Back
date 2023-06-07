package co.edu.uniandes.dse.culturasgastronomicas.controllers.singular;

import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCultureDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCultureDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.GastronomicCultureService;
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
@RestController @RequestMapping("/culture")
public class GastronomicCultureController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCultureService service000;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<GastronomicCultureDetailDTO> findAll() {
        return mapper.map(service000.findAll(), new TypeToken<List<GastronomicCultureDetailDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(HttpStatus.OK)
    public GastronomicCultureDetailDTO findOne(@RequestParam Long id)
            throws EntityNotFoundException {
        return mapper.map(service000.findOne(id), GastronomicCultureDetailDTO.class);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public GastronomicCultureDetailDTO create(@RequestBody GastronomicCultureDTO dto)
            throws BusinessLogicException {
        var culture = service000.create(mapper.map(dto, GastronomicCultureEntity.class));
        return mapper.map(culture, GastronomicCultureDetailDTO.class);
    }

    @PutMapping @ResponseStatus(HttpStatus.OK)
    public GastronomicCultureDetailDTO update(@RequestParam Long id,
                                              @RequestBody GastronomicCultureDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        var culture = service000.update(id, mapper.map(dto, GastronomicCultureEntity.class));
        return mapper.map(culture, GastronomicCultureDetailDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id)
            throws EntityNotFoundException, BusinessLogicException {
        service000.delete(id);
    }
}
