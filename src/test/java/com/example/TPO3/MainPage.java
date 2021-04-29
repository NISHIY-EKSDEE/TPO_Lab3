package com.example.TPO3;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    public SelenideElement logo = $("#wikimapia-logo-icon");
    public SelenideElement commentInput = $x("//*[@id='comment-input']");
    public SelenideElement commentUsername = $x("//input[@id='comment-username']");
    public SelenideElement captcha = $x("//input[@id='adcopy_response']");
    public SelenideElement sendCommentBtn = $x("//button[@id='send-comment']");
    public SelenideElement dropdownMenu = $x("//li[@class='n-0-2 item-2 dropdown']");
    public SelenideElement loginItem = dropdownMenu.$x("a[1]");


    public void login() {
        dropdownMenu.hover();
        loginItem.shouldBe(Condition.visible).click();
        switchTo().frame($x("//iframe[@src='/user/login/']").shouldBe(Condition.visible));
        $x("//input[contains(@class, 'login-username')]").shouldBe(Condition.visible).sendKeys(Config.login);
        $x("//input[contains(@class, 'login-password')]").shouldBe(Condition.visible).sendKeys(Config.password);
        $x("//input[@type='submit'][@value='Войти']").shouldBe(Condition.visible).click();
        switchTo().defaultContent();
    }
}
