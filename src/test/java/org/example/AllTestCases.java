package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.junit.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class AllTestCases {
    public String  baseUrl="https://www.saucedemo.com";
    public WebDriver driver;

    @BeforeTest
    public void launchBrowser(){
        driver=new ChromeDriver();

        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test(priority = 2)
    public void verifyHomePage(){
        String expectedHomePageTitle="Swag Labs";
        String actualHomePageTitle=driver.getTitle();
        assertEquals(expectedHomePageTitle,actualHomePageTitle);
    }
    @Test(priority = 1)
    public void verifyLogin() throws InterruptedException {
        String expectedHomePageTitle="Swag Labs";
        String actualHomePageTitle=driver.getTitle();

        assertEquals(expectedHomePageTitle,actualHomePageTitle);

        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys("standard_user");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//input[@id='login-button']")).click();

        Thread.sleep(3000);
    }

    @Test(priority = 3)
    public void verifySearch() throws Exception {
        Select select=new Select(driver.
                findElement(By.xpath("//body/div[@id='root']/div[@id='page_wrapper']" +
                        "/div[@id='contents_wrapper']/div[@id='header_container']/div[2]/div[1]/span[1]/select[1]")));
        select.selectByVisibleText("Name (Z to A)");

        Thread.sleep(3000);

        JavascriptExecutor jsDriver=(JavascriptExecutor) driver;
        jsDriver.executeScript("scroll(0,200)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.
                elementToBeClickable(By.
                        xpath("//button[@id='add-to-cart-sauce-labs-bolt-t-shirt']"))).click();
        Thread.sleep(3000);
    }
    @Test(priority = 4)
    public void verifyCheckOut() throws InterruptedException {
        JavascriptExecutor jsDriver=(JavascriptExecutor) driver;
        jsDriver.executeScript("scroll(0,-200)");
        driver.
                findElement(By.
                        xpath("//body/div[@id='root']/div[@id='page_wrapper']" +
                                "/div[@id='contents_wrapper']/div[@id='header_container']/div[1]/div[3]/a[1]")).click();
        Thread.sleep(3000);

        String expectedCheckoutTitle="Your Cart";
        String actualCheckoutTitle=driver.
                findElement(By.xpath("//span[contains(text(),'Your Cart')]")).getText();

        assertEquals(expectedCheckoutTitle,actualCheckoutTitle);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.
                elementToBeClickable(By.xpath("//button[@id='checkout']"))).click();

        driver.findElement(By.xpath("//input[@id='first-name']")).sendKeys("Shivam");
        driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys("verma");
        driver.findElement(By.xpath("//input[@id='postal-code']")).sendKeys("123456");

        wait.until(ExpectedConditions.
                visibilityOf(driver.findElement(By.
                        xpath("//span[contains(text(),'Checkout: Your Information')]"))));

        String expectedCheckoutInformationTitle="Checkout: Your Information";
        String actualCheckoutInformationTitle=driver.findElement(By.
                xpath("//span[contains(text(),'Checkout: Your Information')]")).getText();

        assertEquals(expectedCheckoutInformationTitle,actualCheckoutInformationTitle);

        driver.findElement(By.xpath("//input[@id='continue']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.
                xpath("//span[contains(text(),'Checkout: Overview')]"))));

        String expectedCheckoutOverviewTitle="Checkout: Overview";
        String actualCheckoutOverviewTitle=driver.findElement(By.
                xpath("//span[contains(text(),'Checkout: Overview')]")).getText();

        assertEquals(expectedCheckoutOverviewTitle,actualCheckoutOverviewTitle);

        jsDriver.executeScript("scroll(0,200)");

        driver.findElement(By.xpath("//button[@id='finish']")).click();
        String expectedThank="Thank you for your order!";
        String actualThank=driver.findElement(By.className("complete-header")).getText();

        assertEquals(expectedThank,actualThank);
        Thread.sleep(3000);
    }
    @Test(priority = 5)
    public void verifyLogOut(){
        driver.findElement(By.xpath("//button[@id='react-burger-menu-btn']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(By.
                xpath("//a[@id='logout_sidebar_link']"))).click();

        String expectedTitle="Swag Labs";
        String actualTitle=driver.findElement(By.
                xpath("//div[contains(text(),'Swag Labs')]")).getText();

        assertEquals(expectedTitle,actualTitle);
    }
    @AfterTest
    public void closeBrowser(){
        driver.close();
    }
}