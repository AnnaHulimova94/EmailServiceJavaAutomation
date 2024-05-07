package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.ProtonMailAuthPage;
import pages.ProtonMailMainPage;
import util.ConfigProvider;
import util.ScreenShotMaker;

public class ProtonMailMessageSenderTest {

    private WebDriver chromeDriver;

    @BeforeMethod
    public void setUp() {
        chromeDriver = new ChromeDriver();
    }

    @AfterMethod
    public void shutDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenShotMaker.takeScreenshot(chromeDriver, result.getMethod().getMethodName());
        }

        chromeDriver.quit();
    }

    @Test
    public void test_message_sending() {
        test_message_sending(chromeDriver);
    }

    private void test_message_sending(WebDriver driver) {
        ProtonMailMainPage protonMailMainPage = new ProtonMailAuthPage(driver)
                .openPage()
                .login(ConfigProvider.PROTON_MAIL_LOGIN_SECOND_ACCOUNT, ConfigProvider.PROTON_MAIL_PASSWORD_SECOND_ACCOUNT);

        protonMailMainPage
                .sendMail(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT,
                        ConfigProvider.SUBJECT_FOR_EMAIL,
                        ConfigProvider.TEXT_FOR_MAIL)
                .logOut();

        WebElement receivedMessage = new ProtonMailAuthPage(driver)
                .openPage()
                .login(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT, ConfigProvider.PROTON_MAIL_PASSWORD_FIRST_ACCOUNT)
                .getReceivedEmail(0);

        Assert.assertTrue(ProtonMailMainPage.isMessageUnread(receivedMessage));

        Assert.assertEquals(ConfigProvider.PROTON_MAIL_LOGIN_SECOND_ACCOUNT.split("@")[0],
                ProtonMailMainPage.getMessageSender(receivedMessage));

        Assert.assertEquals(ConfigProvider.TEXT_FOR_MAIL, protonMailMainPage
                .openMessage(receivedMessage)
                .getMessageText());
    }
}
