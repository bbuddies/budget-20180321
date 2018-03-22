import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class BudgetPlanTest {
    BudgetRepoStub budgetRepo = new BudgetRepoStub();
    BudgetPlan plan = new BudgetPlan(budgetRepo);

    private void giveBudgets(Budget... budgets) {
        budgetRepo.setBudgets(Arrays.asList(budgets));
    }

    private void assertQuery(LocalDate start, LocalDate end, int expectedAmount) {
        assertEquals(expectedAmount, plan.query(
                new Period(start, end)), 0.01);
    }

    @Test
    public void no_budget() {
        giveBudgets();
        assertQuery(LocalDate.of(2018, 1, 25), LocalDate.of(2018, 3, 3), 0);
    }

    @Test
    public void query_one_budget_of_jan() throws Exception {
        giveBudgets(new Budget("2018-01", 31));
        assertQuery(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 31), 31);
    }

    @Test
    public void query_one_budget_of_feb() throws Exception {
        giveBudgets(new Budget("2018-02", 28));
        assertQuery(
                LocalDate.of(2018, 2, 1),
                LocalDate.of(2018, 2, 28),
                28);
    }

    @Test
    public void query_one_day() throws Exception {
        giveBudgets(new Budget("2018-02", 28));
        assertQuery(
                LocalDate.of(2018, 2, 1),
                LocalDate.of(2018, 2, 1),
                1);
    }

    @Test
    public void query_one_day_in_jan() throws Exception {
        giveBudgets(new Budget("2018-01", 31));
        assertQuery(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 1),
                1);
    }

    @Test
    public void query_two_days() throws Exception {
        giveBudgets(new Budget("2018-01", 31));
        assertQuery(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 2),
                2);
    }

    @Test
    public void query_start_out_of_budget() throws Exception {
        giveBudgets(new Budget("2018-01", 310));
        assertQuery(
                LocalDate.of(2017, 12, 15),
                LocalDate.of(2018, 1, 3),
                30);
    }

    @Test
    public void query_end_out_of_budget() throws Exception {
        giveBudgets(new Budget("2018-01", 3100));
        assertQuery(
                LocalDate.of(2018, 1, 15),
                LocalDate.of(2018, 2, 10),
                1700);
    }

    @Test
    public void query_across_two_budgets() throws Exception {
        giveBudgets(
                new Budget("2018-01", 3100),
                new Budget("2018-02", 280)
        );
        assertQuery(
                LocalDate.of(2018, 1, 15),
                LocalDate.of(2018, 2, 10),
                1700 + 100);
    }

    @Test
    public void query_across_multiple_budgets() throws Exception {
        giveBudgets(
                new Budget("2018-01", 3100),
                new Budget("2018-02", 280),
                new Budget("2018-03", 310),
                new Budget("2018-05", 310000)
        );
        assertQuery(
                LocalDate.of(2017, 1, 15),
                LocalDate.of(2018, 5, 20),
                3100+280+310+200000);
    }
}


