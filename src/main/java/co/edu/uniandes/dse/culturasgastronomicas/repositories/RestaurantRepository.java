package co.edu.uniandes.dse.culturasgastronomicas.repositories;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    @Query("select r from restaurant r join r.recipes rec where rec.id= ?1")
    List<RestaurantEntity> findAllByRecipe(Long recipeID);
}
