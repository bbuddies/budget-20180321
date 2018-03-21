import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BudgetPlanTest {
    private BudgetRepoStub budgetRepo = new BudgetRepoStub();
    private BudgetPlan budgetPlan = new BudgetPlan(budgetRepo);

    private void givenBudgetPeriods(Budget... budgets) {
        budgetRepo.setBudgets(Arrays.asList(budgets));
    }

    @Test
    public void no_budget() {
        givenBudgetPeriods();
        assertEquals(0, budgetPlan.query("2018-01-03", "2018-03-04"), 0.01);
    }

    @Test
    public void query_within_one_budget() throws Exception {
        givenBudgetPeriods(new Budget("2018-01", 310));
        assertEquals(20, budgetPlan.query("2018-01-03", "2018-01-04"), 0.01);
    }

    @Test
    public void query_across_two_budgets() throws Exception {
        givenBudgetPeriods(
                new Budget("2018-01", 310),
                new Budget("2018-02", 28)
        );
        assertEquals(150 + 6, budgetPlan.query("2018-01-17", "2018-02-06"), 0.01);
    }

    @Test
    public void query_across_multiple_budgets() throws Exception {
        givenBudgetPeriods(
                new Budget("2018-01", 310),
                new Budget("2018-02", 28),
                new Budget("2018-03", 3100),
                new Budget("2018-04", 30000)
        );
        assertEquals(100 + 28 + 3100 + 2000, budgetPlan.query("2018-01-22", "2018-04-02"), 0.01);
    }

    @Test
    public void query_december() throws Exception {
        givenBudgetPeriods(
                new Budget("2017-12", 3100)
        );
        assertEquals(1400, budgetPlan.query("2017-12-17", "2017-12-30"), 0.01);
    }

    @Test
    public void query_across_year() throws Exception {
        givenBudgetPeriods(
                new Budget("2017-11", 30000),
                new Budget("2017-12", 3100),
                new Budget("2018-01", 310),
                new Budget("2018-02", 28)
        );
        assertEquals(14000 + 3100 + 310 + 6, budgetPlan.query("2017-11-17", "2018-02-06"), 0.01);
    }

    @Test
    public void query_start_date_out_of_budgets() throws Exception {
        givenBudgetPeriods(new Budget("2018-02", 28));
        assertEquals(5, budgetPlan.query("2018-01-22", "2018-02-05"), 0.01);
    }

    @Test
    public void query_end_date_out_of_budgets() throws Exception {
        givenBudgetPeriods(new Budget("2018-02", 28));
        assertEquals(9, budgetPlan.query("2018-02-20", "2018-03-15"), 0.01);
    }

    @Test
    public void default_amount_of_missing_budgets_is_0() throws Exception {
        givenBudgetPeriods(
                new Budget("2018-01", 310),
                new Budget("2018-02", 28),
                new Budget("2018-04", 30000)
        );
        assertEquals(100 + 28 + 2000, budgetPlan.query("2018-01-22", "2018-04-02"), 0.01);
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
