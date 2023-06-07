package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Transactional @ExtendWith(SpringExtension.class)
@Import(GastronomicCultureToCategoryToProductService.class)
class GastronomicCultureToCategoryToProductServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RepresentativeProductEntity> products = new ArrayList<>();
    @Autowired private GastronomicCultureToCategoryToProductService service;
    @Autowired private TestEntityManager manager;
    private GastronomicCultureEntity culture;
    private GastronomicCategoryEntity category;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
        manager.getEntityManager().createQuery("delete from representative_product").executeUpdate();
    }

    private void insertData() {
        persists: {
            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
            category = factory.manufacturePojo(GastronomicCategoryEntity.class);
            manager.persist(category);

            category.setCulture(culture);
            culture.getCategories().add(category);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(RepresentativeProductEntity.class))
                     .forEach(product -> {
                         product.setCategory(category);
                         manager.persist(product);
                         products.add(product);
                     });
            category.setProducts(products);
        }
    }

    @Test void createOK()
            throws EntityNotFoundException, BusinessLogicException {
        var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
        pojo.setCategory(category);
        var result = service.create(culture.getId(), category.getName(), pojo);
        assertNotNull(result);
        var entity = manager.find(RepresentativeProductEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNonexistentCulture() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
            pojo.setCategory(category);
            service.create(0L, category.getName(), pojo);
        });
    }

    @Test void createNonexistentCategory() {
        assertThrows(EntityNotFoundException.class, () -> service.create(culture.getId(), "", factory.manufacturePojo(
                RepresentativeProductEntity.class)));
    }

    @Test void createDetachedCultureCategory() {
        assertThrows(BusinessLogicException.class, () -> {
            var categoryT = factory.manufacturePojo(GastronomicCategoryEntity.class);
            manager.persist(categoryT);
            category.setCulture(culture);
            var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
            pojo.setCategory(categoryT);
            service.create(culture.getId(), categoryT.getName(), pojo);
        });
    }
}