package co.edu.uniandes.dse.culturasgastronomicas.services.linked;


import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.DishMultimediaService;
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

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional
@Import({DishMultimediaService.class, DishMultimediaToRecipeService.class})
public class DishMultimediaRecipeServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RecipeEntity> recipesList = new ArrayList<>();
    private final List<DishMultimediaEntity> dishMultimedias = new ArrayList<>();
    @Autowired private TestEntityManager entityManager;
    @Autowired private DishMultimediaToRecipeService dishMultimediaRecipeService;
    @Autowired private DishMultimediaService dishMultimediaService;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from dish_multimedia").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from recipe").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            DishMultimediaEntity dishMultimedia = factory.manufacturePojo(DishMultimediaEntity.class);
            entityManager.persist(dishMultimedia);
            dishMultimedias.add(dishMultimedia);
        }
        for (int i = 0; i < 3; i++) {
            RecipeEntity entity = factory.manufacturePojo(RecipeEntity.class);
            entityManager.persist(entity);
            recipesList.add(entity);
            if (i == 0) dishMultimedias.get(i).setRecipe(entity);
        }
    }

    @Test void testRemoveRecipe()
            throws EntityNotFoundException {
        dishMultimediaRecipeService.removeRecipe(dishMultimedias.get(0).getId());
        DishMultimediaEntity response = dishMultimediaService.findOne(dishMultimedias.get(0).getUrl());
        assertNull(response.getRecipe());
    }
}