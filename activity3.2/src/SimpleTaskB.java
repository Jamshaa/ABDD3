// SimpleTaskB.java
import java.math.BigDecimal;

public class SimpleTaskB extends TaskB {
    public SimpleTaskB(BigDecimal cost) {
        super(cost);
    }

    @Override
    public void changeCost(BigDecimal newCost) {
        setCost(newCost);
    }
}
