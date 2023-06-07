package co.edu.uniandes.dse.culturasgastronomicas.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author Luis Borbon
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "michelin_star")
@Table(name = "michelin_star")
public class MichelinStarEntity extends BaseEntity {
    @Temporal(TemporalType.DATE)
    private Date acquired;

    @PodamExclude @ManyToOne @ToString.Exclude
    private RestaurantEntity restaurant;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MichelinStarEntity that = (MichelinStarEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}