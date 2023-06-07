package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Transactional @ExtendWith(SpringExtension.class)
@Import(GastronomicCultureToCountryService.class)
class GastronomicCultureToCountryServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<CountryEntity> countries = new ArrayList<>();
    @Autowired private GastronomicCultureToCountryService service;
    @Autowired private TestEntityManager manager;
    private GastronomicCultureEntity culture;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from country").executeUpdate();
    }

    private void insertData() {
        persists: {
            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
        }

        create: {
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(CountryEntity.class)).forEach(country -> {
                country.getCultures().add(culture);
                manager.persist(country);
                countries.add(country);
            });
            culture.setCountries(countries);
        }
    }

    @Test void createOK()
            throws EntityNotFoundException, BusinessLogicException {
        var pojo = factory.manufacturePojo(CountryEntity.class);
        pojo.getCultures().add(culture);
        var result = service.create(culture.getId(), pojo);
        assertNotNull(result);
        var entity = manager.find(CountryEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNonexistentCulture() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(CountryEntity.class);
            pojo.getCultures().add(culture);
            service.create(0L, pojo);
        });
    }

    @Test void getAll()
            throws EntityNotFoundException {
        var list = service.findAll(culture.getId());
        assertEquals(list.size(), countries.size());
        Collections.sort(list);
        Collections.sort(countries);
        assertEquals(list, countries);
    }

    @Test void getAllNonexistentCulture() {
        assertThrows(EntityNotFoundException.class, () -> service.findAll(0L));
    }

    //There is no get for only one, it does not matter for the controller

    @Test void deleteOK()
            throws EntityNotFoundException, BusinessLogicException {
        var ob = countries.get(2);
        service.delete(culture.getId(), ob.getName());
        assertFalse(culture.getCountries().contains(ob));
    }

    @Test void deleteCultureless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(0L, countries.get(2).getName()));
    }

    @Test void deleteCountryless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(culture.getId(), ""));
    }

    @Test void deleteDetached() {
        var culture2 = manager.persist(factory.manufacturePojo(GastronomicCultureEntity.class));
        assertThrows(BusinessLogicException.class, () -> service.delete(culture2.getId(), countries.get(2).getName()));
    }
}