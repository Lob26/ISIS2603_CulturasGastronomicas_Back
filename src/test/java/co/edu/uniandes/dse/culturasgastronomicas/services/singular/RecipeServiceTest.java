package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
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
 * @author Mejor no se especifica
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(RecipeService.class)
class RecipeServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<RecipeEntity> recipes = new ArrayList<>();
    private GastronomicCultureEntity culture;
    @Autowired private RecipeService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_culture ").executeUpdate();
        manager.getEntityManager().createQuery("delete from restaurant").executeUpdate();
        manager.getEntityManager().createQuery("delete from dish_multimedia").executeUpdate();
        manager.getEntityManager().createQuery("delete from recipe").executeUpdate();
    }

    private void insertData() {
        List<DishMultimediaEntity> multimedia;
        persists: {
            var pojoR = factory.manufacturePojo(DishMultimediaEntity.class);
            manager.persist(pojoR);
            multimedia = Collections.singletonList(pojoR);

            culture = factory.manufacturePojo(GastronomicCultureEntity.class);
            manager.persist(culture);
        }

        create: {
            IntStream.range(0, 3).mapToObj(i -> factory.manufacturePojo(RecipeEntity.class)).forEach(recipe -> {
                recipe.setCulture(culture);
                manager.persist(recipe);
                recipes.add(recipe);
            });
            recipes.get(0).setUrls(multimedia);
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(RecipeEntity.class);
        pojo.setCulture(culture);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(RecipeEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RecipeEntity.class);
            pojo.setCulture(culture);
            pojo.setName(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RecipeEntity.class);
            pojo.setCulture(culture);
            pojo.setName("");
            service.create(pojo);
        });
    }

    @Test void createCultureless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.create(factory.manufacturePojo(RecipeEntity.class)));
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), recipes.size());
        list.stream()
            .map(eList -> recipes.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = recipes.get(0);
        var ob2 = service.findOne(ob1.getId());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyName() {assertThrows(EntityNotFoundException.class, () -> service.findOne(0L));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = recipes.get(1);
        var pojo = factory.manufacturePojo(RecipeEntity.class);
        pojo.setCulture(culture);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RecipeEntity.class);
            pojo.setCulture(culture);
            pojo.setName("");
            service.update(recipes.get(1).getId(), pojo);
        });
    }

    @Test void updateNullName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(RecipeEntity.class);
            pojo.setCulture(culture);
            pojo.setName(null);
            service.update(recipes.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(RecipeEntity.class)));
    }

    @Test void updateCultureless() {
        assertThrows(BusinessLogicException.class,
                     () -> service.update(recipes.get(1).getId(), factory.manufacturePojo(RecipeEntity.class)));
    }

    @Test void deleteOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = recipes.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(RecipeEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}

    @Test void deleteRelated() {
        assertThrows(BusinessLogicException.class, () -> service.delete(recipes.get(0).getId()));
    }
}