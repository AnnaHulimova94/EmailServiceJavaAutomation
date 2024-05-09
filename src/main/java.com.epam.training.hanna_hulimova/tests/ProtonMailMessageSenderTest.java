package tests;

import driver.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import pages.ProtonMailAuthPage;
import pages.ProtonMailMainPage;
import util.ConfigProvider;
import util.TestListener;

@Listeners(TestListener.class)
public class ProtonMailMessageSenderTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp(ITestContext context) {
        driver = DriverSingleton.getDriver(context);
    }

    @AfterMethod
    public void shutDown() {
        DriverSingleton.closeDriver();
    }

    @Test
    public void test_message_sending() {
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
                .waitUntilInboxIsReady()
                .getReceivedEmail(0);

        Assert.assertTrue(ProtonMailMainPage.isMessageUnread(receivedMessage));

        protonMailMainPage.openMessage(receivedMessage);

        Assert.assertEquals(ConfigProvider.PROTON_MAIL_LOGIN_SECOND_ACCOUNT.split("@")[0],
                protonMailMainPage.getMessageSender());

        Assert.assertEquals(ConfigProvider.TEXT_FOR_MAIL, protonMailMainPage.getMessageText());
    }
}
