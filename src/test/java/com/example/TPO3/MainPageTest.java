package com.example.TPO3;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browser = "firefox";
        Configuration.startMaximized = true;
    }

    @BeforeEach
    public void setUp() {
        open("http://wikimapia.org/#lang=ru&lat=59.957395&lon=30.311708&z=17&m=w");
    }

    @Test
    public void commentWithoutLogin() throws InterruptedException {
        actions().moveToElement(mainPage.logo).moveByOffset(650, 600).click().perform();
        switchTo().frame($x("//iframe[@class='panel-content panel-with-header']"));
        mainPage.commentInput.should(exist).sendKeys("bot attack!");
        mainPage.sendCommentBtn.click();
        $x("//img[@id='adcopy-puzzle-image-image']").shouldBe(visible);
        mainPage.commentUsername.shouldBe(visible).sendKeys("bot");
        mainPage.captcha.shouldBe(visible).sendKeys("im unable to read this");
        $x("//form[@id='comment-form-captcha']/button[@type='submit']").click();
        switchTo().defaultContent();
        String msg = $x("//p[@class='alertify-message']").text();
        Assertions.assertEquals("You have entered an incorrect CAPTCHA", msg);
    }

    @Test
    public void loginTest() {
        mainPage.login();
        switchTo().frame($x("//iframe[@src='/user/profile/']").shouldBe(Condition.visible));
        String username = $x("//div[@id='profile-username']/h1").shouldBe(visible).text();
        Assertions.assertEquals(Config.login, username);
    }

    @Test
    public void commentWithLogin() throws InterruptedException {
        mainPage.login();
        $x("//span[@class='close']").shouldBe(visible).click();
        actions().moveToElement(mainPage.logo).moveByOffset(650, 600).click().perform();
        switchTo().frame($x("//iframe[@class='panel-content panel-with-header']"));
        mainPage.commentInput.should(exist).sendKeys("bot attack!");
        mainPage.sendCommentBtn.click();
        Assertions.assertDoesNotThrow(() -> {
            String msg = $x("//div[contains(string(), 'bot attack!')]").text();
            String time = $x("//span[contains(string(), 'прямо сейчас')]").text();
        });
    }

    @Test
    public void addObjectToBuilding() throws InterruptedException {
        mainPage.login();
        $x("//span[@class='close']").shouldBe(visible).click();
        actions().moveToElement(mainPage.logo).moveByOffset(870, 700).click().perform();
        switchTo().frame($x("//iframe[@class='panel-content panel-with-header']"));
        $x("//a[@id='add-child']").shouldBe(visible, Duration.ofSeconds(2)).click();
        switchTo().defaultContent();
        $x("//input[@id='title']").shouldBe(visible).sendKeys("kek");
        $x("//input[@type='submit'][@value='сохранить']").shouldBe(visible).click();
        switchTo().frame($x("//iframe[@class='panel-content panel-with-header']"));
        String name = $x("//div[@id='placeinfo']/h1[1]").shouldBe(visible).text();
        Assertions.assertEquals("kek", name);
    }

//    @Test
//    public void addObjectToMap() throws InterruptedException {
//        $x("//div[@class='wm-noprint add-place']").shouldBe(visible).click();
//        actions().moveToElement(mainPage.logo)
//                .moveByOffset(300, 300).doubleClick()
//                .moveByOffset(300, 0).doubleClick()
//                .moveByOffset(0, 300).doubleClick()
//                .moveByOffset(-300, -300).doubleClick();
//        $x("//input[@id='save-button'][@value='сохранить']").click();
//        $x("//input[@id='title']").shouldBe(visible).sendKeys("shaverma");
//        $x("//input[@type='submit'][@value='сохранить']").shouldBe(visible).click();
//        switchTo().frame($x("//iframe[@class='panel-content panel-with-header']"));
//        String name = $x("//div[@id='placeinfo']/h1[1]").shouldBe(visible).text();
//        Assertions.assertEquals("shaverma", name);
//    }

    @Test
    public void modifyObject() {
        mainPage.login();
        $x("//span[@class='close']").shouldBe(visible).click();
        var modifyItem = $x("//li[@class='n-0-0 item-0 dropdown']").hover();
        modifyItem.$x("ul/li[3]").shouldBe(Condition.visible).click();
        switchTo().frame($x("//iframe[@class='modal-body modal-iframe']").shouldBe(Condition.visible));
        String msg = $x("//p[@class='lead']").shouldBe(visible).text();
        Assertions.assertEquals("Ваш уровень опыта недостаточен для использования этой функции.", msg);
    }

    @Test
    public void filterObjects() {
        var modifyItem = $x("//li[@class='n-0-1 item-1 dropdown']").hover();
        modifyItem.$x("ul/li[2]").shouldBe(Condition.visible).click();
        Assertions.assertDoesNotThrow(() -> {
            $x("//span[contains(@style, background-image: url(\"/img/markers/category-marker.png?660\"))]");
        });
    }
}
