import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestNG {
    String HOST = "http://ec2-54-204-158-246.compute-1.amazonaws.com/";
    String publicIP = "http://54.204.158.246";
    String privateIP = "http://172.31.27.247";
    String login_url = publicIP + ":50001/auth/login";
    String home_url = publicIP + ":50001/";
    String selenium_grid = publicIP + ":4444/wd/hub";
    String signup_url = "http://127.0.0.1:50001/auth/signup";
    String profile_url = "http://127.0.0.1:50001/profile";
    @Test(priority = 1)
    public void test01() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("ngocduy");
        driver.findElement(By.name("password")).sendKeys("123Duy");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assert driver.getCurrentUrl().equals(home_url);
        driver.quit();
    }

    @Test(priority = 2)
    public void test02() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.firefox();
        URL url = new URL(selenium_grid);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get("https://www.google.com");
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }
}
