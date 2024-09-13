package com.lambda.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lambda.dto.Article;
import com.lambda.dto.City;

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

        JsonNode value = node.path("value");
        if (value.isArray() && !value.isEmpty()) {
            for (JsonNode valueNode : value) {
                city.addArticle(new Article(
                        getField(valueNode, "name"),
                        getField(valueNode, "url"),
                        getField(valueNode.path("image"), "contentUrl"),
                        getField(valueNode, "description"),
                        getField(valueNode, "datePublished"))
                );
            }
            city.sortArticles();
        }

        if (node.isArray() && !node.isEmpty()) {
            node = node.get(0);
            String name = getField(node, "name");

            String localName = getField(node.path("local_names"), "en");
            if (localName != null) {
                name = localName;
            }

            city.setName(name);
            city.setState(getField(node, "state"));
        }

        return city;
    }

    public String getField(JsonNode node, String field) {
        return node.path(field).asText(null);
    }
}
