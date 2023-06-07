package co.edu.uniandes.dse.culturasgastronomicas.services.singular;

import co.edu.uniandes.dse.culturasgastronomicas.entities.MichelinStarEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.MichelinStarRepository;
import co.edu.uniandes.dse.culturasgastronomicas.services.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Luis Borbon
 * @version 1.0.0
 */
@Slf4j @Service public class MichelinStarService {
    @Autowired MichelinStarRepository repository;

    /** Create MichelinStar into DB */
    @Transactional public MichelinStarEntity create(MichelinStarEntity entity)
            throws BusinessLogicException {
        log.info("{}: {}", CRUD.C, STR.M_STAR);
        if (entity.getRestaurant() == null) throw new BusinessLogicException(STR.RESTAURANT.nil);
        return entity.getId() != null
                ? repository.findById(entity.getId()).orElseGet(() -> repository.save(entity))
                : repository.save(entity);
    }

    /** Get MichelinStar from DB */
    @Transactional public List<MichelinStarEntity> findAll() {
        log.info("Inicia proceso de consultar todos los premios");
        return repository.findAll();
    }

    /** Get MichelinStar from DB por id */
    @Transactional public MichelinStarEntity findOne(Long michelinId)
            throws EntityNotFoundException {
        log.info("Inicia proceso de consultar michelinStar por id = {}", michelinId);
        Optional<MichelinStarEntity> prizeEntity = repository.findById(michelinId);
        if (prizeEntity.isEmpty()) throw new EntityNotFoundException(STR.M_STAR);
        log.info("Termina proceso de consultar premio con id = {}", michelinId);
        return prizeEntity.get();
    }

    /** Update MichelinStar's instance's data */
    @Transactional public MichelinStarEntity update(Long id,
                                                    MichelinStarEntity entity)
            throws EntityNotFoundException, BusinessLogicException {
        log.info("start::{}:{} [id={}]", CRUD.U, STR.M_STAR, id);
        Optional<MichelinStarEntity> byId = repository.findById(id);
        if (byId.isEmpty()) throw new EntityNotFoundException(STR.M_STAR);
        if (entity.getRestaurant() == null) throw new BusinessLogicException("Restaurant is null");
        entity.setId(id);
        log.info("end::{} [id={}]", CRUD.U, id);
        return repository.save(entity);
    }

    /** Delete MichelinStar's instance */
    @Transactional public void delete(Long id)
            throws EntityNotFoundException {
        log.info("start::{}:{} [id={}]", CRUD.D, STR.M_STAR, id);
        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException(STR.M_STAR);
        repository.deleteById(id);
        log.info("end::{} [id={}]", CRUD.D, id);
    }
}