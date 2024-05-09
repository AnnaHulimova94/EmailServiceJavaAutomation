package pages;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.CustomConditions;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class ProtonMailMainPage extends AbstractPage {

    @FindBy(xpath = "//div[@class='sidebar flex flex-nowrap flex-column no-print outline-none']")
    private WebElement sideBar;

    @FindBy(xpath = "//button[@data-testid='sidebar:compose']")
    private WebElement createMessageButton;

    @FindBy(xpath = "//input[@data-testid='composer:to']")
    private WebElement receiverMailInput;

    @FindBy(xpath = "//input[@data-testid='composer:subject']")
    private WebElement subjectInput;

    @FindBy(xpath = "//iframe[@data-testid='rooster-iframe']")
    private WebElement mailTextFrame;

    @FindBy(xpath = "//iframe[@data-testid='content-iframe']")
    private WebElement mailContentFrame;

    @FindBy(id = "rooster-editor")
    private WebElement mailTextContainerEditor;

    @FindBy(xpath = "//div[@id='proton-root']")
    private WebElement receivedMailText;

    @FindBy(xpath = "//button[@data-testid='composer:send-button']")
    private WebElement sendMailButton;

    @FindBy(xpath = "//span[@class='m-auto']")
    private WebElement actionSelect;

    @FindBy(xpath = "//button[@data-testid='userdropdown:button:logout']")
    private WebElement logOutButton;

    @FindBy(xpath = "//span[text()='Message sent.']")
    private WebElement messageSendNotification;

    @FindAll(@FindBy(xpath = "//div[@data-shortcut-target='item-container']"))
    private List<WebElement> receivedEmailList;

    @FindBy(xpath = "//article[@data-testid='message-view-2']//span[@data-testid='recipient-label']")
    private WebElement messageSender;

    private static final String UNREAD_MESSAGE_STYLE = "unread";

    public ProtonMailMainPage(WebDriver driver) {
        super(driver);
    }

    public ProtonMailMainPage waitUntilInboxIsReady() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.urlContains("/inbox"));

        return this;
    }

    public ProtonMailMainPage sendMail(String receiverMail, String subjectForEmail, String text) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(createMessageButton))
                .click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(receiverMailInput))
                .sendKeys(receiverMail);

        subjectInput.sendKeys(subjectForEmail);

        driver.switchTo().frame(mailTextFrame);
        mailTextContainerEditor.clear();
        mailTextContainerEditor.sendKeys(text);

        driver.switchTo().defaultContent();
        sendMailButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(messageSendNotification));

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(messageSendNotification));

        return this;
    }

    public ProtonMailAuthPage logOut() {
        actionSelect.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(logOutButton))
                .click();

        return new ProtonMailAuthPage(driver);
    }

    public WebElement getReceivedEmail(int index) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait
                .until(ExpectedConditions.visibilityOfAllElements(receivedEmailList))
                .get(index);
    }

    public static boolean isMessageUnread(WebElement webElement) {
        return webElement.getAttribute("class").contains(UNREAD_MESSAGE_STYLE);
    }

    public ProtonMailMainPage openMessage(WebElement webElement) {
        webElement.click();

        return this;
    }

    public String getMessageSender() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(CustomConditions.elementHasText(messageSender));

        return messageSender.getText();
    }

    public String getMessageText() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(mailContentFrame));

        driver.switchTo().frame(mailContentFrame);

        return receivedMailText.getText();
    }
}
