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
public class TestAuctionImageController {

    @Test
    public void  shouldReturn403WhenOfflineUserTryingToAccess() {
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
    public void  shouldReturn401WhenUserTriesEditOrDeleteAnyAuctionPhotoThatIsNotHis() {

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

        //dodanie zdjęcia do aukcji

        json = "{\n" +
                "    \"auctionId\" : 1,\n" +
                "    \"url\" : \"wp.pl\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions/images/1")
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

        //próba edycji zdjęcia z aukcji która nie należy do tego użytkownika

        json ="{\n" +
                "    \"auctionId\" : 1,\n" +
                "    \"url\" : \"onet.pl\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/images/edit/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));

        //próba usunięcia zdjęcia z aukcji która nie należy do tego użytkownika
        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));

    }

    @Test
    public void  completeTestForTheAuctionOwner() {

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
                "    \"name\" : \"Length\"\n" +
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

        //dodanie zdjęcia do aukcji

        json = "{\n" +
                "    \"auctionId\" : 1,\n" +
                "    \"url\" : \"wp.pl\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        //próba dodania zdjęcia do aukcji ltóra nie istnieje

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions/images/2")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

        //próba edycji zdjęcia które nie istnieje

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/images/edit/2")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

        //próba usunięcia zdjęcia aukcji która nie istnieje

        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/images/2")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

        //edycja zdjęcia
        json ="{\n" +
                "    \"auctionId\" : 1,\n" +
                "    \"url\" : \"onet.pl\"\n" +
                "}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/api/allezon/auctions/images/edit/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));

        //usunięcie zdjęcia aukcji
        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/auctions/images/1")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));

    }
}
