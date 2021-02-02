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
public class TestParameterController {

    @Test
    public void shouldReturn403WhenUsersTryToAccessResourcesNotIntendedForThem() {

        //pobranie listy parametrów
        given()
                .when()
                .get("/api/allezon/parameters")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

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

        json = "{\n" +
                "    \"name\" : \"lenggth\"\n" +
                "}";

        //próba dodania
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/parameters")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba edycji
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/parameters/edit/lenggth/length")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba usunięcia
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/parameters/length")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

    }

    @Test
    public void shouldReturn200WhenAdminTryAddEditAndDeleteParameters() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        //dodanie parametru
        json = "{\n" +
                "    \"name\" : \"lenggth\"\n" +
                "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/parameters")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));


        //edycja parametru
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("api/allezon/parameters/edit/lenggth/length")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));
        //sprawdzenie czy podkategoria została dodana


        // usunięcie parametru
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/parameters/length")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldChangeTheForeignKeysInAuctionParametersAfterRemovingParameter() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        //dodanie parametru
        json = "{\n" +
                "    \"name\" : \"length\"\n" +
                "}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/parameters")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

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

        //dodanie pararametru do aukcji

        json = "{\n" +
                "    \"auctionId\" : 1,\n" +
                "    \"parameterId\" : 1,\n" +
                "    \"value\" : \"short\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions/parameter/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //usunięcie parametru na który wskazuje parametr aukcji
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/parameters/length")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));

    }
}
