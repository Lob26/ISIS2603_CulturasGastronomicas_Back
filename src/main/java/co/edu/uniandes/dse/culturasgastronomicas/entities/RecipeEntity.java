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
 * @author Santiago Diaz
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "recipe") @Table(name = "recipe", uniqueConstraints =
        @UniqueConstraint(name = "Rcp_name_culture_ND", columnNames = {"name", "culture_id"})
)
public class RecipeEntity extends BaseEntity {
    private String name;
    @Column(length = 511) private String description;
    @Lob private String instructions;

    @PodamExclude @ManyToOne @ToString.Exclude
    private GastronomicCultureEntity culture;
    @PodamExclude @ManyToMany @ToString.Exclude
    private List<RestaurantEntity> restaurants = new ArrayList<>();
    @PodamExclude @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<DishMultimediaEntity> urls = new ArrayList<>();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeEntity that = (RecipeEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}