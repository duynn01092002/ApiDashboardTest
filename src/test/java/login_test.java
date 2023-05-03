import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
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
    @Test(priority = 1)
    public void testSuccessfulLogin() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
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
    public void testNoCredentialLogin() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.firefox();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get("https://www.google.com");
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }
}
