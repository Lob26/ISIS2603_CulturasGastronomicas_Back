package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RecipeDetailDTO extends RecipeDTO {
    private List<RestaurantDTO> restaurants = new ArrayList<>();
    private List<DishMultimediaDTO> urls = new ArrayList<>();
}
