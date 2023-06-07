package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** @author Pedro Lobato */
@Repository public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findByName(String name);
    @Query("select c from country c join c.cultures gc where gc.id = ?1")
    List<CountryEntity> findAllByCulture(Long cultureID);
}