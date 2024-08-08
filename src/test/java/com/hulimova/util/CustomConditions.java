package com.hulimova.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class CustomConditions {

    public static ExpectedCondition<Boolean> documentStateIsReady(){
        return driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete");
    }

    public static ExpectedCondition<Boolean> elementHasText(WebElement element) {
        return driver -> {
            try {
                return !element.getText().isEmpty();
            } catch (Exception e) {
                return false;
            }
        };
    }
}
