package com.tunein.testclasses;

import com.tunein.pageclasses.MainPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.tunein.pageclasses.MainPage.Language.SPANISH;

public class CarouselClass {
    WebDriver driver;
    String lineText;
    WebElement lineElement;
    ArrayList<String> backgroundColors;
    ArrayList<String> hrefs;

    @BeforeClass
    public void setUp() {
        String driverPath = System.getProperty("user.dir") + "/src/main/resources/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    /***
     * Validate auto scroll works correctly
     */
    //TODO flaky test, consider refactoring
    @Test
    public void verifySliderChange() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(true);
        int slidesAmount = mainPage.getCarouselSlidesAmount();
        hrefs = new ArrayList<>();

        for(int slide = 0; slide < slidesAmount; slide++) {
            hrefs.add(mainPage.getElementHref(mainPage.getHeroImageLink()));
            Thread.sleep(5000);
        }

        Set<String> set = new HashSet<>(hrefs);
        Assert.assertEquals(set.size(), hrefs.size(),
                "Slider doesn't change correctly");
    }

    /***
     * Each slide has 3 lines of text
     * ○ Always uppercase
     * ○ First 2 lines are always #fff
     * ○ Third line is always #1c203c
     */
    @Test
    public void verifySlidesText() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(false);
        mainPage.acceptCookies();
        int slidesAmount = mainPage.getCarouselSlidesAmount();

        //verify text uppercase && color
        for(int slide = 0; slide < slidesAmount; slide++){
            for(int line = 1; line <= 3; line++){
                lineElement = mainPage.getSlideLineElement(slide, line);
                lineText = lineElement.getText()
                        .replaceAll("[^a-zA-Z]","");
                Assert.assertTrue(lineText.chars().allMatch(Character::isUpperCase), "All text is not in uppercase!");
                switch (line){
                    case 1:
                    case 2:
                        Assert.assertEquals(mainPage.getTextHexColor(lineElement), "#ffffff", "Background has wrong color");
                        break;
                    case 3:
                        Assert.assertEquals(mainPage.getTextHexColor(lineElement), "#1c203c", "Background has wrong color");
                        break;
                }
            }
            if(slide < slidesAmount - 1){
                mainPage.clickNextSlide(slide + 1);
            }
        }
    }

    /***
     * Our site is localized to cater to international audiences as well.
     * Check out tunein.com in spanish
     */
    @Test
    public void verifySpanishVersion() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(SPANISH);
        mainPage.acceptCookies();

        Assert.assertTrue(mainPage.spanishTitleIsVisible(),"Website is not localized for Spanish");
    }

    /***
     * Each slide has one hero image and one tile image
     */
    //TODO Take a look at line 116 - possible issue
    @Test
    public void verifyImages() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(false);
        mainPage.acceptCookies();
        int slidesAmount = mainPage.getCarouselSlidesAmount();

        for(int slide = 0; slide < slidesAmount; slide++) {
            Assert.assertTrue(mainPage.getHeroImage().isDisplayed(), "Hero Image is not displayed at " + (slide + 1) + " slide");
            Assert.assertTrue(mainPage.getTileImage().isDisplayed(), "Tile Image is not displayed at " + (slide + 1) + " slide");

            if(slide < slidesAmount - 1){
                mainPage.clickNextSlide(slide + 1);
            }
        }
    }

    /***
     * Each slide triggers a different background color
     * ○ No two slides can be the same color
     */
    //TODO Take a look at line 141 - possible issue
    @Test
    public void verifyBackgroundColor() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(false);
        mainPage.acceptCookies();
        int slidesAmount = mainPage.getCarouselSlidesAmount();
        backgroundColors = new ArrayList<>();

        for(int slide = 0; slide < slidesAmount; slide++) {
            backgroundColors.add(mainPage.getSlideBackgroundColor());
            Set<String> set = new HashSet<>(backgroundColors);
            Assert.assertEquals(set.size(), backgroundColors.size(),
                    "There are slides with the same color");

            if(slide < slidesAmount - 1){
                mainPage.clickNextSlide(slide + 1);
            }
        }
    }

    /***
     * Each slides images and text are anchored to the same href
     */
    //TODO Take a look at line 166 - possible issue
    @Test
    public void verifyImageAndTextHref() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage(false);
        mainPage.acceptCookies();
        int slidesAmount = mainPage.getCarouselSlidesAmount();

        for(int slide = 0; slide < slidesAmount; slide++) {
            hrefs = new ArrayList<>();
            hrefs.add(mainPage.getElementHref(mainPage.getHeroImageLink()));
            hrefs.add(mainPage.getElementHref(mainPage.getTileImage()));
            for(int line = 1; line <= 3; line++) {
                lineElement = mainPage.getSlideLineElement(slide, line);
                hrefs.add(mainPage.getElementHref(lineElement));
            }

            //Href list has no non-equal values
            Assert.assertTrue(new HashSet<>(hrefs).size() <= 1);

            if(slide < slidesAmount - 1){
                mainPage.clickNextSlide(slide + 1);
            }
        }
    }

    /***
     * Resolutions smaller than 992px will see a more company version of the carousel
     * ○ The text and image stack vertically instead of horizontally
     * ○ The manual navigation dots are hidden
     *
     * Out of time ¯\_(ツ)_/¯
     */


    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
