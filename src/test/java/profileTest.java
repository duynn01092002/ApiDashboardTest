import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class profileTest {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");
    private final String profile_url = dotenv.get("PROFILE_URL");
    @Test(priority = 1)
    public void testViewProfile() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//img[@src='https://www.clipartmax.com/png/middle/319-3191274_male-avatar-admin-profile.png']")).click();
        driver.findElement(By.xpath("//a[@href='/profile']")).click();

        assert driver.getCurrentUrl().equals(profile_url);
        driver.quit();
    }
}
