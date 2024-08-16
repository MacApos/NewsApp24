package com.library.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.library.dto.Article;
import com.library.dto.City;

import java.io.IOException;

public class CityDeserializer extends StdDeserializer<City> {
    public CityDeserializer() {
        this(null);
    }

    protected CityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public City deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        City city = new City();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode queryContext = node.get("queryContext");

        if (queryContext != null) {
            String[] originalQuery = queryContext.path("originalQuery").asText("")
                    .trim().split(" *, *");
            if (originalQuery.length >= 1) {
                city.setName(originalQuery[0]);
            }
            if (originalQuery.length >= 2) {
                city.setState(originalQuery[1]);
            }
            JsonNode value = node.path("value");
            if (value.isArray()) {
                for (JsonNode valueNode : value) {
                    city.addArticle(new Article(
                            getField(valueNode, "name"),
                            getField(valueNode, "url"),
                            getField(valueNode, "image"),
                            getField(valueNode, "description"),
                            getField(valueNode, "datePublished"))
                    );
                }

            }
            return city;
        }

        if (!node.isEmpty()) {
            node = node.get(0);
            city.setName(node.path("name").asText(null));
            city.setState(node.path("state").asText(null));
        }

        return city;
    }

    public String getField(JsonNode node, String field) {
        return node.path(field).asText(null);
    }
}
