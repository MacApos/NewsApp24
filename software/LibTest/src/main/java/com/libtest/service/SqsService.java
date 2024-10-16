package com.libtest.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Service
public class SqsService {
    SqsClient sqsClient = SqsClient.builder()
            .region(Region.EU_CENTRAL_1)
            .build();

    public List<Message> receiveMessages() {
        List<Message> messages;
        try {
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName("StandardQueque")
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest
                    .builder()
                    .queueUrl(queueUrl)
                    .build();
            messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
        } catch (QueueNameExistsException e) {
            throw e;
        }
        return messages;
    }
}
