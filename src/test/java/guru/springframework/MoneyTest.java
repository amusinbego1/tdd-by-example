package guru.springframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTest {

    @DisplayName("toString test")
    @ParameterizedTest(name = "Arguments [{arguments}]")
    @CsvFileSource(resources = "/MoneyToStringTest.csv", delimiter = '|')
    void toStringTest(String amount, String currency, String expected) {
        Money money = new Money(Double.parseDouble(amount), Money.Currency.valueOf(currency));
        assertEquals(expected.replace("{amount}", amount), money.toString());
    }

    @ParameterizedTest
    @MethodSource("additionTestSource")
    void additionTest(Money m1, Money m2){
        assertEquals(Money.resultOf(Double::sum, m1, m2), Money.add(m1, m2));
    }

    public static Stream<Arguments> additionTestSource(){
        return Stream.of(
                    Arguments.of(new Money(1), new Money(2)),
                    Arguments.of(new Money(1), new Money(2, Money.Currency.DOLLAR)),
                    Arguments.of(new Money(1, Money.Currency.EURO), new Money(2, Money.Currency.DOLLAR))
                );
    }

    @ParameterizedTest
    @MethodSource("subtractionTestSource")
    void subtractionTest(Money m1, Money m2){
        assertEquals(Money.resultOf((a,b) -> a-b, m1, m2), Money.minus(m1, m2));
    }

    public static Stream<Arguments> subtractionTestSource(){
        return Stream.of(
                Arguments.of(new Money(1), new Money(2)),
                Arguments.of(new Money(1), new Money(2, Money.Currency.DOLLAR)),
                Arguments.of(new Money(1, Money.Currency.EURO), new Money(2, Money.Currency.DOLLAR))
        );
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "1.3|1.32|1,30",
            "1223121.3|1.32|1.223.121,30"
    })
    void testToStringWithNullLocale(Double amount, Double oneUnitInBam, String expected){
        assertEquals(expected, new Money(amount, oneUnitInBam, null).toString());
    }

    @Test
    void testResultOfWithUnaryOperator(){
        Money m = new Money(10);
        assertEquals(m.amount()*10, m.resultOf(amount -> amount*10).amount());
    }


}
