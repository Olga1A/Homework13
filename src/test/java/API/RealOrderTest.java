package API;

import com.google.gson.Gson;
import dto.LoginDto;
import dto.TestOrderDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RealOrderTest {

    //String tokenManually = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbC1vbGdhIiwiZXhwIjoxNzE4MTk2MDE1LCJpYXQiOjE3MTgxNzgwMTV9.t_r_u4O5jp5OAQ9OumZOvVVrBQNhnfxeZbPZxDkogb83aj_U4UEivd81DYJ7wXez7HbMqTBJW1SYlxZaFbmicw";
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    static String tokenAuthomation;

    @BeforeAll
    public static void setup() {

        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        Assumptions.assumeTrue(username != null && password != null);

        Gson gson = new Gson();
        LoginDto loginDto_javaObject = new LoginDto(username,password);

        //Send post request to get token with username & password
        tokenAuthomation = RestAssured
                .given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body(gson.toJson(loginDto_javaObject))
                .post(BASE_URL + "/login/student")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .body()
                .asString();
    }

    @Test
    public void createOrder() {

        Gson gson = new Gson();
        TestOrderDto realOrder_javaObject = new TestOrderDto("Olga A", "55555675", "Good buy");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAuthomation)
                .log()
                .all()
                .when()
                .body(gson.toJson(realOrder_javaObject))
                .post(BASE_URL+"/orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkExistingOrderById() {

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAuthomation)
                .log()
                .all()
                .when()
                .get(BASE_URL+"/orders/769") //manually now, then we will create it before automatically
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
