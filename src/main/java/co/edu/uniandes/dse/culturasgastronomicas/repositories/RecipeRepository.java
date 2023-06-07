package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @author Santiago Diaz */
@Repository public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    @Query("select r from recipe r where r.culture.id = ?1")
    List<RecipeEntity> findAllByCulture(Long cultureID);
    @Query("select r from recipe r join r.restaurants rest where rest.id= ?1")
    List<RecipeEntity> findAllByRestaurant(Long restaurantID);
}
