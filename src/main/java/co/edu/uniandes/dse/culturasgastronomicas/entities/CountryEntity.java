package co.edu.uniandes.dse.culturasgastronomicas.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Pedro Lobato
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "country") @Table(name = "country", uniqueConstraints = {
        @UniqueConstraint(name = "C_name_ND", columnNames = "name"),
        @UniqueConstraint(name = "C_iso_ND", columnNames = "iso")
})
public class CountryEntity extends BaseEntity {
    private String name;
    private Locale iso;

    @PodamExclude @ManyToMany(mappedBy = "countries") @ToString.Exclude
    private List<GastronomicCultureEntity> cultures = new ArrayList<>();
    @PodamExclude @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<RestaurantEntity> restaurants = new ArrayList<>();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CountryEntity that = (CountryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}