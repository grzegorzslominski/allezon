package pl.edu.pjwstk.jaz.allezon.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestLoginController {
    @Test
    public void shouldReturnCode200WhenGiveCorrectDataToLogin() {
        String json = "{ \"email\": \"loginCorrect@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("Logged in."));
    }

    @Test
    public void shouldReturnCode401WhenGiveCorrectEmailAndWrongPassword() {
        String json = "{ \"email\": \"loginCorrectButWrongPassword@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        json = "{ \"email\": \"loginCorrectButWrongPassword@test.com\", \"password\": \"kt\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED))
                .body(equalTo("Email or password is incorrect."));
    }

    @Test
    public void shouldReturnCode401WhenGiveWrongEmailAndCorrectPassword() {
        String json = "{ \"email\": \"WrongEmailAndCorrectPassword@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        json = "{ \"email\": \"ButWrongPassword@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED))
                .body(equalTo("Email or password is incorrect."));
    }
    @Test
    public void shouldReturnCode401WhenGiveDataWhichDontExistsInDatabase() {
        String json = "{ \"email\": \"loginCertainlyDoesNotExist@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED))
                .body(equalTo("Email or password is incorrect."));
    }

    @Test
    public void shouldReturnCode400WhenGiveEmptyPassword() {
        String json = "{ \"email\": \"loginCertainlyDoesNotExist@test.com\", \"password\": \"\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
                .body(equalTo("Email or password is empty."));
    }

    @Test
    public void shouldReturnCode400WhenGiveEmptyLogin() {
        String json = "{ \"email\": \"\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
                .body(equalTo("Email or password is empty."));
    }
}
