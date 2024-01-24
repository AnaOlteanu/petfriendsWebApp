# PetFriends - web application for connecting pet owners
The final master's project presents the development and implementation of an innovative **web application** named PetFriends, with the main objectives of _connecting pet owners_ and providing _specific services_ in this 
domain. It focuses on key aspects of application design, including system architecture, functionalities and technologies used. 
Furthermore, it explores the field of recommendation systems, leading to the implementation of a **content-based filtering algorithm** for offering personalized event recommendations to platform users.
## Technologies used
- **Spring Framework**
    - **Core Spring** for dependency injection and IoC
    - **Spring MVC** for web development
    - **Spring Data JPA** for data access and validation
    - **Spring Security** for user authentication and role-based access control
    - **Slf4j** for logs
- **Thymeleaf**
    - template engine for front-end development
- **MySQL**
    - relational database for data storage
    - MySQL Workbench as the administration tool for the database
## Key Features
- registration and authentication of a user (3 types: regular user, event organizer and platform administrator)
- newsfeed page with posts from friends
- add and edit a post
- post interaction through like system and comments
- search and follow friends
- signaling and adding pet friendly spaces on the map
- list of petshops filtered by city
- events page (current, future and ended) with details about each event
- event recommendations
- 'join' option for an event
- adding events by the event organizer
- admin privileges (request management coming from users that want to become event organizers, petshop management) 


