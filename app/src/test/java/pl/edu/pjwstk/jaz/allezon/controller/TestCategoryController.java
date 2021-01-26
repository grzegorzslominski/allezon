package pl.edu.pjwstk.jaz.allezon.controller;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestCategoryController {
    @Test
    public void completeTestCategoriesForAdminRole() {
        given()
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
        String json = "{ \"name\": \"car\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        var response = given()
                .when()
                .get("/api/allezon/categories")
                .thenReturn();
        String content = response.getBody().asString();
        assert (content.contains("car") == true);
        assert (response.statusCode() == HttpStatus.SC_OK);

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));

        given()
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }

    @Test
    public void shouldReturnForbiddenCodeWhenAuthenticationUserTryGetResourceDesignedForAdmin() {
        String json = "{ \"email\": \"sectionsUser@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }
}
