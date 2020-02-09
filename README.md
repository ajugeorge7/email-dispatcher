## Email Dispatcher Application

This application provides an abstraction between two different email service providers. 
If one of the services goes down, it failover to a different provider without affecting the customers.

It uses the following email service providers:
* [Mailgun](https://www.mailgun.com/)
* [SendGrid](https://sendgrid.com/) 

### Getting Started

These instructions will get you a copy of the project up and running on your local machine for testing purposes.

#### Prerequisites

##### Compiling and running the code from intelliJ

* Make sure you have installed **intelliJ Lombok Plugin** to avoid compilation issues from IDE.
    - Instructions to install the plugin in intelliJ IDE can be found [here](https://projectlombok.org/setup/intellij)

##### Compiling and running the application from terminal

* JRE 1.8
* Works on Linux, Windows and Mac OSX.

### Building the application

The quick way to build the application is by using `mvnw` script in the project folder

```
$ ./mvnw clean install
```

### Running the application

After building the application use the following to run the application
```
$ java -jar target/email-dispatcher-1.0-SNAPSHOT.jar
```

### Testing the API

This project uses the Swagger2 UI which provides the API documentation as well a handy way to test the API.

Once the application is running, launch the following URL in a browser to view the APIs for testing

```
http://localhost:8080/swagger-ui.html
```

The RESTful API used for sending the email is,

* POST /api/v1/email - Sends the email based on the request payload.

#### POST /api/v1/email

Sends the email based on the request payload.

Simple request payload:
```
{
    "from": "ajugeorge@gmail.com",
    "toList":[
        "booking@gmail.com"
    ],
    "subject": "Booking confirmation for a hotel",
    "body": "Please book the hotel for the following dates"
}
```

Payload using the CCs and BCCs list of email addresses:

```
{
    "from": "ajugeorge@gmail.com",
    "toList":[
        "booking@gmail.com"
    ],
    "ccList":[
        "booking_cc@gmail.com"
    ],
    "bccList":[
        "booking_bcc@gmail.com"
    ],
    "subject": "Booking confirmation for a hotel",
    "body": "Please book the hotel for the following dates"
}
```

### API Reference

Once the application is running, use the following URL to read the API reference document

```
http://localhost:8080/swagger-ui.html
```

### Configuration Properties

#### For REST API calls to the email service providers
The application uses a `RestTemplate` to connect to the email service providers APIs. 
The following properties are used to manage the connection timeout and read timeout between those APIs.

| Property    | Default  | Description |
|-------------|----------|-------------|
| http.email.gateway.connectTimeout | 5000 ms | The timeout set for the connection to email service provider APIs. |
| http.email.gateway.readTimeout  | 30000 ms | The timeout set for the read using email service provider APIs. |

#### For Mailgun API

| Property    | Description |
|-------------|-------------|
| email.mailGun.api.url | The url for making REST calls with the Mailgun API |
| email.mailGun.api.user | The username for the accessing the REST endpoints with the Mailgun API using Basic Auth |
| email.mailGun.api.password | The password for the accessing the REST endpoints with the Mailgun API using Basic Auth |

#### For SendGrid API

| Property    | Description |
|-------------|-------------|
| email.sendGrid.api.url | The url for making REST calls with the SendGrid API |
| email.sendGrid.api.key | The API Key for the accessing the REST endpoints with the SendGrid API |

### Built With

* [Spring-Boot](https://projects.spring.io/spring-boot/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Lombok](https://projectlombok.org/features/all) - Simplify the Java code
* [Swagger](https://swagger.io/docs/) - RESTful API documention

### Future Improvements

* Have added a `RetryEmailDispatcherService` which will retry the mail service providers for a certain count configured 
using the `email.send.retry.count` property.

It uses a decorator pattern which allows that service to be used by defining a bean in the `AppConfig` as shown below:
```
@Bean
public EmailDispatcher retryEmailDispatcherService(@Qualifier("emailDispatcherService") EmailDispatcher emailDispatchService) {
    return new RetryEmailDispatcherService(emailDispatchService);
}
```
And access it from the `EmailDispatcherController` as shown below:
```
public EmailDispatcherController(EmailDispatcher retryEmailDispatcherService) {
    this.emailDispatcherService = retryEmailDispatcherService;
}
```
* Secure the application by using spring security.
* Add some health checks for the email service provider API endpoints. 