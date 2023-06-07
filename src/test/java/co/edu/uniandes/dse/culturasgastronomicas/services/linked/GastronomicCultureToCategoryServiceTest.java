package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
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
@Import(GastronomicCultureToCategoryService.class)
class GastronomicCultureToCategoryServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCategoryEntity> categories = new ArrayList<>();
    @Autowired private GastronomicCultureToCategoryService service;
    @Autowired private TestEntityManager manager;
    private GastronomicCultureEntity culture;


    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
    }

    private void insertData() {
        persists: {
            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(GastronomicCategoryEntity.class))
                     .forEach(category -> {
                         category.setCulture(culture);
                         manager.persist(category);
                         categories.add(category);
                     });
            culture.setCategories(categories);
        }
    }

    @Test void createOK()
            throws EntityNotFoundException, BusinessLogicException {
        var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
        pojo.setCulture(culture);
        assertNotNull(service.create(culture.getId(), pojo));
    }

    @Test void createNonexistentCulture() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            pojo.setCulture(culture);
            service.create(0L, pojo);
        });
    }

    @Test void getAll()
            throws EntityNotFoundException {
        var list = service.findAll(culture.getId());
        assertEquals(list.size(), categories.size());
        Collections.sort(list);
        Collections.sort(categories);
        assertEquals(list, categories);
    }

    //There is no get for only one, it does not matter for the controller

    @Test void deleteOK()
            throws EntityNotFoundException, BusinessLogicException {
        var ob = categories.get(2);
        service.delete(culture.getId(), ob.getName());
        assertFalse(culture.getCategories().contains(ob));
    }

    @Test void deleteCultureless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(0L, categories.get(2).getName()));
    }

    @Test void deleteCategoryless() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(culture.getId(), ""));
    }

    @Test void deleteDetached() {
        var culture2 = manager.persist(factory.manufacturePojo(GastronomicCultureEntity.class));
        assertThrows(BusinessLogicException.class, () -> service.delete(culture2.getId(), categories.get(2).getName()));
    }
}