package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** @author Pedro Lobato */
@Getter @Setter
public class CountryDetailDTO extends CountryDTO {
    private List<GastronomicCultureDTO> cultures = new ArrayList<>();
    private List<RestaurantDTO> restaurants = new ArrayList<>();
}
