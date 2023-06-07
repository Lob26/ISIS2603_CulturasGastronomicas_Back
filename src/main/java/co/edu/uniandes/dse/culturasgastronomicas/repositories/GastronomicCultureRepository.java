package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** @author Pedro Lobato */
@Repository public interface GastronomicCultureRepository extends JpaRepository<GastronomicCultureEntity, Long> {
    Optional<GastronomicCultureEntity> findByName(String string);
}