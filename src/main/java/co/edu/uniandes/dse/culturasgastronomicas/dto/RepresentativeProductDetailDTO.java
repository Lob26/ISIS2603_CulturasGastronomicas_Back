package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

/** @author Diego Rubio */
@Getter @Setter public class RepresentativeProductDetailDTO extends RepresentativeProductDTO {
    private GastronomicCategoryDTO category;
}