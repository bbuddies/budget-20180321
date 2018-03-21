import java.time.YearMonth;

public class Budget {
    private String month;
    private int amount;

    public Budget(String month, int amount) {
        this.month = month;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public int getAmount() {
        return amount;
    }

    Period getPeriod() {
        YearMonth startMonth = YearMonth.parse(month);
        return new Period(startMonth.atDay(1), startMonth.atEndOfMonth());
    }

    int getDayCount() {
        return getPeriod().getDayCount();
    }
}
