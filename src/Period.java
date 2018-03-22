import java.time.LocalDate;

class Period {
    private final LocalDate start;
    private final LocalDate end;

    Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    int getOverlappingDayCount(Period another) {
        LocalDate startOfOverlapping = start.isAfter(another.start) ? start : another.start;
        LocalDate endOfOverlapping = end.isBefore(another.end) ? end : another.end;
        return startOfOverlapping.until(endOfOverlapping).getDays() + 1;
    }
}
