package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository public interface GastronomicCategoryRepository extends JpaRepository<GastronomicCategoryEntity, Long> {
    Optional<GastronomicCategoryEntity> findByName(String name);
    @Query("select distinct g from gastronomic_category g where g.name = ?2 and g.culture.id = ?1")
    Optional<GastronomicCategoryEntity> findByNameAndCulture(Long culture, String name);
    @Query("select g from gastronomic_category g where g.culture.id = ?1")
    List<GastronomicCategoryEntity> findAllByCulture(Long culture);
}