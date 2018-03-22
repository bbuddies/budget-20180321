public class BudgetPlan {
    private final BudgetRepo budgetRepo;

    public BudgetPlan(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(Period period) {
        return budgetRepo.findAll().stream().mapToDouble(budget ->
                budget.getAmount()
                        / budget.getDayCount()
                        * period.getOverlappingDayCount(budget.getPeriod())
        ).sum();
    }

}
