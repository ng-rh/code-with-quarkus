Certainly! Below is an updated `README.md` for your Quarkus REST API example project, including a list of APIs and examples:

```markdown
# Quarkus REST API Example

This project is an example of a Quarkus REST API application. It includes multiple REST endpoints and uses Swagger for API documentation.

## Prerequisites

- JDK 11+
- Maven 3.6.3+
- GraalVM (for native mode)

## Getting Started

### Running the Application in Development Mode

To run the application in development mode:

```bash
./mvnw quarkus:dev
```

### Adding Swagger Extension

If you need to add the Swagger extension, run the following command:

```bash
./mvnw quarkus:add-extension -Dextensions="smallrye-openapi"
```

### Accessing the Endpoints

You can access the following endpoints in your browser or using tools like `curl` or Postman.

- **Hello Endpoint**: Returns a simple greeting message.
  - **Example**: `GET http://localhost:8080/api/hello`
  - **Response**: `Hello from Quarkus REST`

- **JSON Endpoint**: Returns a greeting message as JSON.
  - **Example**: `GET http://localhost:8080/api/json`
  - **Response**: 
    ```json
    {
      "message": "Hello from Quarkus REST with JSON"
    }
    ```

- **Personalized Hello Endpoint**: Returns a personalized greeting message based on the provided name.
  - **Example**: `GET http://localhost:8080/api/hello/{name}`
  - **Response**: `Hello, {name} from Quarkus REST` (replace `{name}` with the actual name)

- **Greet Endpoint**: Returns a greeting message with optional name and language parameters.
  - **Example**: `GET http://localhost:8080/api/greet`
  - **Response**: `Hello from Quarkus REST`
  - **Example with parameters**: `GET http://localhost:8080/api/greet?name=Bob&language=es`
  - **Response**: `Hola, Bob from Quarkus REST` (assuming `language=es`)

### Running Tests

To run the tests for this application:

```bash
./mvnw test
```

### Swagger API Documentation

This project uses Swagger UI for API documentation. After starting the application, you can access the Swagger UI at:

```bash
http://localhost:8080/q/swagger-ui/
```

The OpenAPI document is available at:

```bash
http://localhost:8080/q/openapi
```

### Running in Native Mode

To build and run the application in native mode, follow these steps:

1. **Install GraalVM**: Ensure GraalVM is installed and `native-image` tool is available.

2. **Add the `quarkus-native` extension**:
   ```bash
   ./mvnw quarkus:add-extension -Dextensions="quarkus-native"
   ```

3. **Build the native executable**:
   ```bash
   ./mvnw package -Pnative
   ```

4. **Run the native executable**:
   ```bash
   ./target/{project-name}-runner
   ```

   Replace `{project-name}` with your project's artifact ID.

5. **Check the logs for native startup time**:
   ```
   INFO  [io.quarkus] (main) Quarkus 2.7.2.Final on JVM started in 0.034s. Listening on: http://0.0.0.0:8080
   INFO  [io.quarkus] (main) Profile prod activated.
   INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jackson, smallrye-openapi]
   ```

### GreetingResource Class

Here is the `GreetingResource` class with multiple endpoints:

```java
package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@Path("/api")
public class GreetingResource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting jsonHello() {
        return new Greeting("Hello from Quarkus REST with JSON");
    }

    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloName(@PathParam("name") String name) {
        return "Hello, " + name + " from Quarkus REST";
    }

    @GET
    @Path("/greet")
    @Produces(MediaType.TEXT_PLAIN)
    public String greet(@QueryParam("name") String name, @QueryParam("language") String language) {
        String greeting;
        switch (language != null ? language.toLowerCase() : "en") {
            case "es":
                greeting = "Hola";
                break;
            case "fr":
                greeting = "Bonjour";
                break;
            default:
                greeting = "Hello";
                break;
        }
        return greeting + (name != null ? ", " + name : "") + " from Quarkus REST";
    }

    public static class Greeting {
        private String message;

        public Greeting() {
        }

        public Greeting(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
```

### GreetingResourceTest Class

Here is the `GreetingResourceTest` class with tests for the endpoints:

```java
package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/api/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testJsonEndpoint() {
        given()
          .when().get("/api/json")
          .then()
             .statusCode(200)
             .body("message", is("Hello from Quarkus REST with JSON"));
    }

    @Test
    void testHelloNameEndpoint() {
        String name = "Alice";
        given()
          .when().get("/api/hello/" + name)
          .then()
             .statusCode(200)
             .body(is("Hello, " + name + " from Quarkus REST"));
    }

    @Test
    void testGreetEndpointWithoutParams() {
        given()
          .when().get("/api/greet")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testGreetEndpointWithName() {
        String name = "Bob";
        given()
          .when().get("/api/greet?name=" + name)
          .then()
             .statusCode(200)
             .body(is("Hello, " + name + " from Quarkus REST"));
    }

    @Test
    void testGreetEndpointWithNameAndLanguage() {
        String name = "Charlie";
        String language = "es";
        given()
          .when().get("/api/greet?name=" + name + "&language=" + language)
          .then()
             .statusCode(200)
             .body(is("Hola, " + name + " from Quarkus REST"));
    }

    @Test
    void testGreetEndpointWithLanguageOnly() {
        String language = "fr";
        given()
          .when().get("/api/greet?language=" + language)
          .then()
             .statusCode(200)
             .body(is("Bonjour from Quarkus REST"));
    }
}
```