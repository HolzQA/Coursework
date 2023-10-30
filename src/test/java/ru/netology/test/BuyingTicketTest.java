package ru.netology.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.page.BuyingTicketPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.*;

public class BuyingTicketTest {
    BuyingTicketPage buyingTicketPage;


    @BeforeEach
    void setUp() {
        buyingTicketPage = open("http://localhost:8080", BuyingTicketPage.class);
    }

    @AfterAll
    static void cleanAll() {
        cleanDatabase();
    }


    @DisplayName("Should successfully buying with approved card and refusal with declined card")
    @ParameterizedTest
    @CsvSource({
            "approved, Операция одобрена Банком",
            "declined, Банк отказал в проведении операции"
    })
    void validBuying(String cardKey, String message) {
        buyingTicketPage.buyingTicket(getValidBuyingData(cardKey));
        buyingTicketPage.findSuccessMessage(message);
    }

    @DisplayName("Should refusal with not test card")
    @Test
    void buyingWithNotTestCard() {
        buyingTicketPage.buyingTicket(getInvalidBuyingDataWithNotTestCard());
        buyingTicketPage.findErrorMessage("Банк отказал в проведении операции");
    }

    @DisplayName("Should error with invalid format card")       /* 0-пустая, 1-кириллица, 2-латиница, 3-спец. символы */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "2",
            "3"
    })
    void buyingWithInvalidCardNumber(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidCardNumber(index));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }


    @DisplayName("should error with invalid numeric month")   /* 0-прошедший месяц этого года, 1-двузначное число >12 */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1"
    })
    void buyingWithInvalidNumericMonth(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidNumericMonth(index));
        buyingTicketPage.findLocalErrorMessage("Неверно указан срок действия карты");
    }

    @DisplayName("Should error with invalid format month")      /* 0-пустая, 1-кириллица, 2-латиница, 3-спец. символы */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "2",
            "3"
    })
    void buyingWithInvalidMonth(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidMonth(index));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }

    @DisplayName("should error with invalid numeric year")   /* 0-прошедший год, 1-год, дальше, чем на 5 лет вперед */
    @ParameterizedTest
    @CsvSource({
            "0, Истёк срок действия карты",
            "1, Неверно указан срок действия карты"
    })
    void buyingWithInvalidNumericYear(int index, String message) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidNumericYear(index));
        buyingTicketPage.findLocalErrorMessage(message);
    }

    @DisplayName("Should error with invalid format year")      /* 0-пустая, 1-кириллица, 2-латиница, 3-спец. символы */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "2",
            "3"
    })
    void buyingWithInvalidYear(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidYear(index));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }

    @DisplayName("should error about required field Name")
    @Test
    void buyingWithRequiredFieldName() {
        buyingTicketPage.buyingTicket(getBuyingDataWithEmptyName());
        buyingTicketPage.findLocalErrorMessage("Поле обязательно для заполнения");
    }

    @DisplayName("should error with invalid Name")      /* 0-кириллица, 1-строчная латиница, 2-спец. символы, 3-цифры */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "2",
            "3",
    })
    void buyingWithInvalidName(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidName(index));
        buyingTicketPage.findLocalErrorMessage("Поле обязательно для заполнения");
    }


    @DisplayName("Should mistake with invalid format cvc")      /* 0-пустая, 1-кириллица, 2-латиница, 3-спец. символы */
    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "2",
            "3"
    })
    void buyingWithInvalidCVC(int index) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidCVC(index));
        buyingTicketPage.findLocalErrorMessageForCVC("Неверный формат");
    }

    @DisplayName("Should give right amount")
    @Test
    void testingAmountInDatabase() {
        buyingTicketPage.buyingTicket(getValidBuyingData("approved"));
        buyingTicketPage.findSuccessMessage("Операция одобрена Банком");
        Assertions.assertEquals(45000, getAmountFromDatabase());
    }
}
