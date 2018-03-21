import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CalcuMoneyTest {
    private BudgetRepoStub budgetRepo = new BudgetRepoStub();
    private CalcuMoney calcuMoney = new CalcuMoney(budgetRepo);

    private void givenBudgetPeriods(Budget... budgets) {
        budgetRepo.setBudgets(Arrays.asList(budgets));
    }

    @Test
    public void no_budget(){
        givenBudgetPeriods();
        assertEquals(0, calcuMoney.calcuMoney("2018-01-03", "2018-03-04"), 0.01);
    }

}

class BudgetRepoStub implements BudgetRepo {
    private HashMap<String, MoneyInfo> moneyMap = new HashMap<>();

    public void setBudgets(List<Budget> budgets) {
        moneyMap.clear();
        budgets.forEach(budget -> moneyMap.put(budget.getMonth(), new MoneyInfo(budget.getAmount())));
    }

    @Override
    public Integer getMoney(String month) {
        return moneyMap.getOrDefault(month, new MoneyInfo(0)).money;
    }
}
