package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
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
@Import(RecipeToRestaurantService.class)
class RecipeToRestaurantServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RestaurantEntity> restaurants = new ArrayList<>();
    @Autowired private RecipeToRestaurantService service;
    @Autowired private TestEntityManager manager;
    private RecipeEntity recipe;
    private CountryEntity country;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from country").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from restaurant").executeUpdate();
        manager.getEntityManager().createQuery("delete from recipe").executeUpdate();
    }

    private void insertData() {
        persist: {
            recipe = factory.manufacturePojo(RecipeEntity.class);
            manager.persist(recipe);

            country = manager.persist(factory.manufacturePojo(CountryEntity.class));
            //For recipe
            GastronomicCultureEntity culture = manager.persist(factory.manufacturePojo(GastronomicCultureEntity.class));

            recipe.setCulture(culture);
            country.setCultures(Collections.singletonList(culture));
            culture.setCountries(Collections.singletonList(country));
        }

        create: {
            var t = Collections.singletonList(recipe);
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(RestaurantEntity.class)).forEach(restaurant -> {
                restaurant.setCountry(country);
                restaurant.setRecipes(t);
                manager.persist(restaurant);
                restaurants.add(restaurant);
            });
            recipe.setRestaurants(restaurants);
        }
    }

    @Test void createOK()
            throws EntityNotFoundException, BusinessLogicException {
        var pojo = factory.manufacturePojo(RestaurantEntity.class);
        pojo.setCountry(country);
        assertNotNull(service.create(recipe.getId(), pojo));
    }

    @Test void createNonexistentRecipe() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(RestaurantEntity.class);
            pojo.setCountry(country);
            service.create(0L, pojo);
        });
    }

    @Test void createRecipeless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.create(recipe.getId(), factory.manufacturePojo(RestaurantEntity.class)));
    }

    @Test void getAll()
            throws EntityNotFoundException {
        var list = service.findAll(recipe.getId());
        assertEquals(restaurants.size(), list.size());
        Collections.sort(list);
        Collections.sort(restaurants);
        assertEquals(list, restaurants);
    }

    @Test void getAllNonexistentRecipe() {
        assertThrows(EntityNotFoundException.class, () -> service.findAll(0L));
    }

    @Test void getOneOK()
            throws EntityNotFoundException, BusinessLogicException {
        var entity = service.findOne(recipe.getId(), restaurants.get(0).getId());
        assertNotNull(entity);

        assertEquals(restaurants.get(0), entity);
    }

    @Test void getInvalidRestaurant() {
        assertThrows(EntityNotFoundException.class, () -> service.findOne(recipe.getId(), 0L));
    }

    @Test void getInvalidRecipe() {
        assertThrows(EntityNotFoundException.class, () -> service.findOne(0L, restaurants.get(0).getId()));
    }

    @Test void getDetached() {
        var recipe = manager.persist(factory.manufacturePojo(RecipeEntity.class));
        assertThrows(BusinessLogicException.class, ()->service.findOne(recipe.getId(), restaurants.get(0).getId()));
    }

    @Test void deleteOK()
            throws EntityNotFoundException, BusinessLogicException {
        var ob = restaurants.get(2);
        service.delete(recipe.getId(), ob.getId());
        assertFalse(recipe.getRestaurants().contains(ob));
    }

    @Test void deleteRecipeless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(0L, restaurants.get(2).getId()));
    }

    @Test void deleteRestaurantless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(recipe.getId(), 0L));
    }

    @Test void deleteDetached() {
        var recipe2 = manager.persist(factory.manufacturePojo(RecipeEntity.class));
        assertThrows(BusinessLogicException.class, () -> service.delete(recipe2.getId(), restaurants.get(2).getId()));
    }


}