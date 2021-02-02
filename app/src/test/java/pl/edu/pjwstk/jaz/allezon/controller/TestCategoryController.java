
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
public class TestCategoryController {
    @Test
    public void shouldReturnListOfCategories() {
        //pobieranie listy kategorii
        given()
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }
    @Test
    public void shouldReturnForbiddenForNoNAuthenticationUserWhenTryAddCategory() {
        String json = "{ \"name\": \"cars\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturnForbiddenForNoNAuthenticationUserWhenTryDeleteCategory() {
        given()
                .when()
                .delete("/api/allezon/categories/cars")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void completeTestCategoriesForAdminRole() {
        //logowanie admina
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        //utworzenie kategorii przez admina
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        //sprawdzenie czy kategoria została utworzona
        var response = given()
                .when()
                .get("/api/allezon/categories")
                .thenReturn();
        String content = response.getBody().asString();
        assert (content.contains("car") == true);
        assert (response.statusCode() == HttpStatus.SC_OK);

    //usunięcie kategori przez admina
        given()
                .cookies(cookies.getCookies())
                .when()
                .delete("/api/allezon/categories/car")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));
    //sprawdzenie czy kategoria została usunięta
        given()
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[{\"id\":1,\"name\":\"Undefined\"}]"));
    }

    @Test
    public void shouldReturnForbiddenCodeWhenAuthenticationUserTryGetResourceDesignedForAdmin() {
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
        //próba dodania i usunięcia kategorii przez nieautoryzowanego
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
                .when()
                .delete("/api/allezon/categories/car")
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

    @Test
    public void shouldReturnConflictWhenAdminTryAddExistCategory() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        //utworzenie kategorii przez admina
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CONFLICT));

    }

    @Test
    public void shouldReturnNotFoundWhenAdminDeleteNonCategory() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
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
                .delete("/api/allezon/categories/car")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

    }

    @Test
    public void shouldReturnAcceptedWhenAdminTryEditCategory() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        //utworzenie kategorii przez admina
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        //edycja kategorii przez admina
        given()
                .cookies(cookies.getCookies())
                .when()
                .patch("/api/allezon/categories/edit/car/cars")
                .then()
                .statusCode(equalTo(HttpStatus.SC_ACCEPTED));
}

    @Test
    public void shouldReturnNotFoundWhenAdminTryEditNonexistentCategory() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        //edycja kategorii przez admina
        given()
                .cookies(cookies.getCookies())
                .when()
                .patch("/api/allezon/categories/edit/car/cars")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

    }

    @Test
    public void shouldReturnConflictWhenAdminTryRenameCategoryOnNameTaken() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        //utworzenie kategorii przez admina
        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        //edycja kategorii przez admina
        given()
                .cookies(cookies.getCookies())
                .when()
                .patch("/api/allezon/categories/edit/car/car")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CONFLICT));

    }
}
