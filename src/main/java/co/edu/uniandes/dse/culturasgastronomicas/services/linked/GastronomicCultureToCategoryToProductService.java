package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RepresentativeProductRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RepresentativeProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture -> GastronomicCategory and RepresentativeProduct entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(RepresentativeProductService.class) @Slf4j @Service
public class GastronomicCultureToCategoryToProductService {
    @Autowired private GastronomicCategoryRepository repository;
    @Autowired private RepresentativeProductRepository repository2;
    @Autowired private GastronomicCultureRepository checker;
    @Autowired private RepresentativeProductService service;

    /**
     * Retrieves a list of representative product entities for a given culture ID.
     *
     * @param cultureID the ID of the culture for which to retrieve the representative product entities
     * @return a list of representative product entities for the given culture ID
     * @throws EntityNotFoundException if the culture with the given ID is not found
     */
    @Transactional public List<RepresentativeProductEntity> findAll(Long cultureID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.PRODUCT, STR.CULTURE, cultureID);
        if (checker.findById(cultureID).isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        return repository2.findAllByCulture(cultureID);
    }

    /**
     * This method creates a new {@link RepresentativeProductEntity} associated with a {@link GastronomicCategoryEntity} belonging to a {@link GastronomicCultureEntity}.
     *
     * @param cultureID    the ID of the cultural entity to which the category belongs
     * @param categoryName the name of the category to which the product belongs
     * @param entity       the representative product entity to be created
     * @return the created representative product entity
     * @throws EntityNotFoundException if the cultural entity or the category are not found
     * @throws BusinessLogicException  if the category is not associated with the cultural entity
     */
    @Transactional public RepresentativeProductEntity create(Long cultureID,
                                              String categoryName,
                                              RepresentativeProductEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}; {}.name={}]",
                 CRUD.C, CRUD.A, STR.PRODUCT, STR.CULTURE, cultureID, STR.CATEGORY, categoryName);
        if (checker.findById(cultureID).isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        if (repository.findByName(categoryName).isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);
        var category = repository.findByNameAndCulture(cultureID, categoryName);
        if (category.isEmpty()) throw new BusinessLogicException(STR.CULTURE_CATEGORY.det);
        entity.setCategory(category.get());
        var response = service.create(entity);
        category.get().getProducts().add(response);
        return response;
    }

    //Want to update or delete? Use the DB itself not a HTTP request
}
