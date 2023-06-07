package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
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

@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional
@Import({RecipeService.class, RecipeToCultureService.class}) public class RecipeGastronomicCultureServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCultureEntity> gastronomicCulturesList = new ArrayList<>();
    private final List<RecipeEntity> recipesList = new ArrayList<>();
    @Autowired private TestEntityManager entityManager;
    @Autowired private RecipeToCultureService recipeToCultureService;
    @Autowired private RecipeService recipeService;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from recipe").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            RecipeEntity recipes = factory.manufacturePojo(RecipeEntity.class);
            entityManager.persist(recipes);
            recipesList.add(recipes);
        }
        for (int i = 0; i < 3; i++) {
            GastronomicCultureEntity entity = factory.manufacturePojo(GastronomicCultureEntity.class);
            entityManager.persist(entity);
            gastronomicCulturesList.add(entity);
            if (i == 0) {
                recipesList.get(i).setCulture(entity);
            }
        }
    }

    @Test void testRemoveGastronomicCulture()
            throws EntityNotFoundException {
        recipeToCultureService.removeGastronomicCulture(recipesList.get(0).getId());
        RecipeEntity response = recipeService.findOne(recipesList.get(0).getId());
        assertNull(response.getCulture());
    }
}
