package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.RepresentativeProductService;
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


/**
 * Pruebas de logica de la relacion Category - Products
 *
 * @author Diego Rubio
 */
@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional
@Import({RepresentativeProductService.class, RepresentativeProductToCategoryService.class})
public class RepresentativeProductGastronomicCategoryServiceTest {

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCategoryEntity> categoriesList = new ArrayList<>();
    private final List<RepresentativeProductEntity> productsList = new ArrayList<>();
    @Autowired private TestEntityManager entityManager;
    @Autowired private RepresentativeProductToCategoryService productToCategoryService;
    @Autowired private RepresentativeProductService representativeProductService;

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
        entityManager.getEntityManager().createQuery("delete from representative_product").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            RepresentativeProductEntity products = factory.manufacturePojo(RepresentativeProductEntity.class);
            entityManager.persist(products);
            productsList.add(products);
        }
        for (int i = 0; i < 3; i++) {
            GastronomicCategoryEntity entity = factory.manufacturePojo(GastronomicCategoryEntity.class);
            entityManager.persist(entity);
            categoriesList.add(entity);
            if (i == 0) {
                productsList.get(i).setCategory(entity);
            }
        }
    }

    @Test void testRemoveCategory()
            throws EntityNotFoundException {
        productToCategoryService.removeCategory(productsList.get(0).getId());
        RepresentativeProductEntity response = representativeProductService.findOne(productsList.get(0).getId());
        assertNull(response.getCategory());
    }

    @Test void testRemoveCategoryInvalidProduct() {
        assertThrows(EntityNotFoundException.class, () -> productToCategoryService.removeCategory(0L));
    }

}
