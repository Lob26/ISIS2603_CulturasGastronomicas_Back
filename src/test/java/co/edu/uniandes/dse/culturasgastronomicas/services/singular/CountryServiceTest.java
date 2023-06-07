package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pedro Lobato
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(CountryService.class)
class CountryServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<CountryEntity> countries = new ArrayList<>();
    @Autowired private CountryService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from restaurant").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from country").executeUpdate();
    }

    private void insertData() {
        List<RestaurantEntity> restaurants;
        persists: {
            var pojoR = factory.manufacturePojo(RestaurantEntity.class);
            manager.persist(pojoR);
            restaurants = Collections.singletonList(pojoR);
        }

        create: {
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(CountryEntity.class)).forEach(country -> {
                manager.persist(country);
                countries.add(country);
            });
            countries.get(0).setRestaurants(restaurants);
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(CountryEntity.class);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(CountryEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(CountryEntity.class);
            pojo.setName(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(CountryEntity.class);
            pojo.setName("");
            service.create(pojo);
        });
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), countries.size());
        list.stream()
            .map(eList -> countries.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = countries.get(0);
        var ob2 = service.findOne(ob1.getName());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyName() {assertThrows(EntityNotFoundException.class, () -> service.findOne(""));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = countries.get(1);
        var pojo = factory.manufacturePojo(CountryEntity.class);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(CountryEntity.class);
            pojo.setName("");
            service.update(countries.get(1).getId(), pojo);
        });
    }

    @Test void updateNullName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(CountryEntity.class);
            pojo.setName(null);
            service.update(countries.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(CountryEntity.class)));
    }

    @Test void deleteOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = countries.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(CountryEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}

    @Test void deleteRelated() {
        assertThrows(BusinessLogicException.class, () -> service.delete(countries.get(0).getId()));
    }
}