package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ProtonMailAuthPage;
import util.ConfigProvider;

public class ProtonMailAuthTest {

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
    public void test_proton_mail_successful_auth() {
        test_proton_mail_successful_auth(chromeDriver);
    }

    @Test
    public void test_proton_mail_auth_response_with_incorrect_password() {
        test_proton_mail_auth_response_with_incorrect_password(chromeDriver);
    }

    @Test
    public void test_proton_mail_auth_response_with_incorrect_email() {
        test_proton_mail_auth_response_with_incorrect_email(chromeDriver);
    }

    @Test
    public void test_proton_mail_auth_response_with_empty_data() {
        test_proton_mail_auth_response_with_empty_data(chromeDriver);
    }

    private void test_proton_mail_auth_response_with_empty_data(WebDriver driver) {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login("", "");

        Assert.assertEquals(ConfigProvider.FIELD_IS_REQUIRED_MESSAGE, protonMailAuthPage.getEmailIsRequiredMessage());
        Assert.assertEquals(ConfigProvider.FIELD_IS_REQUIRED_MESSAGE, protonMailAuthPage.getPasswordIsRequiredMessage());
    }

    private void test_proton_mail_auth_response_with_incorrect_email(WebDriver driver) {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login("inccorect@proton.me", ConfigProvider.PROTON_MAIL_PASSWORD_FIRST_ACCOUNT);
        Assert.assertEquals(ConfigProvider.EMAIL_DOES_NOT_EXIST_MESSAGE, protonMailAuthPage.getErrorMessage());
    }

    private void test_proton_mail_auth_response_with_incorrect_password(WebDriver driver) {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT, "incorrect_password");
        Assert.assertEquals(ConfigProvider.PASSWORD_IS_INCORRECT_MESSAGE, protonMailAuthPage.getErrorMessage());
    }

    private void test_proton_mail_successful_auth(WebDriver driver) {
        new ProtonMailAuthPage(driver)
                .openPage()
                .login(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT, ConfigProvider.PROTON_MAIL_PASSWORD_FIRST_ACCOUNT)
                .waitUntilInboxIsReady();

        Assert.assertTrue(driver.getCurrentUrl().contains(ConfigProvider.PROTON_SERVICE_URL));
    }
}
