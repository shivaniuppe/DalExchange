# DalExchange

## Overview
DalExchange is a web-based thrift store platform specifically designed for Dalhousie University students. This platform creates a convenient and secure marketplace for buying and selling second-hand items within the student community. By restricting access to Dalhousie students, DalExchange aims to foster a trusted environment where users can confidently exchange goods. Equipped with essential features to ensure a seamless user experience from product listing to payment processing and fulfillment, DalExchange offers a comprehensive solution for student-to-student transactions.

## Tech Stack 
- [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-green)](https://spring.io/projects/spring-boot)
- ![HTML](https://img.shields.io/badge/HTML-orange)
- ![CSS](https://img.shields.io/badge/CSS-blue)
- ![JavaScript](https://img.shields.io/badge/JavaScript-yellow)
- [![React.js](https://img.shields.io/badge/React.js-purple)](https://reactjs.org/)
- [![MySQL](https://img.shields.io/badge/MySQL-yellow)](https://www.mysql.com/)
- [![GitLab](https://img.shields.io/badge/GitLab-blue)](https://about.gitlab.com/)

# Backend Setup

## Prerequisite
- **Java**
- **Spring Boot**
- **Maven**

## Dependencies 
| Dependency Group              | Description                                              | Artifact                       | Version    |
|-------------------------------|----------------------------------------------------------|--------------------------------|------------|
| org.springframework.boot      | Starter for building web, including RESTful, applications| spring-boot-starter-web        |            |
| org.springframework.boot      | Starter for using Spring Data JPA                        | spring-boot-starter-data-jpa   |            |
| org.springframework.boot      | Starter for using Spring Security                        | spring-boot-starter-security   |            |
| org.springframework.boot      | Starter for using Spring Boot's validation support       | spring-boot-starter-validation |            |
| org.springframework.boot      | Starter for using Spring Boot's email support            | spring-boot-starter-mail       |            |
| com.mysql                     | MySQL JDBC driver                                        | mysql-connector-j              |            |
| org.springframework.boot      | Spring Boot dev tools                                    | spring-boot-devtools           |            |
| com.h2database                | H2 in-memory database                                    | h2                             |            |
| org.projectlombok             | Lombok library for reducing boilerplate code             | lombok                         |            |
| jakarta.persistence           | Jakarta Persistence API                                  | jakarta.persistence-api        | 3.1.0      |
| jakarta.validation            | Jakarta Validation API                                   | jakarta.validation-api         | 3.0.2      |
| org.modelmapper               | Object mapping library                                   | modelmapper                    | 3.2.0      |
| org.springframework.boot      | Starter for testing Spring Boot applications             | spring-boot-starter-test       |            |
| junit                         | JUnit testing framework for Java                         | junit                          |            |
| org.springframework.security  | Spring Security Core                                     | spring-security-core           | 6.3.0      |
| org.springframework.security  | Spring Security Web                                      | spring-security-web            | 6.3.0      |
| org.springframework.security  | Spring Security Config                                   | spring-security-config         | 6.3.0      |
| com.cloudinary                | Cloudinary HTTP library                                  | cloudinary-http44              | 1.39.0     |
| io.jsonwebtoken               | Java JWT: JSON Web Token for Java                        | jjwt-api                       | 0.11.5     |
| io.jsonwebtoken               | Implementation of Java JWT                               | jjwt-impl                      | 0.11.5     |
| io.jsonwebtoken               | Jackson support for Java JWT                             | jjwt-jackson                   | 0.11.5     |
| org.mockito                   | Mocking framework for unit tests                         | mockito-core                   |            |
| org.mockito                   | Mockito JUnit Jupiter                                    | mockito-junit-jupiter          |            |
| org.junit.jupiter             | JUnit Jupiter                                            | junit-jupiter                  |            |
| com.stripe                    | Stripe API for Java                                      | stripe-java                    | 20.118.0   |

## Execution
Steps to run the project locally:

### 1. Clone the repository

```bash
git clone https://github.com/shivaniuppe/DalExchange.git
```

### 2. Navigate to backend folder

```bash
Navigate to DalExchange/backend
```

### 3. Build the project

```bash
mvn clean install
```

### 3. Run the application 

```bash
mvn spring-boot:run
```
Finally, the application backend will start running locally.

# Frontend Setup

## Prerequisite
- **NodeJS**
- **npm**

## Dependencies

| Dependency Group                | Description                                       | Artifact                      | Version   |
|---------------------------------|---------------------------------------------------|-------------------------------|-----------|
| @stripe/react-stripe-js         | React components for Stripe integration           | @stripe/react-stripe-js       | 2.7.3    |
| @stripe/stripe-js               | Stripe.js library for the Stripe API               | @stripe/stripe-js             | 4.1.0    |
| @testing-library/jest-dom       | Custom jest matchers to test the state of the DOM  | @testing-library/jest-dom     | 5.17.0   |
| @testing-library/react          | Simple and complete React DOM testing utilities    | @testing-library/react        | 13.4.0   |
| @testing-library/user-event     | Fire events the same way the user does             | @testing-library/user-event   | 13.5.0   |
| axios                           | Promise based HTTP client for the browser and node.js | axios                         | 1.7.2    |
| chart.js                        | Simple yet flexible JavaScript charting library    | chart.js                      | 4.4.3    |
| react                           | JavaScript library for building user interfaces    | react                         | 18.3.1   |
| react-chartjs-2                 | React wrapper for Chart.js                         | react-chartjs-2               | 5.2.0    |
| react-dom                       | React package for working with the DOM             | react-dom                     | 18.3.1   |
| react-hot-toast                 | Smoking hot React notifications                    | react-hot-toast               | 2.4.1    |
| react-router-dom                | Declarative routing for React                      | react-router-dom              | 6.23.1   |
| react-scripts                   | Configuration and scripts for Create React App     | react-scripts                 | 5.0.1    |
| recharts                        | Redefined chart library built with React and D3    | recharts                      | 2.12.7   |
| stripe                          | Stripe API for Node.js                             | stripe                        | 16.5.0   |
| styled-components               | Visual primitives for the component age            | styled-components             | 6.1.12   |
| tailwindcss                     | A utility-first CSS framework                      | tailwindcss                   | 3.4.4    |
| tailwindcss-animate             | Tailwind CSS plugin for animations                 | tailwindcss-animate           | 1.0.7    |

#  ![CI/CD Status](https://img.shields.io/badge/CI/CD-Passing-brightgreen) CI/CD
GitLab CI/CD allows automating the testing, building, and deployment processes for Node.js frontend and SpringBoot Backend using a .gitlab-ci.yml file in the repository root.

![alt text](screenshot/CI_CD.png)


Docker provides a method to publish and deploy the server code onto a virtual machine (VM) Using Below Commands.

## ![Docker](https://img.shields.io/badge/docker-ready-blue) Docker Setup
docker build -t docker.io/<Docker_hub_Repositoy_Id>/backend .

docker push docker.io/<Docker_hub_Repositoy_Id>/backend

docker build -t docker.io/<Docker_hub_Repositoy_Id>/frontend .

docker push docker.io/<Docker_hub_Repositoy_Id>/frontend

docker run -d -p 8080:8080 --name backend docker.io/<Docker_hub_Repositoy_Id>/backend

docker run -d -p 80:80 --name frontend docker.io/<Docker_hub_Repositoy_Id>/frontend


# 🧪 Test
We have used Junit for unit test cases and mockito, following are depdencies we have used for that

```xml
<dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
</dependency>
<dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <scope>test</scope>
</dependency>
<dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <scope>test</scope>
</dependency>
<dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
</dependency>
```


# Coverage
Our current Service(Business Logic) class  line coverage is 100%, method coverage is 100%.

![alt text](screenshot/TestCoverage.png)

# TDD adherence
Throughout the entire project, we strictly followed Test-Driven Development (TDD) methods. From the beginning of each feature until the end of its implementation, we started by creating thorough test cases that outlined the functionality and intended behavior. Every new addition and update was validated against predetermined criteria thanks to this methodical methodology. We attained 100% line coverage as a consequence of our stringent testing procedure, demonstrating the robustness and extensive testing of our software. Our dedication to TDD allowed us to consistently produce high-quality code and enabled the timely identification and fixing of possible problems, which in the end produced software that was more dependable and manageable.

# Test best practices
 We have adhered to recommended practices for testing, which include segmenting the structure, creating brief test cases, and testing one item at a time. and the naming is convincing sufficient to allow viewers to inspect understand what this test case is testing.
# Adherence to SOLID Principles
## Single Responsibility Principle
We have ensured that each class executes single functionality rather than managing mutiple things altogether

![alt text](screenshot/S.png)

## Open/Closed Principle
We have ensured that all the class can be extended by adding new methods for new functionalities without modifying existing code. 

For example, new methods can be added for additional product operations without altering the current methods.

## Liskov Substitution Principle
We have made sure that objects of a superclass is replaceable with objects of a subclass without affecting the functionality of the program.

For example: 
The Mapper interface can have multiple implementations (ProductMapperImpl, ProductModerationMapperImpl), and these implementations can be used interchangeably without affecting the functionality.

![alt text](screenshot/Mapper.png)

## Interface Segregation Principle
Instead of having one large interface, we divided it into multiple smaller, specific-purpose interfaces. This way, clients only need to know about the methods that are of interest to them.

![alt text](screenshot/Interface.png)

## Dependency Inversion Principle

While building the application, we ensured that high-level modules should not depend on low-level modules. Both should depend on abstractions.

Using @Autowired in Spring to inject dependencies allows us to depend on abstractions rather than concrete implementations.

![alt text](screenshot/DIP.png)


# Walkthrough

## Landing page
Via the homepage, you can either login or signup.

![alt text](screenshot/Homepage.png)

## Account Registration
![alt text](screenshot/Registration.png)

Once registered, the user will be asked to verify in order to authenticate.

![alt text](screenshot/VerifyEmail.png)

## Login
User can login via valid credentials used in previous steps.

![alt text](screenshot/Login.png)

## Homepage
Once logged in, all the product listing are displayed along with wide range of filtering options.

![alt text](screenshot/Listing.png)

## Product Details
User can view the preferred product details and also wishlist any product they like.

![alt text](screenshot/ProductDetails.png)

## Trade Request
User can send buy request for the products they like.

![alt text](<screenshot/BuyRequest.png>)

Seller will recieve the request and can accept or reject the proposal.

![alt text](screenshot/SellRequest.png)

## Notification
Users can also view all the requests messages in Notifictions section.

![alt text](screenshot/Notifications.png)

## Payment Transaction
Once the seller accept the proposal, the user has to enter the shipping address.

![alt text](screenshot/OrderShippingAddress.png)

 The customer can initiate a payment using Stripe payment gateway.

![alt text](screenshot/StripeCheckout.png)

## Rating/Review
Once the payment is successful. the user can leave a review/rating according to their convenience.

![alt text](screenshot/SaveRatingOfTheProdcut.png)

## Add Product
Users can also sell their own products with easy to add form.

![alt text](screenshot/AddProduct.png)

## Profile Page
Using profile section, user can view wishlisted products, purchase history, items sold by them and also the reviews published.

![alt text](screenshot/ProfilePage.png)

**1) Edit Profile:**
![alt text](screenshot/EditProfile.png)

**2) Wishlist:**
![alt text](screenshot/Wishlist.png)

**3) Purchase History:**
![alt text](screenshot/PurchaseHistory.png)

**4) Sold Items:**
![alt text](screenshot/SoldItems.png)

**5) Reviews:**
![alt text](screenshot/Reviews.png)

# Admin Moderation
Admin can edit all the essential details and act as a customer support to help users out with their queries and also to check overall statistics.

![alt text](screenshot/Sidebar.png)

## Admin Dashboard
Admin can view comprehensive statistics, compare metrics between the current and previous months, and analyze trends for better decision-making.

![alt text](screenshot/AdminDashboard.png)

## Order Moderation
All the orders can be viewed at once and be edited whenever needed.

![alt text](screenshot/OrderModeration.png)

All the necessary fields can be edited and also the admin can refund a order in case of a scam.

![alt text](<screenshot/Order11.png>)

## User Moderation
Admin can view all the users and edit any details if required.

![alt text](screenshot/UserModeration.png)

Admin can also delete and deactivate a certain account if it doesn't comply with the rules.

![alt text](screenshot/UserEdit.png)

## Product Moderation
Admin can view all the products (including unlisted ones).

![alt text](screenshot/ProductModeration.png)

Admin can edit any details and also unlist any product which doesn't comply with company guidelines.

![alt text](screenshot/ProductEdit.png)

## Feedback Moderation
Admin can view all feedback segregated based on products.

![alt text](screenshot/FeedbackModeration.png)

Admin cannot edit the feedback, but can delete it if the comment is offensive.

![alt text](screenshot/Feedback.png)
