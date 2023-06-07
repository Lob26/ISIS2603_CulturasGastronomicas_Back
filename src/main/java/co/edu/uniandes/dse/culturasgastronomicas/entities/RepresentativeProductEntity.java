package co.edu.uniandes.dse.culturasgastronomicas.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

/**
 * @author Diego Rubio
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "representative_product")
@Table(name = "representative_product", uniqueConstraints =
        @UniqueConstraint(name = "RP_ND", columnNames = {"name", "brand"})
)
public class RepresentativeProductEntity extends BaseEntity {
    private String name;
    private String brand;

    @PodamExclude @ManyToOne @ToString.Exclude
    private GastronomicCategoryEntity category;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RepresentativeProductEntity that = (RepresentativeProductEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }
}