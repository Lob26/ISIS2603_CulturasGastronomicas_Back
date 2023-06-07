package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestaurantDTO {
    private Long id;
    private String name;
    private String city;
    private String contact;
}
