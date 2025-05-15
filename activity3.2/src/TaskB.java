// TaskB.java
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class TaskB {
    protected BigDecimal cost;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /** Constructor públic: exigeix cost > 0 */
    protected TaskB(BigDecimal initialCost) {
        setCost(initialCost);
    }

    /** Constructor intern: permet cost >= 0 */
    protected TaskB(BigDecimal initialCost, boolean allowZero) {
        BigDecimal norm = initialCost.setScale(2, RoundingMode.HALF_UP);
        if (norm.signum() < 0 || (!allowZero && norm.signum() == 0)) {
            throw new IllegalArgumentException("Cost must be positive");
        }
        this.cost = norm;
    }

    /** Normalitza i notifica canvis reals */
    protected void setCost(BigDecimal newCost) {
        BigDecimal norm = newCost.setScale(2, RoundingMode.HALF_UP);
        if (norm.signum() <= 0) {
            throw new IllegalArgumentException("Cost must be positive");
        }
        BigDecimal old = this.cost;
        if (old == null || !old.equals(norm)) {
            this.cost = norm;
            support.firePropertyChange("cost", old, this.cost);
        }
    }

    /** Solució per a inicialitzar zero en CompositeTaskB */
    protected void setCostInternal(BigDecimal newCost) {
        BigDecimal norm = newCost.setScale(2, RoundingMode.HALF_UP);
        this.cost = norm;
    }

    public final BigDecimal costInEuros() {
        return cost;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    public abstract void changeCost(BigDecimal newCost);
}
