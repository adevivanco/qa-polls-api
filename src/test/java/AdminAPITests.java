import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminAPITests {

    private static String templatePollUuid = "";


    @Order(1)
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


    @Order(2)
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


    @Order(3)
    @ParameterizedTest
    @CsvSource({"Inter,Arsenal, 20, 10, 2023-12-17T09:00:00.00+0000",
                "Newcastle,Arsenal, 20, 10, 2023-12-17T09:00:00.00+0000"})
    public void createGames(String homeTeam, String awayTeam, int pointsForResult, int pointsForGoals, String gameDateTime) {

        String userName = "ninjadelcodigo@gmail.com";
        String password = "emili2K22!";
        String baseUrl = "http://localhost:3335";
        // String templatePollUuid = "26c7eeb4-a28c-4dee-92c2-491137c98d90"; /* TODO: GENERATE DYNAMICALLY */

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
        RequestSpecification postGameRequest = RestAssured.given().log().all();
        postGameRequest.header("Content-Type", "application/json");
        postGameRequest.header("Authorization", "Bearer " + token);

        System.out.println(templatePollUuid);

        Response postGameResponse = postGameRequest.body(
            "{ " +
//                    "\"templatePollUuid\": \""  + "26c7eeb4-a28c-4dee-92c2-491137c98d90" + "\"" + "," +
                    "\"templatePollUuid\": \""  + templatePollUuid + "\"" + "," +
                    "\"homeTeamName\": \""  + homeTeam + "\"" + "," +
                    "\"awayTeamName\": \""  + awayTeam + "\"" + "," +
                    "\"pointsForResult\": "  + pointsForResult + "," +
                    "\"pointsForGoals\": "  + pointsForGoals  + "," +
                    "\"gameDateTime\": \""  + gameDateTime + "\"" + "," +
                    "\"phase\": \""  + "Groups" + "\""  +
                " }")
            .post("/template-games");

        postGameResponse.then().log().all();
        Assertions.assertEquals(postGameResponse.getStatusCode(), 200);

        jsonString = postGameResponse.asString();
        Assertions.assertEquals(JsonPath.from(jsonString).get("data.homeTeamName"), homeTeam);
        Assertions.assertEquals(JsonPath.from(jsonString).get("data.awayTeamName"), awayTeam);
    }
}




