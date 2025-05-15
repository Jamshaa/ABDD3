// SimpleTask.java
import java.math.BigDecimal;

public class SimpleTask extends Task {
    public SimpleTask(BigDecimal cost) {
        super(cost);  // usa el constructor públic (cost>0)
    }
    @Override
    public void changeCost(BigDecimal newCost) {
        setCostAndNotify(newCost);
    }
}
