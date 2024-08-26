package com.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class Secrets {
    public final String NEWS_API_KEY = "";
    public final String CITY_API_KEY = "";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Secrets getSecret() {
        String secretName = "com/lambda";
        Region region = Region.EU_CENTRAL_1;

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build()) {

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse getSecretValueResponse;

            try {
                getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
                return objectMapper.readValue(getSecretValueResponse.secretString(), Secrets.class);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
