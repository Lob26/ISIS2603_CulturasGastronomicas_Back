package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.DishMultimediaRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j @Service public class DishMultimediaToRecipeService {
    @Autowired private RecipeRepository recipeRepository;
    @Autowired private DishMultimediaRepository dishMultimediaRepository;

    @Transactional public void removeRecipe(Long dishMultimediaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la recipe del dishmultimedia con id = {}", dishMultimediaId);
        Optional<DishMultimediaEntity> dishMultimediaEntity = dishMultimediaRepository.findById(dishMultimediaId);
        if (dishMultimediaEntity.isEmpty()) throw new EntityNotFoundException(STR.DISH_M);

        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(dishMultimediaEntity.get().getRecipe().getId());
        recipeEntity.ifPresent(recipe -> recipe.getUrls().remove(dishMultimediaEntity.get()));

        dishMultimediaEntity.get().setRecipe(null);
        log.info("Termina proceso de borrar la recipe de la dishmultimedia con id = {}", dishMultimediaId);
    }
}