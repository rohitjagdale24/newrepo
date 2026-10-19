package com.rohit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class WebsiteFunctionalTest {
    private static final String URL = "https://rohit-jagdale.vercel.app/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void openWebsite() {
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hero-title")));
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void homePageShowsExpectedIntroduction() {
        assertEquals("Rohit Jagdale — QA Automation & AI Quality Engineer", driver.getTitle());
        assertEquals("Confidence in every release.", normalizedText(By.id("hero-title")));
        assertTrue(driver.findElement(By.id("home")).getText().contains(
                "I’m Rohit Jagdale. I test the workflows, integrations, and AI responses that people depend on."));
    }

    @Test
    void workNavigationOpensSelectedWorkSection() {
        driver.findElement(By.cssSelector("a[href='#work']")).click();

        wait.until(ExpectedConditions.urlContains("#work"));
        assertEquals("Quality goes deeper than a passing test.", normalizedText(By.id("work-title")));
    }

    @Test
    void aiResponseTabShowsItsExplanation() {
        driver.findElement(By.id("tab-responses")).click();

        wait.until(ExpectedConditions.attributeToBe(By.id("tab-responses"), "aria-selected", "true"));
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("panel-responses")));
        assertTrue(panel.getText().contains("A response should make sense, too."));
        assertTrue(panel.getText().contains("Check that explanations address the observed sales movement"));
    }

    @Test
    void apiWorkDetailsExpandAndShowExpectedText() {
        WebElement details = driver.findElement(By.cssSelector("#work details"));
        assertNull(details.getDomAttribute("open"));

        details.findElement(By.tagName("summary")).click();

        wait.until(d -> details.getDomAttribute("open") != null);
        assertTrue(details.getText().contains("Automated API regression and sanity tests with Karate"));
    }

    @Test
    void automationFilterShowsOnlyAutomationCategories() {
        WebElement filter = driver.findElement(By.cssSelector("button[data-filter='automation']"));
        filter.click();

        wait.until(ExpectedConditions.attributeToBe(filter, "aria-pressed", "true"));
        List<WebElement> visibleRows = driver.findElements(By.cssSelector("#expertise .skill-row:not([hidden])"));
        assertEquals(2, visibleRows.size());
        assertEquals("Web & mobile automation", visibleRows.get(0).findElement(By.tagName("h3")).getText());
        assertEquals("API testing", visibleRows.get(1).findElement(By.tagName("h3")).getText());
        assertEquals("2 expertise categories shown.",
                driver.findElement(By.id("filter-status")).getDomProperty("textContent").trim());
    }

    @Test
    void contactNavigationShowsContactTextAndEmailLink() {
        driver.findElement(By.cssSelector("a[href='#contact']")).click();

        wait.until(ExpectedConditions.urlContains("#contact"));
        assertEquals("Building something that needs to work?", normalizedText(By.id("contact-title")));
        WebElement email = driver.findElement(
                By.cssSelector("#contact a[href='mailto:rohitjagdale24@gmail.com']"));
        assertTrue(email.getText().contains("rohitjagdale24@gmail.com"));
        assertTrue(email.getAttribute("href").startsWith("mailto:rohitjagdale24@gmail.com"));
    }

    private String normalizedText(By locator) {
        return driver.findElement(locator).getText().replaceAll("\\s+", " ").trim();
    }
}
