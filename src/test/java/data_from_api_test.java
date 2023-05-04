import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import org.testng.reporters.XMLConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class data_from_api_test {
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
    public void testTopStatusOK() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("nhom06");
        driver.findElement(By.name("password")).sendKeys("Nhom06");
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
    @Test(priority = 2)
    public void testTotal_HO() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("duan");
        driver.findElement(By.name("password")).sendKeys("Duan123");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> totalHOWeb = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalHOText = totalHOWeb.get(0).getText(); // lấy giá trị text của thẻ h3
        String[] totalHOArray = totalHOText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalHO = totalHOArray[0]; // lấy giá trị số (1287) totalHO
        System.out.println("Actual Total HO value: " + actualTotalHO);
        JSONObject responeJson = getData();
        String expectedTotalHO  = String.valueOf(responeJson.length());
        System.out.println("Expected Total HO value: " + expectedTotalHO);
        assert expectedTotalHO.equals(actualTotalHO);
    }
    @Test(priority = 3)
    public void testTotal_children_in_HO() throws MalformedURLException {
        getDataFromAPI();
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("duan");
        driver.findElement(By.name("password")).sendKeys("Duan123");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalChildrenInHOText = webElements.get(1).getText(); // lấy giá trị text của thẻ h3
        String[] totalChildrenInHOArray = totalChildrenInHOText.split(" "); // tách chuỗi bằng dấu cách
        String actualChildrenInHO = totalChildrenInHOArray[0]; // lấy giá trị số Total Children in HO
        System.out.println("Actual Total Children in HO value: " + actualChildrenInHO);
        String expectedChildrenInHO = String.valueOf(totalItemInHO);
        System.out.println("Expected Total Children in HO value: " + expectedChildrenInHO);
        assert expectedChildrenInHO.equals(actualChildrenInHO);
    }
    @Test(priority = 4)
    public void testTotal_Status_OK() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("duan");
        driver.findElement(By.name("password")).sendKeys("Duan123");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalStatusOkText = webElements.get(2).getText(); // lấy giá trị text của thẻ h3
        String[] totalStatusOkArray = totalStatusOkText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalStatusOk = totalStatusOkArray[0]; // lấy giá trị số Total Status OK
        System.out.println("Actual Total Status OK value: " + actualTotalStatusOk);
        getDataFromAPI();
        String expectedTotalStatusOk = String.valueOf(totalStatusOK);
        System.out.println("Expected Total Status OK value: " + expectedTotalStatusOk);
        assert expectedTotalStatusOk.equals(actualTotalStatusOk);
    }
    @Test(priority = 5)
    public void testTotal_Status_Fail() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);

        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("duan");
        driver.findElement(By.name("password")).sendKeys("Duan123");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//h3[@class='text-bold mb-10']"));
        String totalStatusFailText = webElements.get(3).getText(); // lấy giá trị text của thẻ h3
        String[] totalStatusFailArray = totalStatusFailText.split(" "); // tách chuỗi bằng dấu cách
        String actualTotalStatusFail = totalStatusFailArray[0]; // lấy giá trị số Total Status Fail
        System.out.println("Actual Total Status Fail value: " + actualTotalStatusFail);
        getDataFromAPI();
        String expectedTotalStatusFail = String.valueOf(totalStatusFail);
        System.out.println("Expected Total Status Fail value: " + expectedTotalStatusFail);
        assert expectedTotalStatusFail.equals(actualTotalStatusFail);
    }
}
