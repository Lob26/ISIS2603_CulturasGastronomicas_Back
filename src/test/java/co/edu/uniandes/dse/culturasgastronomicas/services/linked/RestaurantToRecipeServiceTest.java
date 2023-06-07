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
@Import(RestaurantToRecipeService.class)
class RestaurantToRecipeServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RecipeEntity> recipes = new ArrayList<>();
    @Autowired private RestaurantToRecipeService service;
    @Autowired private TestEntityManager manager;
    private RestaurantEntity restaurant;
    private GastronomicCultureEntity culture;//For recipe

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
            restaurant = factory.manufacturePojo(RestaurantEntity.class);
            manager.persist(restaurant);

            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
            //For restaurant
            var country = manager.persist(factory.manufacturePojo(CountryEntity.class));

            restaurant.setCountry(country);
            culture.setCountries(Collections.singletonList(country));
            country.setCultures(Collections.singletonList(culture));
        }

        create: {
        var t = Collections.singletonList(restaurant);
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(RecipeEntity.class)).forEach(recipe -> {
                recipe.setCulture(culture);
                recipe.setRestaurants(t);
                manager.persist(recipe);
                recipes.add(recipe);
            });
            restaurant.setRecipes(recipes);
        }
    }

    @Test void createOK()
            throws EntityNotFoundException, BusinessLogicException {
        var pojo = factory.manufacturePojo(RecipeEntity.class);
        pojo.setCulture(culture);
        assertNotNull(service.create(restaurant.getId(), pojo));
    }

    @Test void createNonexistentRestaurant() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(RecipeEntity.class);
            pojo.setCulture(culture);
            service.create(0L, pojo);
        });
    }

    @Test void createRestaurantless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.create(restaurant.getId(), factory.manufacturePojo(RecipeEntity.class)));
    }

    @Test void getAll()
            throws EntityNotFoundException {
        var list = service.findAll(restaurant.getId());
        assertEquals(recipes.size(), list.size());
        Collections.sort(list);
        Collections.sort(recipes);
        assertEquals(list, recipes);
    }

    @Test void getAllNonexistentRestaurant() {
        assertThrows(EntityNotFoundException.class, () -> service.findAll(0L));
    }

    @Test void getOneOK()
            throws EntityNotFoundException, BusinessLogicException {
        var entity = service.findOne(restaurant.getId(), recipes.get(0).getId());
        assertNotNull(entity);

        assertEquals(recipes.get(0), entity);
    }

    @Test void getInvalidRecipe() {
        assertThrows(EntityNotFoundException.class, () -> service.findOne(restaurant.getId(), 0L));
    }

    @Test void getInvalidRestaurant() {
        assertThrows(EntityNotFoundException.class, () -> service.findOne(0L, recipes.get(0).getId()));
    }

    @Test void getDetached() {
        var restaurant = manager.persist(factory.manufacturePojo(RestaurantEntity.class));
        assertThrows(BusinessLogicException.class, ()->service.findOne(restaurant.getId(), recipes.get(0).getId()));
    }

    @Test void deleteOK()
            throws EntityNotFoundException, BusinessLogicException {
        var ob = recipes.get(2);
        service.delete(restaurant.getId(), ob.getId());
        assertFalse(restaurant.getRecipes().contains(ob));
    }

    @Test void deleteRestaurantless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(0L, recipes.get(2).getId()));
    }

    @Test void deleteRecipeless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(restaurant.getId(), 0L));
    }

    @Test void deleteDetached() {
        var restaurant2 = manager.persist(factory.manufacturePojo(RestaurantEntity.class));
        assertThrows(BusinessLogicException.class, () -> service.delete(restaurant2.getId(), recipes.get(2).getId()));
    }
}
