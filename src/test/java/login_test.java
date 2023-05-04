import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class login_test {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");
//    @Test(priority = 1)
//    public void testSuccessfulLogin() throws InterruptedException, MalformedURLException {
//        DesiredCapabilities dc = DesiredCapabilities.chrome();
//        URL url = new URL(selenium_grid_url);
//        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
//        driver.get(login_url);
//        driver.findElement(By.name("username")).sendKeys("test2");
//        driver.findElement(By.name("password")).sendKeys("Test123");
//        driver.findElement(By.name("user")).click();
//        driver.findElement(By.xpath("//button[@type='submit']")).click();
//        assert driver.getCurrentUrl().equals(home_url);
//        driver.quit();
//    }

    @Test(priority = 2)
    public void testNoCredentialLogin() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("");
        driver.findElement(By.name("password")).sendKeys("");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_message1 = driver.findElement(By.xpath("//span[@id='errorMsg1']"));
        WebElement alert_message2 = driver.findElement(By.xpath("//span[@id='errorMsg2']"));
        assert alert_message1.getText().contains("tên đăng nhập") && alert_message2.getText().contains("mật khẩu");
        driver.quit();
    }

    @Test(priority = 3)
    public void testRoleIsChecked() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("ngocduy");
        driver.findElement(By.name("password")).sendKeys("123Duy");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_role_is_checked = driver.findElement(By.xpath("//div[@class='alert alert-danger']"));
        assert alert_role_is_checked.getText().contains("checkbox Role");
        driver.quit();
    }
    @Test(priority = 4)
    public void testInvalidCredential() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("ngocduy");
        driver.findElement(By.name("password")).sendKeys("123D");
        driver.findElement(By.name("admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_invalid_credential = driver.findElement(By.xpath("//div[@class='alert alert-danger']"));
        assert alert_invalid_credential.getText().contains("Invalid");
        driver.quit();
    }

    @Test(priority = 5)
    public void testAccessDenied() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("ngocduy");
        driver.findElement(By.name("password")).sendKeys("123Duy");
        driver.findElement(By.name("user")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert_access_denied = driver.findElement(By.xpath("//div[@class='alert alert-danger']"));
        assert alert_access_denied.getText().contains("Access");
        driver.quit();
    }
}
