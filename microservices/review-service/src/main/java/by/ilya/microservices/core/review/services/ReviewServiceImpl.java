package by.ilya.microservices.core.review.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import by.ilya.api.core.review.Review;
import by.ilya.api.core.review.ReviewService;
import by.ilya.microservices.core.review.persistence.ReviewEntity;
import by.ilya.microservices.core.review.persistence.ReviewRepository;
import by.ilya.util.exceptions.InvalidInputException;
import by.ilya.util.http.ServiceUtil;

import java.util.List;

@RestController
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ReviewMapper mapper;
    private final ReviewRepository repository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<Review> getReviews(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        List<ReviewEntity> reviewEntities = this.repository.findByProductId(productId);   
        List<Review> reviews =  this.mapper.entityListToApiList(reviewEntities);
        reviews.forEach(r -> r.setServiceAddress(this.serviceUtil.getServiceAddress()));

        LOG.debug("/Review response size: {}", reviews.size());

        return reviews;

    }

    @Override
    public Review createReview(Review review) {
        try{
            ReviewEntity entity = this.mapper.apiToEntity(review);
            ReviewEntity newEntity = this.repository.save(entity);
            return mapper.entityToApi(newEntity);
        } catch(DuplicateKeyException exception){
            throw new InvalidInputException("Duplicate key, Review ID:" + review.getReviewId());
        }
    }

    @Override
    public void deleteReviews(int productId) {
        this.repository.deleteAll(this.repository.findByProductId(productId));
    }
}
