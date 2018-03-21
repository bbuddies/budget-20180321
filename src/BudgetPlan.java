import java.time.LocalDate;
import java.time.YearMonth;

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
        Integer result = 0;
        for (Integer year = period.getStartDate().getYear(); year <= period.getEndDate().getYear(); year++) {
            if (period.getStartDate().getYear() == period.getEndDate().getYear()) {
                YearMonth ym = YearMonth.of(year, period.getStartDate().getMonthValue());
                if (period.getStartDate().getMonthValue() == period.getEndDate().getMonthValue()) {
                    return getMoneyInMonth(period.getStartDate().getMonthValue(), year) * (period.getEndDate().getDayOfMonth() - period.getStartDate().getDayOfMonth() + 1) / ym.lengthOfMonth();
                }
                result += getMoneyInYear(period.getStartDate().getMonthValue() + 1, period.getEndDate().getMonthValue() - 1, year);
                result += getMoneyInMonth(period.getStartDate().getMonthValue(), year) * (ym.lengthOfMonth() - period.getStartDate().getDayOfMonth() + 1) / ym.lengthOfMonth();
                ym = YearMonth.of(year, period.getEndDate().getMonthValue());
                result += getMoneyInMonth(period.getEndDate().getMonthValue(), year) * period.getEndDate().getDayOfMonth() / ym.lengthOfMonth();
                return result;
            }
            if (year.equals(period.getStartDate().getYear())) {
                result += getMoneyInYear(period.getStartDate().getMonthValue() + 1, 12, year);
                YearMonth ym = YearMonth.of(year, period.getStartDate().getMonthValue());
                result += getMoneyInMonth(period.getStartDate().getMonthValue(), year) * (ym.lengthOfMonth() - period.getStartDate().getDayOfMonth() + 1) / ym.lengthOfMonth();
            }
            if (year.equals(period.getEndDate().getYear())) {
                result += getMoneyInYear(1, period.getEndDate().getMonthValue() - 1, year);
                YearMonth ym = YearMonth.of(year, period.getEndDate().getMonthValue());
                result += getMoneyInMonth(period.getEndDate().getMonthValue(), year) * period.getEndDate().getDayOfMonth() / ym.lengthOfMonth();
            }
            if (!year.equals(period.getStartDate().getYear()) && !year.equals(period.getEndDate().getYear())) {
                result += getMoneyInYear(1, 12, year);
            }
        }
        return result;
    }

    private Integer getMoneyInMonth(Integer month, Integer year) {
        return budgetRepo.getMoney(getMoneyStr(year, month));
    }

    private String getMoneyStr(Integer year, Integer month) {
        return String.valueOf(year) + "-" + String.format("%02d", month);
    }

    private Integer getMoneyInYear(int startMonth, int endMonth, Integer year) {
        Integer result = 0;
        for (Integer month = startMonth; month <= endMonth; month++) {
            result += budgetRepo.getMoney(getMoneyStr(year, month));
        }
        return result;
    }

}
