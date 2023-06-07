package co.edu.uniandes.dse.culturasgastronomicas.controllers.singular;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RepresentativeProductDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RepresentativeProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/products") public class RepresentativeProductController {
    @Autowired private ModelMapper mapper;
    @Autowired private RepresentativeProductService service000;

    @GetMapping("/all") @ResponseStatus(HttpStatus.OK) public List<RepresentativeProductDTO> findAll() {
        return mapper.map(service000.findAll(), new TypeToken<List<RepresentativeProductDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(HttpStatus.OK) public RepresentativeProductDTO findOne(@RequestParam Long id)
            throws EntityNotFoundException {
        return mapper.map(service000.findOne(id), RepresentativeProductDTO.class);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RepresentativeProductDTO create(@RequestBody RepresentativeProductDTO dto)
            throws BusinessLogicException {
        var product = service000.create(mapper.map(dto, RepresentativeProductEntity.class));
        return mapper.map(product, RepresentativeProductDTO.class);
    }

    @PutMapping @ResponseStatus(HttpStatus.OK)
    public RepresentativeProductDTO update(@RequestParam Long id, @RequestBody RepresentativeProductDTO dto)
            throws EntityNotFoundException, BusinessLogicException {
        var product = service000.update(id, mapper.map(dto, RepresentativeProductEntity.class));
        return mapper.map(product, RepresentativeProductDTO.class);
    }

    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@RequestParam Long id)
            throws EntityNotFoundException {
        service000.delete(id);
    }
}