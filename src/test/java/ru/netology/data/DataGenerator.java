package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    static final int yearLimit = 5;
    static final int dayLimit = yearLimit*365;

    static final HashMap<String, String> cards = new HashMap<>();

    static Faker fakerEn = new Faker(new Locale("en"));
    static Faker fakerRu = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    public  static void putCards() {
        cards.put("approved", "1111 2222 3333 4444");
        cards.put("declined", "5555 6666 7777 8888");
    }


    @Value
    public static class BuyingData {
        String cardNumber;
        String month;
        String year;
        String name;
        String cvc;
    }

    public enum ScenarioNumberField {
        EMPTY,
        CYRILLIC,
        LATIN,
        SPECIAL_SYMBOL
    }

    public enum ScenarioStringField {
        CYRILLIC,
        LATIN_LOWERCASE,
        SPECIAL_SYMBOL,
        NUMBER
    }

    public static String generateValidDate(String format) {
        return LocalDate.now().plusDays(new Random().nextInt(dayLimit)).format(DateTimeFormatter.ofPattern(format));
    }

    public static String generateNotTestCardNumber() {
        return fakerEn.numerify("9###############");
    }

    public static String generateInvalidEmptyString() {
        String s = " ";
        return s;
    }

    public static String generateInvalidStringEn(String str) {
        return fakerEn.bothify(str);
    }

    public static String generateInvalidStringRu(String str) {
        return fakerRu.bothify(str);
    }

    public static String generateInvalidMonth() {
        return Integer.toString(new Random().nextInt(87) + 13);
    }

    public static String generateInvalidMonthInCurrentYear() {
        int nowMonth = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("MM")));
        return new DecimalFormat("00"). format(new Random().nextInt(nowMonth));
    }

    public static String generateInvalidYearPast() {
        int nowYear = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yy")));
        return new DecimalFormat("00").format(new Random().nextInt(nowYear));
    }

    public static String generateInvalidYearFuture() {
        int nowYear = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yy")));
        return new DecimalFormat("00").format(nowYear + yearLimit + new Random().nextInt(100 - yearLimit - nowYear));
    }

    public static String generateValidName() {
        return fakerEn.name().firstName().toUpperCase() + " " + fakerEn.name().lastName().toUpperCase();
    }

    public static String generateInvalidNameRu() {
        return fakerRu.name().firstName().toUpperCase() + " " + fakerRu.name().lastName().toUpperCase();
    }

    public static String generateInvalidNameEnSmall() {
        return fakerEn.name().firstName() + " " + fakerEn.name().lastName();
    }

    public static String generateValidCVC() {
        return new DecimalFormat("000").format(new Random().nextInt(1000));
    }

    public static BuyingData getValidBuyingData(String cardKey) {
        return new BuyingData(
                cards.get(cardKey),
                generateValidDate("MM"),
                generateValidDate("yy"),
                generateValidName(),
                generateValidCVC()
        );
    }

    public static BuyingData getInvalidBuyingDataWithNotTestCard() {
        return new BuyingData(
                generateNotTestCardNumber(),
                generateValidDate("MM"),
                generateValidDate("yy"),
                generateValidName(),
                generateValidCVC()
        );
    }

    public static BuyingData getBuyingDataWithInvalidCardNumber(ScenarioNumberField scenario) {
        switch(scenario) {
            case EMPTY:
                return new BuyingData(
                        generateInvalidEmptyString(),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case CYRILLIC:
                return new BuyingData(
                        generateInvalidStringRu("?????"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case LATIN:
                return new BuyingData(
                        generateInvalidStringEn("?????"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case SPECIAL_SYMBOL:
                return new BuyingData(
                        generateInvalidStringEn("!%"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
        }
        return null;
    }

    public static BuyingData getBuyingDataWithInvalidNumericMonth(boolean isPastMonth) {
        if (isPastMonth) {
            return new BuyingData(
                    cards.get("approved"),
                    generateInvalidMonthInCurrentYear(),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yy")),
                    generateValidName(),
                    generateValidCVC()
            );
        } else {
            return new BuyingData(
                    cards.get("approved"),
                    generateInvalidMonth(),
                    generateValidDate("yy"),
                    generateValidName(),
                    generateValidCVC()
            );
        }
    }

    public static BuyingData getBuyingDataWithInvalidMonth(ScenarioNumberField scenario) {
        switch(scenario) {
            case EMPTY:
                return new BuyingData(
                        cards.get("approved"),
                        generateInvalidEmptyString(),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case CYRILLIC:
                return new BuyingData(
                        cards.get("approved"),
                        generateInvalidStringRu("??"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case LATIN:
                return new BuyingData(
                        cards.get("approved"),
                        generateInvalidStringEn("??"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
            case SPECIAL_SYMBOL:
                return new BuyingData(
                        cards.get("approved"),
                        generateInvalidStringEn("!%"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateValidCVC()
                );
        }
        return null;
    }

    public static BuyingData getBuyingDataWithInvalidNumericYear(boolean isPastYear) {
        if (isPastYear) {
            return new BuyingData(
                    cards.get("approved"),
                    generateValidDate("MM"),
                    generateInvalidYearPast(),
                    generateValidName(),
                    generateValidCVC()
            );
        } else {
            return new BuyingData(
                    cards.get("approved"),
                    generateValidDate("MM"),
                    generateInvalidYearFuture(),
                    generateValidName(),
                    generateValidCVC()
            );
        }
    }

    public static BuyingData getBuyingDataWithInvalidYear(ScenarioNumberField scenario) {
        switch(scenario) {
            case EMPTY:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateInvalidEmptyString(),
                        generateValidName(),
                        generateValidCVC()
                );
            case CYRILLIC:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateInvalidStringRu("??"),
                        generateValidName(),
                        generateValidCVC()
                );
            case LATIN:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateInvalidStringEn("??"),
                        generateValidName(),
                        generateValidCVC()
                );
            case SPECIAL_SYMBOL:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateInvalidStringEn("!%"),
                        generateValidName(),
                        generateValidCVC()
                );
        }
        return null;
    }
    public static BuyingData getBuyingDataWithEmptyName() {
        return new BuyingData(
                cards.get("approved"),
                generateValidDate("MM"),
                generateValidDate("yy"),
                generateInvalidEmptyString(),
                generateValidCVC()
        );
    }

    public static BuyingData getBuyingDataWithInvalidName(ScenarioStringField scenario) {
        switch (scenario) {
            case CYRILLIC:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateInvalidNameRu(),
                        generateValidCVC()
                );
            case LATIN_LOWERCASE:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateInvalidNameEnSmall(),
                        generateValidCVC()
                );
            case SPECIAL_SYMBOL:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateInvalidStringEn("!%"),
                        generateValidCVC()
                );
            case NUMBER:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateInvalidStringEn("#####"),
                        generateValidCVC()
                );
        }
        return null;
    }

    public static BuyingData getBuyingDataWithInvalidCVC(ScenarioNumberField scenario) {
        switch (scenario) {
            case EMPTY:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateInvalidEmptyString()
                );
            case CYRILLIC:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateInvalidStringRu("??")
                );
            case LATIN:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateInvalidStringEn("??")
                );
            case SPECIAL_SYMBOL:
                return new BuyingData(
                        cards.get("approved"),
                        generateValidDate("MM"),
                        generateValidDate("yy"),
                        generateValidName(),
                        generateInvalidStringEn("!%")
                );
        }
        return null;
    }


}
