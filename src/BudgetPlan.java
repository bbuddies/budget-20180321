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
        if (period.getStartDate().getYear() == period.getEndDate().getYear()) {
            YearMonth ym = YearMonth.of(period.getStartDate().getYear(), period.getStartDate().getMonthValue());
            if (period.getStartDate().getMonthValue() == period.getEndDate().getMonthValue())
                return getMoneyInMonth(period.getStartDate().getMonthValue(), period.getStartDate().getYear()) * getDayCountBetween(period.getStartDate(), period.getEndDate()) / ym.lengthOfMonth();
            result += getMoneyInYear(period.getStartDate().getMonthValue() + 1, period.getEndDate().getMonthValue() - 1, period.getStartDate().getYear());
            result += getMoneyInMonth(period.getStartDate().getMonthValue(), period.getStartDate().getYear()) * (ym.lengthOfMonth() - period.getStartDate().getDayOfMonth() + 1) / ym.lengthOfMonth();
            ym = YearMonth.of(period.getStartDate().getYear(), period.getEndDate().getMonthValue());
            result += getMoneyInMonth(period.getEndDate().getMonthValue(), period.getStartDate().getYear()) * period.getEndDate().getDayOfMonth() / ym.lengthOfMonth();
            return result;
        }
        for (Integer year = period.getStartDate().getYear(); year <= period.getEndDate().getYear(); year++) {
            if (year.equals(period.getStartDate().getYear())) {
                for (Integer month = period.getStartDate().getMonthValue() + 1; month <= 12; month++) {
                    YearMonth yearMonth = YearMonth.of(year, month);
                    result += getMoneyInMonth(yearMonth.getMonthValue(), yearMonth.getYear())
                            * getOverlappingDayCount(period, toPeriod(yearMonth))
                            / yearMonth.lengthOfMonth();
                }

                YearMonth yearMonth = YearMonth.from(period.getStartDate());
                result += getMoneyInMonth(yearMonth.getMonthValue(), yearMonth.getYear())
                        * getOverlappingDayCount(period, toPeriod(yearMonth))
                        / yearMonth.lengthOfMonth();
            }
            if (year.equals(period.getEndDate().getYear())) {
                for (Integer month = 1; month <= period.getEndDate().getMonthValue() - 1; month++) {
                    YearMonth yearMonth = YearMonth.of(year, month);
                    result += getMoneyInMonth(yearMonth.getMonthValue(), yearMonth.getYear())
                            * getOverlappingDayCount(period, toPeriod(yearMonth))
                            / yearMonth.lengthOfMonth();
                }

                result += getMoneyInMonth(period.getEndDate().getMonthValue(), year) * getDayCountBetween(period.getEndDate().withDayOfMonth(1), period.getEndDate()) / period.getEndDate().lengthOfMonth();
            }
            if (!year.equals(period.getStartDate().getYear()) && !year.equals(period.getEndDate().getYear())) {
                for (Integer month = 1; month <= 12; month++) {
                    result += getMoneyInMonth(month, year) * getDayCountBetween(LocalDate.of(year, month, 1), LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth()))/ YearMonth.of(year, month).lengthOfMonth();
                }
            }
        }
        return result;
    }

    private Period toPeriod(YearMonth startMonth) {
        return new Period(startMonth.atDay(1), startMonth.atEndOfMonth());
    }

    private int getOverlappingDayCount(Period period, Period another) {
        LocalDate startOfOverlapping = period.getStartDate().isAfter(another.getStartDate()) ? period.getStartDate() : another.getStartDate();
        return getDayCountBetween(startOfOverlapping, another.getEndDate());
    }

    private int getDayCountBetween(LocalDate start, LocalDate end) {
        return end.getDayOfMonth() - start.getDayOfMonth() + 1;
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
            result += getMoneyInMonth(month, year);
        }
        return result;
    }

}
