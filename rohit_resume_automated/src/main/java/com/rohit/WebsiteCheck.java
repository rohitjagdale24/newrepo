package com.rohit;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebsiteCheck {
    private static final String URL = "https://rohit-jagdale.vercel.app/";

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.get(URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement body = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            if (!driver.getCurrentUrl().startsWith(URL) || !body.isDisplayed()) {
                throw new IllegalStateException("The requested website did not load correctly.");
            }

            System.out.println("Page title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Website opened successfully.");
        } finally {
            driver.quit();
        }
    }
}
