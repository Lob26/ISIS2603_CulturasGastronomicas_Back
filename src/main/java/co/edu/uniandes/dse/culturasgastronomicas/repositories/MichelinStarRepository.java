package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author Luis Borbon */
@Repository public interface MichelinStarRepository extends JpaRepository<MichelinStarEntity, Long> {}