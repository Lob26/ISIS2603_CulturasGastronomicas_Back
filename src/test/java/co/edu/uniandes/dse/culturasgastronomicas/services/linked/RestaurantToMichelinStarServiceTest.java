package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RestaurantEntity;
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


@ExtendWith(SpringExtension.class) @DataJpaTest @Transactional @Import(RestaurantToMichelinStarService.class)
public class RestaurantToMichelinStarServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RestaurantEntity> restaurants = new ArrayList<>();
    private final List<MichelinStarEntity> stars = new ArrayList<>();
    @Autowired private RestaurantToMichelinStarService service;
    @Autowired private TestEntityManager manager;

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
        manager.getEntityManager().createQuery("delete from michelin_star").executeUpdate();
        manager.getEntityManager().createQuery("delete from restaurant").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        IntStream.range(0, 3).forEach(this::relationsEntities);
        restaurants.get(0).setStars(stars);
    }

    private void relationsEntities(int i) {
        RestaurantEntity restaurant = factory.manufacturePojo(RestaurantEntity.class);
        restaurants.add(restaurant);
        manager.persist(restaurant);
        MichelinStarEntity star = factory.manufacturePojo(MichelinStarEntity.class);
        stars.add(star);
        stars.get(i).setRestaurant(restaurants.get(0));
        manager.persist(star);
    }

    @Test void testAdd()
            throws EntityNotFoundException {
        RestaurantEntity restaurant = restaurants.get(0);
        MichelinStarEntity star = stars.get(1);
        MichelinStarEntity response = service.addStar(restaurant.getId(), star.getId());
        assertNotNull(response);
        assertEquals(star.getId(), response.getId());
    }

    @Test void testAddInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> service.addStar(restaurants.get(0).getId(), 0L));
    }

    @Test void testAddInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.addStar(0L, stars.get(1).getId()));
    }

    @Test void testGets()
            throws EntityNotFoundException {
        List<MichelinStarEntity> list = service.getStars(restaurants.get(0).getId());
        assertEquals(list.size(), stars.size());
    }

    @Test void testGetsInvalid() {
        assertThrows(EntityNotFoundException.class, () -> service.getStars(0L));
    }

    @Test void testGet()
            throws EntityNotFoundException, BusinessLogicException {
        RestaurantEntity restaurant = restaurants.get(0);
        MichelinStarEntity star = stars.get(0);
        MichelinStarEntity response = service.getStar(restaurant.getId(), star.getId());
        assertEquals(star, response);
    }

    @Test void testGetInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> service.getStar(0L, stars.get(0).getId()));
    }

    @Test void testGetInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.getStar(restaurants.get(0).getId(), 0L));
    }
}