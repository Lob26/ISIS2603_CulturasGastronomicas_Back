package co.edu.uniandes.dse.culturasgastronomicas.controllers.singular;

import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCategoryDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.GastronomicCategoryDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.GastronomicCategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/categories")
public class GastronomicCategoryController {
    @Autowired private ModelMapper mapper;
    @Autowired private GastronomicCategoryService service000;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK)
    public List<GastronomicCategoryDetailDTO> findAll() {
        return mapper.map(service000.findAll(), new TypeToken<List<GastronomicCategoryDetailDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(HttpStatus.OK)
    public GastronomicCategoryDetailDTO findOne(@RequestParam Long id)
            throws EntityNotFoundException {
        return mapper.map(service000.findOne(id), GastronomicCategoryDetailDTO.class);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public GastronomicCategoryDetailDTO create(@RequestBody GastronomicCategoryDTO dto)
            throws BusinessLogicException {
        var category = service000.create(mapper.map(dto, GastronomicCategoryEntity.class));
        return mapper.map(category, GastronomicCategoryDetailDTO.class);
    }

    @PutMapping @ResponseStatus(HttpStatus.OK)
    public GastronomicCategoryDetailDTO update(@RequestParam Long id,
                                               @RequestBody GastronomicCategoryDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        var category = service000.update(id, mapper.map(dto, GastronomicCategoryEntity.class));
        return mapper.map(category, GastronomicCategoryDetailDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id)
            throws EntityNotFoundException, BusinessLogicException {
        service000.delete(id);
    }
}
