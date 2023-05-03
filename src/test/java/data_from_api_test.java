import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class data_from_api_test {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");
    public static JSONObject getData() {
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
        RestAssured.baseURI = "http://cads-api.fpt.vn/fiber-detection/v2/using_json_inf/2022/12";
        response = RestAssured.given()
                .header("Authorization", "Bearer " + access_token)
                .when()
                .get();
        responseJson = new JSONObject(response.getBody().asString());
        return responseJson;
    }

    @Test
    public void testTopStatusOK() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("ngocduy");
        driver.findElement(By.name("password")).sendKeys("123Duy");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> listStatusOK = driver.findElements(By.xpath("//p[contains(text(),'items')]"));
        List<WebElement> listHOItem = driver.findElements(By.xpath("//p[contains(text(),'/HO')]"));
        JSONObject responeJson = getData();
        List<String> listStatus = new ArrayList<>();
        List<String> listHO = new ArrayList<>();
        for (WebElement statusOK : listStatusOK) {
            listStatus.add(statusOK.getText());
        }
        for (WebElement ho : listHOItem) {
            listHO.add(ho.getText());
        }
        boolean flag = true;
        for (int i = 0;i < listHO.size();i++) {
            int item_count = 0;
            JSONObject HO_data = responeJson.getJSONObject(listHO.get(i));
            for (String key : HO_data.keySet()) {
                JSONObject item_data = HO_data.getJSONObject(key);
                if (item_data.getString("status").equals("ok")) {
                    item_count++;
                }
            }
            if (!listStatus.get(i).equals(item_count + " items")) {
                flag = false;
            }
        }
        assert flag == true;
    }
}
