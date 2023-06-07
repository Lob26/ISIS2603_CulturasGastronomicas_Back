package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RestaurantDetailDTO extends RestaurantDTO {
    private CountryDTO country;
    private List<RecipeDTO> recipes = new ArrayList<>();
    private List<MichelinStarDTO> stars = new ArrayList<>();
}
