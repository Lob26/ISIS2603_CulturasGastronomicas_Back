package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
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
 * @author Mejor no especificar
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(RestaurantService.class)
class RestaurantServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RestaurantEntity> restaurants = new ArrayList<>();
    private CountryEntity country;
    @Autowired private RestaurantService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from michelin_star").executeUpdate();
        manager.getEntityManager().createQuery("delete from restaurant").executeUpdate();
    }

    private void insertData() {
        List<MichelinStarEntity> stars;
        persist: {
            country = factory.manufacturePojo(CountryEntity.class);
            manager.persist(country);
            var pojoS = factory.manufacturePojo(MichelinStarEntity.class);
            manager.persist(pojoS);
            stars = Collections.singletonList(pojoS);
        }

        create: {
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(RestaurantEntity.class)).forEach(restaurant -> {
                restaurant.setCountry(country);
                manager.persist(restaurant);
                restaurants.add(restaurant);
            });
            restaurants.get(0).setStars(stars);
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(RestaurantEntity.class);
        pojo.setCountry(country);
        factory.populatePojo(pojo);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(RestaurantEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            pojo.setName(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            pojo.setName("");
            service.create(pojo);
        });
    }

    @Test void createCountryless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.create(factory.manufacturePojo(RestaurantEntity.class)));
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), restaurants.size());
        list.stream()
            .map(eList -> restaurants.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = restaurants.get(0);
        var ob2 = service.findOne(ob1.getId());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOne0ID() {assertThrows(EntityNotFoundException.class, () -> service.findOne(0L));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = restaurants.get(1);
        var pojo = factory.manufacturePojo(RestaurantEntity.class);
        pojo.setCountry(country);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            pojo.setName("");
            service.update(restaurants.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            service.update(0L, pojo);
        });
    }

    @Test void updateCountryless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            service.update(restaurants.get(1).getId(), pojo);
        });
    }

    @Test void deleteOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = restaurants.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(RestaurantEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}

    @Test void deleteRelated() {
        assertThrows(BusinessLogicException.class, () -> service.delete(restaurants.get(0).getId()));
    }
}