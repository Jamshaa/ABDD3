// TaskObsTest.java
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void simpleTaskNotifiesObservers() {
        SimpleTask t = new SimpleTask(new BigDecimal("10.00"));
        AtomicReference<BigDecimal> oldV = new AtomicReference<>();
        AtomicReference<BigDecimal> newV = new AtomicReference<>();

        t.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                CostChanged cc = (CostChanged) arg;
                oldV.set(cc.oldCost());
                newV.set(cc.newCost());
            }
        });

        t.changeCost(new BigDecimal("12.34"));

        assertEquals(new BigDecimal("10.00"), oldV.get());
        assertEquals(new BigDecimal("12.34"), newV.get());
        assertEquals(new BigDecimal("12.34"), t.costInEuros());
    }

    @Test
    void compositeRecalculatesOnSubtaskChange() {
        SimpleTask t1 = new SimpleTask(new BigDecimal("5.00"));
        SimpleTask t2 = new SimpleTask(new BigDecimal("7.50"));
        CompositeTask comp = new CompositeTask(List.of(t1, t2));

        assertEquals(new BigDecimal("12.50"), comp.costInEuros());

        t2.changeCost(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("15.00"), comp.costInEuros());
    }

    @Test
    void nestedCompositePropagatesChange() {
        SimpleTask leaf = new SimpleTask(new BigDecimal("3.00"));
        CompositeTask inner = new CompositeTask(List.of(leaf));
        CompositeTask outer = new CompositeTask(List.of(inner));

        AtomicReference<BigDecimal> cap = new AtomicReference<>();
        outer.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                CostChanged cc = (CostChanged) arg;
                cap.set(cc.newCost());
            }
        });

        leaf.changeCost(new BigDecimal("4.00"));
        assertEquals(new BigDecimal("4.00"), inner.costInEuros());
        assertEquals(new BigDecimal("4.00"), outer.costInEuros());
        assertEquals(new BigDecimal("4.00"), cap.get());
    }

    @Test
    void compositeTaskCannotBeChangedDirectly() {
        CompositeTask comp = new CompositeTask(List.of(new SimpleTask(new BigDecimal("2.00"))));
        assertThrows(UnsupportedOperationException.class,
                () -> comp.changeCost(new BigDecimal("5.00")));
    }
}
