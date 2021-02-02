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
public class TestAuctionParameterEntity {


    @Test
    public void  shouldReturn403WhenOfflineUserTryingToAccess() {
        //próba dodania
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/allezon/auctions/parameter/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba usunięcia
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/allezon/auctions/parameter/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        //próba edycji
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/api/allezon/auctions/parameter/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

    }

    @Test
    public void  shouldReturn401WhenUserTriesEditOrDeleteAnyAuctionThatIsNotHis() {

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

        //próba edycji parametru aukcji która nie należy do tego użytkownika

        json = "{\n" +
                "    \"value\" : \"5\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/parameter/edit/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));

        //próba usunięcia parametru aukcji która nie należy do tego użytkownika
        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/parameter/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));

    }
}
