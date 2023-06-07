package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j @Service public class RecipeToCultureService {
    @Autowired private RecipeRepository recipeRepository;
    @Autowired private GastronomicCultureRepository gastronomicCultureRepository;

    @Transactional public void removeGastronomicCulture(Long recipeId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la GastronomicCulture del recipe con id = {}", recipeId);
        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) throw new EntityNotFoundException(STR.RECIPE);

        Optional<GastronomicCultureEntity> gastronomicCultureEntity = gastronomicCultureRepository.findById(recipeEntity.get()
                .getCulture().getId());
        gastronomicCultureEntity.ifPresent(gastronomicCulture -> gastronomicCulture.getRecipes()
                .remove(recipeEntity.get()));

        recipeEntity.get().setCulture(null);
        log.info("Termina proceso de borrar la gastronomicCulture de la recipe con id = {}", recipeId);
    }
}