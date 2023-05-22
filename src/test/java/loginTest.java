import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class loginTest {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");

    @Test(priority = 1)
    public void testSuccessfulLogin() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assert driver.getCurrentUrl().equals(home_url);
        driver.quit();
    }

    @Test(priority = 2)
    public void testNoCredentialLogin() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("");
        driver.findElement(By.name("password")).sendKeys("");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        String validation_message = driver.findElement(By.name("username")).getAttribute("validationMessage");
        assert validation_message.contains("Please fill out this field.");

        driver.quit();
    }

    @Test(priority = 3)
    public void testInvalidCredential() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.manage().window().maximize();
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("123D");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_invalid_credential = driver.findElement(By.xpath("//div[@class='alert alert-danger']"));
        assert alert_invalid_credential.getText().contains("Invalid");
        driver.quit();
    }

    @Test(priority = 4)
    public void testAccessDenied() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("123");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_access_denied = driver.findElement(By.xpath("//div[@class='alert alert-danger']"));
        assert alert_access_denied.getText().contains("Access");
        driver.quit();
    }

    @Test(priority = 5)
    public void testLogout() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//img[@src='https://www.clipartmax.com/png/middle/319-3191274_male-avatar-admin-profile.png']")).click();
        driver.findElement(By.xpath("//a[text()=' Sign Out ']")).click();
        assert driver.getCurrentUrl().equals(login_url);
        driver.quit();
    }
}
