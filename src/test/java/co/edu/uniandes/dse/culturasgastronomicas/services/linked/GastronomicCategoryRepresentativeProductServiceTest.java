package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de logica de la relacion Category - Products
 *
 * @author Diego Rubio
 */
@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional
@Import({GastronomicCategoryService.class, GastronomicCategoryToProductService.class})
public class GastronomicCategoryRepresentativeProductServiceTest {

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCategoryEntity> categoriesList = new ArrayList<>();
    private final List<RepresentativeProductEntity> productsList = new ArrayList<>();
    @Autowired private GastronomicCategoryToProductService categoryToProductService;
    @Autowired private TestEntityManager entityManager;

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
        entityManager.getEntityManager().createQuery("delete from representative_product").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            RepresentativeProductEntity product = factory.manufacturePojo(RepresentativeProductEntity.class);
            entityManager.persist(product);
            productsList.add(product);
        }

        for (int i = 0; i < 3; i++) {
            GastronomicCategoryEntity entity = factory.manufacturePojo(GastronomicCategoryEntity.class);
            entityManager.persist(entity);
            categoriesList.add(entity);
            if (i == 0) {
                productsList.get(i).setCategory(entity);
                entity.getProducts().add(productsList.get(i));
            }
        }
    }

    @Test void testAddProduct()
            throws EntityNotFoundException {
        GastronomicCategoryEntity entity = categoriesList.get(0);
        RepresentativeProductEntity representativeProductEntity = productsList.get(1);
        RepresentativeProductEntity response = categoryToProductService.addProduct(representativeProductEntity.getId(),
                                                                                   entity.getId());

        assertNotNull(response);
        assertEquals(representativeProductEntity.getId(), response.getId());
    }

    @Test void testAddInvalidProduct() {
        assertThrows(EntityNotFoundException.class, () -> {
            GastronomicCategoryEntity entity = categoriesList.get(0);
            categoryToProductService.addProduct(0L, entity.getId());
        });
    }

    @Test void testAddProductInvalidCategory() {
        assertThrows(EntityNotFoundException.class, () -> {
            RepresentativeProductEntity representativeProductEntity = productsList.get(1);
            categoryToProductService.addProduct(representativeProductEntity.getId(), 0L);
        });
    }

    @Test void testGetProducts()
            throws EntityNotFoundException {
        List<RepresentativeProductEntity> list = categoryToProductService.getProducts(categoriesList.get(0).getId());
        assertEquals(1, list.size());
    }

    @Test void testGetProductsInvalidCategory() {
        assertThrows(EntityNotFoundException.class, () -> categoryToProductService.getProducts(0L));
    }

    @Test void testGetProduct()
            throws EntityNotFoundException, BusinessLogicException {
        GastronomicCategoryEntity entity = categoriesList.get(0);
        RepresentativeProductEntity representativeProductEntity = productsList.get(0);
        RepresentativeProductEntity response = categoryToProductService.getProduct(entity.getId(),
                                                                                   representativeProductEntity.getId());

        assertEquals(representativeProductEntity.getId(), response.getId());
        assertEquals(representativeProductEntity.getName(), response.getName());
        assertEquals(representativeProductEntity.getBrand(), response.getBrand());
    }

    @Test void testGetProductInvalidCategory() {
        assertThrows(EntityNotFoundException.class, () -> {
            RepresentativeProductEntity representativeProductEntity = productsList.get(0);
            categoryToProductService.getProduct(0L, representativeProductEntity.getId());
        });
    }

    @Test void testGetInvalidProduct() {
        assertThrows(EntityNotFoundException.class, () -> {
            GastronomicCategoryEntity entity = categoriesList.get(0);
            categoryToProductService.getProduct(entity.getId(), 0L);
        });
    }

    @Test public void getProductNoAsociadoTest() {
        assertThrows(BusinessLogicException.class, () -> {
            GastronomicCategoryEntity entity = categoriesList.get(0);
            RepresentativeProductEntity representativeProductEntity = productsList.get(1);
            categoryToProductService.getProduct(entity.getId(), representativeProductEntity.getId());
        });
    }

}