package co.edu.uniandes.dse.culturasgastronomicas.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Santiago Diaz
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "dish_multimedia")
@Table(name = "dish_multimedia", uniqueConstraints =
        @UniqueConstraint(name = "DM_url_ND", columnNames = "url")
)
public class DishMultimediaEntity extends BaseEntity {
    @Column(length = 511) private String url;

    @PodamExclude @ManyToOne @ToString.Exclude
    private RecipeEntity recipe;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DishMultimediaEntity that = (DishMultimediaEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}
