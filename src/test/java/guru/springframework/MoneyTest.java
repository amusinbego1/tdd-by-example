package guru.springframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTest {
    @Test
    void testToStringDollar() {
        Money fiveDollars = new Money(5, Money.Currency.DOLLAR);
        assertEquals("$5.00", fiveDollars.toString());
    }

    @Test
    void testToStringBam() {
        Money fiveDollars = new Money(5, Money.Currency.BAM);
        assertEquals("5,00 KM", fiveDollars.toString());
    }

    @Test
    void testAdditionWithSameCurrency(){
        Money oneBam = new Money(1);
        Money twoBam = new Money(2);
        assertEquals(Money.resultOf(Double::sum, oneBam, twoBam), Money.add(oneBam, twoBam));
    }

    @Test
    void testAdditionWithOneBaseAndOneNonBaseCurrency(){
        Money oneBam = new Money(1);
        Money twoDollar = new Money(2, Money.Currency.DOLLAR);
        assertEquals(Money.resultOf(Double::sum, oneBam, twoDollar), Money.add(oneBam, twoDollar));
    }

    @Test
    void testAdditionWithBothNonBaseCurrencies(){
        Money oneEuro = new Money(1, Money.Currency.EURO);
        Money twoDollar = new Money(2, Money.Currency.DOLLAR);
        assertEquals(Money.resultOf(Double::sum, oneEuro, twoDollar), Money.add(oneEuro, twoDollar));
    }

    @Test
    void testMinusWithBothNonBaseCurrencies(){
        Money oneEuro = new Money(1, Money.Currency.EURO);
        Money twoDollar = new Money(2, Money.Currency.DOLLAR);
        assertEquals(Money.resultOf((a, b) -> a-b, oneEuro, twoDollar), Money.minus(oneEuro, twoDollar));
    }

    @Test
    void testToStringWithNullLocale(){
        Money undefinedCurrency = new Money(1.3, 1.32, null);
        Money undefinedCurrency2 = new Money(1223121.3, 1.32, null);
        assertEquals("1,30", undefinedCurrency.toString());
        assertEquals("1.223.121,30", undefinedCurrency2.toString());
    }

    @Test
    void testResultOfWithUnaryOperator(){
        Money m = new Money(10);
        assertEquals(m.amount()*10, m.resultOf(amount -> amount*10).amount());
    }


}
