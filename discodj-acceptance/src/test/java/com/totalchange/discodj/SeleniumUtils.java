package com.totalchange.discodj;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumUtils {
    private static SeleniumUtils instance = new SeleniumUtils();
    
    public WebDriver getWebDriver() {
        return new FirefoxDriver();
    }
    
    public static SeleniumUtils getInstance() {
        return instance;
    }
}
