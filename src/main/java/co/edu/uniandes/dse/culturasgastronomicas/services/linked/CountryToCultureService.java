package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.CountryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCultureRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** @author Pedro Lobato */
@Slf4j @Service public class CountryToCultureService {
    @Autowired private CountryRepository countryRep;
    @Autowired private GastronomicCultureRepository cultureRep;

    /** Associate Culture's instance to a certain Country into DB */
    @Transactional public GastronomicCultureEntity addCulture(Long countryID, Long cultureID) throws
            EntityNotFoundException {
        log.info("{}: {}", CRUD.A, STR.COUNTRY_CULTURE);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        Optional<GastronomicCultureEntity> culture = cultureRep.findById(cultureID);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        country.get().getCultures().add(culture.get());
        return culture.get();
    }

    /** Get Culture's instance associated to a certain Country */
    @Transactional public List<GastronomicCultureEntity> getCultures(Long countryID) throws EntityNotFoundException {
        log.info("{}: {} [{}.id={}]", CRUD.R_A, STR.COUNTRY_CULTURE, STR.COUNTRY, countryID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        if (country.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);
        return country.get().getCultures();
    }

    /** Get Culture's instance associated to a certain Country */
    @Transactional public GastronomicCultureEntity getCulture(Long countryID, Long cultureID) throws
            EntityNotFoundException, BusinessLogicException {
        log.info("{}: {} [id={}; id={}]", CRUD.R_O, STR.COUNTRY_CULTURE, countryID, cultureID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        Optional<GastronomicCultureEntity> culture = cultureRep.findById(cultureID);

        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);

        if (!country.get().getCultures().contains(culture.get()))
            throw new BusinessLogicException(STR.COUNTRY_CULTURE.det);
        return culture.get();
    }

    /** Disassociate a Culture's instance from an association to a certain Country */
    @Transactional public void removeCulture(Long countryID, Long cultureID) throws EntityNotFoundException {
        log.info("{}:{} [id={}; id={}]", CRUD.D, STR.COUNTRY_CULTURE, countryID, cultureID);
        Optional<CountryEntity> country = countryRep.findById(countryID);
        Optional<GastronomicCultureEntity> culture = cultureRep.findById(cultureID);

        if (country.isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (culture.isEmpty()) throw new EntityNotFoundException(STR.CULTURE);

        country.get().getCultures().remove(culture.get());
    }
}
