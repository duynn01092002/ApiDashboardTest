import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import org.testng.annotations.Test;

public class api_test {
    private String token;
    @Test(priority = 1)
    public void testGetToken() {
        String clientId = "H8J1NKema4LrrUu6TYq6kH5if1JX6UyQ";
        String clientSecret = "RimknsnMuXAzi6gzWqinaUyLMgS95tbp";
        String url = "https://cads-api.fpt.vn/fiber-detection/v2/getToken";
        String payload = String.format("{\"clientId\":\"%s\",\"clientSecret\":\"%s\"}", clientId, clientSecret);
        Response response = RestAssured.given()
                .header("Content-Type","application/json")
                .body(payload)
                .post(url);
        JSONObject responseJson = new JSONObject(response.getBody().asString());
        String access_token = responseJson.getString("access_token");
        this.token = access_token;
        assert response.getStatusCode() == 200 && !access_token.isEmpty();
    }
    @Test(priority = 2)
    public void testGetData() {
        RestAssured.baseURI = "http://cads-api.fpt.vn/fiber-detection/v2/using_json_inf/2022/12";
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + this.token)
                .when()
                .get();
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        assert statusCode == 200 && responseBody != null;
    }

    @Test(priority = 3)
    public void testGetTokenFail() {
        String clientId = "";
        String clientSecret = "RimknsnMuXAzi6gzWqinaUyLMgS95tbp";
        String url = "https://cads-api.fpt.vn/fiber-detection/v2/getToken";
        String payload = String.format("{\"clientId\":\"%s\",\"clientSecret\":\"%s\"}", clientId, clientSecret);
        Response response = RestAssured.given()
                .header("Content-Type","application/json")
                .body(payload)
                .post(url);
        JSONObject responseJson = new JSONObject(response.getBody().asString());
        String error = responseJson.getString("error");
        assert response.getStatusCode() == 200 && error.equals("invalid_client");
    }

    @Test(priority = 4)
    public void testNoTokenGetData() {
        RestAssured.baseURI = "http://cads-api.fpt.vn/fiber-detection/v2/using_json_inf/2022/12";
        Response response = RestAssured.given()
                .when()
                .get();
        int statusCode = response.getStatusCode();
        JSONObject responseJson = new JSONObject(response.getBody().asString());
        String error = responseJson.getString("error");
        assert statusCode == 401 && error.equals("invalid_request");
    }

    @Test(priority = 5)
    public void testInvalidTokenGetData() {
        RestAssured.baseURI = "http://cads-api.fpt.vn/fiber-detection/v2/using_json_inf/2022/12";
        Response response = RestAssured.given()
                .header("Authorization", "Bearer "+"")
                .when()
                .get();
        int statusCode = response.getStatusCode();
        JSONObject responseJson = new JSONObject(response.getBody().asString());
        String error = responseJson.getString("error");
        assert statusCode == 401 && error.equals("invalid_token");
    }
}
