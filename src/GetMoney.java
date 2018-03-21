import java.util.HashMap;

public class GetMoney {
    private HashMap<String, MoneyInfo> moneyMap = new HashMap<String, MoneyInfo>();

    public GetMoney(){
        moneyMap.put("2018-01", new MoneyInfo(31));
    }

    public Integer getMoney(String month) {
        if (!moneyMap.containsKey(month)){
            return null;
        }
        return moneyMap.get(month).money;
    }
}
