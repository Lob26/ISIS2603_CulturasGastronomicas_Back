package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.GastronomicCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and GastronomicCategory entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(GastronomicCategoryService.class) @Slf4j @Service
public class GastronomicCultureToCategoryService {
    @Autowired private GastronomicCategoryRepository repository;
    @Autowired private GastronomicCultureRepository checker;
    @Autowired private GastronomicCategoryService service;

    /**
     * Retrieves all categories associated with a given GastronomicCulture.
     *
     * @param cultureID the ID of the GastronomicCulture to retrieve countries for.
     * @return a list of GastronomicCategoryEntity objects associated with the given GastronomicCulture.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     */
    @Transactional public List<GastronomicCategoryEntity> findAll(Long cultureID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.CATEGORY, STR.CULTURE, cultureID);
        if (checker.findById(cultureID).isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        return repository.findAllByCulture(cultureID);
    }

    /**
     * Creates a new {@link GastronomicCategoryEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID the ID of the GastronomicCulture to create the GastronomicCategoryEntity for.
     * @param entity    the GastronomicCategoryEntity object to create.
     * @return the newly created GastronomicCategoryEntity object.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the GastronomicCategoryEntity cannot be created.
     */
    @Transactional public GastronomicCategoryEntity create(Long cultureID,
                                                           GastronomicCategoryEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}]", CRUD.C, CRUD.A, STR.CATEGORY, STR.CULTURE, cultureID);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        entity.setCulture(culture.get());
        var response = service.create(entity);
        if (repository.findByNameAndCulture(cultureID, entity.getName()).isEmpty())
            culture.get().getCategories().add(response);
        return response;
    }

    /**
     * Deletes a {@link GastronomicCategoryEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID    the ID of the GastronomicCulture to delete the GastronomicCategoryEntity from.
     * @param categoryName the name of the GastronomicCategoryEntity to delete.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the GastronomicCategoryEntity cannot be deleted.
     */
    @Transactional public void delete(Long cultureID,
                                      String categoryName)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={}; {}.name={}]",
                 CRUD.D, STR.CATEGORY, STR.CULTURE, cultureID, STR.CATEGORY, categoryName);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        if (repository.findByName(categoryName).isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);
        var category = repository.findByNameAndCulture(cultureID, categoryName);
        if (category.isEmpty()) throw new BusinessLogicException(STR.CULTURE_CATEGORY.det);
        culture.get().getCategories().remove(category.get());
        service.delete(category.get().getId());
    }
}
