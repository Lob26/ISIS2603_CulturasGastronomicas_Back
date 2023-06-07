package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RestaurantService;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Transactional @ExtendWith(SpringExtension.class)
@Import({CountryToRestaurantService.class, RestaurantService.class})
class CountryRestaurantServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RestaurantEntity> restaurants = new ArrayList<>();
    @Autowired private CountryToRestaurantService service;
    @Autowired private RestaurantService serviceB;
    @Autowired private TestEntityManager manager;
    private CountryEntity country;
    private MichelinStarEntity star;
    private RecipeEntity recipe;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("DELETE FROM restaurant").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM recipe").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM country").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM gastronomic_category").executeUpdate();
    }

    private void insertData() {
        country = factory.manufacturePojo(CountryEntity.class);
        manager.persist(country);
        relationEntities();
        IntStream.range(0, 3).forEach(this::relationsEntities);
    }


    private void relationEntities() {
        star = factory.manufacturePojo(MichelinStarEntity.class);
        manager.persist(star);
        recipe = factory.manufacturePojo(RecipeEntity.class);
        manager.persist(recipe);
    }

    private void relationsEntities(int i) {
        var restaurant = factory.manufacturePojo(RestaurantEntity.class);
        restaurant.setCountry(country);
        restaurant.getRecipes().add(recipe);
        restaurant.getStars().add(star);

        country.getRestaurants().add(restaurant);
        manager.persist(restaurant);
        restaurants.add(restaurant);
    }

    @Test void testAdd() throws EntityNotFoundException, BusinessLogicException {
        RestaurantEntity pojo = factory.manufacturePojo(RestaurantEntity.class);
        pojo.setCountry(country);
        pojo.getRecipes().add(recipe);

        RestaurantEntity stored = service.addRestaurant(country.getId(), serviceB.create(pojo).getId());
        assertNotNull(stored);
        assertEquals(stored, pojo);

        RestaurantEntity last = service.getRestaurant(country.getId(), stored.getId());
        assertEquals(last, pojo);
    }

    @Test void testAddInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> {
            RestaurantEntity pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            pojo.getRecipes().add(recipe);
            service.addRestaurant(0L, serviceB.create(pojo).getId());
        });
    }

    @Test void testAddInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.addRestaurant(country.getId(), 0L));
    }

    @Test void testGets() throws EntityNotFoundException {
        List<RestaurantEntity> list = service.getRestaurants(country.getId());
        assertEquals(restaurants.size(), list.size());
        IntStream.range(0, restaurants.size()).mapToObj(i -> list.contains(restaurants.get(0)))
                .forEach(Assertions::assertTrue);
    }

    @Test void testGet() throws EntityNotFoundException, BusinessLogicException {
        RestaurantEntity entity = restaurants.get(0);
        RestaurantEntity stored = service.getRestaurant(country.getId(), entity.getId());
        assertNotNull(stored);

        assertEquals(entity, stored);
    }

    @Test void testGetInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> service.getRestaurant(0L, restaurants.get(0).getId()));
    }

    @Test void testGetInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.getRestaurant(country.getId(), 0L));
    }

    @Test void testGetUnassociated() {
        assertThrows(BusinessLogicException.class, () -> {
            CountryEntity pCountry = factory.manufacturePojo(CountryEntity.class);
            manager.persist(pCountry);

            System.out.println(pCountry.getId());

            RestaurantEntity pRestaurant = factory.manufacturePojo(RestaurantEntity.class);
            pRestaurant.setCountry(country);
            pRestaurant.getRecipes().add(recipe);
            manager.persist(pRestaurant);

            service.getRestaurant(pCountry.getId(), pRestaurant.getId());
        });
    }

    @Test void testRemove() throws EntityNotFoundException {
        for (RestaurantEntity e : restaurants)
            service.removeRestaurant(country.getId(), e.getId());
        assertTrue(service.getRestaurants(country.getId()).isEmpty());
    }

    @Test void testRemoveInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (RestaurantEntity e : restaurants)
                service.removeRestaurant(0L, e.getId());
        });
    }

    @Test void testRemoveInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.removeRestaurant(country.getId(), 0L));
    }
}