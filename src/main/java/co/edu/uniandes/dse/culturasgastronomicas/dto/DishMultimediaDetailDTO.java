package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DishMultimediaDetailDTO extends DishMultimediaDTO {
    private RecipeDTO recipe;
}
