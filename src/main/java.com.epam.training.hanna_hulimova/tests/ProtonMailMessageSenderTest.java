package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ProtonMailAuthPage;
import pages.ProtonMailMainPage;
import util.ConfigProvider;

public class ProtonMailMessageSenderTest {

    private WebDriver chromeDriver;

    @Before
    public void setUp() {
        chromeDriver = new ChromeDriver();
    }

    @After
    public void shutDown() {
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
