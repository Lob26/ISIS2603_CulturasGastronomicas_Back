package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RecipeService;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Transactional @ExtendWith(SpringExtension.class)
@Import({RecipeService.class, RecipeToMultimediaService.class}) public class RecipeDishMultimediaServiceTest {

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RecipeEntity> recipesList = new ArrayList<>();
    private final List<DishMultimediaEntity> dishMultimediasList = new ArrayList<>();
    @Autowired private RecipeToMultimediaService recipeToMultimediaService;
    @Autowired private TestEntityManager entityManager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from dish_multimedia ").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from recipe").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            DishMultimediaEntity dishMultimedia = factory.manufacturePojo(DishMultimediaEntity.class);
            entityManager.persist(dishMultimedia);
            dishMultimediasList.add(dishMultimedia);
        }

        for (int i = 0; i < 3; i++) {
            RecipeEntity entity = factory.manufacturePojo(RecipeEntity.class);
            entityManager.persist(entity);
            recipesList.add(entity);
            if (i == 0) {
                dishMultimediasList.get(i).setRecipe(entity);
                entity.getUrls().add(dishMultimediasList.get(i));
            }
        }
    }

    @Test void testAddDishMultimedia()
            throws EntityNotFoundException {
        RecipeEntity entity = recipesList.get(0);
        DishMultimediaEntity dishMultimediaEntity = dishMultimediasList.get(1);
        DishMultimediaEntity response = recipeToMultimediaService.addDishMultimedia(dishMultimediaEntity.getId(),
                                                                                    entity.getId());

        assertNotNull(response);
        assertEquals(dishMultimediaEntity.getId(), response.getId());
    }

    @Test void testAddInvalidDishMultimedia() {
        assertThrows(EntityNotFoundException.class, () -> {
            RecipeEntity entity = recipesList.get(0);
            recipeToMultimediaService.addDishMultimedia(0L, entity.getId());
        });
    }

    @Test void testAddDishMultimediaInvalidRecipe() {
        assertThrows(EntityNotFoundException.class, () -> {
            DishMultimediaEntity dishMultimediaEntity = dishMultimediasList.get(1);
            recipeToMultimediaService.addDishMultimedia(dishMultimediaEntity.getId(), 0L);
        });
    }

    @Test void testGetDishMultimedias()
            throws EntityNotFoundException {
        List<DishMultimediaEntity> list = recipeToMultimediaService.getDishMultimedia(recipesList.get(0).getId());
        assertEquals(1, list.size());
    }

    @Test void testGetDishMultimediasInvalidRecipe() {
        assertThrows(EntityNotFoundException.class, () -> recipeToMultimediaService.getDishMultimedia(0L));
    }


    @Test void testGetDishMultimedia()
            throws EntityNotFoundException, BusinessLogicException {
        RecipeEntity entity = recipesList.get(0);
        DishMultimediaEntity dishMultimediaEntity = dishMultimediasList.get(0);
        DishMultimediaEntity response = recipeToMultimediaService.getDishMultimedia(entity.getId(),
                                                                                    dishMultimediaEntity.getId());

        assertEquals(dishMultimediaEntity.getId(), response.getId());
        assertEquals(dishMultimediaEntity.getUrl(), response.getUrl());
    }

    @Test void testGetDishMultimediaInvalidRecipe() {
        assertThrows(EntityNotFoundException.class, () -> {
            DishMultimediaEntity dishMultimediaEntity = dishMultimediasList.get(0);
            recipeToMultimediaService.getDishMultimedia(0L, dishMultimediaEntity.getId());
        });
    }

    @Test void testGetInvalidDishMultimedia() {
        assertThrows(EntityNotFoundException.class, () -> {
            RecipeEntity entity = recipesList.get(0);
            recipeToMultimediaService.getDishMultimedia(entity.getId(), 0L);
        });
    }

    @Test public void getDishMultimediaNoAsociadoTest() {
        assertThrows(BusinessLogicException.class, () -> {
            RecipeEntity entity = recipesList.get(0);
            DishMultimediaEntity dishMultimediaEntity = dishMultimediasList.get(1);
            recipeToMultimediaService.getDishMultimedia(entity.getId(), dishMultimediaEntity.getId());
        });
    }

}

