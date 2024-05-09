package tests;

import driver.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import pages.ProtonMailAuthPage;
import util.ConfigProvider;
import util.TestListener;

@Listeners(TestListener.class)
public class ProtonMailAuthTest {

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
    public void test_proton_mail_auth_response_with_empty_data() {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login("", "");

        Assert.assertEquals(ConfigProvider.FIELD_IS_REQUIRED_MESSAGE, protonMailAuthPage.getEmailIsRequiredMessage());
        Assert.assertEquals(ConfigProvider.FIELD_IS_REQUIRED_MESSAGE, protonMailAuthPage.getPasswordIsRequiredMessage());
    }

    @Test
    public void test_proton_mail_auth_response_with_incorrect_email() {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login("inccorect@proton.me", ConfigProvider.PROTON_MAIL_PASSWORD_FIRST_ACCOUNT);
        Assert.assertEquals(ConfigProvider.EMAIL_DOES_NOT_EXIST_MESSAGE, protonMailAuthPage.getErrorMessage());
    }

    @Test
    public void test_proton_mail_auth_response_with_incorrect_password() {
        ProtonMailAuthPage protonMailAuthPage = new ProtonMailAuthPage(driver)
                .openPage()
                .setEnglishLocalization();

        protonMailAuthPage.login(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT, "incorrect_password");
        Assert.assertEquals(ConfigProvider.PASSWORD_IS_INCORRECT_MESSAGE, protonMailAuthPage.getErrorMessage());
    }

    @Test
    public void test_proton_mail_successful_auth() {
        new ProtonMailAuthPage(driver)
                .openPage()
                .login(ConfigProvider.PROTON_MAIL_LOGIN_FIRST_ACCOUNT, ConfigProvider.PROTON_MAIL_PASSWORD_FIRST_ACCOUNT)
                .waitUntilInboxIsReady();

        Assert.assertTrue(driver.getCurrentUrl().contains(ConfigProvider.PROTON_SERVICE_URL));
    }
}
