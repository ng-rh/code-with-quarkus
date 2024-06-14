package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

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
