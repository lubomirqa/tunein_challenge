package com.tunein.pageclasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import java.util.List;

public class MainPage {

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver driver;
    private final String BASE_URL = "https://tunein.com/?abtest=5015&homecarouselautoscroll=false";
    private final String BASE_URL_AUTO_SCROLL = "https://tunein.com/?abtest=5015";
    private final String SPANISH_URL = BASE_URL + "&lang=es";
    private final String SLIDER_DOTS = "//div[@data-testid='carouselHeader']/ul/li";
    private final String SLIDER_DOT = "//li[@data-testid='carouselControlDot-']";
    private final String DOT_TEXT_TO_CHANGE = "carouselControlDot-";
    private final String CAROUSEL_TEXT = "//div[@data-testid='heroCarouselTextContainer']" ;
    private final String CAROUSEL_TEXT_TO_CHANGE = "heroCarouselTextContainer";
    private final String LINE = "//a[@data-testid='line']";
    private final String LINE_TO_CHANGE = "line";
    private final String ACCEPT_COOKIES = "//button[text()='I Accept']";
    private final String SPANISH_TITLE = "//a[@data-testid='topSectionLink-0']/span";
    private final String HERO_IMAGE = "//li[contains(@class, 'selected')]//div[@data-testid='heroImage']";
    private final String HERO_IMAGE_LINK = "//li[contains(@class, 'selected')]/a";
    private final String TILE_IMAGE = "//a[@data-testid='carouselTileLinkOverlay' and @data-isactive='true']";
    private final String SLIDE_ELEMENT = "//div[contains(@class, 'landing-module__topSection')]";

    public enum Language {
        SPANISH
    }

    public void openMainPage(boolean scrollAuto) {
        if (scrollAuto) {
            driver.get(BASE_URL_AUTO_SCROLL);
        } else {
            driver.get(BASE_URL);
        }
    }

    public void openMainPage(Language language) {
        switch(language) {
            case SPANISH:
                driver.get(SPANISH_URL);
        }
    }

    public void acceptCookies() {
        if(driver.findElements(By.xpath(ACCEPT_COOKIES)).size() != 0){
            driver.findElement(By.xpath(ACCEPT_COOKIES)).click();
        }
    }

    public int getCarouselSlidesAmount() {
        List<WebElement> slideDotList = driver.findElements(By.xpath(SLIDER_DOTS));
        return slideDotList.size();
    }

    public String removeAndAppendNumberToXpath(String xpath, String textToChange, int number) {
        xpath = xpath.replaceAll("\\d","");
        xpath = xpath.replace(textToChange, textToChange + number);
        return xpath;
    }

    public void clickNextSlide(int slideNumber) {
        String nextDot = removeAndAppendNumberToXpath(SLIDER_DOT, DOT_TEXT_TO_CHANGE, slideNumber);
        driver.findElement(By.xpath(nextDot))
                .click();
    }

    public WebElement getSlideLineElement(int slideNumber, int lineNumber) {
        String nextSlide = removeAndAppendNumberToXpath(CAROUSEL_TEXT, CAROUSEL_TEXT_TO_CHANGE, slideNumber);
        String nextLine = removeAndAppendNumberToXpath(LINE, LINE_TO_CHANGE, lineNumber);
        return driver.findElement(By.xpath(nextSlide + nextLine ));
    }

    public String getTextHexColor(WebElement element) {
        String color = element.getCssValue("color");
        return Color.fromString(color).asHex();
    }

    public boolean spanishTitleIsVisible() {
        return driver.findElement(By.xpath(SPANISH_TITLE)).isDisplayed();
    }

    public WebElement getHeroImage() {
        return driver.findElement(By.xpath(HERO_IMAGE));
    }

    public WebElement getHeroImageLink() {
        return driver.findElement(By.xpath(HERO_IMAGE_LINK));
    }

    public WebElement getTileImage() {
        return driver.findElement(By.xpath(TILE_IMAGE));
    }

    public String getSlideBackgroundColor() {
        String color = driver.findElement(By.xpath(SLIDE_ELEMENT)).getCssValue("background-color");
        return Color.fromString(color).asHex();
    }

    public String getElementHref(WebElement element) {
        return element.getAttribute("href");
    }
}
