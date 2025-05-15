// CompositeTask.java
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class CompositeTask extends Task implements Observer {
    private final List<Task> subtasks;

    public CompositeTask(List<Task> subtasks) {
        super(BigDecimal.ZERO, true);           // ara permet zero
        Objects.requireNonNull(subtasks);
        this.subtasks = List.copyOf(subtasks);
        for (Task t : this.subtasks) {
            t.addObserver(this);
        }
        recalcCost();
    }

    @Override
    public void changeCost(BigDecimal newCost) {
        throw new UnsupportedOperationException("Cannot change cost of CompositeTask");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof CostChanged) {
            recalcCost();
        }
    }

    private void recalcCost() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Task t : subtasks) {
            sum = sum.add(t.costInEuros());
        }
        // reuse setCostAndNotify to validar >0 i notificar
        setCostAndNotify(sum);
    }
}
