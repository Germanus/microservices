package by.ilya.microservices.composite.product.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import by.ilya.api.core.product.Product;
import by.ilya.api.core.product.ProductService;
import by.ilya.api.core.recommendation.Recommendation;
import by.ilya.api.core.recommendation.RecommendationService;
import by.ilya.api.core.review.Review;
import by.ilya.api.core.review.ReviewService;
import by.ilya.util.exceptions.InvalidInputException;
import by.ilya.util.exceptions.NotFoundException;
import by.ilya.util.http.HttpErrorInfo;

import static org.springframework.http.HttpMethod.GET;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
        RestTemplate restTemplate, ObjectMapper mapper,
        @Value("${app.product-service.host}") String productServiceHost,
        @Value("${app.product-service.port}") int    productServicePort,

        @Value("${app.recommendation-service.host}") String recommendationServiceHost,
        @Value("${app.recommendation-service.port}") int    recommendationServicePort,

        @Value("${app.review-service.host}") String reviewServiceHost,
        @Value("${app.review-service.port}") int    reviewServicePort){

        this.restTemplate = restTemplate;
        this.mapper = mapper;    
        productServiceUrl        = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        reviewServiceUrl         = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";

    }     


    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + productId;

            LOG.debug("Will call getReviews API on URL: {}", url);
            List<Review> reviews = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {}).getBody();

            LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try {
            String url = recommendationServiceUrl + productId;

            LOG.debug("Will call getRecommendations API on URL: {}", url);
            List<Recommendation> recommendations = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {}).getBody();

            LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
            return recommendations;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Product getProduct(int productId) {
        try {
            String url = productServiceUrl + productId;
            LOG.debug("Will call getProduct API on URL: {}", url);

            Product product = restTemplate.getForObject(url, Product.class);
            LOG.debug("Found a product with id: {}", product.getProductId());

            return product;

        } catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {
                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY :
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }
    }

    @Override
    public Review createReview(Review review) {
        return null;
    }


    @Override
    public void deleteReviews(int productId) {
        
    }


    @Override
    public Recommendation createRecommendation(Recommendation body) {
        return null;
    }


    @Override
    public void deleteRecommendations(int productId) {
        
    }


    @Override
    public Product createProduct(Product body) {
        try {
            return this.restTemplate.postForObject(productServiceUrl, body, Product.class);
        } catch (HttpClientErrorException  ex) {
            throw handleHttpClientException(ex);
        }        
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            restTemplate.delete(productServiceUrl + "/" + productId);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()){
            case NOT_FOUND: return new NotFoundException(getErrorMessage(ex));
            case UNPROCESSABLE_ENTITY:  return new InvalidInputException(getErrorMessage(ex));
            default:
                LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }


    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
    
}
