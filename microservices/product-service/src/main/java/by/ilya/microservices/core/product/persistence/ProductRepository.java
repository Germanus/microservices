package by.ilya.microservices.core.product.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import by.ilya.microservices.core.product.persistence.ProductEntity;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String> {

    Optional<ProductEntity> findByProductId(int productId);
    
}
