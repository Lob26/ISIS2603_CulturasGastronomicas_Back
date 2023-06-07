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
 * @author Diego Rubio
 * @version 1.0.0-alpha
 */
@Getter @Setter @ToString
@Entity(name = "gastronomic_category") @Table(name = "gastronomic_category", uniqueConstraints =
        @UniqueConstraint(name = "GCat_name_culture_ND", columnNames = {"name", "culture_id"})
)
public class GastronomicCategoryEntity extends BaseEntity {
    private String name;
    private String url;

    @PodamExclude @ManyToOne(cascade = CascadeType.REMOVE) @ToString.Exclude
    private GastronomicCultureEntity culture;
    @PodamExclude @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true) @ToString.Exclude
    private List<RepresentativeProductEntity> products = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GastronomicCategoryEntity entity = (GastronomicCategoryEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
