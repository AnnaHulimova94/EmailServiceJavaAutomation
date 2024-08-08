package com.hulimova.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;

public class DriverSingleton {

    private static WebDriver driver;

    public static WebDriver getDriver(ITestContext context) {
        if (driver == null) {
            switch (context.getCurrentXmlTest().getParameter("environment")) {
                case "firefox": {
                    driver = new FirefoxDriver();
                    break;
                }
                default: {
                    driver = new ChromeDriver();
                    break;
                }
            }
        }

        driver.manage().window().maximize();

        return driver;
    }

    public static void closeDriver() {
        driver.quit();
        driver = null;
    }
}
