package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.DishMultimediaEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.DishMultimediaRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Santiago Diaz
 * @version 1.0.0
 */
@Slf4j @Service public class DishMultimediaService {
    @Autowired DishMultimediaRepository repository;

    /** Create DishMultimedia into DB */
    @Transactional public DishMultimediaEntity create(DishMultimediaEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.DISH_M);
        if (entity.getUrl() == null || entity.getUrl().isBlank()) throw new BusinessLogicException("Blank url");
        if (entity.getRecipe() == null) throw new BusinessLogicException("Recipe is null");
        return repository.findByUrl(entity.getUrl()).orElseGet(() -> repository.save(entity));
    }

    /** Get DishMultimedia from DB */
    @Transactional public List<DishMultimediaEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.DISH_M);
        return repository.findAll();
    }

    /** Get DishMultimedia from DB by ID */
    @Transactional public DishMultimediaEntity findOne(String url)
            throws EntityNotFoundException {
        log.info("{}: {} [url={}]", CRUD.R_O, STR.DISH_M, url);
        Optional<DishMultimediaEntity> byId = repository.findByUrl(url);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.DISH_M);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update DishMultimedia's instance's data */
    @Transactional public DishMultimediaEntity update(Long id,
                                                      DishMultimediaEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.DISH_M, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.COUNTRY);
        if (entity.getUrl() == null || entity.getUrl().isBlank()) throw new BusinessLogicException("Blank id");
        if (entity.getRecipe() == null) throw new BusinessLogicException("Recipe is null");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete DishMultimedia's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.DISH_M, id);
        Optional<DishMultimediaEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.DISH_M);
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}