package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mejor no especificar
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(RepresentativeProductService.class)
class RepresentativeProductServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RepresentativeProductEntity> products = new ArrayList<>();
    private GastronomicCategoryEntity category;
    @Autowired private RepresentativeProductService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
        manager.getEntityManager().createQuery("delete from representative_product").executeUpdate();
    }

    private void insertData() {
        persists: {
            category = factory.manufacturePojo(GastronomicCategoryEntity.class);
            manager.persist(category);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(RepresentativeProductEntity.class))
                     .forEach(product -> {
                         product.setCategory(category);
                         manager.persist(product);
                         products.add(product);
                     });
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
        pojo.setCategory(category);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(RepresentativeProductEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createCategoryless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
            service.create(pojo);
        });
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), products.size());
        list.stream()
            .map(eList -> products.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = products.get(0);
        var ob2 = service.findOne(ob1.getId());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyUrl() {assertThrows(EntityNotFoundException.class, () -> service.findOne(0L));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = products.get(1);
        var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
        pojo.setCategory(category);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(RepresentativeProductEntity.class)));
    }

    @Test void updateCategoryless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RepresentativeProductEntity.class);
            service.update(products.get(1).getId(), pojo);
        });
    }

    @Test void deleteOK() throws EntityNotFoundException {
        var ob = products.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(RepresentativeProductEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}
}