import java.time.YearMonth;

public class CalcuMonty {
    public double calcuMoney(String start, String end){
        try {
            Integer result = 0;

            Integer startDay = Integer.valueOf(start.substring(8, 10));
            Integer endDay = Integer.valueOf(end.substring(8, 10));

            Integer startMonth = Integer.valueOf(start.substring(5, 7));
            Integer endMonth = Integer.valueOf(end.substring(5, 7));

            Integer startYear = Integer.valueOf(start.substring(0, 4));
            Integer endYear = Integer.valueOf(end.substring(0, 4));

            for (Integer year = startYear; year <= endYear; year++){
                if (startYear.equals(endYear)){
                    result += getMoneyInYear(startMonth + 1, endMonth - 1, year);
                    YearMonth ym = YearMonth.of(year, startMonth);
                    result += getMoneyInMonth(startMonth, year)*(ym.lengthOfMonth() - startDay + 1)/ym.lengthOfMonth();
                    ym = YearMonth.of(year, endMonth);
                    result += getMoneyInMonth(endMonth, year)*endDay/ym.lengthOfMonth();
                    return result;
                }
                if (year.equals(startYear)){
                    result += getMoneyInYear(startMonth + 1, 12, year);
                    YearMonth ym = YearMonth.of(year, startMonth);
                    result += getMoneyInMonth(startMonth, year)*(ym.lengthOfMonth() - startDay + 1)/ym.lengthOfMonth();
                }
                if (year.equals(endYear)){
                    result += getMoneyInYear(1, endMonth - 1, year);
                    YearMonth ym = YearMonth.of(year, endMonth);
                    result += getMoneyInMonth(endMonth, year)*endDay/ym.lengthOfMonth();
                }
                if(!year.equals(startYear) && !year.equals(endYear)){
                    result += getMoneyInYear(1, 12, year);
                }
                return result;
            }
            return result;

        }
        catch (Exception ex){
            return -1;
        }
    }

    private Integer getMoneyInMonth(Integer month, Integer year) {
        GetMoney gm = new GetMoney();
        return gm.getMoney(getMoneyStr(year, month));
    }

    private String getMoneyStr(Integer year, Integer month) {
        return String.valueOf(year) + "-" + String.format("%02d", month);
    }

    private Integer getMoneyInYear(int startMonth, int endMonth, Integer year) {
        Integer result = 0;
        for (Integer month = startMonth; month <= endMonth; month++){
            GetMoney gm = new GetMoney();
            result += gm.getMoney(getMoneyStr(year, month));
        }
        return result;
    }
}
