package co.edu.uniandes.dse.culturasgastronomicas.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pedro Lobato
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "gastronomic_culture")
@Table(name = "gastronomic_culture", uniqueConstraints =
        @UniqueConstraint(name = "GC_name_ND", columnNames = "name")
)
public class GastronomicCultureEntity extends BaseEntity {
    private String name;
    private String url;
    @Column(length = 511) private String description;

    @PodamExclude @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<RecipeEntity> recipes = new ArrayList<>();
    @PodamExclude @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<GastronomicCategoryEntity> categories = new ArrayList<>();
    @PodamExclude @ManyToMany @ToString.Exclude
    private List<CountryEntity> countries = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GastronomicCultureEntity that = (GastronomicCultureEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}