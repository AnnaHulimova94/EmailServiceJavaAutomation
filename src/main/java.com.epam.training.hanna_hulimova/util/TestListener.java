package util;

import driver.DriverSingleton;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    private Logger log = LogManager.getRootLogger();

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error(result.getThrowable().getMessage());
            takeScreenshot(DriverSingleton.getDriver(result.getTestContext()));
        }
    }

    private void takeScreenshot(WebDriver driver) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = timeStamp + ".png";
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(screenshotFile, new File("screenshots/" + fileName));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
