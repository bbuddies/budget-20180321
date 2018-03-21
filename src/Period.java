import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;

    Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    int getOverlappingDayCount(Period another) {
        LocalDate startOfOverlapping = startDate.isAfter(another.startDate) ? startDate : another.startDate;
        LocalDate endOfOverlapping = endDate.isBefore(another.endDate) ? endDate : another.endDate;
        return endOfOverlapping.getDayOfMonth() - startOfOverlapping.getDayOfMonth() + 1;
    }

    public int getDayCount() {
        return endDate.getDayOfMonth() - startDate.getDayOfMonth() + 1;
    }

    List<YearMonth> createYearMonthList() {
        List<YearMonth> yearMonthList = new ArrayList<>();
        for (YearMonth yearMonth = YearMonth.from(startDate); yearMonth.isBefore(YearMonth.from(endDate.plusMonths(1))); yearMonth = yearMonth.plusMonths(1)) {
            yearMonthList.add(yearMonth);
        }
        return yearMonthList;
    }
}
