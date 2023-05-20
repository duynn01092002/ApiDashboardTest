import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class chartTest {

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
                .header("Content-Type", "application/json")
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

    public static List<String> getRefValuesForChart1() {
        String highestDate = "2022-12-09";
        String lowestDate = "2022-12-03";
        int cntHighestDate = 0;
        int cntLowestDate = 0;
        JSONObject responseData = getData();
        for (String key : responseData.keySet()) {
            JSONObject HO_Item = responseData.getJSONObject(key);
            for (String item_key : HO_Item.keySet()) {
                JSONObject item_data = HO_Item.getJSONObject(item_key);
                String dateTimeString = item_data.getString("date");
                if (dateTimeString.contains(highestDate))
                    cntHighestDate++;
                else if (dateTimeString.contains(lowestDate)) {
                    cntLowestDate++;
                }
            }
        }
        highestDate = highestDate + "=" + cntHighestDate;
        lowestDate = lowestDate + "=" + cntLowestDate;
        List<String> refDates = new ArrayList<>();
        refDates.add(highestDate);
        refDates.add(lowestDate);
        return refDates;
    }

    public static Map<String, Integer> formatChart4Data(String data) {
        String[] pairs = data.split(", ");
        String newData = "";
        for (int i = 0; i < pairs.length; i++) {
            if (i == pairs.length - 1)
                break;
            newData += "\n" + pairs[i];
        }
        Type mapType = new TypeToken<Map<String, Map<String, Integer>>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, Map<String, Integer>> jsonData = gson.fromJson(data, mapType);

        List<String> keys = new ArrayList<>(jsonData.keySet());
        String standardKey = "";
        Map<String, Integer> firstValue = jsonData.get(keys.get(0));
        int standardCount = firstValue.get("fail");
        for (int i = 1; i < keys.size(); i++) {
            if (standardCount <= jsonData.get(keys.get(i)).get("fail")) {
                standardCount = jsonData.get(keys.get(i)).get("fail");
                standardKey = keys.get(i);
            }
        }
        Map<String, Integer> standardValue = new HashMap<>();
        standardValue.put(standardKey, standardCount);
        return standardValue;
    }

    public static Map<String, Integer> getRefValueForChart4() {
        String refKey = "2022-12-09";
        Integer refCount = 0;
        JSONObject responseData = getData();
        for (String key : responseData.keySet()) {
            JSONObject HO_Item = responseData.getJSONObject(key);
            for (String item_key : HO_Item.keySet()) {
                JSONObject item_data = HO_Item.getJSONObject(item_key);
                String dateTimeString = item_data.getString("date");
                String statusString = item_data.getString("status");
                if (dateTimeString.contains(refKey) && statusString.equals("fail"))
                    refCount++;
            }
        }
        Map<String, Integer> refValue = new HashMap<>();
        refValue.put(refKey,refCount);
        return refValue;
    }

    public static Map<Double, Integer> formatChart5Data(String data) {
        Double standardKey = 0.0;
        Integer standardCount = 0;

        data = data.replaceAll("[{} ]", "");

        // Tạo một map để lưu trữ kết quả
        Map<Double, Integer> map = new HashMap<>();

        // Tách chuỗi thành các cặp key-value
        String[] pairs = data.split(",");

        // Xử lý từng cặp key-value
        for (String pair : pairs) {
            // Tách key và value
            String[] keyValue = pair.split("=");

            // Chuyển đổi key và value sang kiểu Double và Integer
            Double key = Double.parseDouble(keyValue[0]);
            Integer value = Integer.parseInt(keyValue[1]);

            // Thêm key và value vào map
            map.put(key, value);
        }
        List<Double> keys = new ArrayList<>(map.keySet());
        standardCount = map.get(keys.get(0));
        for (int i = 1;i < map.size();i++) {
            if (standardCount <= map.get(keys.get(i))) {
                standardCount = map.get(keys.get(i));
                standardKey = keys.get(i);
            }
        }
        Map<Double, Integer> standardValue = new HashMap<>();
        standardValue.put(standardKey,standardCount);
        return standardValue;
    }

    public static Map<Double, Integer> getRefValueForChart5() {
        double refKey = 14.2;
        Integer refCount = 0;
        JSONObject responseData = getData();
        for (String key : responseData.keySet()) {
            JSONObject HO_Item = responseData.getJSONObject(key);
            for (String item_key : HO_Item.keySet()) {
                JSONObject item_data = HO_Item.getJSONObject(item_key);
                JSONArray toProceedResult = item_data.getJSONArray("predict_result");
                String statusString = item_data.getString("status");
                List<Double> predictResult = new ArrayList<>();
                for (int i = 0;i<toProceedResult.length();i++) {
                    if (statusString.equals("fail")) {
                        try {
                            double value = toProceedResult.getDouble(i);
                            predictResult.add(value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                for (double result : predictResult) {
                    if (result == refKey)
                        refCount++;
                }
            }
        }
        Map<Double, Integer> refValue = new HashMap<>();
        refValue.put(refKey,refCount);
        return refValue;
    }

    @Test(priority = 1)
    public void chart1Test() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String data = ((JavascriptExecutor) driver).executeScript("return getDataForChart1AndChart2();").toString();
        String[] parts = data.split(", ");

        List<String> refDates = getRefValuesForChart1();
        String highestDate = refDates.get(0);
        String lowestDate = refDates.get(1);
        boolean flag1 = false;
        boolean flag2 = false;

        for (String part : parts) {
            if (part.equals(highestDate) || part.contains(highestDate))
                flag1 = true;
        }
        for (String part : parts) {
            if (part.equals(lowestDate) || part.contains(lowestDate))
                flag2 = true;
        }
        assert flag1 && flag2;

        driver.quit();
    }

    @Test(priority = 2)
    public void chart4Test() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String data = ((JavascriptExecutor) driver).executeScript("return getDataForChart4();").toString();

        Map<String, Integer> standardValue = formatChart4Data(data);
        Map<String, Integer> refValue = getRefValueForChart4();

        assert standardValue.equals(refValue);

        driver.quit();
    }

    @Test(priority = 3)
    public void chart5Test() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String data = ((JavascriptExecutor) driver).executeScript("return getDataForChart5();").toString();
        Map<Double, Integer> standardValue = formatChart5Data(data);
        Map<Double, Integer> refValue = getRefValueForChart5();
        assert standardValue.equals(refValue);
        driver.quit();
    }
}
