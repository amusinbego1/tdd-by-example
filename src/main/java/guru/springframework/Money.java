package guru.springframework;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public final class Money{
    private final double amount;
    private double oneUnitInBam;
    private Locale locale;
    public enum Currency{
        BAM(1, new Locale("bs", "BA")),
        DOLLAR(1.83, new Locale("en", "US")),
        EURO(1.96, new Locale("de", "DE"));
        private double oneUnitInBam;
        private Locale locale;
        Currency(double oneUnitInBam, Locale locale){
            this.oneUnitInBam = oneUnitInBam;
            this.locale = locale;
        }
    }

    public Money(double amount){
        this(amount, Currency.BAM);
    }

    public Money(double amount, Currency currency) {
        this.amount = amount;
        this.oneUnitInBam = currency.oneUnitInBam;
        this.locale = currency.locale;

    }

    public Money(double amount, double oneUnitInBam, Locale locale) {
        this.amount = amount;
        this.oneUnitInBam = oneUnitInBam;
        this.locale = locale;
    }

    public double amount() {
        return amount;
    }

    @Override
    public String toString() {
        return locale != null ? NumberFormat.getCurrencyInstance(locale).format(amount) : new DecimalFormat("###,###.00").format(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (Double.compare(amount, money.amount) != 0) return false;
        if (Double.compare(oneUnitInBam, money.oneUnitInBam) != 0) return false;
        return Objects.equals(locale, money.locale);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(oneUnitInBam);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        return result;
    }

    public static Money resultOf(DoubleBinaryOperator binaryOperator, Money m1, Money m2){
        return new Money(binaryOperator.applyAsDouble(
                m1.amount()* m1.oneUnitInBam, m2.amount()* m2.oneUnitInBam
                    ) / m1.oneUnitInBam,
                m1.oneUnitInBam,
                m1.locale);
    }

    public Money resultOf(DoubleUnaryOperator unaryOperator){
        return new Money(unaryOperator.applyAsDouble(this.amount), this.oneUnitInBam, this.locale);
    }


    public static Money add(Money m1, Money m2){
        return resultOf(Double::sum, m1, m2);
    }

    public static Money minus(Money m1, Money m2){
        return resultOf((a,b)->a-b, m1, m2);
    }
}
