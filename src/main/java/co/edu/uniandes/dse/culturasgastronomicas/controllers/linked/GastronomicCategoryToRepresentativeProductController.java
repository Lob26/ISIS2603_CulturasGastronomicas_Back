package co.edu.uniandes.dse.culturasgastronomicas.controllers.linked;

import co.edu.uniandes.dse.culturasgastronomicas.dto.RepresentativeProductDTO;
import co.edu.uniandes.dse.culturasgastronomicas.dto.RepresentativeProductDetailDTO;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.linked.GastronomicCategoryToProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/categories/{category_id}/products")
public class GastronomicCategoryToRepresentativeProductController {

    @Autowired private GastronomicCategoryToProductService service;

    @Autowired private ModelMapper modelMapper;

    @PostMapping @ResponseStatus(code = HttpStatus.OK)
    public RepresentativeProductDTO addRepresentativeProduct(@PathVariable("category_id") Long categoryId,
                                                             @RequestParam("product_id") Long representativeProductId)
            throws EntityNotFoundException {
        RepresentativeProductEntity representativeProductEntity = service.addProduct(representativeProductId,
                                                                                     categoryId);
        return modelMapper.map(representativeProductEntity, RepresentativeProductDTO.class);
    }

    @GetMapping("/all") @ResponseStatus(code = HttpStatus.OK)
    public List<RepresentativeProductDetailDTO> getRepresentativeProducts(@PathVariable("category_id") Long categoryId)
            throws EntityNotFoundException {
        List<RepresentativeProductEntity> representativeProductList = service.getProducts(categoryId);
        return modelMapper.map(representativeProductList,
                               new TypeToken<List<RepresentativeProductDetailDTO>>() {}.getType());
    }

    @GetMapping @ResponseStatus(code = HttpStatus.OK)
    public RepresentativeProductDetailDTO getRepresentativeProduct(@PathVariable("category_id") Long categoryId,
                                                                   @RequestParam("product_id") Long representativeProductId)
            throws EntityNotFoundException, BusinessLogicException {
        RepresentativeProductEntity representativeProductEntity = service.getProduct(categoryId,
                                                                                     representativeProductId);
        return modelMapper.map(representativeProductEntity, RepresentativeProductDetailDTO.class);
    }
}