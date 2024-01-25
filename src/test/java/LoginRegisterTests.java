import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LoginRegisterTests {

    @Test
    public void registerWithAnExistingUsername() {
        // Set the base URI for your API
        RestAssured.baseURI = "https://mc11qn4zok.execute-api.us-east-2.amazonaws.com";


        given()
                .header("Content-Type", "application/json")
                .body("{ " +
                        "\"email\": \"andresdev@gmail.com\"," +
                        "\"password\": \"Test@1234\"" +
                        " }")
                .log().all()
                .when()
                .post("/register")
                .then()
                .log().all()
                .statusCode(400);
    }

}
