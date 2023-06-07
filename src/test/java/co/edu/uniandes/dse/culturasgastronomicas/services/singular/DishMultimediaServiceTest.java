package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pedro Lobato
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(DishMultimediaService.class)
class DishMultimediaServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<DishMultimediaEntity> multimedia = new ArrayList<>();
    private RecipeEntity recipe;
    @Autowired private DishMultimediaService service;
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
            recipe = factory.manufacturePojo(RecipeEntity.class);
            manager.persist(recipe);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(DishMultimediaEntity.class))
                     .forEach(multimediaI -> {
                         multimediaI.setRecipe(recipe);
                         manager.persist(multimediaI);
                         multimedia.add(multimediaI);
                     });
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
        pojo.setRecipe(recipe);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(DishMultimediaEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            pojo.setRecipe(recipe);
            pojo.setUrl(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            pojo.setRecipe(recipe);
            pojo.setUrl("");
            service.create(pojo);
        });
    }

    @Test void createRecipeless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            service.create(pojo);
        });
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), multimedia.size());
        list.stream()
            .map(eList -> multimedia.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = multimedia.get(0);
        var ob2 = service.findOne(ob1.getUrl());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyUrl() {assertThrows(EntityNotFoundException.class, () -> service.findOne(""));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = multimedia.get(1);
        var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
        pojo.setRecipe(recipe);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyUrl() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            pojo.setRecipe(recipe);
            pojo.setUrl("");
            service.update(multimedia.get(1).getId(), pojo);
        });
    }

    @Test void updateNullUrl() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            pojo.setRecipe(recipe);
            pojo.setUrl(null);
            service.update(multimedia.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(DishMultimediaEntity.class)));
    }

    @Test void updateRecipeless() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(DishMultimediaEntity.class);
            service.update(multimedia.get(1).getId(), pojo);
        });
    }

    @Test void deleteOK() throws EntityNotFoundException {
        var ob = multimedia.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(DishMultimediaEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}
}