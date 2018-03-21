import java.util.HashMap;

public class GetMoney implements BudgetRepo {
    private HashMap<String, MoneyInfo> moneyMap = new HashMap<>();

    public GetMoney(){
        moneyMap.put("2018-01", new MoneyInfo(31));
    }

    public Integer getMoney(String month) {
        if (!moneyMap.containsKey(month)){
            return null;
        }
        return moneyMap.getOrDefault(month, new MoneyInfo(0)).money;
    }
}
