package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.*;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
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
 * @author Pedro Lobato
 * @version 1.0.0-alpha
 */
@DataJpaTest @Transactional @ExtendWith(SpringExtension.class) @Import(GastronomicCultureService.class)
class GastronomicCultureServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCultureEntity> cultures = new ArrayList<>();
    @Autowired private GastronomicCultureService service;
    @Autowired private TestEntityManager manager;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from gastronomic_category").executeUpdate();
        manager.getEntityManager().createQuery("delete from country").executeUpdate();
        manager.getEntityManager().createQuery("delete from recipe").executeUpdate();
        manager.getEntityManager().createQuery("delete from gastronomic_culture").executeUpdate();
    }

    private void insertData() {
        List<CountryEntity> countries;
        List<GastronomicCategoryEntity> categories;
        List<RecipeEntity> recipes;
        persists: {
            var pojoC = factory.manufacturePojo(CountryEntity.class);
            manager.persist(pojoC);
            countries = Collections.singletonList(pojoC);

            var pojoGC = factory.manufacturePojo(GastronomicCategoryEntity.class);
            manager.persist(pojoGC);
            categories = Collections.singletonList(pojoGC);

            var pojoR = factory.manufacturePojo(RecipeEntity.class);
            manager.persist(pojoR);
            recipes = Collections.singletonList(pojoR);
        }

        create: {
            IntStream.range(0, 3)
                     .mapToObj(i -> factory.manufacturePojo(GastronomicCultureEntity.class))
                     .forEach(culture -> {
                         manager.persist(culture);
                         cultures.add(culture);
                     });
            cultures.get(0).setCountries(countries);
            cultures.get(0).setCategories(categories);
            cultures.get(0).setRecipes(recipes);
        }
    }

    @Test void createOK() throws BusinessLogicException {
        var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
        var result = service.create(pojo);
        assertNotNull(result);
        var entity = manager.find(GastronomicCultureEntity.class, result.getId());
        assertEquals(pojo, entity);
    }

    @Test void createNull() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
            pojo.setName(null);
            service.create(pojo);
        });
    }

    @Test void createBlank() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
            pojo.setName("");
            service.create(pojo);
        });
    }

    @Test void getAll() {
        var list = service.findAll();
        assertEquals(list.size(), cultures.size());
        list.stream()
            .map(eList -> cultures.stream().anyMatch(eStored -> eList.getId().equals(eStored.getId())))
            .forEach(Assertions::assertTrue);
    }

    @Test void getOneOK() throws EntityNotFoundException {
        var ob1 = cultures.get(0);
        var ob2 = service.findOne(ob1.getId());
        assertNotNull(ob2);
        assertEquals(ob1, ob2);
    }

    @Test void getOneEmptyName() {assertThrows(EntityNotFoundException.class, () -> service.findOne(0L));}

    @Test void updateOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = cultures.get(1);
        var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
        service.update(ob.getId(), pojo);
        assertEquals(ob, pojo);
    }

    @Test void updateEmptyName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
            pojo.setName("");
            service.update(cultures.get(1).getId(), pojo);
        });
    }

    @Test void updateNullName() {
        assertThrows(BusinessLogicException.class, () -> {
            var pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
            pojo.setName(null);
            service.update(cultures.get(1).getId(), pojo);
        });
    }

    @Test void update0ID() {
        assertThrows(EntityNotFoundException.class,
                     () -> service.update(0L, factory.manufacturePojo(GastronomicCultureEntity.class)));
    }

    @Test void deleteOK() throws EntityNotFoundException, BusinessLogicException {
        var ob = cultures.get(2);
        service.delete(ob.getId());
        assertNull(manager.find(GastronomicCultureEntity.class, ob.getId()));
    }

    @Test void delete0ID() {assertThrows(EntityNotFoundException.class, () -> service.delete(0L));}

    @Test void deleteRelated() {
        assertThrows(BusinessLogicException.class, () -> service.delete(cultures.get(0).getId()));
    }
}