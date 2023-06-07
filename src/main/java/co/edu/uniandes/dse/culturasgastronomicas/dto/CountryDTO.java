package co.edu.uniandes.dse.culturasgastronomicas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/** @author Pedro Lobato */
@Getter @Setter
public class CountryDTO {
    private Long id;
    private String name;
    private Locale iso;
}
