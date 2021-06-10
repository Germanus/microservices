package by.ilya.microservices.core.recommendation.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import by.ilya.api.core.recommendation.Recommendation;
import by.ilya.api.core.recommendation.RecommendationService;
import by.ilya.microservices.core.recommendation.persistence.RecommendationEntity;
import by.ilya.microservices.core.recommendation.persistence.RecommendationRepository;
import by.ilya.util.exceptions.InvalidInputException;
import by.ilya.util.http.ServiceUtil;

import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        List<RecommendationEntity> recommendationEntities = this.repository.findByProductId(productId);   
        List<Recommendation> recomendations =  this.mapper.entityListToApiList(recommendationEntities);
        recomendations.forEach(r -> r.setServiceAddress(this.serviceUtil.getServiceAddress()));

        LOG.debug("/recommendation response size: {}", recomendations.size());

        return recomendations;
    }

    @Override
    public Recommendation createRecommendation(Recommendation recommendation) {
        try{
            RecommendationEntity entity = this.mapper.apiToEntity(recommendation);
            RecommendationEntity newEntity = this.repository.save(entity);
            return this.mapper.entityToApi(newEntity);
        } catch(DuplicateKeyException exception){
            throw new InvalidInputException("Duplicate key, Product Id: " + recommendation.getProductId() + ", Review Id:" + recommendation.getRecommendationId());
        }
    }

    @Override
    public void deleteRecommendations(int productId) {
        this.repository.deleteAll(this.repository.findByProductId(productId));
    }
}
