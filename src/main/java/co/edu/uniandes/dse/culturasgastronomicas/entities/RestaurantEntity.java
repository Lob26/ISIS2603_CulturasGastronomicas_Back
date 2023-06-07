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
 * @author Luis Borbon
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "restaurant") @Table(name = "restaurant", uniqueConstraints =
        @UniqueConstraint(name = "Rst_ND", columnNames = {"name", "city", "contact", "country_id"})
)
public class RestaurantEntity extends BaseEntity {
    private String name;
    private String city;
    private String contact;

    @PodamExclude @ManyToOne
    private CountryEntity country;
    @PodamExclude @ManyToMany(mappedBy = "restaurants") @ToString.Exclude
    private List<RecipeEntity> recipes = new ArrayList<>();
    @PodamExclude @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<MichelinStarEntity> stars = new ArrayList<>();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}
