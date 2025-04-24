package tasks.apiTask;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RestAssured SimpleBooksApiTest")
@Feature("CRUD Operations")
public class SimpleBooksApiTests {

    // Base URL for the API
    private static final String BASE_URL = "https://simple-books-api.glitch.me";
    // Authentication token and order ID
    private static String accessToken;
    private static String orderId;

    // Example data
    private final String headerAuthorizationKey = "Authorization";
    private final int exampleBookID = 1;
    private final String exampleCustomerName = "Test Customer Name";
    String exampleUpdateCustomerName = "Update Test Customer Name";


    @Test(description = "GET - Get all books")
    void getListOfBooks() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].type", notNullValue())
                .body("[0].available", notNullValue()).log().all();
    }

    @Test(description = "GET - Get a single book")
    void getSingleBook() {
        // int bookId = 1; // Example book ID
        given()
                .baseUri(BASE_URL)
                .when()
                .get("/books/" + exampleBookID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(exampleBookID))
                .body("name", notNullValue())
                .body("type", notNullValue())
                .body("available", notNullValue()).log().all();
    }

    @Test(description = "Create - Create Authentication Access Token")
    void createAuthToken() {
        // Generate random client name and email to avoid conflicts
        String clientName = "TestClient" + new Random().nextInt(10000);
        String clientEmail = "test" + new Random().nextInt(10000) + "@example.com";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("clientName", clientName);
        credentials.put("clientEmail", clientEmail);

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/api-clients");

        response.then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("accessToken", notNullValue());

        accessToken = response.jsonPath().getString("accessToken");
        System.out.println("Generated Access Token: " + accessToken);
    }

    @Test(description = "POST - Create a new order")
    void postOrders() {

        createAuthToken(); // Ensure we have an access token

        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("bookId", exampleBookID); // Example book ID
        orderBody.put("customerName", exampleCustomerName);

        Response response = given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(orderBody)
                .when()
                .post("/orders");

        response.then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("created", equalTo(true))
                .body("orderId", notNullValue());

        orderId = response.jsonPath().getString("orderId");
        System.out.println("Created Order ID: " + orderId);
    }

    @Test(description = "GET - Get a specific order")
    void getSingleOrder() {
        postOrders(); // Ensure order exists

        given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer " + accessToken)
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(orderId))
                .body("bookId", equalTo(exampleBookID)) // Assuming the order was for book ID 1
                .body("customerName", equalTo(exampleCustomerName)).log().all();
    }

    @Test(description = "PATCH - Update an order")
    void patchOrder() {
        postOrders(); // Ensure an order exists

        Map<String, String> updateBody = new HashMap<>();
        updateBody.put("customerName", exampleUpdateCustomerName);

        given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .patch("/orders/" + orderId)
                .then()
                .statusCode(204);

        //  check the update by a GET request
        given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(200)
                .body("customerName", equalTo(exampleUpdateCustomerName)).log().all();
    }

    @Test(description = "DELETE - Delete an order")
    void deleteOrder() {

        postOrders(); // Ensure an order exists

        given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer " + accessToken)
                .when()
                .delete("/orders/" + orderId)
                .then()
                .statusCode(204);

        // Verify the order was deleted by a GET request (should return 404)
        given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer " + accessToken)
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(404)
                .body("error", equalTo("No order with id " + orderId + ".")).log().all();
    }

    @Test(description = "Test authentication with invalid token")
    void testInvalidAuthentication() {
        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("bookId", exampleBookID);
        orderBody.put("customerName", exampleCustomerName);

        given()
                .baseUri(BASE_URL)
                .header(headerAuthorizationKey, "Bearer invalid_token")
                .contentType(ContentType.JSON)
                .body(orderBody)
                .when()
                .post("/orders")
                .then()
                .statusCode(401)
                .body("error", containsString("Invalid")).log().all();
    }

}
