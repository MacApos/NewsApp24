//package com.lambda;
//
//import com.library.dto.City;
//import com.library.service.DynamoDBService;
//import com.library.service.FetchDataService;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
//
//import java.util.ArrayList;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@SpringBootApplication (scanBasePackages = {"example", "com.library"})
//public class FunctionConfiguration {
//	private final FetchDataService fetchDataService;
//	private final DynamoDBService dynamoDbService;
//
//	public FunctionConfiguration(FetchDataService fetchDataService, DynamoDBService dynamoDbService) {
//		this.fetchDataService = fetchDataService;
//		this.dynamoDbService = dynamoDbService;
//	}
//
//    /*
//	 * You need this main method (empty) or explicit <start-class>example.FunctionConfiguration</start-class>
//	 * in the POM to ensure boot plug-in makes the correct entry
//	 */
//	public static void main(String[] args) {
//		// empty unless using Custom runtime at which point it should include
////		 SpringApplication.run(FunctionConfiguration.class, args);
//	}
//
//	@Bean
//	public Function<String, String> uppercase() {
//		ArrayList<City> cities = new ArrayList<>();
//		PagePublisher<City> allNews = dynamoDbService.getAllNews();
//		allNews.items().subscribe(cities::add);
//		String names = cities.stream().map(City::getName).collect(Collectors.joining(", "));
//		return value -> {
//			if (value.equals("exception")) {
//				throw new RuntimeException("Intentional exception");
//			}
//			else {
//				return names;
//			}
//		};
//	}
//}
