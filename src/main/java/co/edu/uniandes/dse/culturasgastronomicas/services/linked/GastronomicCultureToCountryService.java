package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.CountryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing the relationship between GastronomicCulture and Country entities.
 *
 * @author Pedro Lobato
 * @version 1.0.0
 */
@Import(CountryService.class) @Slf4j @Service
public class GastronomicCultureToCountryService {
    @Autowired private CountryRepository repository;
    @Autowired private GastronomicCultureRepository checker;
    @Autowired private CountryService service;

    /**
     * Retrieves all countries associated with a given GastronomicCulture.
     *
     * @param cultureID the ID of the GastronomicCulture to retrieve countries for.
     * @return a list of CountryEntity objects associated with the given GastronomicCulture.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     */
    @Transactional public List<CountryEntity> findAll(Long cultureID)
            throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.COUNTRY, STR.CULTURE, cultureID);
        if (checker.findById(cultureID).isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        return repository.findAllByCulture(cultureID);
    }

    /**
     * Creates a new {@link CountryEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID the ID of the GastronomicCulture to create the CountryEntity for.
     * @param entity    the CountryEntity object to create.
     * @return the newly created CountryEntity object.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the CountryEntity cannot be created.
     */
    @Transactional public CountryEntity create(Long cultureID,
                                               CountryEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{},{}: {} [{}.id={}]", CRUD.C, CRUD.A, STR.COUNTRY, STR.CULTURE, cultureID);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        var response = service.create(entity);
        if (!culture.get().getCountries().contains(response)) culture.get().getCountries().add(response);
        return response;
    }

    /**
     * Deletes a {@link CountryEntity} associated with a given {@link GastronomicCultureEntity}.
     *
     * @param cultureID   the ID of the GastronomicCulture to delete the CountryEntity from.
     * @param countryName the name of the CountryEntity to delete.
     * @throws EntityNotFoundException if the given GastronomicCulture does not exist.
     * @throws BusinessLogicException  if the CountryEntity cannot be deleted.
     */
    @Transactional public void delete(Long cultureID,
                                      String countryName)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [{}.id={}]", CRUD.D, STR.COUNTRY, STR.CULTURE, cultureID);
        var culture = checker.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        var country = service.findOne(countryName);
        if (!culture.get().getCountries().remove(country)) throw new BusinessLogicException(STR.CULTURE_COUNTRY.det);
        country.getCultures().remove(culture.get());
    }
}