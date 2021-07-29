package by.ilya.microservices.core.recommendation.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.ilya.api.core.recommendation.Recommendation;
import by.ilya.microservices.core.recommendation.persistence.RecommendationEntity;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {
    
    @Mappings({
        @Mapping(target = "rate", source = "entity.rating"),
        @Mapping(target = "serviceAddress", ignore = true)        
    })
    Recommendation entityToApi(RecommendationEntity entity);

    @Mappings({
        @Mapping(target = "rating", source="rate"),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    RecommendationEntity apiToEntity(Recommendation recommendation);

    List<Recommendation> entityListToApiList(List<RecommendationEntity> entities);
    List<RecommendationEntity> apitListToEntityList(List<Recommendation> recommendations);
    
}
