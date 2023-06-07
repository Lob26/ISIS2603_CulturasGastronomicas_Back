package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RepresentativeProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j @Service public class RepresentativeProductToCategoryService {
    @Autowired private RepresentativeProductRepository productRepository;
    @Autowired private GastronomicCategoryRepository categoryRepository;

    @Transactional public void removeCategory(Long productId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la Categoria del producto con id = {}", productId);
        Optional<RepresentativeProductEntity> productEntity = productRepository.findById(productId);
        if (productEntity.isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);

        Optional<GastronomicCategoryEntity> categoryEntity = categoryRepository.findById(productEntity.get()
                .getCategory().getId());
        categoryEntity.ifPresent(category -> category.getProducts().remove(productEntity.get()));

        productEntity.get().setCategory(null);
        log.info("Termina proceso de borrar la categoria del producto con id = {}", productId);
    }

}
