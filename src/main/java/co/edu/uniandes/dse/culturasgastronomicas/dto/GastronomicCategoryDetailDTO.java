package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** @author Diego Rubio */
@Getter @Setter public class GastronomicCategoryDetailDTO extends GastronomicCategoryDTO {
    private GastronomicCultureDTO culture;
    private List<RepresentativeProductDTO> products = new ArrayList<>();
}
