import java.time.LocalDate;
import java.time.YearMonth;

public class Budget {
    private final String month;
    private double amount;

    public Budget(String month, int amount) {
        this.month = month;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public int getDayCount() {
        return getYearMonth().lengthOfMonth();
    }

    public LocalDate getStartDate() {
        return getYearMonth().atDay(1);
    }

    private YearMonth getYearMonth() {
        return YearMonth.parse(month);
    }

    public LocalDate getEndDate() {
        return getYearMonth().atEndOfMonth();
    }

    public Period getPeriod() {
        return new Period(getStartDate(), getEndDate());
    }
}
