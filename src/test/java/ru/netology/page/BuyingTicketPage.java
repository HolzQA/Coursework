package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.*;

public class BuyingTicketPage {
    private SelenideElement dayTravel =$x("//*[text()='Путешествие дня']");
    private SelenideElement buyButton = $x("//*[text()='Купить']");
    private SelenideElement cardPayment = $x("//*[text()='Оплата по карте']");
    private SelenideElement cardNumber = $x("//*[text()='Номер карты']/following-sibling::span/input");
    private SelenideElement month = $x("//*[text()='Месяц']/following-sibling::span/input");
    private SelenideElement year = $x("//*[text()='Год']/following-sibling::span/input");
    private SelenideElement name = $x("//*[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvc = $x("//*[text()='CVC/CVV']/following-sibling::span/input");
    private SelenideElement continueButton = $x("//button[contains(span,'Продолжить')]");
    private SelenideElement successMessage = $(".notification_status_ok .notification__content");
    private SelenideElement errorMessage = $(".notification_status_error .notification__content");
    private SelenideElement localError = $(".input__sub");
    private SelenideElement localErrorName = $x("//*[text()='Владелец']/following-sibling::*[@class='input__sub']");
    private SelenideElement localErrorCVC =$x("//*[text()='CVC/CVV']/following-sibling::*[@class='input__sub']");

    public BuyingTicketPage() {
        dayTravel.shouldBe(visible);
        buyButton.click();
        cardPayment.shouldBe(visible);
        putCards();
    }

    public void buyingTicket(BuyingData data) {
        cardNumber.setValue(data.getCardNumber());
        month.setValue(data.getMonth());
        year.setValue(data.getYear());
        name.setValue(data.getName());
        cvc.setValue(data.getCvc());
        continueButton.click();
    }

    public void findSuccessMessage(String expectedText) {
        successMessage.shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text(expectedText));
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text(expectedText));
    }

    public void findLocalErrorMessage(String expectedText) {
        localError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void findLocalErrorMessageForCVC(String expectedText) {
        localErrorCVC.shouldBe(visible).shouldHave(text(expectedText));
    }

}


