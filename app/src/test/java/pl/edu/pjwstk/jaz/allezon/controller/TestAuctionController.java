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
public class TestAuctionController {

    @Test
    public void shouldReturn200And403ForUnloggedUser(){

        //próba pobrania listy aukcji
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));


        //próba pobrania listy aukcji z miniturą
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/photo")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));


        //próba dodania
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/allezon/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba usunięcia
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/allezon/auctions/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba edycji
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/api/allezon/auctions/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void completeTestForAuctionOwner(){

        //logowanie
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

//dodanie aukcji przez admina
        json = "{\n" +
                "    \"subcategoryId\" : 1,\n" +
                "    \"title\" : \"New car\",\n" +
                "    \"description\" : \"This is description\",\n" +
                "    \"price\" : 7000.00\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));



        json = "{ \"email\": \"user@test.com\", \"password\": \"testpw\"}";
        //rejestracja i  logowanie innego użytkownika
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);


        //próba dodania aukcji z nieistniejącą podkategorią
        json = "{\n" +
                "    \"subcategoryId\" : 15,\n" +
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
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));


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

        // pobranie listy z miniaturą

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/photo")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[{\"auction\":null,\"firstPhoto\":null},{\"auction\":{\"id\":2,\"authorId\":2,\"subcategoryId\":1,\"title\":\"New car\",\"description\":\"This is description\",\"price\":15000.0},\"firstPhoto\":\"default.photo/5.com\"}]"));



        //próba edycji i usunięcia nieistniejącej aukcji

        json = "{\n" +
                "    \"subcategoryId\" : 1,\n" +
                "    \"title\" : \"New car\",\n" +
                "    \"description\" : \"This is new description\",\n" +
                "    \"price\" : 15000.00\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/edit/3")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/3")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));


        //próba edycji i usunięcia aukcji której nie jesteśmy autorami

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
                .patch("/api/allezon/auctions/edit/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));

        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));


        // edycja i usunięcie aukcji której jesteśmy właścicielami

        json = "{\n" +
                "    \"subcategoryId\" : 1,\n" +
                "    \"title\" : \"New car\",\n" +
                "    \"description\" : \"This is new description\",\n" +
                "    \"price\" : 13500.50\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/edit/2")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));

        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/2")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));




    }





}
