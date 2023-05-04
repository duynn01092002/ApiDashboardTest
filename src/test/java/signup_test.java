import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class signup_test {

    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");
    private final String signup_url = dotenv.get("SIGNUP_URL");

    @Test(priority = 1)
    public void TestSignUpSuccess() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("nhom06");
        driver.findElement(By.name("password")).sendKeys("Nhom06");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("email")).sendKeys("nhom06@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-admin")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.quit();
    }

    @Test(priority = 2)
    public void TestSignUpDuplicateUsername() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("nhom06");
        driver.findElement(By.name("password")).sendKeys("Test123");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("Test123@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-normal")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Username đã tồn tại");
        driver.quit();
    }

    @Test(priority = 5)
    public void TestSignUpShortPassword() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("UserTest");
        driver.findElement(By.name("password")).sendKeys("Te12");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("Email@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-normal")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Mật khẩu phải từ 6 đến 20 ký tự");
        driver.quit();
    }

    @Test(priority = 6)
    public void TestSignUpUpperAlphabetPassword() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("UserTest");
        driver.findElement(By.name("password")).sendKeys("test123");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("Email@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-normal")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Mật khẩu phải chứa ít nhất một chữ hoa");
        driver.quit();
    }

    @Test(priority = 7)
    public void TestSignUpLowerAlphabetPassword() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("UserTest");
        driver.findElement(By.name("password")).sendKeys("T12345");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("Email@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-normal")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Mật khẩu phải chứa ít nhất một chữ thường");
        driver.quit();
    }

    @Test(priority = 8)
    public void TestSignUpNumberPassword() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("UserTest");
        driver.findElement(By.name("password")).sendKeys("Testpass");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("Email@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.xpath("//input[@id='radio-normal']")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Mật khẩu phải chứa ít nhất một số");
        driver.quit();
    }

    @Test(priority = 13)
    public void TestSignUpDuplicateEmail() throws InterruptedException, MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(signup_url);
        driver.findElement(By.name("username")).sendKeys("UserTest123");
        driver.findElement(By.name("password")).sendKeys("Test123");
        driver.findElement(By.name("fullname")).sendKeys("Test Name");
        driver.findElement(By.name("age")).sendKeys("1");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.name("address")).sendKeys("Test Address");
        driver.findElement(By.name("email")).sendKeys("nhom06@gmail.com");
        driver.findElement(By.name("avatar")).sendKeys("http://link");
        driver.findElement(By.id("radio-normal")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By alertLocator = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        WebElement alert_checkCredentials = driver.findElement(alertLocator);
        String alert_text = alert_checkCredentials.getText();
        assert alert_text.equals("Email đã được sử dụng");
        driver.quit();
    }
}
