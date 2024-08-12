package com.library.service;

import com.library.dto.City;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;


@Service
public class DynamoDBService {
    private final DynamoDbAsyncTable<City> table;

    public DynamoDBService(DynamoDbAsyncTable<City> table) {
        this.table = table;
    }

    public CompletableFuture<City> getNews(String cityName){
        Key key = Key.builder().partitionValue(cityName).build();
        return table.getItem(key);
    }

    public PagePublisher<City> getAllNews(){
        return table.scan();
    }

    public void putNews(City news){
        table.putItem(news);
    }

    public void deleteNews(String cityName){

    }

    public void updateNews(){

    }


    private class CitySubscriber implements Subscriber<Page<City>> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private Subscription subscription;
        private final List<City> itemsFromAllPages = new ArrayList<>();

        @Override
        public void onSubscribe(Subscription sub) {
            subscription = sub;
            subscription.request(1L);
            try {
                latch.await(); // Called by main thread blocking it until latch is released.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onNext(Page<City> productCatalogPage) {
            // 8. Collect all the ProductCatalog instances in the page, then ask the publisher for one more page.
            itemsFromAllPages.addAll(productCatalogPage.items());
            subscription.request(1L);
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public void onComplete() {
            latch.countDown(); // Call by subscription thread; latch releases.
        }

        List<City> getSubscribedItems() {
            return this.itemsFromAllPages;
        }
    }

}


