# NewsApp24

This repository contains news service application targeted for cities in the United States, which would range in size from big metropolises to small villages.



## Concept

Prerequisites for this project involved news only for three cities; however, the final goal was to cover approximately 30 thousand locations. Therefore, I was aiming to create a scalable application with the possibility of adding news news automatically on the user's demand. I assumed the data flow would look as follows: at first, news articles sorted by news would be received from the news API for this, I chose the Bing Search News API. To reduce the number of requests, which would lower the price of the API service, the core concept was to store articles in a database. In that case, users who would search news for a news not yet present in the database would make a single request to the API server. After that, received response in the form of news-specific news articles would be saved to the database. Therefore, other users interested in news for the same area would not make calls directly to the API server but rather locally to the database. Records stored in the database would be updated at regular intervals.

## Structure

Code structure is separated into two groups: the first contains infrastructure necessary to manage AWS services, and the second involves a business logic of the application.

### Software

Data flow takes place in the _[software](software)_ directory. Its core is the Spring application, located in _[ElasticBeanstalk](software/ElasticBeanstalk)_, which uses REST architecture to communicate with client side in a non-blocking way with help of the Spring WebFlux framework. The application employs code from _[DataProcessingLibrary](software/DataProcessingLibrary)_, which defines functions for fetching and converting data as well as connecting to the database and saving it there. The reason for such separation was to avoid redundant code since the same functionality was applied in the _[Lambda](software/Lambda)_ module, which performs automatic updates of content stored in the database. Thus, the Spring application serves as an endpoint that validates data with a separate geocoding API and saves it to the database, where it is systematically updated by the Lambda function. Both modules use _[DataProcessingLibrary](software/DataProcessingLibrary)_. 

The application frontend is included in the _[StaticSite](software/StaticSite)_ directory. It was written in React and Redux, which provide a common state for all components. The user interface is simple yet, in my opinion, elegant. Users can choose a news, for which news they are interested in, from a fixed list of propositions ordered by state or type their own location in the search bar, which uses an autocompletion service from the Google Places API. By default, the landing page shows trending news articles for the whole United States.

### Infrastructure

The project required several AWS services to fulfill established goals. Manual deployment for each of them via AWS Console would be a tedious task, which is why I used the AWS Cloud Deployment Kit. This solution is also more universal, as it requires only credentials configuration to be employed on a different account or computer.

The Spring package was deployed as an ElasticBeanstalk application in the _[ElasticBeanstalkStack](infrastructure/src/main/java/com/infrastructure/ElasticBeanstalkStack.java)_ file. ‘Lambda’ directory, which includes a handler for Lambda function, was deployed in _[LambdaStack](infrastructure/src/main/java/com/infrastructure/LambdaStack.java)_. This function was initially set with the Amazon EventBridge rule to update news every day at 12:00 pm, although this schedule can be changed for more current news. The domain for the application website was registered outside of the CDK project with the name _[newsapp24.com](http://newsapp24.com/)_; however, the S3 buckets for website hosting and subdomain, that would redirect users to the root domain from the _www.newsapp24.com_ address, as well as ARecords for root domain and subdomain, were deployed programmatically in in _[StaticSiteStack](infrastructure/src/main/java/com/infrastructure/StaticSiteStack.java)_. API keys were managed by Secrets Manager.

## Additional information
The _[deploy](deploy.sh)_ script in the root directory will automatically commit and push changes to the repository on GitHub and deploy the CDK project. After running it, the user will be prompt to enter the message of a commit; if it is left blank, the message will be set to the current date.