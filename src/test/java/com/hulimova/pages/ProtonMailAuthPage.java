package com.hulimova.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.hulimova.util.ConfigProvider;
import com.hulimova.util.CustomConditions;

import java.time.Duration;

public class ProtonMailAuthPage extends AbstractPage {

    @FindBy(id = "username")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//div[@class='sign-layout-main-content']//button[@type='submit']")
    private WebElement signInButton;

    @FindBy(xpath = "//div[@data-testid='login:error-block']")
    private WebElement errorMessageBlock;

    @FindBy(xpath = "//button[@data-testid='dropdown-button']")
    private WebElement localizationButton;

    @FindBy(xpath = "//button[@lang='en']")
    private WebElement englishLocalizationButton;

    @FindBy(xpath = "//div[@id='id-3']//span[text()='This field is required']")
    private WebElement emailIsRequiredMessage;

    @FindBy(xpath = "//div[@id='id-4']//span[text()='This field is required']")
    private WebElement passwordIsRequiredMessage;

    public ProtonMailAuthPage(WebDriver driver) {
        super(driver);
    }

    public ProtonMailAuthPage openPage() {
        driver.get(ConfigProvider.PROTON_SERVICE_URL);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(CustomConditions.documentStateIsReady());

        return this;
    }

    public ProtonMailAuthPage setEnglishLocalization() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(localizationButton)).click();
        englishLocalizationButton.click();

        return this;
    }

    public ProtonMailMainPage login(String email, String password) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(emailInput))
                .sendKeys(email);

        passwordInput.sendKeys(password);
        signInButton.click();

        return new ProtonMailMainPage(driver);
    }

    public String getErrorMessage() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(errorMessageBlock))
                .getText();
    }

    public String getEmailIsRequiredMessage() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(emailIsRequiredMessage))
                .getText();
    }

    public String getPasswordIsRequiredMessage() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(passwordIsRequiredMessage))
                .getText();
    }
}
