package API;

import Specs.RequestSpecOrders;
import com.google.gson.Gson;
import dto.TestOrderDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import  utils.TestDataGenerator;

import static utils.TestDataGenerator.generateRandomCustomerComments;
import static utils.TestDataGenerator.generateRandomCustomerName;

public class ApiDeliveryTest {

    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    public static final String BASE_PATH = "/test-orders/";


    @Test
    public void checkOrderDetailsWithCorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "1")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkErrorStatusCodeWithIncorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "11")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkErrorStatusCodeWithZeroOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "0")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkOrderDetailsWithCorrectOrderIdAndCheckStatus() {
        String receivedOrderStatus = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "1")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("status");

        Assertions.assertEquals("OPEN", receivedOrderStatus);
    }

    @Test
    public void checkOrderAndResposne() {
        int orderIdRequested = 5;
        int receivedOrderId = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + orderIdRequested)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        Assertions.assertEquals(orderIdRequested, receivedOrderId);
    }

    @Test
    public void checkOrderCreation() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Olga\",\n" +
                "  \"customerPhone\": \"585858\",\n" +
                "  \"comment\": \"Please ring my number\",\n" +
                "  \"id\": 0\n" +
                "}";

        String receivedStatus = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("status");


        Assertions.assertEquals("OPEN", receivedStatus);
    }

    @Test
    public void createOrderWithIncorrectStatusAndCheckResponseMessage() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\":\"CLOSED\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Olga\",\n" +
                "  \"customerPhone\": \"585858\",\n" +
                "  \"comment\": \"Please ring my number\",\n" +
                "  \"id\": 0\n" +
                "}";

        String responseBody = RestAssured
                .given()
                .contentType(ContentType.JSON)
//                .queryParam("username", "name")
//                .queryParam("password", "12345678")
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .extract()
                .body()
                .asString();

        Assertions.assertEquals("Incorrect query", responseBody);
    }

    @Test
    public void createNewOrderWithDtoPattern(){
        TestOrderDto orderDtoRequest = new TestOrderDto("Mark", "51234567","leave my order by the door");
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithDtoPattern() {
        // order creation
        TestOrderDto orderDtoRequest = new TestOrderDto("olga", "55445544", "call me");
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createNewOrderWithDtoPatternAndSetters(){
        TestOrderDto orderDtoRequest = new TestOrderDto();
        orderDtoRequest.setCustomerName("Irina");
        orderDtoRequest.setCustomerPhone("58923451");
        orderDtoRequest.setComment("Knock three times");
        String requestBodyAsJason = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJason)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithDtoPatternAndSetters() {
        // order creation by default constructor
        TestOrderDto orderDtoRequest = new TestOrderDto();
        orderDtoRequest.setComment("call me two times");
        orderDtoRequest.setCustomerName("name");
        orderDtoRequest.setCustomerPhone("33445566");
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithDtoPatternAndSettersAndRandomValues() {
        // order creation by default constructor
        TestOrderDto orderDtoRequest = new TestOrderDto();

        orderDtoRequest.setComment(generateRandomCustomerComments());
        orderDtoRequest.setCustomerName(generateRandomCustomerName());
        orderDtoRequest.setCustomerPhone(TestDataGenerator.generateRandomCustomerPhone());
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithSpec() {
        //Static method from the class name
        TestOrderDto orderDtoRequest = new TestOrderDto();

        orderDtoRequest.setComment(generateRandomCustomerComments());
        orderDtoRequest.setCustomerName(generateRandomCustomerName());
        orderDtoRequest.setCustomerPhone(TestDataGenerator.generateRandomCustomerPhone());
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .spec(RequestSpecOrders.getSpec())
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    //Response body check all fields
    public void checkOrderResponseBody() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Olga A\",\n" +
                "  \"customerPhone\": \"58585855\",\n" +
                "  \"comment\": \"Please call my mobile\",\n" +
                "  \"id\": 0\n" +
                "}";

Gson gson = new Gson(); //intstance

        Response responseBody = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .response();
        //Transformation from Json to Java object of test order dto class
        TestOrderDto order = gson.fromJson(responseBody.asString(),TestOrderDto.class);
        Assertions.assertEquals("OPEN", order.getStatus());
        Assertions.assertEquals("Olga A", order.getCustomerName());
        Assertions.assertEquals("58585855", order.getCustomerPhone());
        Assertions.assertEquals("Please call my mobile", order.getComment());
        Assertions.assertNotNull(order.getId());
    }
}
