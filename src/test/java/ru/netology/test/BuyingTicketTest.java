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
    @Test
    void validBuyingWithApproved() {
        buyingTicketPage.buyingTicket(getValidBuyingData("approved"));
        buyingTicketPage.findSuccessMessage("Операция одобрена Банком");
    }

    @DisplayName("Should successfully buying with approved card and refusal with declined card")
    @Test
    void validBuyingWithDeclined() {
        buyingTicketPage.buyingTicket(getValidBuyingData("declined"));
        buyingTicketPage.findErrorMessage("Банк отказал в проведении операции");
    }

    @DisplayName("Should refusal with not test card")
    @Test
    void buyingWithNotTestCard() {
        buyingTicketPage.buyingTicket(getInvalidBuyingDataWithNotTestCard());
        buyingTicketPage.findErrorMessage("Банк отказал в проведении операции");
    }

    @DisplayName("Should error with invalid format card")
    @ParameterizedTest
    @CsvSource({
            "EMPTY",
            "CYRILLIC",
            "LATIN",
            "SPECIAL_SYMBOL"
    })
    void buyingWithInvalidCardNumber(ScenarioNumberField scenario) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidCardNumber(scenario));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }


    @DisplayName("should error with invalid numeric month")
    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void buyingWithInvalidNumericMonth(boolean isPastMonth) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidNumericMonth(isPastMonth));
        buyingTicketPage.findLocalErrorMessage("Неверно указан срок действия карты");
    }

    @DisplayName("Should error with invalid format month")
    @ParameterizedTest
    @CsvSource({
            "EMPTY",
            "CYRILLIC",
            "LATIN",
            "SPECIAL_SYMBOL"
    })
    void buyingWithInvalidMonth(ScenarioNumberField scenario) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidMonth(scenario));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }

    @DisplayName("should error with invalid numeric year")
    @ParameterizedTest
    @CsvSource({
            "true, Истёк срок действия карты",
            "false, Неверно указан срок действия карты"
    })
    void buyingWithInvalidNumericYear(boolean isPastYear, String message) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidNumericYear(isPastYear));
        buyingTicketPage.findLocalErrorMessage(message);
    }

    @DisplayName("Should error with invalid format year")
    @ParameterizedTest
    @CsvSource({
            "EMPTY",
            "CYRILLIC",
            "LATIN",
            "SPECIAL_SYMBOL"
    })
    void buyingWithInvalidYear(ScenarioNumberField scenario) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidYear(scenario));
        buyingTicketPage.findLocalErrorMessage("Неверный формат");
    }

    @DisplayName("should error about required field Name")
    @Test
    void buyingWithRequiredFieldName() {
        buyingTicketPage.buyingTicket(getBuyingDataWithEmptyName());
        buyingTicketPage.findLocalErrorMessage("Поле обязательно для заполнения");
    }

    @DisplayName("should error with invalid Name")
    @ParameterizedTest
    @CsvSource({
            "CYRILLIC",
            "LATIN_LOWERCASE",
            "SPECIAL_SYMBOL",
            "NUMBER"
    })
    void buyingWithInvalidName(ScenarioStringField scenario) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidName(scenario));
        buyingTicketPage.findLocalErrorMessage("Поле обязательно для заполнения");
    }


    @DisplayName("Should mistake with invalid format cvc")
    @ParameterizedTest
    @CsvSource({
            "EMPTY",
            "CYRILLIC",
            "LATIN",
            "SPECIAL_SYMBOL"
    })
    void buyingWithInvalidCVC(ScenarioNumberField scenario) {
        buyingTicketPage.buyingTicket(getBuyingDataWithInvalidCVC(scenario));
        buyingTicketPage.findLocalErrorMessageForCVC("Неверный формат");
    }

    @DisplayName("Should give right amount")
    @Test
    void testingAmountInDatabase() {
        buyingTicketPage.buyingTicket(getValidBuyingData("approved"));
        buyingTicketPage.findSuccessMessage("Операция одобрена Банком");
        Assertions.assertEquals(4500000, getAmountFromDatabase());
    }
}
