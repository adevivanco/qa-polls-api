import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.Request;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class e2eTests {

    String templatePollUuid = "";
    @Test
    public void testGetRequest() {
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
            .statusCode(201);
    }



    @ParameterizedTest
    @CsvSource({"EPL round Round 21,2024-18-17T15:00:00.00+0000"})
    public void createTemplatePoll(String templatePollName, String submissionLimitDate) {
        String userName = "ninjadelcodigo@gmail.com";
        String password = "emili2K22!";

        RestAssured.baseURI = "http://localhost:3335";
        RequestSpecification request = RestAssured.given().log().all();

        //Step - 1
        //Test will start from generating Token for Authorization
        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"email\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/authenticate");

        response.then().log().all();
        Assertions.assertEquals(response.getStatusCode(), 200);

        String jsonString = response.asString();
        Assertions.assertTrue(jsonString.contains("token"));

        //This token will be used in later requests
        String token = JsonPath.from(jsonString).get("token");

        RequestSpecification tpPollRequest = RestAssured.given().log().all();
        tpPollRequest.header("Content-Type", "application/json");
        tpPollRequest.header("Authorization", "Bearer " + token);

        Response tpPollResponse = tpPollRequest.body(
                "{ " +
                "\"templatePollName\": \""  + templatePollName + "\"" + "," +
                "\"submissionLimitDate\": \""  + submissionLimitDate + "\"" +
                " }").post("/template-polls");

        tpPollResponse.then().log().all();
        Assertions.assertEquals(response.getStatusCode(), 200);

        jsonString = tpPollResponse.asString();
        templatePollUuid = JsonPath.from(jsonString).get("uuid");

        System.out.println(templatePollUuid);
    }


    @ParameterizedTest
    @CsvSource({"AL,peru.png", "Chelsea,chelsea.png", "Juventus,juventus.png"})
    public void createTeams(String teamName, String teamImage) {

        String userName = "ninjadelcodigo@gmail.com";
        String password = "emili2K22!";
        String baseUrl = "http://localhost:3335";

        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given().log().all();

        //Step - 1
        //Test will start from generating Token for Authorization
        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"email\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/authenticate");

        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);

        String jsonString = response.asString();
        Assert.assertTrue(jsonString.contains("token"));

        //This token will be used in later requests
        String token = JsonPath.from(jsonString).get("token");


        //Step - 2
        // create a team
        given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ " +
                "\"name\": \""  + teamName + "\"" + "," +
                "\"image\": \""  + teamImage + "\"" +
                " }")
            .log().all()
        .when()
            .post("/teams")
        .then()
                .log().all()
                .statusCode(200)
                .body("data.name", equalTo(teamName));
    }


    @ParameterizedTest
    @CsvSource({"Inter,inter.png", "Chelsea,chelsea.png", "Juventus,juventus.png"})
    public void createGames(String homeTeam, String awayTeam) {

        String userName = "ninjadelcodigo@gmail.com";
        String password = "emili2K22!";
        String baseUrl = "http://localhost:3335";
        String templatePollUuid = "26c7eeb4-a28c-4dee-92c2-491137c98d90"; /* TODO: GENERATE DYNAMICALLY */

        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given().log().all();

        //Step - 1
        //Test will start from generating Token for Authorization
        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"email\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/authenticate");


        Assert.assertEquals(response.getStatusCode(), 200);

        String jsonString = response.asString();
        Assert.assertTrue(jsonString.contains("token"));

        //This token will be used in later requests
        String token = JsonPath.from(jsonString).get("token");

        System.out.println(token);

        //Step - 2
        // create a team
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body("{ " +
                        "\"name\": \""  + homeTeam + "\"" + "," +
                        "\"image\": \""  + awayTeam + "\"" +
                        " }")
                .log().all()
                .when()
                .post("/teams")
                .then()
                .log().all()
                .statusCode(200);
    }
}




