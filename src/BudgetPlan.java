import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class BudgetPlan {
    private final BudgetRepo budgetRepo;

    public BudgetPlan(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(String start, String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return query(new Period(startDate, endDate));
    }

    private double query(Period period) {
        return period.createYearMonthList().stream().mapToDouble(yearMonth -> getAmountOfOverlapping(period, budgetRepo.getBudget(yearMonth))).sum();
    }

    private int getAmountOfOverlapping(Period period, Budget budget) {
        return budget.getAmount()
                * period.getOverlappingDayCount(budget.getPeriod())
                / budget.getDayCount();
    }
}
