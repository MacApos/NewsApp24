package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.News;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class FetchDataService {
    public static final HttpClient httpClient = HttpClient.newBuilder().build();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final SecretsService SECRETS_SERVICE = SecretsService.getSecrets();

    public static final String NEWS_HOST = "api.bing.microsoft.com";
    public static final String NEWS_PATH = "/v7.0/news/search";
    public static final String NEWS_API_KEY = SECRETS_SERVICE.NEWS_API_KEY;
    public static final HashMap<String, String> NEWS_API_URI_PARAMS = new HashMap<>(Map.of(
            "q", "",
            "count", "25",
            "mkt", "en-US",
            "originalImg", "true",
            "setLang", "en-US",
            "sortBy", "Relevance"
    ));

    public static final HashMap<String, String> NEWS_API_URI_HEADERS = new HashMap<>(Map.of("Ocp-Apim-Subscription-Key", NEWS_API_KEY));
    public static final String TRENDING = "TRENDING";

    private final WebClient webClient;

    public FetchDataService(WebClient webClient) {
        this.webClient = webClient;
    }

    public HttpRequest prepareRequest(String host, String path, Map<String, String> params,
                                      Map<String, String> headers) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path);

        String query = "";
        if (params != null) {
            params.forEach(uriComponentsBuilder::queryParam);
        }

        URI uri1 = uriComponentsBuilder.build().toUri();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient.get().uri(uri1);

        URI uri;
        try {
            uri = new URI("https", null, host, -1, path, query, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder().uri(uri);
        if (headers != null) {
            headers.forEach(httpRequestBuilder::header);
            headers.forEach(requestHeadersSpec::header);
        }
        requestHeadersSpec.retrieve().bodyToFlux(Object.class).next().block();
        return httpRequestBuilder.build();
    }

    public Mono<News> prepareResponse(String host, String path, Map<String, String> params,
                                      Map<String, String> headers, boolean fakeData) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path);

        if (params != null) {
            params.forEach(uriComponentsBuilder::queryParam);
        }
        String uriString = uriComponentsBuilder
                .build()
                .toUriString();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient.get()
                .uri(uriComponentsBuilder
                        .build()
                        .toUriString());
        if (headers != null) {
            headers.forEach(requestHeadersSpec::header);
        }

        String fakeNews = " {\"_type\": \"News\", \"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/news\\/search?q=New-York%2cNew-York\", \"queryContext\": {\"originalQuery\": \"New-York,New-York\", \"adultIntent\": false}, \"totalEstimatedMatches\": 1320, \"sort\": [{\"name\": \"Najlepsze dopasowanie\", \"id\": \"relevance\", \"isSelected\": true, \"url\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/news\\/search?q=New-York%2cNew-York\"}, {\"name\": \"Najnowsze\", \"id\": \"date\", \"isSelected\": false, \"url\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/news\\/search?q=New-York%2cNew-York&sortby=date\"}], \"value\": [{\"name\": \"What Harris or Trump Would Mean for New York’s Housing and Transit\", \"url\": \"https:\\/\\/www.msn.com\\/en-us\\/news\\/politics\\/what-harris-or-trump-would-mean-for-new-york-s-housing-and-transit\\/ar-AA1ssim1\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.ReBfO0taJT6iCWrN6AEqEC&pid=News\", \"width\": 700, \"height\": 466}}, \"description\": \"Help to buy and build housing are core planks for the Democratic nominee, while the Republican has vowed to kill driver congestion fees the MTA is counting on.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/db72b52a-588b-5610-9583-7a7fd86e087e\", \"name\": \"New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/1a466af2-ed23-25bd-794d-1ca925e4681b\", \"name\": \"Donald Trump\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/ef5cf66f-32b7-7271-286a-8e8313eda5c5\", \"name\": \"Kamala Harris\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/60d5dc2b-c915-460b-b722-c9e3485499ca\", \"name\": \"New York City\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"THE CITY on MSN.com\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.D5rSRbxxtGc7S-F66IlfEg&pid=news\"}}}], \"datePublished\": \"2024-10-17T18:47:00.0000000Z\"}, {\"name\": \"This East Meadow Bagel Shop Among Top 100 In NY, New Ranking Says\", \"url\": \"https:\\/\\/www.msn.com\\/en-us\\/news\\/us\\/these-bagel-shops-among-top-100-in-ny-new-ranking-says\\/ar-AA1snoQM\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.J_Fcx5jluJNV3XWZIYejfC&pid=News\", \"width\": 700, \"height\": 350}}, \"description\": \"Whether for breakfast, lunch, or dinner, these bagels are the cream (cheese?) of the crop.Nearly three dozen businesses outside of New York City made the “Top 100 Bagel Shops in New York 2024” ranking by Yelp.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/41257565-485b-7852-4700-1981c31f3f74\", \"name\": \"East Meadow, New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/60d5dc2b-c915-460b-b722-c9e3485499ca\", \"name\": \"New York City\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/59f181ac-51aa-7a01-f2b7-dc46121798bf\", \"name\": \"Bagel and cream cheese\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/caeb7b9a-f5d7-4686-8fb5-cf7628296b13\", \"name\": \"New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/22085aea-f5d7-91fc-2c9f-76299629eef7\", \"name\": \"Yelp\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/48013d35-ec8f-6422-c32c-604424ddba99\", \"name\": \"Pandemic: Covid-19\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/065e0a42-5ed0-8916-583e-08112979d729\", \"name\": \"Sesame\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/07c38b41-e377-7534-dacb-74f22d5ec911\", \"name\": \"Bulgogi\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/6681da68-8d93-a00c-6f48-a32fafc38e21\", \"name\": \"Kimchi\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"Daily Voice Westchester County NY on MSN.com\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.CoDeul8pmU1Uw2EQvtIcFA&pid=news\"}}}], \"datePublished\": \"2024-10-17T14:30:00.0000000Z\"}, {\"name\": \"Upstate New York set for stunning peak fall foliage this weekend\", \"url\": \"https:\\/\\/www.fingerlakes1.com\\/2024\\/10\\/17\\/upstate-new-york-set-for-stunning-peak-fall-foliage-this-weekend\\/\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.6O5Ev5s0XJYfR_OM4Sg28i&pid=News\", \"width\": 700, \"height\": 446}}, \"description\": \"Upstate New York is primed for a spectacular weekend of peak fall foliage, with vibrant colors expected across all regions. According to the latest I LOVE NY Fall Foliage Report, peak conditions will be visible from October 16 to 22,\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/6dff6f82-a90f-b48d-01fd-0020828d8fbe\", \"name\": \"Upstate New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/df86621b-a0a3-c379-f34d-e5e62e6d23da\", \"name\": \"Cayuga County, New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/04830e08-777c-1281-ac37-f798858f5daa\", \"name\": \"Auburn, New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/e264ecc5-e173-0c85-4f2e-e5295ec829c7\", \"name\": \"Cortland County, New York\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"fingerlakes1\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.6Mf5UTpa6QHMwL8MqS54Vw&pid=news\"}}}], \"datePublished\": \"2024-10-17T14:05:00.0000000Z\"}, {\"name\": \"New York Fed’s Latest Tool Shows Bank Reserves Still Abundant\", \"url\": \"https:\\/\\/www.bloomberg.com\\/news\\/articles\\/2024-10-17\\/new-york-fed-s-latest-tool-shows-bank-reserves-still-abundant\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT._bcLQKNAFrRCRbrSi9VdWS&pid=News\", \"width\": 700, \"height\": 466}}, \"description\": \"The amount of reserves underlying the US banking system remains plentiful, according to a Federal Reserve Bank of New York gauge that will be published monthly.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/c5ad466d-fd68-f389-4727-7f3b9daaebf6\", \"name\": \"Bloomberg L.P.\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/50667142-16cc-4ff3-8749-e04c18be437c\", \"name\": \"Federal Reserve Bank of New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/8c59a0ba-0021-cfdd-b696-d3192600ac6c\", \"name\": \"Wall Street\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/8d504280-8e5f-99e5-4688-5e3b38e4c30d\", \"name\": \"Federal Reserve\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/e462b1a6-0644-0686-3192-eb1772fb9b9b\", \"name\": \"Qt\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"Bloomberg L.P.\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.yAnAsiiCHte5V2il_I5mug&pid=news\"}}}], \"datePublished\": \"2024-10-17T14:00:00.0000000Z\"}, {\"name\": \"New York Comic Con 2024: Show hours, guests, ticket info and more\", \"url\": \"https:\\/\\/www.msn.com\\/en-us\\/travel\\/article\\/new-york-comic-con-2024-show-hours-guests-ticket-info-and-more\\/ar-AA1sqKRl\", \"description\": \"Costumes, massive exhibits, and thousands of characters wandering through the Javits Center in NYC only mean one thing: Comic Con is back! Here's what you need to know.\", \"provider\": [{\"_type\": \"Organization\", \"name\": \"FOX 5 New York on MSN.com\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.A_wgay6nFgfB-P7rnmXtOA&pid=news\"}}}], \"datePublished\": \"2024-10-17T11:12:00.0000000Z\"}, {\"name\": \"New York Giants Week 7: A Look at the Philadelphia Eagles Defense\", \"url\": \"https:\\/\\/www.msn.com\\/en-us\\/sports\\/nfl\\/new-york-giants-week-7-a-look-at-the-philadelphia-eagles-defense\\/ar-AA1srOac\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.HlwyqjwBTgMu38Y4E1FnNS&pid=News\", \"width\": 700, \"height\": 393}}, \"description\": \"The New York Giants offense has struggled mightily so far this season with the fourth-lowest scoring offense in the NFL right now and are set to face their division rival Philadelphia Eagles, who allow the 19th-most points.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/6e010b75-103f-1cf6-0c78-acf6527945af\", \"name\": \"Philadelphia Eagles\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/c585f0f2-498b-4cbf-0a33-5c5455aee098\", \"name\": \"New York Giants\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/04320aa0-e19d-4582-9be5-c7a0ba3af2d3\", \"name\": \"Vic Fangio\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/7688f870-363e-e756-7aad-0ed71972cbf6\", \"name\": \"Josh Sweat\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/42019e63-56b1-5649-750c-513d46ae428c\", \"name\": \"Dexter Lawrence\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/193e64bc-f7b0-6812-9cc6-fc52a57d7ef0\", \"name\": \"Brian Burns\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/ab2ae02d-858b-78ac-9464-2618c8014d23\", \"name\": \"New York Jets\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"New York Giants On SI on MSN.com\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.DAlg1K6d6FcCd2ockBPdcQ&pid=news\"}}}], \"datePublished\": \"2024-10-17T20:00:02.0000000Z\"}, {\"name\": \"Yankees vs. Guardians score, live updates: New York looks for 3-0 lead as series shifts to Cleveland for ALCS Game 3\", \"url\": \"https:\\/\\/sports.yahoo.com\\/live\\/yankees-vs-guardians-score-live-updates-highlights-new-york-cleveland-alcs-game-3-200042654.html\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.DURepGwsZCh2sglCZhD9Bi&pid=News\", \"width\": 700, \"height\": 466}}, \"description\": \"The American League Championship Series heads to Cleveland with the New York Yankees up 2-0 on the Cleveland Guardians after their big bats jumped all over the AL Central champs in the first two games in The Bronx.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/3a27ba26-cc96-dd69-15ea-0d248f25b924\", \"name\": \"2001 American League Championship Series\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/34f268cb-0019-fa41-d7e4-7fe472f43c04\", \"name\": \"New York Yankees\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/de315c8b-e1b6-27fb-021e-778bfa85ca81\", \"name\": \"Cleveland Guardians\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/c720ea78-152a-eacb-71e0-a261a22551b7\", \"name\": \"American League Central\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/7df5a4fa-bd7b-afb1-8d15-ee3b522a9231\", \"name\": \"The Bronx\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/2de2b021-df60-c59d-3ae8-93c3a367ef82\", \"name\": \"Giancarlo Stanton\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/87b16b46-ee48-6f3d-1dad-d35544ef6ed9\", \"name\": \"Juan Soto\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/3bf7655a-151a-4c2f-bcc3-37954b331e80\", \"name\": \"Aaron Judge\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/13a1e4cf-f342-4c9b-cbae-de93d568bf50\", \"name\": \"World Series\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/7972934a-c131-9afe-ead3-35787c054960\", \"name\": \"Progressive Field\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/3adc71b7-92e1-751b-e47e-e2bee9a5170f\", \"name\": \"Detroit Tigers\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"Yahoo! Sports\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.S719DbZoJh4YKmcI5HTI3A&pid=news\"}}}], \"datePublished\": \"2024-10-17T20:03:00.0000000Z\", \"video\": {\"name\": \"Yankees vs. Guardians score, live updates: New York looks for 3-0 lead as series shifts to Cleveland for ALCS Game 3\", \"thumbnailUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVF.bf%2BNqLLIjp%2FQXJTjqoq1uw&pid=News\", \"thumbnail\": {\"width\": 520, \"height\": 346}}}, {\"name\": \"Dziennikarka \\\"New York Times'a\\\" miażdży książkę Melanii Trump\", \"url\": \"https:\\/\\/www.msn.com\\/pl-pl\\/wiadomosci\\/polska\\/dziennikarka-new-york-times-a-mia%C5%BCd%C5%BCy-ksi%C4%85%C5%BCk%C4%99-melanii-trump\\/ar-AA1rRUSB\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.TO56YKhY9uUVnFhALSVX7C&pid=News\", \"width\": 700, \"height\": 393}}, \"description\": \"\\\"Jeśli w »Melanii« jest jakaś szczera prawda, to jest nią to, że kocha swojego syna Barrona i będzie go chronić za wszelką cenę (...)\\\" — ocenia autobiografię byłej pierwszej damy Alexandra Jacobs.\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/614b60a2-4ccd-3ecb-15dc-6e2fba54ed28\", \"name\": \"The New York Times\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/6f7c7020-66da-2934-a3bb-190380654848\", \"name\": \"Melania Trump\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/7e91ee1d-e3a3-3844-a60e-4bec4e455b04\", \"name\": \"Camel\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"MSN\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.Y_NBXQC-7-xdFa0P1xXxXw&pid=news\"}}}], \"datePublished\": \"2024-10-08T09:53:00.0000000Z\"}, {\"name\": \"Meet the two new rescued sea otters at the New York Aquarium\", \"url\": \"https:\\/\\/www.msn.com\\/en-us\\/travel\\/news\\/meet-the-two-new-rescued-sea-otters-at-the-new-york-aquarium\\/ar-AA1srOVm\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.ZExE31C9yt09Qi51ahxJKy&pid=News\", \"width\": 700, \"height\": 521}}, \"description\": \"Move over Moo Deng, New York has two new superstar animals.  Two rescued otters have recently made their way to the New York Aquarium in Coney Island, and as of\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/6536fcc1-ba1e-dd01-46c4-f333275e51b2\", \"name\": \"Coney Island\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/424dcc23-810f-1f93-5c6c-084ec2611bd5\", \"name\": \"Brooklyn\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/ca2602dd-ba8c-e10f-533e-3deeeeeb6e0e\", \"name\": \"New York Aquarium\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/3009d91d-d582-4c34-85ba-772ba09e5be1\", \"name\": \"California\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/caeb7b9a-f5d7-4686-8fb5-cf7628296b13\", \"name\": \"New York\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/e0c39cd7-2af9-b368-bc80-e19cff19854d\", \"name\": \"Wildlife Conservation Society\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/eaa441e6-3d87-6680-5bd7-0da870ef44a6\", \"name\": \"Sea otter\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/8ea5091b-acc2-e133-1f80-b3f74547f77c\", \"name\": \"United States Fish and Wildlife Service\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"WPIX New York City, NY on MSN.com\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.GAmChu04jb0oRIHDjXS0wA&pid=news\"}}}], \"datePublished\": \"2024-10-17T14:05:00.0000000Z\", \"video\": {\"name\": \"Meet the two new rescued sea otters at the New York Aquarium\", \"thumbnailUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVF.GW9MibRZOetSj8UU3LRr2Q&pid=News\", \"thumbnail\": {\"width\": 520, \"height\": 387}}}, {\"name\": \"What time is New York Liberty - Minnesota Lynx Game 4? Where to watch it and Schedule for the WNBA Finals 2024\", \"url\": \"https:\\/\\/www.marca.com\\/en\\/basketball\\/wnba\\/2024\\/10\\/17\\/670f4c3846163f827a8b457b.html\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=OVFT.QUXNcf9m7shjQmyjAf-ECC&pid=News\", \"width\": 700, \"height\": 466}}, \"description\": \"The 2024 WNBA Finals will show us with an exciting showdown between the Minnesota Lynx and the New York Liberty. A matchup that promises to put the finishing touch to another magni\", \"about\": [{\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/05ffa160-1727-a7b5-3733-1e3bfe70cd92\", \"name\": \"New York Liberty\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/3b9a54d2-409a-79b5-1bcb-423e3b4706fb\", \"name\": \"Minnesota Lynx\"}, {\"readLink\": \"https:\\/\\/api.bing.microsoft.com\\/api\\/v7\\/entities\\/66dde14a-be74-232f-7d63-21772f29a0df\", \"name\": \"WNBA Finals\"}], \"provider\": [{\"_type\": \"Organization\", \"name\": \"MARCA\", \"image\": {\"thumbnail\": {\"contentUrl\": \"https:\\/\\/www.bing.com\\/th?id=ODF.ToEIHob9aIfZAX_hYYZPtA&pid=news\"}}}], \"datePublished\": \"2024-10-17T19:20:00.0000000Z\"}]}\n";
        Mono<News> cityMono;
        if (fakeData) {
            try {
                News news = objectMapper.readValue(fakeNews, News.class);
                cityMono = Mono.just(news);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            Mono<Object> objectMono = requestHeadersSpec.retrieve().bodyToFlux(Object.class).single();
            cityMono = requestHeadersSpec.retrieve().bodyToFlux(News.class).single();
        }

//        HttpRequest request = prepareRequest(host, path, params, headers);
//        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
//                HttpResponse.BodyHandlers.ofString());
//        Mono<City> cityMono = Mono.fromFuture(response)
//                .map(HttpResponse::body).flatMap(body -> {
//                    City city;
//                    try {
//                        city = objectMapper.readValue(body, City.class);
//                    } catch (JsonProcessingException ex) {
//                        return Mono.error(new RuntimeException(ex));
//                    }
//                    return Mono.just(city);
//                });
        return cityMono;
    }

    public Mono<News> fetchNews(News news) {
        String query;
        if (news.getCityName().equals(TRENDING)) {
            NEWS_API_URI_PARAMS.put("category", "us");
            query = "usa news";
        } else {
            query = news.prepareQuery();
        }
        NEWS_API_URI_PARAMS.put("q", query);
        Mono<News> map = prepareResponse(NEWS_HOST, NEWS_PATH, NEWS_API_URI_PARAMS, NEWS_API_URI_HEADERS, true)
                .map(fetchedCity -> {
                    fetchedCity.setCityName(news.getCityName());
                    fetchedCity.setState(news.getState());
                    return fetchedCity;
                });
        return map;
    }
}
