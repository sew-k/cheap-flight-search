# Introduction

Application based on REST API enables:
- multiple searching for cheapest, direct itirenaries to user's favourites destinations based on user's annual holiday plan and weather in specified destinations
- managing stored data, including holiday plans, favourite routes, purchased (or not) itineraries
- (*in future*) searching for travel basing on weather data
- (*in future*) scheduling - regular (for example daily) searching for itineraries for choosing cheapest ones
- (*in future*) scheduling - possibility to get email with reminder of purchased itirenary with weather forecast data on arrival day to destination

# Getting started

Application shares view layer implemented in Vaadin framework. Possibility to test it by build and compile code in chosen IDE.

## Requirements

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

## How to use

1. Clone code from GitHub repository

2. Set up MySQL local database

3. Build and run application in your IDE

## Frontend pages

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

# Endpoints

1. User


# Features to be implemented

1. User
- using application as default User
- User authentication using Spring Security
- managing dataset by Administrator

2. Calendar
- full year calendar view with holiday plan visualized on it
- ability to pick vacation dates on full year calendar in CalendarView

3. Weather
- using more data (ex. humidity, wind force)
- using forecast data (now only current temperature in specific places)

4. Better Exceptions handling
- when incorrect data input
- for error responses from external API's

4. Vaadin - view layer
- to split into frontend and backend separate applications (to consider)
- development of more responsive UI

5. Deploying backend (replit) and database (cloud)

6. Skyscanner
- search results for more actual and correct data from Skyscanner API

# Troubleshooting

1. There is an issue of using Swagger (swagger-ui) for API endpoints documentation in the project - not fixed yet

2. Before searching for itineraries in ItineraryView it is needed to set up data by clicking "set airports" button

3. Not handled exception when User duplicates searching requests for Itineraries

