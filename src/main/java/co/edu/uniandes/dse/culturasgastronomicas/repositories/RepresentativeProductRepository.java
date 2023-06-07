package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface RepresentativeProductRepository extends JpaRepository<RepresentativeProductEntity, Long> {
    @Query("select r from representative_product r where r.category.culture.id = ?1")
    List<RepresentativeProductEntity> findAllByCulture(Long cultureID);
}