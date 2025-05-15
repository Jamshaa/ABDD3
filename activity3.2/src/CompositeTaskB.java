// CompositeTaskB.java
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class CompositeTaskB extends TaskB implements PropertyChangeListener {
    private final List<TaskB> subtasks;

    public CompositeTaskB(List<TaskB> subtasks) {
        super(BigDecimal.ZERO, true);          // usa constructor intern
        Objects.requireNonNull(subtasks);
        this.subtasks = List.copyOf(subtasks);
        for (TaskB t : this.subtasks) {
            t.addPropertyChangeListener(this);
        }
        recalc();
    }

    @Override
    public void changeCost(BigDecimal newCost) {
        throw new UnsupportedOperationException("Cannot change cost of CompositeTaskB");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("cost".equals(evt.getPropertyName())) {
            recalc();
        }
    }

    private void recalc() {
        BigDecimal sum = subtasks.stream()
                .map(TaskB::costInEuros)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        setCost(sum);
    }
}
