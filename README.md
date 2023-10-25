# Cheap_Flight_Search

## Introduction

The main purpose of this application is to practice building and development of application based on REST API services with implementation of various tools and integration data from external sources.

Application features:
- multiple searching for cheapest, direct itineraries to user's favourites destinations based on user's annual holiday plan and weather in specified destinations
- managing stored data, including holiday plans, favourite routes, purchased (or not) itineraries
- (*in future*) searching for travel basing on weather data
- (*in future*) scheduling - regular (for example daily) searching for itineraries for choosing cheapest ones
- (*in future*) scheduling - possibility to get email with reminder of purchased itinerary with weather forecast data on arrival day to destination

## Getting started

Application shares view layer implemented in Vaadin framework. Possibility to test it by build and compile code in chosen IDE.

### Requirements

Not deployed on cloud yet. Only local testing possible;

1. MySQL database and user with all privileges as follows:
````
- database name: flights_db
- url: //localhost:3306
- username=passenger_user
- password=Pass123
````
2. Java 17 JDK.

3. IDE with Gradle build tool, such as IntelliJ to compile and run code.

4. REST client, such as Postman to test API endpoints.

5. Favourite browser for checking usability and UX of simplified view layer.

### How to use

1. Clone code from GitHub repository.

2. Set up MySQL local database.

3. Build and run application in your IDE.

4. Test application endpoints by Postman or use it in favourite browser.

### Frontend pages

1. Application **main page**
- http://localhost:8080/main

2. **Airports** managing
- http://localhost:8080/main/airports

3. **Calendar** and vacation plans managing
- http://localhost:8080/main/calendar

4. **User** data managing (panel for Administrator only) - access from main page currently disabled
- http://localhost:8080/main/users

5. **Routes** (and favourite ones) managing
- http://localhost:8080/main/routes

6. **Itineraries** searching, trip plans managing
- http://localhost:8080/main/itineraries

7. **Quick search** for itinerary
- http://localhost:8080/main/quick_search

8. **Sign up** form for new User
- http://localhost:8080/signup

9. **Log in** form for stored User
- http://localhost:8080/login

## Endpoints

All API endpoints documentation is available through Swagger: http://localhost:8080/swagger-ui/ 

### Airports 

#### Get all airports
- **URL**: `/v1/cheapflightsearch/airports`
- **Method**: GET
- **Parameters**: -
- **Response**: 
  - json
    ```
    [  
       {
        "airportId": int,
        "country": String,
        "city": String,
        "iataCode": String
       },
       {
        ...
       }
    ]
    ```
#### Get airport by ID
- **URL**: `/v1/cheapflightsearch/airports/{id}`
- **Method**: GET
- **Parameters**: 
  ```{id} - ID of the airport in database```
- **Response**: 
  - json
    ```
       {
        "airportId": int,
        "country": String,
        "city": String,
        "iataCode": String
       }
    ```
#### Get airport by IATA code
- **URL**: `/v1/cheapflightsearch/airports/iata/{iata_code}`
- **Method**: GET
- **Parameters**:
  ```{iata_code} - IATA code of the airport in database```
- **Response**: 
  - json
    ```
       {
        "airportId": int,
        "country": String,
        "city": String,
        "iataCode": String
       }
    ```
#### Save new airport to database
- **URL**: `/v1/cheapflightsearch/airports`
- **Method**: POST
- **Parameters**: 
  - json
    ```
       {
        "country": String,
        "city": String,
        "iataCode": String
       }
    ```
- **Response**: void

#### Update airport with ID
- **URL**: `/v1/cheapflightsearch/airports/update/{id}`
- **Method**: PUT
- **Parameters**:   
  - ```{id} - ID of the airport to update in database```
  - json
    ```
       {
        "airportId": int,
        "country": String,
        "city": String,
        "iataCode": String
       }
    ```
- **Response**: 
  - json
    ```
       {
        "airportId": int,
        "country": String,
        "city": String,
        "iataCode": String
       }
    ```
#### Update airport with IATA code
- **URL**: `/v1/cheapflightsearch/airports/update/iata/{iata_code}`
- **Method**: PUT
- **Parameters**:
    - ```{iata_code} - - IATA code of the airport to update in database```
    - json
      ```
         {
          "airportId": int,
          "country": String,
          "city": String,
          "iataCode": String
         }
      ```
- **Response**:
    - json
      ```
         {
          "airportId": int,
          "country": String,
          "city": String,
          "iataCode": String
         }
      ```

### Routes

#### Get all routes
- **URL**: `/v1/cheapflightsearch/routes`
- **Method**: GET
- **Parameters**: -
- **Response**:
  - json
    ```
    [
       {
        "daysOfWeek": [
          "FRIDAY"
        ],
        "destination": String,
        "favourite": true,
        "origin": String,
        "routeId": int,
        "userId": int
       },
       {
        ...
       }
    ]
    ```

#### Get route by ID
- **URL**: `/v1/cheapflightsearch/routes/{id}`
- **Method**: GET
- **Parameters**:
  - ```{id} - ID of the route in database```
- **Response**:
  - json
    ```
    {
        "daysOfWeek": [
            "FRIDAY"
        ],
        "destination": String,
        "favourite": true,
        "origin": String,
        "routeId": int,
        "userId": int
    }
    ```

#### Save new route to database
- **URL**: `/v1/cheapflightsearch/routes`
- **Method**: POST
- **Parameters**:
  - json
    ```
    {
        "daysOfWeek": [
           "FRIDAY"
        ],
        "destination": String,
        "favourite": true,
        "origin": String,
        "routeId": int,
        "userId": int
    }
    ```
- **Response**: void

#### Update route with ID
- **URL**: `/v1/cheapflightsearch/routes/update/{id}`
- **Method**: PUT
- **Parameters**:
  - ```{id} - ID of the route to update in database```
  - json
    ```
    {
        "daysOfWeek": [
           "FRIDAY"
        ],
        "destination": String,
        "favourite": true,
        "origin": String,
        "routeId": int,
        "userId": int
    }
    ```
- **Response**:
  - json (updated route)
    ```
    {
        "daysOfWeek": [
           "FRIDAY"
        ],
        "destination": String,
        "favourite": true,
        "origin": String,
        "routeId": int,
        "userId": int
    }
    ```

### Itineraries

#### Get all itineraries
- **URL**: `/v1/cheapflightsearch/itineraries`
- **Method**: GET
- **Parameters**: -
- **Response**:
  - json
    ```
    [  
       {
        "itineraryMark": String,
        "price": double,
        "purchaseLink": String
       },
       {
        ...
       }
    ]
    ```
#### Get itinerary by ID
- **URL**: `/v1/cheapflightsearch/itineraries/{id}`
- **Method**: GET
- **Parameters**:
  - ```{id} - ID of the itinerary in database```
- **Response**:
  - json
    ```
       {
        "itineraryMark": String,
        "price": double,
        "purchaseLink": String
       }
    ```
#### Delete itinerary by ID
- **URL**: `/v1/cheapflightsearch/itineraries/{id}`
- **Method**: DELETE
- **Parameters**:
  - ```{id} - ID of the itinerary in database```
- **Response**: void

#### Update itinerary with ID
- **URL**: `/v1/cheapflightsearch/itineraries/update/{id}`
- **Method**: PUT
- **Parameters**:
  - ```{id} - ID of the itinerary to update in database```
  - json
    ```
       {
        "itineraryMark": String,
        "price": double,
        "purchaseLink": String
       }
    ```
- **Response**:
  - json (updated itinerary)
    ```
       {
        "itineraryMark": String,
        "price": double,
        "purchaseLink": String
       }
    ```
    
#### Get itineraries matching favourite routes and holiday plans
*- for comprehensive testing*

### Users

*- to be completed*

### Weather

*- to be completed*
      
## Features to be implemented

1. **User**
- [x] using application as default User - *developed ADMIN and USER roles, also possible act as Anonymous [2023.08.24]*
- [x] User authentication using Spring Security - *implemented [2023.08.23, 2023.08.31 - with using of DB]*
- [x] managing dataset by Administrator - *implemented [2023.09.20]*

2. **Calendar**
- [ ] full year calendar view with holiday plan visualized on it
- [ ] ability to pick vacation dates on full year calendar in CalendarView

3. **Weather**
- [ ] using more data (ex. humidity, wind force)
- [ ] using forecast data (now only current temperature in specific places)

4. **Better Exceptions handling**
- [ ] when incorrect data input
- [ ] for error responses from external API's

5. **Vaadin** - view layer
- [ ] to split onto frontend and backend separate applications (to consider)
- [ ] development of more responsive UI
- [ ] **QuickSearchView** - origin and destination airport - to enable also choosing from saved airports (and custom IATA code input)
- [x] **ItinerariesView** - to add new feature: searching results based on User's holiday plans and favourite routes - *implemented*

6. Application deploy
- [ ] deploying backend on *replit* (to consider) 
- [ ] deploying database on cloud

7. Skyscanner
- [ ] search results for more actual and correct data from Skyscanner API

8. Scheduled actions
- [ ] it is needed to resolve how every User's personal configuration of scheduled actions (ex. email sending) will be stored and managed
- [ ] scheduled reminder email about upcoming trip - with for example weather data in destination city
- [ ] setting scheduled searching engine feature for finding cheapest flights to purchase
- [ ] scheduled information email with latest prices of favourite itineraries

9. Email creation and management
- [ ] creating SimpleMailMessage with searching results
- [ ] creating SimpleMailMessage with reminder about upcoming (purchased) trips
- [ ] to add email configuration management in application view layer
- [ ] creating MimeMessage instead of SimpleMailMessage to implement buttons with direct url to purchase chosen itinerary

## Troubleshooting

1. ~~There is an issue of using Swagger (swagger-ui) for API endpoints documentation in the project~~ - FIXED
2. ~~Before searching for itineraries in ItineraryView it is needed to set up data by clicking "set airports" button~~ - out of date
3. ~~Not handled exception when User duplicates searching requests for Itineraries~~ - FIXED
4. ~~CalendarView is now useless due to not store current data about User and it's calendar~~ - FIXED
5. ~~CalendarView not connected to authenticated User since Spring Security has been developed~~ - FIXED
6. ~~Routes without relations to User entity - unable to manage this by User~~ - FIXED
7. ~~When searching multiple Itineraries basing on holiday plans and favourite routes, it is unable to get from Itinerary 
information such as origin/ destination/ dates etc.~~ - FIXED (deleted, logic moved to ItineraryView)
8. ~~When adding the same holiday plan, application adds new instead of modify existing or skip changes.~~ - FIXED
9. ~~**SuggestionsView**: If only one flight available (without flight back for example) - this result shouldn't be taken into account.~~ - FIXED (deleted, logic moved to ItineraryView)
10. If a direct flight is unavailable, the search engine provides a route to an intermediate stop. This only happens during the initial search for this route. When refreshing, the entire route with transfers is searched.
11. When using 'quick search', and other input data using there is an issue of changing all 'trip plan data' related with printed itineraries.
12. In RouesView, when adding the same route, it appears instead of overwrite previous one.