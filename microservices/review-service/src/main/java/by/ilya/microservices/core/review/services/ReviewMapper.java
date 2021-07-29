package by.ilya.microservices.core.review.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.ilya.api.core.review.Review;
import by.ilya.microservices.core.review.persistence.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    
    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)        
    })
    Review entityToApi(ReviewEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    ReviewEntity apiToEntity(Review review);

    List<Review> entityListToApiList(List<ReviewEntity> entities);
    List<ReviewEntity> apitListToEntityList(List<Review> reviews);
    
}
