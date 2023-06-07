package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.DishMultimediaRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j @Service public class RecipeToMultimediaService {
    @Autowired private DishMultimediaRepository dishMultimediaRepository;
    @Autowired private RecipeRepository recipeRepository;

    @Transactional public DishMultimediaEntity addDishMultimedia(Long dishMultimediaId,
                                                                 Long recipeId)
            throws
            EntityNotFoundException {
        log.info("Inicia proceso de agregarle un DishMultimedia a la recipe con id = {}", recipeId);

        Optional<DishMultimediaEntity> dishMultimediaEntity = dishMultimediaRepository.findById(dishMultimediaId);
        if (dishMultimediaEntity.isEmpty()) throw new EntityNotFoundException(STR.DISH_M);

        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);

        dishMultimediaEntity.get().setRecipe(recipeEntity.get());
        log.info("Termina proceso de agregarle un dishMultimedia a la reipe con id = {}", recipeId);
        return dishMultimediaEntity.get();
    }

    @Transactional public List<DishMultimediaEntity> getDishMultimedia(Long recipeId)
            throws EntityNotFoundException {
        log.info("Inicia proceso de consultar los dishMultimedia asociados a la recipe con id = {}", recipeId);
        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);

        return recipeEntity.get().getUrls();
    }

    @Transactional public DishMultimediaEntity getDishMultimedia(Long recipeId,
                                                                 Long dishMultimediaId)
            throws
            EntityNotFoundException, BusinessLogicException {
        log.info("Inicia proceso de consultar el DishMultimedia con id = {} de la recipe con id = " +
                         recipeId, dishMultimediaId);

        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);

        Optional<DishMultimediaEntity> dishMultimediaEntity = dishMultimediaRepository.findById(dishMultimediaId);
        if (dishMultimediaEntity.isEmpty()) throw new EntityNotFoundException(STR.DISH_M);

        log.info("Termina proceso de consultar el dishMultimedia con id = {} de la recipe con id = " +
                         recipeId, dishMultimediaId);

        if (!recipeEntity.get().getUrls().contains(dishMultimediaEntity.get()))
            throw new BusinessLogicException("The dishMultimedia is not associated to the recipe");

        return dishMultimediaEntity.get();
    }
}