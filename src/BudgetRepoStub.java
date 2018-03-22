import java.util.List;

public class BudgetRepoStub implements BudgetRepo {
    private List<Budget> budgets;

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    @Override
    public List<Budget> findAll() {
        return budgets;
    }
}
