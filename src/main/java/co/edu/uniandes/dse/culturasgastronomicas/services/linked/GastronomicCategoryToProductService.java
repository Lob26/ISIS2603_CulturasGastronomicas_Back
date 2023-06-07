package co.edu.uniandes.dse.culturasgastronomicas.services.linked;

import co.edu.uniandes.dse.culturasgastronomicas.entities.GastronomicCategoryEntity;
import co.edu.uniandes.dse.culturasgastronomicas.entities.RepresentativeProductEntity;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.culturasgastronomicas.exceptions.STR;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.GastronomicCategoryRepository;
import co.edu.uniandes.dse.culturasgastronomicas.repositories.RepresentativeProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j @Service public class GastronomicCategoryToProductService {
    @Autowired private RepresentativeProductRepository productRepository;
    @Autowired private GastronomicCategoryRepository categoryRepository;

    @Transactional public RepresentativeProductEntity addProduct(Long productId, Long categoryId) throws
            EntityNotFoundException {
        log.info("Inicia proceso de agregarle un producto a la categoria con id = {}", categoryId);

        Optional<RepresentativeProductEntity> productEntity = productRepository.findById(productId);
        if (productEntity.isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);

        Optional<GastronomicCategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        if (categoryEntity.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);

        productEntity.get().setCategory(categoryEntity.get());
        log.info("Termina proceso de agregarle un producto a la categoria con id = {}", categoryId);
        return productEntity.get();
    }

    @Transactional public List<RepresentativeProductEntity> getProducts(Long categoryId) throws
            EntityNotFoundException {
        log.info("Inicia proceso de consultar los productos asociados a la categoria con id = {}", categoryId);
        Optional<GastronomicCategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        if (categoryEntity.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);

        return categoryEntity.get().getProducts();
    }

    @Transactional public RepresentativeProductEntity getProduct(Long categoryId, Long productId) throws
            EntityNotFoundException, BusinessLogicException {
        log.info("Inicia proceso de consultar el producto con id = {} de la categoria con id = " +
                categoryId, productId);

        Optional<GastronomicCategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        if (categoryEntity.isEmpty()) throw new EntityNotFoundException(STR.CATEGORY);

        Optional<RepresentativeProductEntity> productEntity = productRepository.findById(productId);
        if (productEntity.isEmpty()) throw new EntityNotFoundException(STR.PRODUCT);

        log.info("Termina proceso de consultar el producto con id = {} de la categoria con id = " +
                categoryId, productId);

        if (!categoryEntity.get().getProducts().contains(productEntity.get()))
            throw new BusinessLogicException("The product is not associated to the category");

        return productEntity.get();
    }
}
