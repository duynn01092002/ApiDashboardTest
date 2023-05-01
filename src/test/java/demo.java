import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class demo {
    public static void main(String[] args) {
        System.out.println("Hello world");
        WebDriver driver;

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.get("https://google.com");

        driver.quit();
    }
}
