# NewsApp24

This repository contains news service application designed for cities in the United States, which would range in size from big metropolises to small villages.

## Concept

The ultimate goal of this project was to provide news for approximately 30 thousand locations spread across an area of the US, therefore, I was aiming to create a scalable application with the possibility of adding city news automatically on the user's demand. I assumed the data flow would look as follows: at first, news articles sorted by city would be received from the external API; for this, I chose the Bing Search News API. To reduce the number of requests, which would lower the price of the API service, the core concept was to store articles in a database. In that case, users who would search news for a city not yet present in the database would make a single request to the API server. After that, received response in the form of city-specific news articles would be saved to the database. Therefore, other users interested in news for the same area would not make calls directly to the API server but rather locally to the database. Records stored in the database would be updated at regular intervals.

## Structure

Code structure is separated into two groups: the first contains infrastructure necessary to manage AWS services, and the second involves a business logic of the application.

### Software

Data flow takes place in the _[software](software)_ directory. The back-end application is located in _[ElasticBeanstalk](software/ElasticBeanstalk)_. Its core is the Spring JPA module, which simplifies connecting to the database and saving news there. The application uses REST architecture to communicate with the client side in a non-blocking way with help of the Spring WebFlux framework. It also defines functions for validating location names (with a separate geocoding API) as well as fetching and converting data. This directory also involves [cron.yaml](software/ElasticBeanstalk/cron.yaml) file, which declares rule for periodic tasks performed in the ElasticBeanstalk worker environment.

The _[StaticSite](software/StaticSite)_ is a directory of front-end application. It was written in React and Redux, which provide a common state for all components. The user interface is simple yet, in my opinion, elegant. Users can choose a city, for which news they are interested in, from a fixed list of propositions ordered by state or type their own location in the search bar, which uses an autocompletion service from the Google Places API. By default, the landing page shows trending news articles for the whole United States.

### Infrastructure

Services required for this project were managed with Terrarform. It created a Virtual Private Cloud with necessary security groups and subnets. The Spring package, which communicates with front-end application, was deployed in the ElasticBeanstalk web environment. The same source file was used in another worker environment to perform periodic database updates. Back and front-end application files are stored in S3 buckets, and the database was set up through Relational Database Service. The domain for the application website was registered with the name _[newsapp24.com](http://newsapp24.com/)_. Users will also be redirected to the root domain from the _www.newsapp24.com_ address. Appropriate ARecords for both domains were defined in the Route 53 hosted zone. Additionally, the Secrets Manager service was incorporated into the infrastructure to manage all required API keys and other sensitive data.

## Additional information
The _[deploy.sh](deploy.sh)_ script in the root directory will automatically commit and push changes to the repository on GitHub and deploy project. After running it, the user will be prompt to enter the message of a commit; if it is left blank, the message will be set to the current date.