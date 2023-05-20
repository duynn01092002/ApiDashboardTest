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
import java.util.Iterator;
import java.util.List;

public class overviewDataTest {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");
    int totalItemInHO = 0;
    int totalStatusOK = 0;
    int totalStatusFail = 0;
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
    public void getDataFromAPI() {
        JSONObject data = getData();
        this.totalItemInHO = 0;
        this.totalStatusOK = 0;
        this.totalStatusFail = 0;
        Iterator<String> keys = data.keys();
        while (keys.hasNext()) {
            String keyHO = keys.next();
            JSONObject childrenDict = data.getJSONObject(keyHO);
            this.totalItemInHO += childrenDict.length();
            Iterator<String> hashKeys = childrenDict.keys();
            while (hashKeys.hasNext()) {
                String hashKey = hashKeys.next();
                JSONObject itemDict = childrenDict.getJSONObject(hashKey);
                if (itemDict.getString("status").equals("ok")) {
                    totalStatusOK++;
                } else {
                    totalStatusFail++;
                }
            }
        }
    }

    @Test(priority = 1)
    public void testTotal_HO() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> totalHOWeb = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalHOText = totalHOWeb.get(0).getText(); // lấy giá trị text của thẻ h3
        String[] totalHOArray = totalHOText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalHO = totalHOArray[0]; // lấy giá trị số (1287) totalHO
        JSONObject responeJson = getData();
        String expectedTotalHO  = String.valueOf(responeJson.length());
        assert expectedTotalHO.equals(actualTotalHO);
        driver.quit();
    }
    @Test(priority = 2)
    public void testTotal_children_in_HO() throws MalformedURLException {
        getDataFromAPI();
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalChildrenInHOText = webElements.get(1).getText(); // lấy giá trị text của thẻ h3
        String[] totalChildrenInHOArray = totalChildrenInHOText.split(" "); // tách chuỗi bằng dấu cách
        String actualChildrenInHO = totalChildrenInHOArray[0]; // lấy giá trị số Total Children in HO
        String expectedChildrenInHO = String.valueOf(totalItemInHO);
        assert expectedChildrenInHO.equals(actualChildrenInHO);
        driver.quit();
    }
    @Test(priority = 3)
    public void testTotal_Status_OK() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalStatusOkText = webElements.get(2).getText(); // lấy giá trị text của thẻ h3
        String[] totalStatusOkArray = totalStatusOkText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalStatusOk = totalStatusOkArray[0]; // lấy giá trị số Total Status OK
        getDataFromAPI();
        String expectedTotalStatusOk = String.valueOf(totalStatusOK);
        assert expectedTotalStatusOk.equals(actualTotalStatusOk);
        driver.quit();
    }
    @Test(priority = 4)
    public void testTotal_Status_Fail() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalStatusFailText = webElements.get(3).getText(); // lấy giá trị text của thẻ h3
        String[] totalStatusFailArray = totalStatusFailText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalStatusFail = totalStatusFailArray[0]; // lấy giá trị số Total Status Fail
        getDataFromAPI();
        String expectedTotalStatusFail = String.valueOf(totalStatusFail);
        assert expectedTotalStatusFail.equals(actualTotalStatusFail);
        driver.quit();
    }
}
