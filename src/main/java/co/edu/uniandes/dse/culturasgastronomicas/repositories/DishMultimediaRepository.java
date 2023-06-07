package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** @author Santiago Diaz */
@Repository public interface DishMultimediaRepository extends JpaRepository<DishMultimediaEntity, Long> {
    Optional<DishMultimediaEntity> findByUrl(String url);
}