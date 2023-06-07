package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mejor no especificar
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(GastronomicCategoryService.class)
class GastronomicCategoryServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCategoryEntity> categories = new ArrayList<>();
    private GastronomicCultureEntity culture;
    @Autowired private GastronomicCategoryService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from representative_product").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
    }

    private void insertData() {
        List<RepresentativeProductEntity> products;
        persist: {
            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
            var pojoS = factory.manufacturePojo(RepresentativeProductEntity.class);
            manager.persist(pojoS);
            products = Collections.singletonList(pojoS);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(GastronomicCategoryEntity.class))
                     .forEach(category -> {
                         category.setCulture(culture);
                         manager.persist(category);
                         categories.add(category);
                     });
            categories.get(0).setProducts(products);
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
        pojo.setCulture(culture);
        factory.populatePojo(pojo);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(GastronomicCategoryEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            pojo.setCulture(culture);
            pojo.setName(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            pojo.setCulture(culture);
            pojo.setName("");
            service.create(pojo);
        });
    }

    @Test void createCultureless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.create(factory.manufacturePojo(GastronomicCategoryEntity.class)));
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), categories.size());
        list.stream()
            .map(eList -> categories.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = categories.get(0);
        var ob2 = service.findOne(ob1.getName());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOne0ID() {assertThrows(EntityNotFoundException.class, () -> service.findOne(""));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = categories.get(1);
        var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
        pojo.setCulture(culture);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            pojo.setCulture(culture);
            pojo.setName("");
            service.update(categories.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            pojo.setCulture(culture);
            service.update(0L, pojo);
        });
    }

    @Test void updateCultureless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCategoryEntity.class);
            service.update(categories.get(1).getId(), pojo);
        });
    }

    @Test void deleteOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = categories.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(GastronomicCategoryEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}

    @Test void deleteRelated() {
        assertThrows(BusinessLogicException.class, () -> service.delete(categories.get(0).getId()));
    }
}