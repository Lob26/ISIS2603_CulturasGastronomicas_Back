package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.GastronomicCategoryService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Pruebas de logica de la relacion Category - Culture
 *
 * @author Diego Rubio
 */
@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional
@Import({GastronomicCategoryService.class, GastronomicCategoryToCultureService.class})
public class GastronomicCategoryGastronomicCultureServiceTest {

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCultureEntity> culturesList = new ArrayList<>();
    private final List<GastronomicCategoryEntity> categoriesList = new ArrayList<>();
    @Autowired private TestEntityManager entityManager;
    @Autowired private GastronomicCategoryToCultureService categoryToCultureService;
    @Autowired private GastronomicCategoryService categoryService;

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            GastronomicCategoryEntity categories = factory.manufacturePojo(GastronomicCategoryEntity.class);
            entityManager.persist(categories);
            categoriesList.add(categories);
        }
        for (int i = 0; i < 3; i++) {
            GastronomicCultureEntity entity = factory.manufacturePojo(GastronomicCultureEntity.class);
            entityManager.persist(entity);
            culturesList.add(entity);
            if (i == 0) {
                categoriesList.get(i).setCulture(entity);
            }
        }
    }

    @Test void testRemoveCulture()
            throws EntityNotFoundException {
        categoryToCultureService.removeCutlture(categoriesList.get(0).getId());
        GastronomicCategoryEntity response = categoryService.findOne(categoriesList.get(0).getId());
        assertNull(response.getCulture());
    }

    @Test void testRemoveCultureInvalidCategory() {
        assertThrows(EntityNotFoundException.class, () -> categoryToCultureService.removeCutlture(0L));
    }

}
