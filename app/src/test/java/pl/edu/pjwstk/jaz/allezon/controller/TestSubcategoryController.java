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
public class TestSubcategoryController {

    @Test
    public void shouldReturn200WhenAuthenticationUserTryGetSubcategoryList() {
        String json = "{ \"email\": \"user@test.com\", \"password\": \"testpw\"}";
        //rejestracja i  logowanie użytkownika
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

        given()
                .cookies(cookies.getCookies())
                .when()
                .get("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }

    @Test
    public void shouldReturn403WhenUnauthenticatedUserTryGetAccess() {


        given()
                .when()
                .get("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .when()
                .post("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .when()
                .delete("/api/allezon/subcategories/phone")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .when()
                .patch("/api/allezon/subcategories/edit")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn403WhenUnauthorizedUserTryGetAccess() {

        String json = "{ \"email\": \"user@test.com\", \"password\": \"testpw\"}";
        //rejestracja i  logowanie użytkownika
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

        json = "{\n" + "    \"name\" : \"nowa\",\n" + "    \"categoryId\" : 3\n" + "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/subcategories/phone")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/subcategories/edit/phone")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn200WhenWhenAdminUsingAppropriateMethods() {

        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        //dodanie kategori
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //dodanie podkategori
        json = "{\n" + "    \"name\" : \"phhone\",\n" + "    \"categoryId\" : 1\n" + "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //edycja podkategorii
        json = "{\"name\" : \"phone\",\n" + "    \"categoryId\" : 1\n" + "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("api/allezon/subcategories/edit/phhone")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));
        //sprawdzenie czy podkategoria została dodana


        // usunięcie podkategori
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/subcategories/phone")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));
    }


    @Test
    public void shouldChangeForeignKeyInAuctionsThatWereInDeletedSubcategory() {

        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        //dodanie kategori
        json = "{ \"name\": \"cars\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //dodanie podkategori
        json = "{\n" + "    \"name\" : \"hovercraft\",\n" + "    \"categoryId\" : 1\n" + "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //dodanie aukcji
        json = "{\n" +
                "    \"subcategoryId\" : 1,\n" +
                "    \"title\" : \"New car\",\n" +
                "    \"description\" : \"This is description\",\n" +
                "    \"price\" : 15000.00\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));


        // usunięcie podkategori
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/subcategories/hovercraft")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));


    }


}







