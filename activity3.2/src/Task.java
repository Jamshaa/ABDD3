// Task.java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Observable;

public abstract class Task extends Observable {
    protected BigDecimal cost;

    /** Constructor intern, permet zero o positiu */
    protected Task(BigDecimal initialCost, boolean allowZero) {
        BigDecimal adjusted = initialCost.setScale(2, RoundingMode.HALF_UP);
        if (adjusted.signum() < 0 || (!allowZero && adjusted.signum() == 0)) {
            throw new IllegalArgumentException("cost must be positive" + (allowZero ? " or zero" : ""));
        }
        this.cost = adjusted;
    }

    /** Constructor públic: només cost > 0 */
    protected Task(BigDecimal initialCost) {
        this(initialCost, false);
    }

    protected void setCostAndNotify(BigDecimal newCost) {
        BigDecimal norm = newCost.setScale(2, RoundingMode.HALF_UP);
        if (norm.signum() <= 0) {
            throw new IllegalArgumentException("cost must be positive");
        }
        if (!norm.equals(this.cost)) {
            BigDecimal old = this.cost;
            this.cost = norm;
            setChanged();
            notifyObservers(new CostChanged(old, norm));
        }
    }

    public final BigDecimal costInEuros() {
        return cost;
    }

    public abstract void changeCost(BigDecimal newCost);
}
