import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductApiIT {
    @Test
    public void testGetProducts_ShouldReturnProductsArray()
    {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .get("https://icmayost93.execute-api.us-east-1.amazonaws.com/Prod/product");

        Assertions.assertEquals(response.statusCode(), 200);
        String responseBody = response.asString();
        Assertions.assertNotNull(responseBody);
    }
}
