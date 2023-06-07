package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MichelinStarDetailDTO extends MichelinStarDTO {
    private RestaurantDTO restaurant;
}
