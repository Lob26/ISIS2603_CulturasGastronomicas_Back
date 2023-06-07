package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
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
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(MichelinStarService.class)
class MichelinStarServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<MichelinStarEntity> stars = new ArrayList<>();
    private RestaurantEntity restaurant;
    @Autowired private MichelinStarService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from recipe").executeUpdate();
        manager.getEntityManager().createQuery("delete from dish_multimedia").executeUpdate();
    }

    private void insertData() {
        persists: {
            restaurant = factory.manufacturePojo(RestaurantEntity.class);
            manager.persist(restaurant);
        }

        create: {
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(MichelinStarEntity.class)).forEach(star -> {
                star.setRestaurant(restaurant);
                manager.persist(star);
                stars.add(star);
            });
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(MichelinStarEntity.class);
        pojo.setRestaurant(restaurant);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(MichelinStarEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createRestaurantless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(MichelinStarEntity.class);
            service.create(pojo);
        });
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), stars.size());
        list.stream()
            .map(eList -> stars.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = stars.get(0);
        var ob2 = service.findOne(ob1.getId());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyUrl() {assertThrows(EntityNotFoundException.class, () -> service.findOne(0L));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = stars.get(1);
        var pojo = factory.manufacturePojo(MichelinStarEntity.class);
        pojo.setRestaurant(restaurant);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(MichelinStarEntity.class)));
    }

    @Test void updateRestaurantless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(MichelinStarEntity.class);
            service.update(stars.get(1).getId(), pojo);
        });
    }

    @Test void deleteOK() throws EntityNotFoundException {
        var ob = stars.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(MichelinStarEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}
}