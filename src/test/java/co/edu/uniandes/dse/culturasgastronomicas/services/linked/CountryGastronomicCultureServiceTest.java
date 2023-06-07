package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.CountryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCultureEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RecipeEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.services.singular.GastronomicCultureService;
import org.junit.jupiter.api.Assertions;
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
@Import({CountryToCultureService.class, GastronomicCultureService.class})
class CountryGastronomicCultureServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<GastronomicCultureEntity> cultures = new ArrayList<>();
    @Autowired private CountryToCultureService service;
    @Autowired private GastronomicCultureService serviceB;
    @Autowired private TestEntityManager manager;
    private CountryEntity country;
    private GastronomicCategoryEntity category;
    private RecipeEntity recipe;

    @BeforeEach void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        manager.getEntityManager().createQuery("DELETE FROM restaurant").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM recipe").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM country").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM gastronomic_culture").executeUpdate();
        manager.getEntityManager().createQuery("DELETE FROM gastronomic_category").executeUpdate();
    }

    private void insertData() {
        country = factory.manufacturePojo(CountryEntity.class);
        manager.persist(country);
        relationEntities();
        IntStream.range(0, 3).forEach(this::relationsEntities);
    }

    private void relationEntities() {
        category = factory.manufacturePojo(GastronomicCategoryEntity.class);
        manager.persist(category);
        recipe = factory.manufacturePojo(RecipeEntity.class);
        manager.persist(recipe);
    }

    private void relationsEntities(int i) {
        var culture = factory.manufacturePojo(GastronomicCultureEntity.class);
        culture.getCountries().add(country);
        culture.getCategories().add(category);
        culture.getRecipes().add(recipe);
        country.getCultures().add(culture);
        manager.persist(culture);
        cultures.add(culture);
    }

    @Test void testAdd() throws EntityNotFoundException, BusinessLogicException {
        GastronomicCultureEntity pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
        pojo.getCountries().add(country);
        pojo.getCategories().add(category);
        pojo.getRecipes().add(recipe);

        GastronomicCultureEntity stored = service.addCulture(country.getId(), serviceB.create(pojo).getId());
        assertNotNull(stored);
        assertEquals(stored, pojo);

        GastronomicCultureEntity last = service.getCulture(country.getId(), stored.getId());
        assertEquals(last, pojo);
    }

    @Test void testAddInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> {
            GastronomicCultureEntity pojo = factory.manufacturePojo(GastronomicCultureEntity.class);
            pojo.getCountries().add(country);
            pojo.getCategories().add(category);
            pojo.getRecipes().add(recipe);
            service.addCulture(0L, serviceB.create(pojo).getId());
        });
    }

    @Test void testAddInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.addCulture(country.getId(), 0L));
    }

    @Test void testGets() throws EntityNotFoundException {
        List<GastronomicCultureEntity> list = service.getCultures(country.getId());
        assertEquals(cultures.size(), list.size());
        IntStream.range(0, cultures.size()).mapToObj(i -> list.contains(cultures.get(0)))
                .forEach(Assertions::assertTrue);
    }

    @Test void testGet() throws EntityNotFoundException, BusinessLogicException {
        GastronomicCultureEntity entity = cultures.get(0);
        GastronomicCultureEntity stored = service.getCulture(country.getId(), entity.getId());
        assertNotNull(stored);

        assertEquals(entity, stored);
    }

    @Test void testGetInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> service.getCulture(0L, cultures.get(0).getId()));
    }

    @Test void testGetInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.getCulture(country.getId(), 0L));
    }

    @Test void testGetUnassociated() {
        assertThrows(BusinessLogicException.class, () -> {
            CountryEntity pCountry = factory.manufacturePojo(CountryEntity.class);
            manager.persist(pCountry);

            System.out.println(pCountry.getId());

            GastronomicCultureEntity pCulture = factory.manufacturePojo(GastronomicCultureEntity.class);
            pCulture.getCountries().add(country);
            pCulture.getCategories().add(category);
            pCulture.getRecipes().add(recipe);
            manager.persist(pCulture);

            service.getCulture(pCountry.getId(), pCulture.getId());
        });
    }

    @Test void testRemove() throws EntityNotFoundException {
        for (GastronomicCultureEntity e : cultures)
            service.removeCulture(country.getId(), e.getId());
        assertTrue(service.getCultures(country.getId()).isEmpty());
    }

    @Test void testRemoveInvalidA() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (GastronomicCultureEntity e : cultures)
                service.removeCulture(0L, e.getId());
        });
    }

    @Test void testRemoveInvalidB() {
        assertThrows(EntityNotFoundException.class, () -> service.removeCulture(country.getId(), 0L));
    }
}