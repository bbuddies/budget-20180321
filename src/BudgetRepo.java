import java.time.YearMonth;

public interface BudgetRepo {
    Integer getMoney(String month);

    Budget getBudget(YearMonth yearMonth);
}
