package by.ilya.microservices.core.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import by.ilya.api.core.product.Product;
import by.ilya.api.core.product.ProductService;
import by.ilya.microservices.core.product.persistence.ProductEntity;
import by.ilya.microservices.core.product.persistence.ProductRepository;
import by.ilya.util.exceptions.InvalidInputException;
import by.ilya.util.exceptions.NotFoundException;
import by.ilya.util.http.ServiceUtil;

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
        this.productRepository = repository;
        this.mapper = mapper;
    }

    @Override
    public Product getProduct(int productId) {
        LOG.debug("/product return the found product for productId={}", productId);

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        ProductEntity productEntity = this.productRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
        Product product = this.mapper.entityToApi(productEntity);
        product.setServiceAddress(this.serviceUtil.getServiceAddress());
        return product;
    }

    @Override
    public Product createProduct(Product product) {
        try{
            LOG.info("create PROduct INFO");
            LOG.debug("create PROduct DEBUG");
            LOG.warn("create PROduct warn");
            ProductEntity entity = this.mapper.apiToEntity(product);
            ProductEntity newEntity = this.productRepository.save(entity);
            return mapper.entityToApi(newEntity);
        } catch(DuplicateKeyException exception){
            throw new InvalidInputException("Duplicate key, Product ID:" + product.getProductId());
        }
    }

    @Override
    public void deleteProduct(int productId) {
        this.productRepository.findByProductId(productId).ifPresent(this.productRepository::delete);
    }
}