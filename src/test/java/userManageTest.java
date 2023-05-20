import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class userManageTest {
    private final Dotenv dotenv = Dotenv.load();
    private final String selenium_grid_url = dotenv.get("SELENIUM_GRID_URL");
    private final String home_url = dotenv.get("HOME_URL");
    private final String login_url = dotenv.get("LOGIN_URL");

    @Test(priority = 1)
    public void testViewUserManagePage() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.manage().window().maximize();
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//a[@class='collapsed']//span[@class='text']")).click();
        driver.findElement(By.xpath("//a[@href='/admin/user-manage/']")).click();
        List<WebElement> elementEmailList = driver.findElements(By.xpath("//p//a[@href='#0']"));
        List<String> stringEmailList = new ArrayList<>();
        for (WebElement element : elementEmailList) {
            stringEmailList.add(element.getText());
        }
        assert stringEmailList.contains("admin@gmail.com");
        driver.quit();
    }

    @Test(priority = 2)
    public void testUpdateUser() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.manage().window().maximize();
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//a[@class='collapsed']//span[@class='text']")).click();
        driver.findElement(By.xpath("//a[@href='/admin/user-manage/']")).click();
        List<WebElement> elementEmailList = driver.findElements(By.xpath("//p//a[@href='#0']"));
        List<String> stringEmailList = new ArrayList<>();
        for (WebElement element : elementEmailList) {
            stringEmailList.add(element.getText());
        }
        int pos = 0;
        for (int i = 0;i < stringEmailList.size();i++) {
            if (stringEmailList.get(i).equals("nhom06@gmail.com")) {
                pos = i + 1;
            }
        }
        driver.findElement(By.xpath("//button[@class='text-primary'][@data-userid="+pos+"]")).click();
        driver.findElement(By.name("fullname")).clear();
        driver.findElement(By.name("fullname")).sendKeys("edited");
        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
        assert driver.findElement(By.xpath("//p[contains(text(),'edited')]")) != null;
        driver.quit();
    }

    @Test(priority = 3)
    public void testDeleteUser() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.manage().window().maximize();
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//a[@class='collapsed']//span[@class='text']")).click();
        driver.findElement(By.xpath("//a[@href='/admin/user-manage/']")).click();
        List<WebElement> elementEmailList = driver.findElements(By.xpath("//p//a[@href='#0']"));
        int oldSize = elementEmailList.size();
        List<String> stringEmailList = new ArrayList<>();
        for (WebElement element : elementEmailList) {
            stringEmailList.add(element.getText());
        }
        int pos = 0;
        for (int i = 0;i < stringEmailList.size();i++) {
            if (stringEmailList.get(i).equals("nhom06@gmail.com")) {
                pos = i + 1;
            }
        }
        driver.findElement(By.xpath("//button[@class='text-danger'][@data-userid="+pos+"]")).click();
        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
        oldSize -= 1;
        List<WebElement> newElementEmailList = driver.findElements(By.xpath("//p//a[@href='#0']"));
        int newSize = newElementEmailList.size();
        assert oldSize == newSize;
        driver.quit();
    }

    @Test(priority = 4)
    public void testRecoveryAllUser() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL(selenium_grid_url);
        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
        driver.get(login_url);
        driver.manage().window().maximize();
        // Dang nhap va chuyen huong de trang quan ly nguoi dung
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//a[@class='collapsed']//span[@class='text']")).click();
        driver.findElement(By.xpath("//a[@href='/admin/user-manage/']")).click();
        // Lay so luong user ban dau, trong thung rac va sau khi khoi phuc
        int oldSize = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
        driver.findElement(By.xpath("//i[@class='lni lni-trash-can']")).click();
        int inBinSize = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
        // Thuc hien khoi phuc nguoi dung
        driver.findElement(By.xpath("//a[@onclick='recoveryUserDeleted()']")).click();
        driver.findElement(By.xpath("//button[contains(text(), 'Yes')]")).click();
        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
        // Tro lai trang nguoi dung
        driver.findElement(By.xpath("//a[text()='User manage']")).click();
        int newSize = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
        assert newSize == oldSize + inBinSize;
        driver.quit();
    }
//    @Test(priority = 5)
//    public void testPermanentlyDelete() throws MalformedURLException {
//        DesiredCapabilities dc = DesiredCapabilities.chrome();
//        URL url = new URL(selenium_grid_url);
//        RemoteWebDriver driver = new RemoteWebDriver(url,dc);
//        driver.get(login_url);
//        driver.manage().window().maximize();
//        driver.findElement(By.name("username")).sendKeys("admin");
//        driver.findElement(By.name("password")).sendKeys("admin");
//        driver.findElement(By.xpath("//button[@type='submit']")).click();
//        driver.findElement(By.xpath("//a[@class='collapsed']//span[@class='text']")).click();
//        driver.findElement(By.xpath("//a[@href='/admin/user-manage']")).click();
//        // lay so luong user ban dau, trong thung rac va sau khi xoa vinh vien
//        int oldSize = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
//        driver.findElement(By.xpath("//i[@class='lni lni-trash-can']")).click();
//        int inBinSize1 = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
//        // Thuc hien xoa vinh vien
//        driver.findElement(By.xpath("//a[@onclick='deleteAllUserDeleted()']")).click();
//        driver.findElement(By.xpath("//button[contains(text(), 'Yes')]")).click();
//        driver.findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']")).click();
//        int inBinSize2 = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
//        driver.findElement(By.xpath("//a[text()='User manage']")).click();
//        int newSize = driver.findElements(By.xpath("//p//a[@href='#0']")).size();
//        assert oldSize == newSize && inBinSize1 - inBinSize1 == inBinSize2;
//    }
}
