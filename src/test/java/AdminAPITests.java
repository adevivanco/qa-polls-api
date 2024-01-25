import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.*;

public class AdminAPITests {

    String templatePollUuid = "";


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
    @CsvSource({"ATP,peru.png", "Chelsea,chelsea.png", "Juventus,juventus.png"})
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

        RequestSpecification postTeamRequest = RestAssured.given().log().all();
        postTeamRequest.header("Content-Type", "application/json");
        postTeamRequest.header("Authorization", "Bearer " + token);

        Response postTeamResponse = postTeamRequest.body(
             "{ " +
                    "\"name\": \""  + teamName + "\"" + "," +
                    "\"image\": \""  + teamImage + "\"" +
               " }")
                .post("/teams");

        postTeamResponse.then().log().all();
        Assertions.assertEquals(response.getStatusCode(), 200);

        jsonString = postTeamResponse.asString();
        Assertions.assertEquals(JsonPath.from(jsonString).get("data.name"), teamName);
        Assertions.assertEquals(JsonPath.from(jsonString).get("data.image"), teamImage);

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

    }
}




