package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeDTO {
    private Long id;
	private String name;
	private String description;
	private String instructions;
	private GastronomicCultureDTO culture;
}
