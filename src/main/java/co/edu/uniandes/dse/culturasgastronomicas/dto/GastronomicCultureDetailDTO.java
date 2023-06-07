package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** @author Pedro Lobato */
@Getter @Setter
public class GastronomicCultureDetailDTO extends GastronomicCultureDTO {
    private List<RecipeDTO> recipes = new ArrayList<>();
    private List<GastronomicCategoryDTO> categories = new ArrayList<>();
    private List<CountryDTO> countries = new ArrayList<>();
}
