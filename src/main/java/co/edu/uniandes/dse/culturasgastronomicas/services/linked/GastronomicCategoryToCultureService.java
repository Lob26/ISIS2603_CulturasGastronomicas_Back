package co.edu.uniandes.dse.culturasgastronomicas.services.linked;


import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j @Service public class GastronomicCategoryToCultureService {
    @Autowired private GastronomicCategoryRepository categoryRepository;
    @Autowired private GastronomicCultureRepository cultureRepository;

    @Transactional public void removeCutlture(Long categoryId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la Cultura de la categoria con id = {}", categoryId);
        Optional<GastronomicCategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        if (categoryEntity.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);

        Optional<GastronomicCultureEntity> cultureEntity = cultureRepository.findById(categoryEntity.get().getCulture()
                .getId());
        cultureEntity.ifPresent(culture -> culture.getCategories().remove(categoryEntity.get()));

        categoryEntity.get().setCulture(null);
        log.info("Termina proceso de borrar la cultura de la categoria con id = {}", categoryId);
    }

}
