package com.library.mapper;

import com.library.dto.Answer;
import com.library.dto.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring", uses = ArticleMapper.class)
public interface CityMapper {
    @Mapping(source = "queryContext", target = "name", qualifiedByName = "queryToName")
    @Mapping(source = "queryContext", target = "state", qualifiedByName = "queryToState")
    @Mapping(source = "value", target = "articles")
    City answerToCity(Answer answer);

    @Named("queryToName")
    static String queryToName(Map<String, String> queryContext) {
        return queryContext.get("originalQuery").split(",")[0];
    }

    @Named("queryToState")
    static String queryToState(Map<String, String> queryContext) {
        return queryContext.get("originalQuery").split(",")[1];
    }
}
