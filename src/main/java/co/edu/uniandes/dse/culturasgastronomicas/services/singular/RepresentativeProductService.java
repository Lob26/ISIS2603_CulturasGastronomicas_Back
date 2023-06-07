package co.edu.uniandes.dse.culturasgastronomicas.services.singular;


import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RepresentativeProductRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Diego Rubio
 * @version 1.0.0
 */
@Slf4j @Service public class RepresentativeProductService {
    @Autowired RepresentativeProductRepository repository;

    /** Create Product into DB */
    @Transactional public RepresentativeProductEntity create(RepresentativeProductEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.PRODUCT);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCategory() == null) throw new BusinessLogicException("Category is null");
        return entity.getId() != null
                ? repository.findById(entity.getId()).orElseGet(() -> repository.save(entity))
                : repository.save(entity);
    }

    /** Get Product from DB */
    @Transactional public List<RepresentativeProductEntity> findAll() {
        log.info("{}: {}", CRUD.R_A, STR.PRODUCT);
        return repository.findAll();
    }

    /** Get Product from DB by ID */
    @Transactional public RepresentativeProductEntity findOne(Long id)
            throws EntityNotFoundException {
        log.info("{}: {} [id={}]", CRUD.R_O, STR.PRODUCT, id);
        Optional<RepresentativeProductEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);
        log.info("end::{}", CRUD.R_O);
        return byId.get();
    }

    /** Update Product's instance's data */
    @Transactional public RepresentativeProductEntity update(Long id,
                                                             RepresentativeProductEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.PRODUCT, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);
        if (entity.getName() == null || entity.getName().isBlank()) throw new BusinessLogicException("Blank name");
        if (entity.getCategory() == null) throw new BusinessLogicException("Category is null");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete Product's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.PRODUCT, id);
        Optional<RepresentativeProductEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);

        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}
