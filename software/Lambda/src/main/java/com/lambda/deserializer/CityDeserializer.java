package com.lambda.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lambda.dto.Article;
import com.lambda.dto.City;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            if (originalQuery.length > 0) {
                city.setName(originalQuery[0]);
            }
            if (originalQuery.length > 1) {
                city.setState(originalQuery[1]);
            }
            JsonNode value = node.path("value");
            if (value.isArray()) {

                ArrayList<Article> articles = new ArrayList<>();
                for (JsonNode valueNode : value) {
                    articles.add(new Article(
                            getField(valueNode, "name"),
                            getField(valueNode, "url"),
                            getField(valueNode.path("image"), "contentUrl"),
                            getField(valueNode, "description"),
                            getField(valueNode, "datePublished"))
                    );
                }
                List<Article> list = articles.stream().sorted().toList();
            }
            return city;
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
